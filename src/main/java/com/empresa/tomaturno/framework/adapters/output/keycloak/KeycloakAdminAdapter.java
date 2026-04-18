package com.empresa.tomaturno.framework.adapters.output.keycloak;

import com.empresa.tomaturno.framework.adapters.exceptions.InternalServerException;
import com.empresa.tomaturno.usuario.application.command.port.dto.CrearUsuarioKeycloakCommand;
import com.empresa.tomaturno.usuario.application.command.port.output.KeycloakAdminPort;
import com.empresa.tomaturno.usuario.dominio.entity.Usuario;
import com.empresa.tomaturno.usuario.dominio.vo.DatosPersonales;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class KeycloakAdminAdapter implements KeycloakAdminPort {

    private final Keycloak keycloak;

    public KeycloakAdminAdapter(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @ConfigProperty(name = "tomaturno.keycloak.realm", defaultValue = "tomaturno")
    String realm;

    @Override
    public String crearUsuario(CrearUsuarioKeycloakCommand cmd) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(cmd.username());
        user.setFirstName(cmd.nombres());
        user.setLastName(cmd.apellidos());
        user.setEnabled(true);

        Map<String, List<String>> attrs = new HashMap<>();
        if (cmd.idSucursal() != null)
            attrs.put("idSucursal", List.of(String.valueOf(cmd.idSucursal())));
        user.setAttributes(attrs);

        String keycloakId;
        try (Response response = keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() == 201) {
                keycloakId = CreatedResponseUtil.getCreatedId(response);
            } else if (response.getStatus() == 409) {
                // El usuario ya existe en Keycloak — se obtiene su ID sin volver a crearlo
                List<UserRepresentation> existentes = keycloak.realm(realm).users()
                        .searchByUsername(cmd.username(), true);
                if (existentes.isEmpty()) {
                    throw new InternalServerException("Usuario ya existe en Keycloak pero no se pudo recuperar su ID");
                }
                return existentes.get(0).getId();
            } else {
                throw new InternalServerException("Error al crear usuario en Keycloak: " + response.getStatus());
            }
        }

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(true);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(cmd.contrasena());
        keycloak.realm(realm).users().get(keycloakId).resetPassword(cred);

        RoleRepresentation role = obtenerOCrearRol(cmd.perfil());
        keycloak.realm(realm).users().get(keycloakId).roles().realmLevel().add(List.of(role));

        return keycloakId;
    }

    private RoleRepresentation obtenerOCrearRol(String perfil) {
        String nombreRol = perfil.toUpperCase();
        try {
            return keycloak.realm(realm).roles().get(nombreRol).toRepresentation();
        } catch (WebApplicationException e) {
            // El rol no existe — se crea automáticamente
            RoleRepresentation nuevo = new RoleRepresentation();
            nuevo.setName(nombreRol);
            keycloak.realm(realm).roles().create(nuevo);
            return keycloak.realm(realm).roles().get(nombreRol).toRepresentation();
        }
    }

    @Override
    public List<Usuario> enriquecerUsuarios(List<Usuario> usuarios) {
        usuarios.forEach(usuario -> {
            try {
                UserRepresentation kcUser = resolverUsuarioKeycloak(usuario);
                if (kcUser == null) return;

                DatosPersonales datosPersonales = DatosPersonales.reconstituir(
                        kcUser.getFirstName(),
                        kcUser.getLastName(),
                        usuario.getDui(),
                        usuario.getTelefono());

                List<RoleRepresentation> roles = keycloak.realm(realm)
                        .users()
                        .get(kcUser.getId())
                        .roles()
                        .realmLevel()
                        .listAll();

                String perfil = roles == null ? null : roles.stream()
                        .map(RoleRepresentation::getName)
                        .filter(r -> !r.startsWith("default-roles-") && !r.equals("offline_access") && !r.equals("uma_authorization"))
                        .findFirst()
                        .orElse(null);

                if (perfil != null && !perfil.isBlank()) {
                    usuario.asignarDatosKeycloak(kcUser.getUsername(), datosPersonales, perfil);
                } else {
                    // Asignar solo nombres/apellidos; dejar el perfil existente intacto
                    usuario.asignarNombresKeycloak(kcUser.getUsername(), datosPersonales);
                }
            } catch (Exception e) {
                // Usuario no encontrado en Keycloak — se omite el enriquecimiento
            }
        });
        return usuarios;
    }

    private UserRepresentation resolverUsuarioKeycloak(Usuario usuario) {
        if (usuario.getKeycloakId() != null) {
            return keycloak.realm(realm).users().get(usuario.getKeycloakId()).toRepresentation();
        }
        if (usuario.getCodigoUsuario() != null) {
            List<UserRepresentation> encontrados = keycloak.realm(realm).users()
                    .searchByUsername(usuario.getCodigoUsuario(), true);
            return encontrados.isEmpty() ? null : encontrados.get(0);
        }
        return null;
    }
}

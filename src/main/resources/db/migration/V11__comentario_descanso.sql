-- Comentario libre para el descanso: obligatorio cuando el tipo es OTRO (el operador
-- explica el motivo), opcional para los demás tipos.
ALTER TABLE tomaturno.usuarioestadooperador ADD COLUMN IF NOT EXISTS comentario varchar(255);

-- La vista se recrea para incluir la columna nueva.
DROP VIEW IF EXISTS tomaturno.vwusuarioestadooperador;

CREATE VIEW tomaturno.vwusuarioestadooperador AS
SELECT
    u.id,
    u.idusuario,
    u.idsucursal,
    u.idpuesto,
    us.codigousuario,
    u2.nombrecompleto,
    p.nombre           AS puesto,
    u.idestadooperador,
    c.nombre            AS estadooperador,
    u.idtipodescanso,
    c2.nombre           AS tipodescanso,
    u.comentario,
    u.fechainicio,
    u.fechafin
FROM tomaturno.usuarioestadooperador u
INNER JOIN tomaturno.usuario u2 ON u2.id = u.idusuario AND u2.idsucursal = u.idsucursal
LEFT JOIN tomaturno.usuario us ON us.id = u.idusuario
LEFT JOIN tomaturno.puesto p ON p.id = u.idpuesto AND p.idsucursal = u.idsucursal
LEFT JOIN tomaturno.catalogodeta c ON c.idcatalogo = 3 AND c.iddetalle = u.idestadooperador
LEFT JOIN tomaturno.catalogodeta c2 ON c2.idcatalogo = 4 AND c2.iddetalle = u.idtipodescanso
WHERE u.fechainicio::date = CURRENT_DATE;

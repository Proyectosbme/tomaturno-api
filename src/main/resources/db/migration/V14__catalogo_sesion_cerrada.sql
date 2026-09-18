-- Nuevo tipo de descanso automático: "SESION CERRADA". No es seleccionable por el
-- operador (no se agrega a OPCIONES_TIPO_DESCANSO en el front); lo asigna el propio
-- backend cuando detecta que un operador con caja ACTIVA se quedó sin ninguna sesión de
-- WebSocket conectada por más de 30 segundos (cerró el navegador, perdió la conexión,
-- cerró sesión) — ver EstadoOperadorAutomaticoOrquestador. Así deja de contarse como
-- tiempo activo y deja de recibir turnos nuevos mientras está desconectado; al reconectar
-- vuelve a ACTIVA solo.
INSERT INTO tomaturno.catalogodeta (idcatalogo, iddetalle, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES
    (4, 5, 'SESION CERRADA', 'Descanso automático: el operador se quedó sin conexión (cerró el navegador o perdió la sesión) teniendo la caja activa.', 'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (idcatalogo, iddetalle) DO NOTHING;

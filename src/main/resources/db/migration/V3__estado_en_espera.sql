-- Detalle catálogo: nuevo estado de turno EN_ESPERA
INSERT INTO tomaturno.catalogodeta (idcatalogo, iddetalle, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES
    (1, 6, 'EN_ESPERA', 'Turno en espera', 'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (idcatalogo, iddetalle) DO NOTHING;

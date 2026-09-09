-- Nuevo tipo de descanso: trámites contables (el cajero contable no atiende mientras
-- hace su cierre/trámites contables). Catálogo 4 (TIPOS_DESCANSO) ya es extensible por
-- diseño: agregar filas nuevas aquí no requiere tocar código del backend.
INSERT INTO tomaturno.catalogodeta (idcatalogo, iddetalle, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES
    (4, 4, 'TRAMITES CONTABLES', 'Pausa para trámites contables del cajero (cierre, cuadre, etc.)', 'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (idcatalogo, iddetalle) DO NOTHING;

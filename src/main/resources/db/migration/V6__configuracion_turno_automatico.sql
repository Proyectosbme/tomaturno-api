-- Nueva configuración por sucursal: llamado automático de turnos (0=inactivo, 1=activo).
-- Backfill para sucursales ya existentes; las sucursales nuevas la reciben vía
-- ConfiguracionDefaultBean.crearConfiguracionesParaSucursal en la creación.
INSERT INTO tomaturno.configuracion (idconfiguracion, idsucursal, nombre, parametro, descripcion, estado, fechacreacion, usercreacion)
SELECT
    COALESCE((SELECT MAX(c.idconfiguracion) FROM tomaturno.configuracion c WHERE c.idsucursal = s.id), 0) + 1,
    s.id,
    'TURNO_AUTOMATICO',
    0,
    'Llama automáticamente el siguiente turno en cola. 0=desactivado, 1=activado',
    1,
    NOW(),
    'sistema'
FROM tomaturno.sucursal s
WHERE NOT EXISTS (
    SELECT 1 FROM tomaturno.configuracion c2
    WHERE c2.idsucursal = s.id AND c2.nombre = 'TURNO_AUTOMATICO'
);

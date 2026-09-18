-- Agrega tomaturno.turno.fechatraslado: fecha/hora real del traslado. Se completa
-- únicamente en el turno destino de una reasignación (idturnorelacionado IS NOT NULL,
-- ver Turno.reasignarA()); en el resto de turnos queda NULL.
--
-- Es un campo exclusivo para reportes: no participa en el orden de atención, que sigue
-- dependiendo de fechacreacion (con el +1s ya existente para preservar el orden de
-- llegada al trasladar). Ver TurnoJpaRepository.buscarPorFiltros.

ALTER TABLE tomaturno.turno ADD COLUMN IF NOT EXISTS fechatraslado timestamp NULL;

COMMENT ON COLUMN tomaturno.turno.fechatraslado IS
    'Fecha/hora real del traslado. NULL salvo en el turno destino de una reasignación '
    '(idturnorelacionado IS NOT NULL). Solo para reportes (vwturnoshoy/vwturnos3meses); '
    'no afecta el orden de atención.';

-- Vistas: en los turnos trasladados se muestra fechatraslado como fecha de inicio en vez
-- de fechacreacion (que trae el +1s interno usado solo para ordenar la cola destino). El
-- resto de columnas, joins, WHERE y ORDER BY quedan igual que en
-- V4__correccion_vistas_turno.sql: es un cambio puramente visual.

CREATE OR REPLACE VIEW tomaturno.VWTURNOSHOY AS
SELECT t.id,
    t.codigoturno,
    tic.id AS idsucursalticket,
    tic.nombre AS sucursalticket,
    u.id AS idusuario,
    u.nombrecompleto,
    u.codigousuario,
    p.id AS idpuesto,
    p.idsucursal AS idpuestosucursal,
    p.nombre AS puesto,
    c.id AS idcola,
    c.nombre AS cola,
    cd.iddetalle AS idetalle,
    cd.nombre AS detalle,
    t.tipocasoespecial,
    COALESCE(dt.nombre, 'NORMAL'::character varying) AS caso_especial,
    -- CREATE OR REPLACE VIEW exige que una columna ya existente no cambie de tipo. Un CASE
    -- entre dos timestamp(6) pierde la precisión (queda como timestamp sin typmod), así que
    -- se castea de vuelta a timestamp(6) para que siga siendo el mismo tipo que ya tenía
    -- fechacreacion en esta vista.
    (CASE WHEN t.idturnorelacionado IS NOT NULL THEN t.fechatraslado ELSE t.fechacreacion END)::timestamp(6) AS fechacreacion,
    t.fechallamada,
    t.fechafinalizacion,
    t.idcatalogoestado,
    t.idcatalogoestadodetalle,
    est.nombre AS estado,
    t.idturnorelacionado
   FROM tomaturno.turno t
     LEFT JOIN tomaturno.catalogodeta dt ON dt.idcatalogo = 2 AND dt.iddetalle = t.tipocasoespecial
     LEFT JOIN tomaturno.catalogodeta est ON est.idcatalogo = t.idcatalogoestado AND est.iddetalle = t.idcatalogoestadodetalle
     LEFT JOIN tomaturno.cola c ON c.id = t.idcola AND t.idsucursal = c.idsucursal
     LEFT JOIN tomaturno.coladetalle cd ON cd.idcola = t.idcola AND cd.iddetalle = t.iddetalle AND cd.idsucursal = c.idsucursal
     LEFT JOIN tomaturno.sucursal tic ON tic.id = t.idsucursal
     LEFT JOIN tomaturno.usuario u ON u.id = t.idusuario AND u.idsucursal = t.idsucursalpuesto
     LEFT JOIN tomaturno.puesto p ON p.id = t.idpuesto AND p.idsucursal = t.idsucursalpuesto
  WHERE t.fechacreacion::date = CURRENT_DATE
  ORDER BY t.fechacreacion;

DROP MATERIALIZED VIEW IF EXISTS tomaturno.VWTURNOS3MESES;

CREATE MATERIALIZED VIEW tomaturno.VWTURNOS3MESES AS
SELECT t.id,
    t.codigoturno,
    tic.id AS idsucursalticket,
    tic.nombre AS sucursalticket,
    u.id AS idusuario,
    u.nombrecompleto,
    u.codigousuario,
    p.id AS idpuesto,
    p.idsucursal AS idpuestosucursal,
    p.nombre AS puesto,
    c.id AS idcola,
    c.nombre AS cola,
    cd.iddetalle AS idetalle,
    cd.nombre AS detalle,
    t.tipocasoespecial,
    COALESCE(dt.nombre, 'NORMAL'::character varying) AS caso_especial,
    -- Este view se recrea desde cero (DROP + CREATE, no OR REPLACE), así que el cast acá no
    -- es obligatorio, pero se deja igual que en VWTURNOSHOY por consistencia.
    (CASE WHEN t.idturnorelacionado IS NOT NULL THEN t.fechatraslado ELSE t.fechacreacion END)::timestamp(6) AS fechacreacion,
    t.fechallamada,
    t.fechafinalizacion,
    t.idcatalogoestado,
    t.idcatalogoestadodetalle,
    est.nombre AS estado,
    t.idturnorelacionado
   FROM tomaturno.turno t
     LEFT JOIN tomaturno.catalogodeta dt ON dt.idcatalogo = 2 AND dt.iddetalle = t.tipocasoespecial
     LEFT JOIN tomaturno.catalogodeta est ON est.idcatalogo = t.idcatalogoestado AND est.iddetalle = t.idcatalogoestadodetalle
     LEFT JOIN tomaturno.cola c ON c.id = t.idcola AND t.idsucursal = c.idsucursal
     LEFT JOIN tomaturno.coladetalle cd ON cd.idcola = t.idcola AND cd.iddetalle = t.iddetalle AND cd.idsucursal = c.idsucursal
     LEFT JOIN tomaturno.sucursal tic ON tic.id = t.idsucursal
     LEFT JOIN tomaturno.usuario u ON u.id = t.idusuario AND u.idsucursal = t.idsucursalpuesto
     LEFT JOIN tomaturno.puesto p ON p.id = t.idpuesto AND p.idsucursal = t.idsucursalpuesto
  WHERE t.fechacreacion >= (CURRENT_DATE - '3 mons'::interval)
  ORDER BY t.fechacreacion;

-- Reprogramar el refresh diario (igual que V4: el materialized view se recreó arriba y
-- pg_cron necesita que el job apunte otra vez a un objeto existente).
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'pg_cron') THEN
        PERFORM cron.schedule('vwturnos3meses_refresh', '0 2 * * *', 'REFRESH MATERIALIZED VIEW tomaturno.VWTURNOS3MESES');
    END IF;
END
$$;

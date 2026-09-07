-- Corrección de VWTURNOSHOY y VWTURNOS3MESES: se agrega el prefijo de esquema
-- tomaturno. y las condiciones de sucursal faltantes en los joins de cola y coladetalle.

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
    t.fechacreacion,
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
    t.fechacreacion,
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

-- Programar la actualización cada día a las 2 AM.
-- Se usa la forma con nombre de job (idempotente): si ya existe un job con ese
-- nombre (por ejemplo el creado en V2), pg_cron lo reemplaza en vez de duplicarlo.
-- Se valida que la extensión pg_cron exista antes de llamarla, para no romper
-- la migración en ambientes donde no está instalada.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'pg_cron') THEN
        PERFORM cron.schedule('vwturnos3meses_refresh', '0 2 * * *', 'REFRESH MATERIALIZED VIEW tomaturno.VWTURNOS3MESES');
    END IF;
END
$$;

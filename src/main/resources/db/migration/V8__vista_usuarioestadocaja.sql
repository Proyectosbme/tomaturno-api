-- Vista: historial de estado de caja de HOY, con nombres resueltos (usuario, puesto,
-- estado de caja y tipo de descanso). Base para la pantalla "Tiempos muertos" de Monitoreo.
-- CREATE OR REPLACE porque los views se pueden probar manualmente por psql sin romper
-- Flyway después (a diferencia de CREATE TABLE, que ya nos dio ese problema en la V7).
CREATE OR REPLACE VIEW tomaturno.vwusuarioestadocaja AS
SELECT
    u.id,
    u.idusuario,
    u.idsucursal,
    u.idpuesto,
    us.codigousuario,
    u2.nombrecompleto,
    p.nombre         AS puesto,
    u.idestadocaja,
    c.nombre          AS estadocaja,
    u.idtipodescanso,
    c2.nombre         AS tipodescanso,
    u.fechainicio,
    u.fechafin
FROM tomaturno.usuarioestadocaja u
INNER JOIN tomaturno.usuario u2 ON u2.id = u.idusuario AND u2.idsucursal = u.idsucursal
LEFT JOIN tomaturno.usuario us ON us.id = u.idusuario
LEFT JOIN tomaturno.puesto p ON p.id = u.idpuesto AND p.idsucursal = u.idsucursal
LEFT JOIN tomaturno.catalogodeta c ON c.idcatalogo = 3 AND c.iddetalle = u.idestadocaja
LEFT JOIN tomaturno.catalogodeta c2 ON c2.idcatalogo = 4 AND c2.iddetalle = u.idtipodescanso
WHERE u.fechainicio::date = CURRENT_DATE;

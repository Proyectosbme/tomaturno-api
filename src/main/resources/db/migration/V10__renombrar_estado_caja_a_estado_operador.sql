-- "Estado de caja" fue un nombre engañoso: no todo operador maneja una caja física
-- (puede ser atención, asesor, etc.), todos comparten la misma pantalla/funcionalidad.
-- Se renombra el concepto completo a "estado del operador".

-- La vista depende de la tabla/columna que se va a renombrar — se recrea al final.
DROP VIEW IF EXISTS tomaturno.vwusuarioestadocaja;

ALTER TABLE IF EXISTS tomaturno.usuarioestadocaja RENAME TO usuarioestadooperador;
ALTER TABLE IF EXISTS tomaturno.usuarioestadooperador RENAME COLUMN idestadocaja TO idestadooperador;
ALTER INDEX IF EXISTS tomaturno.idx_usuarioestadocaja_actual RENAME TO idx_usuarioestadooperador_actual;

UPDATE tomaturno.catalogo
SET nombre = 'ESTADOS_OPERADOR', descripcion = 'Estados operativos del operador'
WHERE id = 3 AND nombre = 'ESTADOS_CAJA';

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
    u.fechainicio,
    u.fechafin
FROM tomaturno.usuarioestadooperador u
INNER JOIN tomaturno.usuario u2 ON u2.id = u.idusuario AND u2.idsucursal = u.idsucursal
LEFT JOIN tomaturno.usuario us ON us.id = u.idusuario
LEFT JOIN tomaturno.puesto p ON p.id = u.idpuesto AND p.idsucursal = u.idsucursal
LEFT JOIN tomaturno.catalogodeta c ON c.idcatalogo = 3 AND c.iddetalle = u.idestadooperador
LEFT JOIN tomaturno.catalogodeta c2 ON c2.idcatalogo = 4 AND c2.iddetalle = u.idtipodescanso
WHERE u.fechainicio::date = CURRENT_DATE;

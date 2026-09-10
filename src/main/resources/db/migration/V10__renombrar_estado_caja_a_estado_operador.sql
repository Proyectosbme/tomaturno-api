-- "Estado de caja" fue un nombre engañoso: no todo operador maneja una caja física
-- (puede ser atención, asesor, etc.), todos comparten la misma pantalla/funcionalidad.
-- Se renombra el concepto completo a "estado del operador".

-- La vista depende de la tabla/columna que se va a renombrar — se recrea al final.
DROP VIEW IF EXISTS tomaturno.vwusuarioestadocaja;
DROP VIEW IF EXISTS tomaturno.vwusuarioestadooperador;

-- Bloque DO: en local a veces se prueba el rename a mano por psql antes de correr la
-- migración (el mismo problema que tuvimos con CREATE TABLE en la V7), así que esto
-- debe converger al estado final sin importar si ya se aplicó parcial o totalmente.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'tomaturno' AND table_name = 'usuarioestadocaja') THEN
        IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'tomaturno' AND table_name = 'usuarioestadooperador') THEN
            -- Las dos existen: la nueva ya se probó a mano, la vieja se descarta.
            DROP TABLE tomaturno.usuarioestadocaja;
        ELSE
            ALTER TABLE tomaturno.usuarioestadocaja RENAME TO usuarioestadooperador;
        END IF;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = 'tomaturno' AND table_name = 'usuarioestadooperador' AND column_name = 'idestadocaja') THEN
        ALTER TABLE tomaturno.usuarioestadooperador RENAME COLUMN idestadocaja TO idestadooperador;
    END IF;
END $$;

ALTER INDEX IF EXISTS tomaturno.idx_usuarioestadocaja_actual RENAME TO idx_usuarioestadooperador_actual;

UPDATE tomaturno.catalogo
SET nombre = 'ESTADOS_OPERADOR', descripcion = 'Estados operativos del operador'
WHERE id = 3 AND nombre = 'ESTADOS_CAJA';

CREATE OR REPLACE VIEW tomaturno.vwusuarioestadooperador AS
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

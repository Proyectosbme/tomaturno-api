-- Secuencia para el id de referencia de turno (usado por idTurnoRelacionado).
-- Reemplaza el cálculo en la aplicación (MAX(id)+1 sobre toda la tabla), que
-- no es atómico y permite choques entre creaciones/reasignaciones concurrentes.
CREATE SEQUENCE IF NOT EXISTS tomaturno.seq_turno_id;

-- Arranca la secuencia después del mayor id ya existente para no repetir valores.
SELECT setval('tomaturno.seq_turno_id', COALESCE((SELECT MAX(id) FROM tomaturno.turno), 0) + 1, false);

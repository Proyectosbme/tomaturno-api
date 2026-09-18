-- Backfill de tomaturno.turno.fechatraslado para los traslados que ya existían antes de
-- V12__fecha_traslado_turno.sql: esa migración solo agregó la columna, pero no pudo
-- completarla retroactivamente porque el valor sale de otro turno (el original), no de
-- una constante que se pueda poner en el ALTER TABLE.
--
-- Para cada turno que es destino de un traslado (idturnorelacionado IS NOT NULL) se toma
-- la fechafinalizacion del turno original (orig.id = t.idturnorelacionado) y se guarda como
-- su fechatraslado — el mismo valor que Turno.reasignarA() ya calcula para los traslados
-- nuevos desde V12. Idempotente: solo toca filas que todavía están en NULL.

UPDATE tomaturno.turno t
SET fechatraslado = orig.fechafinalizacion
FROM tomaturno.turno orig
WHERE t.idturnorelacionado = orig.id
  AND t.idturnorelacionado IS NOT NULL
  AND t.fechatraslado IS NULL;

-- Datos iniciales del sistema

-- Catálogo: Estados de turno
INSERT INTO tomaturno.catalogo (id, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES (1, 'ESTADOS_TURNOS', 'Estados posibles de un turno', 'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (id) DO NOTHING;

-- Detalle catálogo: Estados de turno
INSERT INTO tomaturno.catalogodeta (idcatalogo, iddetalle, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES
    (1, 1, 'CREADO',      'Turno recién creado, en espera',                   'sistema', NOW(), NULL, NULL, 1),
    (1, 2, 'LLAMADO',     'Turno llamado, en atención',                       'sistema', NOW(), NULL, NULL, 1),
    (1, 3, 'TRASLADO',    'Turno trasladado a otra cola o sucursal',           'sistema', NOW(), NULL, NULL, 1),
    (1, 4, 'FINALIZADO',  'Turno finalizado y atendido',                      'sistema', NOW(), NULL, NULL, 1),
    (1, 5, 'SIN_ATENDER', 'Turno no atendido, el usuario no se presentó',     'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (idcatalogo, iddetalle) DO NOTHING;


-- Datos iniciales del sistema

-- Catálogo: Estados de turno
INSERT INTO tomaturno.catalogo (id, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES (2, 'CASOS ESPECIALES', 'Casos especiales de usuarios', 'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (id) DO NOTHING;

-- Detalle catálogo: Estados de turno
INSERT INTO tomaturno.catalogodeta (idcatalogo, iddetalle, nombre, descripcion, usuario_creacion, fecha_creacion, usuario_modificacion, fecha_modificacion, estado)
VALUES
    (2, 1, 'TERCERA EDAD',  'Personas de tercera edad (60 años o más)',  'sistema', NOW(), NULL, NULL, 1),
    (2, 2, 'EMBARAZADA',   'Mujer embarazada',                          'sistema', NOW(), NULL, NULL, 1),
    (2, 3, 'DISCAPACIDAD', 'Persona con alguna discapacidad',           'sistema', NOW(), NULL, NULL, 1),
    (2, 4, 'OTROS',        'Otro caso especial según visualización',    'sistema', NOW(), NULL, NULL, 1),
    (2, 5, 'NINGUNO',      'Persona sin condición especial',            'sistema', NOW(), NULL, NULL, 1)
ON CONFLICT (idcatalogo, iddetalle) DO NOTHING;
CREATE VIEW VWTURNOSHOY AS
select 
t.id ,  t.codigoturno ,tic.id as idsucursalticket, tic.nombre sucursalticket,
u.id idusuario, u.nombrecompleto ,u.codigousuario ,
p.id as idpuesto, p.idsucursal as idpuestosucursal,p.nombre as puesto,
c.id as idcola, c.nombre as cola , cd.iddetalle as idetalle, cd.nombre as detalle , 
t.tipocasoespecial, COALESCE(dt.nombre ,'NORMAL')  as caso_especial,
t.fechacreacion ,t.fechallamada , t.fechafinalizacion ,
t.idcatalogoestado ,t.idcatalogoestadodetalle   ,est.nombre as estado , t.idturnorelacionado 
from turno t
left join catalogodeta dt on dt.idcatalogo =2 and dt.iddetalle = t.tipocasoespecial 
left join catalogodeta est on est.idcatalogo = t.idcatalogoestado and est.iddetalle =t.idcatalogoestadodetalle
left join cola c on c.id = t.idcola 
left join coladetalle cd on cd.idcola = t.idcola  and cd.iddetalle = t.iddetalle 
left join sucursal tic on tic.id =t.idsucursal 
left join usuario u on u.id =t.idusuario and u.idsucursal = t.idsucursalpuesto 
left join puesto p on p.id = t.idpuesto and p.idsucursal =t.idsucursalpuesto 
WHERE t.fechacreacion::date = CURRENT_DATE 
order by t.fechacreacion;

CREATE MATERIALIZED VIEW VWTURNOS3MESES AS
select 
t.id ,  t.codigoturno ,tic.id as idsucursalticket, tic.nombre sucursalticket,
u.id idusuario, u.nombrecompleto ,u.codigousuario ,
p.id as idpuesto, p.idsucursal as idpuestosucursal,p.nombre as puesto,
c.id as idcola, c.nombre as cola , cd.iddetalle as idetalle, cd.nombre as detalle , 
t.tipocasoespecial, COALESCE(dt.nombre ,'NORMAL')  as caso_especial,
t.fechacreacion ,t.fechallamada , t.fechafinalizacion ,
t.idcatalogoestado ,t.idcatalogoestadodetalle   ,est.nombre as estado , t.idturnorelacionado 
from turno t
left join catalogodeta dt on dt.idcatalogo =2 and dt.iddetalle = t.tipocasoespecial 
left join catalogodeta est on est.idcatalogo = t.idcatalogoestado and est.iddetalle =t.idcatalogoestadodetalle
left join cola c on c.id = t.idcola 
left join coladetalle cd on cd.idcola = t.idcola  and cd.iddetalle = t.iddetalle 
left join sucursal tic on tic.id =t.idsucursal 
left join usuario u on u.id =t.idusuario and u.idsucursal = t.idsucursalpuesto 
left join puesto p on p.id = t.idpuesto and p.idsucursal =t.idsucursalpuesto 
WHERE t.fechacreacion >= CURRENT_DATE - INTERVAL '3 months' 
order by t.fechacreacion ;


-- Programar la actualización cada día a las 2 AM
SELECT cron.schedule('0 2 * * *', 'REFRESH MATERIALIZED VIEW tomaturno.VWTURNOS3MESES');
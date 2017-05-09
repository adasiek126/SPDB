select b.delaysec-a.delaysec as diff, a.delaysec as origin_delay, b.delaysec as destination_delay, to_char(a.realarrival, 'HH24:MI') as hour, 
a.orderincourse as origin_point, b.orderincourse as destination_point, a.daycourse_loid,
points_a.latitude as origin_lat, points_a.longitude as origin_lng, points_b.latitude as destination_lat, points_b.longitude destination_lng
from csipdaystopping a, csipdaystopping b,csipstoppoint points_a,csipstoppoint points_b
where a.delaysec is not null and b.delaysec is not null and points_a.loid = a.stoppoint_loid and points_b.loid = b.stoppoint_loid 
and cast(to_char(a.realarrival, 'HH24') as integer) between 14 and 15
and to_char(a.strapdate, 'dy') like 'tue' 
and to_char(a.strapdate,'YY-MM-DD')=to_char(b.strapdate,'YY-MM-DD') 
and a.orderincourse+1 = b.orderincourse 
and a.line_loid=b.line_loid and a.vehicle=b.vehicle
and a.daycourse_loid=b.daycourse_loid;
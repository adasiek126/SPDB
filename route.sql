select latitude, longitude from csipconnectionnode node left join csipconnection con on node.connection_loid=con.loid 
where con.fromstoppoint_loid=579 
and con.tostoppoint_loid=580 
order by orderno asc;
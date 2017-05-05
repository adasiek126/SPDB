package pl.edu.pw.elka.spdb.dao.route;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;

import java.util.List;

public interface IRouteDAO {
    List<Route> findFastestRoute(MapEntry start, MapEntry end);
    Route findRouteBetween(MapEntry start, MapEntry end);
    Route findRouteBetween(Long startId, Long endId);
}

package pl.edu.pw.elka.spdb.dao.publictransportroute;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;

import java.time.Duration;
import java.util.List;

public interface IPublicTransportRouteDAO {
    List<PublicTransportRoute> findFastestPublicTransportRoute(MapEntry start, MapEntry end,
                                                               Duration timeNeededToChange);

    PublicTransportRoute findPublicTransportRouteBetween(MapEntry start, MapEntry end);
    PublicTransportRoute findPublicTransportRouteBetween(Long startId, Long endId);
}

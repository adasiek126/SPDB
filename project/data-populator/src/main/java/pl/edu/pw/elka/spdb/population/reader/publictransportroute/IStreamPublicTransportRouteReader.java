package pl.edu.pw.elka.spdb.population.reader.publictransportroute;

import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;

import java.io.InputStream;
import java.util.Map;

public interface IStreamPublicTransportRouteReader {
    Map<String, PublicTransportRoute> readPublicTransportRoutes(Map<String, Route> routes,
                                                                InputStream publicTransportRoutesStream);
}

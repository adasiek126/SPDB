package pl.edu.pw.elka.spdb.population.reader.route;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;

import java.io.InputStream;
import java.util.Map;

public interface IStreamRouteReader {
    Map<String, Route> readRoutes(Map<String, MapEntry> entries, InputStream routesStream);
}

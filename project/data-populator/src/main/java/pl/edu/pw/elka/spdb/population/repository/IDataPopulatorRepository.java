package pl.edu.pw.elka.spdb.population.repository;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;

import java.util.List;

public interface IDataPopulatorRepository {
    List<MapEntry> getEntries();
    List<Route> getRoutes();
    List<PublicTransportRoute> getPublicTransportRoutes();
}

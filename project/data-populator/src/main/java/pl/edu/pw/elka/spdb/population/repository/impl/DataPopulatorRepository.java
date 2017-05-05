package pl.edu.pw.elka.spdb.population.repository.impl;

import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.population.repository.IDataPopulatorRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DataPopulatorRepository implements IDataPopulatorRepository {
    private List<MapEntry> entries;
    private List<Route> routes;

    @Override
    public List<MapEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();

            entries.add(new MapEntry(new Coordinates(52.220067, 21.012119), true));
            entries.add(new MapEntry(new Coordinates(52.220146, 21.004913), true));
            entries.add(new MapEntry(new Coordinates(52.223008, 21.004934), true));
            entries.add(new MapEntry(new Coordinates(52.227885, 21.001865), true));
            entries.add(new MapEntry(new Coordinates(52.230014, 21.011886), true));
            entries.add(new MapEntry(new Coordinates(52.219893, 21.018152), true));
            entries.add(new MapEntry(new Coordinates(52.223232, 21.015984), true));
            entries.add(new MapEntry(new Coordinates(52.226229, 21.014161), true));
            entries.add(new MapEntry(new Coordinates(52.228353, 21.010203), false));
        }

        return entries;
    }

    @Override
    public List<Route> getRoutes() {
        if (entries == null) {
            entries = getEntries();
        }

        if (routes == null) {
            routes = new ArrayList<>();

            routes.add(routeBetweenEntries(0, 1, Duration.ofMinutes(3)));
            routes.add(routeBetweenEntries(1, 2, Duration.ofMinutes(2)));
            routes.add(routeBetweenEntries(2, 3, Duration.ofMinutes(4)));
            routes.add(routeBetweenEntries(3, 4, Duration.ofMinutes(5)));
            routes.add(routeBetweenEntries(0, 5, Duration.ofMinutes(2)));
            routes.add(routeBetweenEntries(5, 6, Duration.ofMinutes(2)));
            routes.add(routeBetweenEntries(6, 7, Duration.ofMinutes(2)));
            routes.add(routeBetweenEntries(7, 4, Duration.ofMinutes(4)));
        }

        return routes;
    }

    @Override
    public List<PublicTransportRoute> getPublicTransportRoutes() {
        if (routes == null) {
            routes = getRoutes();
        }

        List<PublicTransportRoute> publicTransportRoutes = new ArrayList<>();

        publicTransportRoutes.add(new PublicTransportRoute(10, routes.get(0)));
        publicTransportRoutes.add(new PublicTransportRoute(10, routes.get(1)));
        publicTransportRoutes.add(new PublicTransportRoute(10, routes.get(2)));
        publicTransportRoutes.add(new PublicTransportRoute(9, routes.get(3)));
        publicTransportRoutes.add(new PublicTransportRoute(15, routes.get(4)));
        publicTransportRoutes.add(new PublicTransportRoute(15, routes.get(5)));
        publicTransportRoutes.add(new PublicTransportRoute(15, routes.get(6)));
        publicTransportRoutes.add(new PublicTransportRoute(15, routes.get(7)));

        return publicTransportRoutes;
    }

    private Route routeBetweenEntries(int indexFrom, int indexTo, Duration duration) {
        return entries.get(indexFrom).addRoute(entries.get(indexTo), duration);
    }
}

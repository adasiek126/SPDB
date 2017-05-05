package pl.edu.pw.elka.spdb.population.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.population.reader.entry.IStreamMapEntryReader;
import pl.edu.pw.elka.spdb.population.reader.publictransportroute.IStreamPublicTransportRouteReader;
import pl.edu.pw.elka.spdb.population.reader.route.IStreamRouteReader;
import pl.edu.pw.elka.spdb.population.repository.IDataPopulatorRepository;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamDataPopulatorRepository implements IDataPopulatorRepository {
    private final InputStream entriesStream;
    private final InputStream routesStream;
    private final InputStream publicTransportRoutesStream;

    @Autowired
    private IStreamMapEntryReader mapEntryReader;

    @Autowired
    private IStreamRouteReader routeReader;

    @Autowired
    private IStreamPublicTransportRouteReader publicTransportRouteReader;

    private Map<String, MapEntry> entries;
    private Map<String, Route> routes;
    private Map<String, PublicTransportRoute> publicTransportRoutes;

    public StreamDataPopulatorRepository(InputStream entriesStream, InputStream routesStream,
                                         InputStream publicTransportRoutesStream) {
        this.entriesStream = entriesStream;
        this.routesStream = routesStream;
        this.publicTransportRoutesStream = publicTransportRoutesStream;
    }

    @Override
    public List<MapEntry> getEntries() {
        if (entries == null) {
            entries = mapEntryReader.readEntries(entriesStream);
        }

        return entries.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    @Override
    public List<Route> getRoutes() {
        if (entries == null) {
            entries = mapEntryReader.readEntries(entriesStream);
        }

        if (routes == null) {
            routes = routeReader.readRoutes(entries, routesStream);
        }

        return routes.entrySet().stream().map(r -> r.getValue()).collect(Collectors.toList());
    }

    @Override
    public List<PublicTransportRoute> getPublicTransportRoutes() {
        if (entries == null) {
            entries = mapEntryReader.readEntries(entriesStream);
        }

        if (routes == null) {
            routes = routeReader.readRoutes(entries, routesStream);
        }

        if (publicTransportRoutes == null) {
            publicTransportRoutes = publicTransportRouteReader.readPublicTransportRoutes(routes,
                    publicTransportRoutesStream);
        }

        return publicTransportRoutes.entrySet().stream().map(r -> r.getValue()).collect(Collectors.toList());
    }
}

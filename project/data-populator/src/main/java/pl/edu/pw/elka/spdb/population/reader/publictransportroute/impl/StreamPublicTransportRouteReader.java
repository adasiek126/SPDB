package pl.edu.pw.elka.spdb.population.reader.publictransportroute.impl;

import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.population.reader.StreamReaderException;
import pl.edu.pw.elka.spdb.population.reader.publictransportroute.IStreamPublicTransportRouteReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class StreamPublicTransportRouteReader implements IStreamPublicTransportRouteReader {
    @Override
    public Map<String, PublicTransportRoute> readPublicTransportRoutes(Map<String, Route> routes, InputStream
            publicTransportRoutesStream) {
        Map<String, PublicTransportRoute> publicTransportRoutes = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(publicTransportRoutesStream))) {
            for (String line; (line = reader.readLine()) != null; ) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] splitLine = line.split("\\t+");

                if (splitLine.length != 3) {
                    String exceptionMessage = "Malformed input file, detected too " + (splitLine.length < 3 ?
                            "few" : "many") + " columns.";

                    throw new StreamReaderException(exceptionMessage);
                }

                String id = splitLine[0];
                int publicTransportLine = Integer.parseInt(splitLine[1]);
                Route route = routes.get(splitLine[2]);

                if (route == null) {
                    throw new StreamReaderException("No route exists with key: " + splitLine[2]);
                }

                publicTransportRoutes.put(id, new PublicTransportRoute(publicTransportLine, route));
            }
        } catch (IOException e) {
            throw new StreamReaderException("Could not read entries", e);
        }

        return publicTransportRoutes;
    }
}

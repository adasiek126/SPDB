package pl.edu.pw.elka.spdb.population.reader.route.impl;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.population.reader.StreamReaderException;
import pl.edu.pw.elka.spdb.population.reader.route.IStreamRouteReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class StreamRouteReader implements IStreamRouteReader {
    @Override
    public Map<String, Route> readRoutes(Map<String, MapEntry> entries, InputStream routesStream) {
        Map<String, Route> routes = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(routesStream))) {
            for (String line; (line = reader.readLine()) != null; ) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] splitLine = line.split("\\t+");

                if (splitLine.length != 4) {
                    String exceptionMessage = "Malformed input file, detected too " + (splitLine.length < 4 ?
                            "few" : "many") + " columns.";

                    throw new StreamReaderException(exceptionMessage);
                }

                String id = splitLine[0];
                MapEntry from = entries.get(splitLine[1]);
                MapEntry to = entries.get(splitLine[2]);
                Duration duration = Duration.ofSeconds(Long.parseLong(splitLine[3]));

                if (from == null) {
                    throw new StreamReaderException("No entry exists with key: " + splitLine[1]);
                } else if (to == null) {
                    throw new StreamReaderException("No entry exists with key: " + splitLine[2]);
                }

                routes.put(id, new Route(from, to, duration));
            }
        } catch (IOException e) {
            throw new StreamReaderException("Could not read entries", e);
        }

        return routes;
    }
}

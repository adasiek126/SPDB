package pl.edu.pw.elka.spdb.tests.population.reader.route.impl;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.population.reader.StreamReaderException;
import pl.edu.pw.elka.spdb.population.reader.route.IStreamRouteReader;
import pl.edu.pw.elka.spdb.population.reader.route.impl.StreamRouteReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class StreamRouteReaderTests extends TestCase {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testReadRoutesMethodTooFewColumnsInFileValidation() {
        String routes = "#\tid \t\tfrom\t\tto\t\t\ttravel_time_in_seconds\n" +
                "EN_0001\t\tEN_0002\t\t180\n" +
                "RO_0002\t\tEN_0002\t\tEN_0003\t\t120";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Map<String, MapEntry> entries = new LinkedHashMap<>();
        entries.put("EN_0001", firstEntry);
        entries.put("EN_0002", secondEntry);
        entries.put("EN_0003", thirdEntry);

        InputStream routesStream = new ByteArrayInputStream(routes.getBytes());
        IStreamRouteReader routeReader = new StreamRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("Malformed input file, detected too few columns.");

        routeReader.readRoutes(entries, routesStream);
    }

    @Test
    public void testReadRoutesMethodTooManyColumnsInFileValidation() {
        String routes = "#\tid \t\tfrom\t\tto\t\t\ttravel_time_in_seconds\n" +
                "RO_0001\t\tEN_0001\t\tEN_0002\t\t180\t\tOpis\n" +
                "RO_0002\t\tEN_0002\t\tEN_0003\t\t120";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Map<String, MapEntry> entries = new LinkedHashMap<>();
        entries.put("EN_0001", firstEntry);
        entries.put("EN_0002", secondEntry);
        entries.put("EN_0003", thirdEntry);

        InputStream routesStream = new ByteArrayInputStream(routes.getBytes());
        IStreamRouteReader routeReader = new StreamRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("Malformed input file, detected too many columns.");

        routeReader.readRoutes(entries, routesStream);
    }

    @Test
    public void testReadRoutesMethodFromRouteExistsValidation() {
        String routes = "#\tid \t\tfrom\t\tto\t\t\ttravel_time_in_seconds\n" +
                "RO_0001\t\tEN_0001\t\tEN_0002\t\t180\n" +
                "RO_0002\t\tEN_0002\t\tEN_0003\t\t120";

        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Map<String, MapEntry> entries = new LinkedHashMap<>();
        entries.put("EN_0002", secondEntry);
        entries.put("EN_0003", thirdEntry);

        InputStream routesStream = new ByteArrayInputStream(routes.getBytes());
        IStreamRouteReader routeReader = new StreamRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("No entry exists with key: EN_0001");

        routeReader.readRoutes(entries, routesStream);
    }

    @Test
    public void testReadRoutesMethodToRouteExistsValidation() {
        String routes = "#\tid \t\tfrom\t\tto\t\t\ttravel_time_in_seconds\n" +
                "RO_0001\t\tEN_0001\t\tEN_0002\t\t180\n" +
                "RO_0002\t\tEN_0002\t\tEN_0003\t\t120";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);

        Map<String, MapEntry> entries = new LinkedHashMap<>();
        entries.put("EN_0001", firstEntry);
        entries.put("EN_0002", secondEntry);

        InputStream routesStream = new ByteArrayInputStream(routes.getBytes());
        IStreamRouteReader routeReader = new StreamRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("No entry exists with key: EN_0003");

        routeReader.readRoutes(entries, routesStream);
    }

    @Test
    public void testReadRoutesMethod() {
        String routes = "#\tid \t\tfrom\t\tto\t\t\ttravel_time_in_seconds\n" +
                        "RO_0001\t\tEN_0001\t\tEN_0002\t\t180\n" +
                        "RO_0002\t\tEN_0002\t\tEN_0003\t\t120";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Map<String, MapEntry> entries = new LinkedHashMap<>();
        entries.put("EN_0001", firstEntry);
        entries.put("EN_0002", secondEntry);
        entries.put("EN_0003", thirdEntry);

        InputStream routesStream = new ByteArrayInputStream(routes.getBytes());
        IStreamRouteReader routeReader = new StreamRouteReader();

        Map<String, Route> readRoutes = routeReader.readRoutes(entries, routesStream);

        assertNotNull(readRoutes);
        assertEquals(2, readRoutes.size());
        assertTrue(readRoutes.containsKey("RO_0001"));
        assertEquals(firstEntry.getCoordinates(), readRoutes.get("RO_0001").getRouteFrom().getCoordinates());
        assertEquals(secondEntry.getCoordinates(), readRoutes.get("RO_0001").getRouteTo().getCoordinates());
        assertEquals(180, readRoutes.get("RO_0001").getDuration().getSeconds());
        assertTrue(readRoutes.containsKey("RO_0002"));
        assertEquals(secondEntry.getCoordinates(), readRoutes.get("RO_0002").getRouteFrom().getCoordinates());
        assertEquals(thirdEntry.getCoordinates(), readRoutes.get("RO_0002").getRouteTo().getCoordinates());
        assertEquals(120, readRoutes.get("RO_0002").getDuration().getSeconds());
    }
}

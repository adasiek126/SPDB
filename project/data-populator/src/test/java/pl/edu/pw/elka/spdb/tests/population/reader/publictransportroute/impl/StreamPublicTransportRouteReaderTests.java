package pl.edu.pw.elka.spdb.tests.population.reader.publictransportroute.impl;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.population.reader.StreamReaderException;
import pl.edu.pw.elka.spdb.population.reader.publictransportroute.IStreamPublicTransportRouteReader;
import pl.edu.pw.elka.spdb.population.reader.publictransportroute.impl.StreamPublicTransportRouteReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class StreamPublicTransportRouteReaderTests extends TestCase {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testReadPublicTransportRoutesMethodTooFewColumnsInFileValidation() {
        String publicTransportRoutes =  "#\tid \t\tline\troute\n" +
                                        "PTRO_0001\tRO_0001\n" +
                                        "PTRO_0002\t15\t\tRO_0002";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Route firstRoute = new Route(firstEntry, secondEntry, Duration.ofSeconds(180));
        Route secondRoute = new Route(secondEntry, thirdEntry, Duration.ofSeconds(120));

        Map<String, Route> routes = new LinkedHashMap<>();
        routes.put("RO_0001", firstRoute);
        routes.put("RO_0002", secondRoute);

        InputStream routesStream = new ByteArrayInputStream(publicTransportRoutes.getBytes());
        IStreamPublicTransportRouteReader publicTransportRouteReader = new StreamPublicTransportRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("Malformed input file, detected too few columns.");

        publicTransportRouteReader.readPublicTransportRoutes(routes, routesStream);
    }

    @Test
    public void testReadPublicTransportRoutesMethodTooManyColumnsInFileValidation() {
        String publicTransportRoutes =  "#\tid \t\tline\troute\n" +
                                        "PTRO_0001\t10\t\tRO_0001\n" +
                                        "PTRO_0002\t15\t\tRO_0002\tOpis opis\topis #2";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Route firstRoute = new Route(firstEntry, secondEntry, Duration.ofSeconds(180));
        Route secondRoute = new Route(secondEntry, thirdEntry, Duration.ofSeconds(120));

        Map<String, Route> routes = new LinkedHashMap<>();
        routes.put("RO_0001", firstRoute);
        routes.put("RO_0002", secondRoute);

        InputStream routesStream = new ByteArrayInputStream(publicTransportRoutes.getBytes());
        IStreamPublicTransportRouteReader publicTransportRouteReader = new StreamPublicTransportRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("Malformed input file, detected too many columns.");

        publicTransportRouteReader.readPublicTransportRoutes(routes, routesStream);
    }

    @Test
    public void testReadPublicTransportRoutesMethodRouteExistsValidation() {
        String publicTransportRoutes = "#\tid \t\tline\troute\n" +
                "PTRO_0001\t10\t\tRO_0001\n" +
                "PTRO_0002\t15\t\tRO_0002";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);

        Route firstRoute = new Route(firstEntry, secondEntry, Duration.ofSeconds(180));

        Map<String, Route> routes = new LinkedHashMap<>();
        routes.put("RO_0001", firstRoute);

        InputStream routesStream = new ByteArrayInputStream(publicTransportRoutes.getBytes());
        IStreamPublicTransportRouteReader publicTransportRouteReader = new StreamPublicTransportRouteReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("No route exists with key: RO_0002");

        publicTransportRouteReader.readPublicTransportRoutes(routes, routesStream);
    }

    @Test
    public void testReadPublicTransportRoutesMethod() {
        String publicTransportRoutes =  "#\tid \t\tline\troute\n" +
                                        "PTRO_0001\t10\t\tRO_0001\n" +
                                        "PTRO_0002\t15\t\tRO_0002";

        MapEntry firstEntry = new MapEntry(new Coordinates(52.220067, 21.012119), true);
        MapEntry secondEntry = new MapEntry(new Coordinates(52.220146, 21.004913), true);
        MapEntry thirdEntry = new MapEntry(new Coordinates(52.223008, 21.004934), true);

        Route firstRoute = new Route(firstEntry, secondEntry, Duration.ofSeconds(180));
        Route secondRoute = new Route(secondEntry, thirdEntry, Duration.ofSeconds(120));

        Map<String, Route> routes = new LinkedHashMap<>();
        routes.put("RO_0001", firstRoute);
        routes.put("RO_0002", secondRoute);

        InputStream routesStream = new ByteArrayInputStream(publicTransportRoutes.getBytes());
        IStreamPublicTransportRouteReader publicTransportRouteReader = new StreamPublicTransportRouteReader();

        Map<String, PublicTransportRoute> readRoutes =
                publicTransportRouteReader.readPublicTransportRoutes(routes, routesStream);

        assertNotNull(readRoutes);
        assertEquals(2, readRoutes.size());
        assertTrue(readRoutes.containsKey("PTRO_0001"));
        assertEquals(firstEntry.getCoordinates(), readRoutes.get("PTRO_0001").getRouteFrom().getCoordinates());
        assertEquals(secondEntry.getCoordinates(), readRoutes.get("PTRO_0001").getRouteTo().getCoordinates());
        assertEquals(10, readRoutes.get("PTRO_0001").getLine());
        assertEquals(180, readRoutes.get("PTRO_0001").getDuration().getSeconds());
        assertTrue(readRoutes.containsKey("PTRO_0002"));
        assertEquals(secondEntry.getCoordinates(), readRoutes.get("PTRO_0002").getRouteFrom().getCoordinates());
        assertEquals(thirdEntry.getCoordinates(), readRoutes.get("PTRO_0002").getRouteTo().getCoordinates());
        assertEquals(15, readRoutes.get("PTRO_0002").getLine());
        assertEquals(120, readRoutes.get("PTRO_0002").getDuration().getSeconds());
    }
}

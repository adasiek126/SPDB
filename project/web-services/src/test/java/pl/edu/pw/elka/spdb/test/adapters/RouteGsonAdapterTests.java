package pl.edu.pw.elka.spdb.test.adapters;

import junit.framework.TestCase;
import org.junit.Test;
import pl.edu.pw.elka.spdb.adapters.gson.RouteGsonAdapter;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;

import java.time.Duration;

public class RouteGsonAdapterTests extends TestCase {
    @Test
    public void testConstructor() {
        MapEntry entryFrom = new MapEntry(12L, new Coordinates(52.12, 23.11));
        MapEntry entryTo = new MapEntry(99L, new Coordinates(52.1233213, 21.12123));
        Route route = new Route(50L, entryFrom, entryTo, Duration.ofSeconds(400));

        RouteGsonAdapter routeGsonAdapter = new RouteGsonAdapter(route);

        assertNotNull(routeGsonAdapter.getId());
        assertEquals(50L, routeGsonAdapter.getId().longValue());
        assertNotNull(routeGsonAdapter.getRouteFrom());
        assertNotNull(routeGsonAdapter.getRouteFrom().getId());
        assertEquals(12L, routeGsonAdapter.getRouteFrom().getId().longValue());
        assertEquals(52.12, routeGsonAdapter.getRouteFrom().getLatitude());
        assertEquals(23.11, routeGsonAdapter.getRouteFrom().getLongitude());
        assertNotNull(routeGsonAdapter.getRouteTo());
        assertNotNull(routeGsonAdapter.getRouteTo().getId());
        assertEquals(99L, routeGsonAdapter.getRouteTo().getId().longValue());
        assertEquals(52.1233213, routeGsonAdapter.getRouteTo().getLatitude());
        assertEquals(21.12123, routeGsonAdapter.getRouteTo().getLongitude());
        assertNotNull(routeGsonAdapter.getDuration());
        assertEquals(400L, routeGsonAdapter.getDuration());
    }

    @Test
    public void testToRouteMethod() {
        MapEntry entryFrom = new MapEntry(12L, new Coordinates(52.12, 23.11));
        MapEntry entryTo = new MapEntry(99L, new Coordinates(52.1233213, 21.12123));
        Route route = new Route(50L, entryFrom, entryTo, Duration.ofSeconds(400));
        RouteGsonAdapter routeGsonAdapter = new RouteGsonAdapter(route);

        Route generatedRoute = routeGsonAdapter.toRoute();

        assertNotNull(generatedRoute.getId());
        assertEquals(50L, generatedRoute.getId().longValue());
        assertNotNull(generatedRoute.getRouteFrom());
        assertNotNull(generatedRoute.getRouteFrom().getId());
        assertEquals(12L, generatedRoute.getRouteFrom().getId().longValue());
        assertNotNull(generatedRoute.getRouteFrom().getCoordinates());
        assertEquals(52.12, generatedRoute.getRouteFrom().getCoordinates().getLatitude());
        assertEquals(23.11, generatedRoute.getRouteFrom().getCoordinates().getLongitude());
        assertNotNull(generatedRoute.getRouteTo());
        assertNotNull(generatedRoute.getRouteTo().getId());
        assertEquals(99L, generatedRoute.getRouteTo().getId().longValue());
        assertNotNull(generatedRoute.getRouteTo().getCoordinates());
        assertEquals(52.1233213, generatedRoute.getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.12123, generatedRoute.getRouteTo().getCoordinates().getLongitude());
        assertNotNull(generatedRoute.getDuration());
        assertEquals(Duration.ofSeconds(400), generatedRoute.getDuration());
    }
}

package pl.edu.pw.elka.spdb.test.providers;

import junit.framework.TestCase;
import org.junit.Test;
import pl.edu.pw.elka.spdb.adapters.list.RouteListAdapter;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.providers.RouteListProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.ArrayList;

public class RouteListProviderTests extends TestCase {
    @Test
    public void testWriteToMethod() {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            RouteListProvider provider = new RouteListProvider();
            RouteListAdapter adapter = new RouteListAdapter(new ArrayList<>());
            MapEntry firstMapEntry = new MapEntry(new Coordinates(52.1234, 21.6789));
            firstMapEntry.setId(16L);
            MapEntry secondMapEntry = new MapEntry(new Coordinates(52.6789, 21.1234));
            secondMapEntry.setId(17L);
            Route routeForwards = firstMapEntry.addRoute(secondMapEntry, Duration.ofMinutes(5));
            Route routeBackwards = secondMapEntry.addRoute(firstMapEntry, Duration.ofMinutes(5));
            adapter.getRoutes().add(routeForwards);
            adapter.getRoutes().add(routeBackwards);

            provider.writeTo(adapter, null, null, null, null, null, outputStream);
            String routeAsJson = outputStream.toString();

            assertEquals("[{\"routeFrom\":{\"id\":16,\"latitude\":52.1234,\"longitude\":21.6789}," +
                            "\"routeTo\":{\"id\":17,\"latitude\":52.6789,\"longitude\":21.1234},\"duration\":300}," +
                            "{\"routeFrom\":{\"id\":17,\"latitude\":52.6789,\"longitude\":21.1234}," +
                            "\"routeTo\":{\"id\":16,\"latitude\":52.1234,\"longitude\":21.6789},\"duration\":300}]",
                    routeAsJson
            );
        } catch (Exception ex) {
            fail("Exception was thrown");
        }
    }

    @Test
    public void testReadFromMethod() {
        try (InputStream inputStream = new ByteArrayInputStream((
                "[{\"routeFrom\":{\"id\":16,\"latitude\":52.1234,\"longitude\":21.6789}," +
                        "\"routeTo\":{\"id\":17,\"latitude\":52.6789,\"longitude\":21.1234},\"duration\":300}," +
                        "{\"routeFrom\":{\"id\":17,\"latitude\":52.6789,\"longitude\":21.1234}," +
                        "\"routeTo\":{\"id\":16,\"latitude\":52.1234,\"longitude\":21.6789}," +
                        "\"duration\":300}]").getBytes())) {
            RouteListProvider provider = new RouteListProvider();
            RouteListAdapter readRoutes = provider.readFrom(RouteListAdapter.class, null, null, null, null,
                    inputStream);

            assertEquals(2, readRoutes.getRoutes().size());
            assertEquals(16L, readRoutes.getRoutes().get(0).getRouteFrom().getId().longValue());
            assertEquals(17L, readRoutes.getRoutes().get(0).getRouteTo().getId().longValue());
            assertEquals(52.1234, readRoutes.getRoutes().get(0).getRouteFrom().getCoordinates().getLatitude());
            assertEquals(21.6789, readRoutes.getRoutes().get(0).getRouteFrom().getCoordinates().getLongitude());
            assertEquals(52.6789, readRoutes.getRoutes().get(0).getRouteTo().getCoordinates().getLatitude());
            assertEquals(21.1234, readRoutes.getRoutes().get(0).getRouteTo().getCoordinates().getLongitude());
            assertEquals(300L, readRoutes.getRoutes().get(0).getDuration().getSeconds());
            assertEquals(17L, readRoutes.getRoutes().get(1).getRouteFrom().getId().longValue());
            assertEquals(16L, readRoutes.getRoutes().get(1).getRouteTo().getId().longValue());
            assertEquals(52.6789, readRoutes.getRoutes().get(1).getRouteFrom().getCoordinates().getLatitude());
            assertEquals(21.1234, readRoutes.getRoutes().get(1).getRouteFrom().getCoordinates().getLongitude());
            assertEquals(52.1234, readRoutes.getRoutes().get(1).getRouteTo().getCoordinates().getLatitude());
            assertEquals(21.6789, readRoutes.getRoutes().get(1).getRouteTo().getCoordinates().getLongitude());
            assertEquals(300L, readRoutes.getRoutes().get(1).getDuration().getSeconds());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown");
        }
    }
}

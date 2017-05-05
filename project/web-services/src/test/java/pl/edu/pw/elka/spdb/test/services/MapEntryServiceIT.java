package pl.edu.pw.elka.spdb.test.services;

import junit.framework.TestCase;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import pl.edu.pw.elka.spdb.adapters.list.PublicTransportRouteListAdapter;
import pl.edu.pw.elka.spdb.adapters.list.RouteListAdapter;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.providers.MapEntryProvider;
import pl.edu.pw.elka.spdb.providers.PublicTransportRouteListProvider;
import pl.edu.pw.elka.spdb.providers.RouteListProvider;

import javax.ws.rs.core.Response;
import java.util.List;

public class MapEntryServiceIT extends TestCase {
    private static String endpointUrl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        endpointUrl = System.getProperty("service.url");
    }

    @Test
    public void testGetNearestMapEntry() throws Exception {
        JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
        clientFactoryBean.setAddress(endpointUrl + "/entry/nearest/52.2206062/21.0105747/publicTransportStop/false");
        clientFactoryBean.setProvider(new MapEntryProvider());
        WebClient client = clientFactoryBean.createWebClient();

        Response response = client.accept("application/json").get();
        MapEntry mapEntry = response.readEntity(MapEntry.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(52.220067, mapEntry.getCoordinates().getLatitude());
        assertEquals(21.012119, mapEntry.getCoordinates().getLongitude());
    }

    @Test
    public void testGetNearestPublicTransportStop() throws Exception {
        JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
        clientFactoryBean.setAddress(endpointUrl + "/entry/nearest/52.228353/21.010203/publicTransportStop/true");
        clientFactoryBean.setProvider(new MapEntryProvider());
        WebClient client = clientFactoryBean.createWebClient();

        Response response = client.accept("application/json").get();
        MapEntry mapEntry = response.readEntity(MapEntry.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(52.230014, mapEntry.getCoordinates().getLatitude());
        assertEquals(21.011886, mapEntry.getCoordinates().getLongitude());
    }

    @Test
    public void testGetShortestPath() throws Exception {
        Long startingId = getMapEntryId(52.220067, 21.012119, false);
        Long finishingId = getMapEntryId(52.230014, 21.011886, false);
        String relativeUrl = String.format("/entry/shortestPath/%d/%d/publicTransport/false", startingId, finishingId);
        JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
        clientFactoryBean.setAddress(endpointUrl + relativeUrl);
        clientFactoryBean.setProvider(new RouteListProvider());
        WebClient client = clientFactoryBean.createWebClient();
        Response response = client.accept("application/json").get();
        List<Route> routes = response.readEntity(RouteListAdapter.class).getRoutes();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(4, routes.size());

        assertEquals(52.220067, routes.get(0).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.012119, routes.get(0).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.219893, routes.get(0).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.018152, routes.get(0).getRouteTo().getCoordinates().getLongitude());

        assertEquals(52.219893, routes.get(1).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.018152, routes.get(1).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.223232, routes.get(1).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.015984, routes.get(1).getRouteTo().getCoordinates().getLongitude());

        assertEquals(52.223232, routes.get(2).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.015984, routes.get(2).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.226229, routes.get(2).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.014161, routes.get(2).getRouteTo().getCoordinates().getLongitude());

        assertEquals(52.226229, routes.get(3).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.014161, routes.get(3).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.230014, routes.get(3).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.011886, routes.get(3).getRouteTo().getCoordinates().getLongitude());
    }

    private Long getMapEntryId(double latitude, double longitude, boolean publicTransport) throws Exception {
        String relativeUrl = String.format("/entry/nearest/%f/%f/publicTransportStop/" + publicTransport, latitude,
                longitude).replace(",", ".");
        JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
        clientFactoryBean.setAddress(endpointUrl + relativeUrl);
        clientFactoryBean.setProvider(new MapEntryProvider());
        WebClient client = clientFactoryBean.createWebClient();
        Response response = client.accept("application/json").get();
        MapEntry entry = response.readEntity(MapEntry.class);

        return entry.getId();
    }

    @Test
    public void testGetShortestPublicTransportPath() throws Exception {
        Long startingId = getMapEntryId(52.220067, 21.012119, true);
        Long finishingId = getMapEntryId(52.230014, 21.011886, true);
        String relativeUrl = String.format("/entry/shortestPath/%d/%d/publicTransport/true/changeDuration/120",
                startingId, finishingId);
        JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
        clientFactoryBean.setAddress(endpointUrl + relativeUrl);
        clientFactoryBean.setProvider(new PublicTransportRouteListProvider());
        WebClient client = clientFactoryBean.createWebClient();
        Response response = client.accept("application/json").get();
        List<PublicTransportRoute> routes = response.readEntity(PublicTransportRouteListAdapter.class).getRoutes();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(4, routes.size());

        assertEquals(15, routes.get(0).getLine());
        assertEquals(52.220067, routes.get(0).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.012119, routes.get(0).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.219893, routes.get(0).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.018152, routes.get(0).getRouteTo().getCoordinates().getLongitude());

        assertEquals(15, routes.get(1).getLine());
        assertEquals(52.219893, routes.get(1).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.018152, routes.get(1).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.223232, routes.get(1).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.015984, routes.get(1).getRouteTo().getCoordinates().getLongitude());

        assertEquals(15, routes.get(2).getLine());
        assertEquals(52.223232, routes.get(2).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.015984, routes.get(2).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.226229, routes.get(2).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.014161, routes.get(2).getRouteTo().getCoordinates().getLongitude());

        assertEquals(15, routes.get(3).getLine());
        assertEquals(52.226229, routes.get(3).getRouteFrom().getCoordinates().getLatitude());
        assertEquals(21.014161, routes.get(3).getRouteFrom().getCoordinates().getLongitude());
        assertEquals(52.230014, routes.get(3).getRouteTo().getCoordinates().getLatitude());
        assertEquals(21.011886, routes.get(3).getRouteTo().getCoordinates().getLongitude());
    }
}

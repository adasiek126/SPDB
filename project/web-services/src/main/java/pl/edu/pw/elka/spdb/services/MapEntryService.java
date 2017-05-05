package pl.edu.pw.elka.spdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.elka.spdb.adapters.list.PublicTransportRouteListAdapter;
import pl.edu.pw.elka.spdb.adapters.list.RouteListAdapter;
import pl.edu.pw.elka.spdb.dao.entry.IMapEntryDAO;
import pl.edu.pw.elka.spdb.dao.publictransportroute.IPublicTransportRouteDAO;
import pl.edu.pw.elka.spdb.dao.route.IRouteDAO;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.List;

@Path("/entry")
public class MapEntryService {
    @Autowired
    private IMapEntryDAO mapEntryDAO;

    @Autowired
    private IRouteDAO routeDAO;

    @Autowired
    private IPublicTransportRouteDAO publicTransportRouteDAO;

    @GET
    @Path("/nearest/{latitude}/{longitude}/publicTransportStop/{publicTransportStop}")
    @Produces("application/json")
    public Response getNearestMapEntry(@PathParam("latitude") double latitude,
                                       @PathParam("longitude") double longitude,
                                       @PathParam("publicTransportStop") boolean publicTransportStop) {
        MapEntry nearestMapEntry;

        if (publicTransportStop) {
            nearestMapEntry = mapEntryDAO.findNearestPublicTransportStop(latitude, longitude);
        } else {
            nearestMapEntry = mapEntryDAO.findNearestMapEntry(latitude, longitude);
        }

        Response response = Response.ok().entity(nearestMapEntry).build();

        return response;
    }

    @GET
    @Path("/shortestPath/{startingNodeId}/{finishingNodeId}/publicTransport/{publicTransport}{changeDuration:" +
            "(/changeDuration/[^/]+?)?}")
    @Produces("application/json")
    public Response getShortestPath(@PathParam("startingNodeId") long startingNodeId,
                                    @PathParam("finishingNodeId") long finishingNodeId,
                                    @PathParam("publicTransport") boolean publicTransport,
                                    @PathParam("changeDuration") String changeDuration) {
        MapEntry startingNode = mapEntryDAO.findMapEntryById(startingNodeId);
        MapEntry finishingNode = mapEntryDAO.findMapEntryById(finishingNodeId);

        if (!publicTransport) {
            List<Route> shortestPath = routeDAO.findFastestRoute(startingNode, finishingNode);

            Response response = Response.ok().entity(new RouteListAdapter(shortestPath)).build();
            return response;
        } else {
            List<PublicTransportRoute> shortestPath = publicTransportRouteDAO.findFastestPublicTransportRoute
                    (startingNode, finishingNode, Duration.ofSeconds(parseChangeDurationParameter(changeDuration)));

            Response response = Response.ok().entity(new PublicTransportRouteListAdapter(shortestPath)).build();
            return response;
        }
    }

    private long parseChangeDurationParameter(String changeDuration) {
        if (changeDuration == null || changeDuration.isEmpty()) {
            return 0L;
        }

        return Long.valueOf(changeDuration.split("/")[1]);
    }
}

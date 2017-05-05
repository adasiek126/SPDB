package pl.edu.pw.elka.spdb.adapters.list;

import pl.edu.pw.elka.spdb.model.PublicTransportRoute;

import java.util.List;

public class PublicTransportRouteListAdapter {
    private List<PublicTransportRoute> routes;

    public PublicTransportRouteListAdapter(List<PublicTransportRoute> routes) {
        this.routes = routes;
    }

    public List<PublicTransportRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<PublicTransportRoute> routes) {
        this.routes = routes;
    }
}

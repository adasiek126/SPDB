package pl.edu.pw.elka.spdb.adapters.list;

import pl.edu.pw.elka.spdb.model.Route;

import java.util.List;

public class RouteListAdapter {
    private List<Route> routes;

    public RouteListAdapter(List<Route> routes) {
        this.routes = routes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}

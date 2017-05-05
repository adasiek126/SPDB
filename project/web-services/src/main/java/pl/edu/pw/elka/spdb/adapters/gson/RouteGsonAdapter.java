package pl.edu.pw.elka.spdb.adapters.gson;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;

import java.time.Duration;

public class RouteGsonAdapter {
    private Long id;
    private MapEntryGsonAdapter routeFrom;
    private MapEntryGsonAdapter routeTo;
    private long duration;

    public RouteGsonAdapter(Route route) {
        this.id = route.getId();

        if (route.getRouteFrom() != null) {
            this.routeFrom = new MapEntryGsonAdapter(route.getRouteFrom());
        }

        if (route.getRouteTo() != null) {
            this.routeTo = new MapEntryGsonAdapter(route.getRouteTo());
        }

        if (route.getDuration() != null) {
            this.duration = route.getDuration().getSeconds();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MapEntryGsonAdapter getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(MapEntryGsonAdapter routeFrom) {
        this.routeFrom = routeFrom;
    }

    public MapEntryGsonAdapter getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(MapEntryGsonAdapter routeTo) {
        this.routeTo = routeTo;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Route toRoute() {
        MapEntry routeFrom = null;
        MapEntry routeTo = null;

        if (this.routeFrom != null) {
            routeFrom = this.routeFrom.toMapEntry();
        }

        if (this.routeTo != null) {
            routeTo = this.routeTo.toMapEntry();
        }

        return new Route(id, routeFrom, routeTo, Duration.ofSeconds(duration));
    }
}

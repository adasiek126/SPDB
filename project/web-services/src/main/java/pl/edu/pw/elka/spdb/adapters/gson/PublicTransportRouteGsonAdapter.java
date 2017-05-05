package pl.edu.pw.elka.spdb.adapters.gson;

import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;

import java.time.Duration;

public class PublicTransportRouteGsonAdapter {
    private Long id;
    private int line;
    private MapEntryGsonAdapter routeFrom;
    private MapEntryGsonAdapter routeTo;
    private long duration;

    public PublicTransportRouteGsonAdapter(PublicTransportRoute publicTransportRoute) {
        this.id = publicTransportRoute.getId();
        this.line = publicTransportRoute.getLine();

        if (publicTransportRoute.getRouteFrom() != null) {
            this.routeFrom = new MapEntryGsonAdapter(publicTransportRoute.getRouteFrom());
        }

        if (publicTransportRoute.getRouteTo() != null) {
            this.routeTo = new MapEntryGsonAdapter(publicTransportRoute.getRouteTo());
        }

        if (publicTransportRoute.getDuration() != null) {
            this.duration = publicTransportRoute.getDuration().getSeconds();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
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

    public PublicTransportRoute toRoute() {
        MapEntry routeFrom = null;
        MapEntry routeTo = null;

        if (this.routeFrom != null) {
            routeFrom = this.routeFrom.toMapEntry();
        }

        if (this.routeTo != null) {
            routeTo = this.routeTo.toMapEntry();
        }

        return new PublicTransportRoute(id, line, new Route(id, routeFrom, routeTo, Duration.ofSeconds(duration)));
    }
}

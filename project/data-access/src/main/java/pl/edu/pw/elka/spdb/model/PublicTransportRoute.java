package pl.edu.pw.elka.spdb.model;

import org.springframework.data.neo4j.annotation.*;

import java.time.Duration;

@RelationshipEntity(type = "PUBLIC_TRANSPORT_TO")
public class PublicTransportRoute {
    @GraphId
    private Long id;

    private int line;

    @StartNode
    private MapEntry routeFrom;

    @EndNode
    private MapEntry routeTo;

    @GraphProperty(propertyType = Long.class)
    private Duration duration;

    public PublicTransportRoute() {
    }

    public PublicTransportRoute(int line, Route route) {
        this(null, line, route);
    }

    public PublicTransportRoute(Long id, int line, Route route) {
        this.id = id;
        this.line = line;
        this.routeFrom = route.getRouteFrom();
        this.routeTo = route.getRouteTo();
        this.duration = route.getDuration();
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

    public MapEntry getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(MapEntry routeFrom) {
        this.routeFrom = routeFrom;
    }

    public MapEntry getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(MapEntry routeTo) {
        this.routeTo = routeTo;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}

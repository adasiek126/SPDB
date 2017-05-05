package pl.edu.pw.elka.spdb.model;

import org.springframework.data.neo4j.annotation.*;

import java.time.Duration;

@RelationshipEntity(type = "ROUTES_TO")
public class Route {
    @GraphId
    private Long id;
    @StartNode
    private MapEntry routeFrom;
    @EndNode
    private MapEntry routeTo;
    @GraphProperty(propertyType = Long.class)
    private Duration duration;

    public Route() {
    }

    public Route(MapEntry routeFrom, MapEntry routeTo, Duration duration) {
        this(null, routeFrom, routeTo, duration);
    }

    public Route(Long id, MapEntry routeFrom, MapEntry routeTo, Duration duration) {
        this.id = id;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

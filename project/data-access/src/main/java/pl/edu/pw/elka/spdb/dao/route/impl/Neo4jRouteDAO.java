package pl.edu.pw.elka.spdb.dao.route.impl;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.elka.spdb.dao.route.IRouteDAO;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;
import pl.edu.pw.elka.spdb.relationships.MapEntryRelationships;
import pl.edu.pw.elka.spdb.repositories.IMapEntryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class Neo4jRouteDAO implements IRouteDAO {
    @Autowired
    private IMapEntryRepository mapEntryRepository;
    @Autowired
    private Neo4jTemplate template;

    @Override
    public List<Route> findFastestRoute(MapEntry start, MapEntry end) {
        List<Route> fastestRoute = new ArrayList<>();
        Node startNode = template.getNode(start.getId());
        Node endNode = template.getNode(end.getId());

        WeightedPath shortestPath = getShortestPath(startNode, endNode);

        if (shortestPath != null) {
            shortestPath.relationships().forEach(rel -> fastestRoute.add(findRouteBetween(rel.getStartNode().getId(),
                    rel.getEndNode().getId())));
        }

        return fastestRoute;
    }

    private WeightedPath getShortestPath(Node start, Node end) {
        PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(PathExpanders.forTypeAndDirection
                (DynamicRelationshipType.withName(MapEntryRelationships.ROUTES_TO.getValue()),
                        Direction.OUTGOING), "duration");

        return finder.findSinglePath(start, end);
    }

    @Override
    public Route findRouteBetween(MapEntry start, MapEntry end) {
        Route route = mapEntryRepository.getRelationshipBetween(start, end, Route.class,
                MapEntryRelationships.ROUTES_TO.getValue());

        if (route != null) {
            route.setRouteFrom(start);
            route.setRouteTo(end);
        }

        return route;
    }

    @Override
    public Route findRouteBetween(Long startId, Long endId) {
        return findRouteBetween(mapEntryRepository.findOne(startId), mapEntryRepository.findOne(endId));
    }
}

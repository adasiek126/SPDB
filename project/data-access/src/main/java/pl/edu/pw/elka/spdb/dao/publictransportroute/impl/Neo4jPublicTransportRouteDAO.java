package pl.edu.pw.elka.spdb.dao.publictransportroute.impl;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.elka.spdb.dao.publictransportroute.IPublicTransportRouteDAO;
import pl.edu.pw.elka.spdb.evaluators.PublicTransportCostEvaluator;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.relationships.MapEntryRelationships;
import pl.edu.pw.elka.spdb.repositories.IMapEntryRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class Neo4jPublicTransportRouteDAO implements IPublicTransportRouteDAO {
    @Autowired
    private IMapEntryRepository mapEntryRepository;
    @Autowired
    private Neo4jTemplate template;

    @Override
    public List<PublicTransportRoute> findFastestPublicTransportRoute(MapEntry start, MapEntry end, Duration
            timeNeededToChange) {
        validateFindFastestPublicTransportRouteArguments(start, end);

        List<PublicTransportRoute> fastestRoute = new ArrayList<>();
        Node startNode = template.getNode(start.getId());
        Node endNode = template.getNode(end.getId());

        WeightedPath shortestPath = getShortestPublicTransportPath(startNode, endNode, timeNeededToChange);

        if (shortestPath != null) {
            shortestPath.relationships().forEach(rel -> fastestRoute.add(
                    findPublicTransportRouteBetween(rel.getStartNode().getId(), rel.getEndNode().getId())));
        }

        return fastestRoute;
    }

    private void validateFindFastestPublicTransportRouteArguments(MapEntry start, MapEntry end) {
        if (!start.getPublicTransportStop()) {
            throw new IllegalArgumentException("Public transport route must start with a bus stop!");
        } else if (!end.getPublicTransportStop()) {
            throw new IllegalArgumentException("Public transport route must finish with a bus stop!");
        }
    }

    private WeightedPath getShortestPublicTransportPath(Node start, Node end, Duration timeNeededToChange) {
        RelationshipType publicTransportRelationship = DynamicRelationshipType.withName(
                MapEntryRelationships.PUBLIC_TRANSPORT_TO.getValue());

        PathExpander expander = PathExpanders.forTypeAndDirection(publicTransportRelationship, Direction.OUTGOING);
        PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(expander,
                new PublicTransportCostEvaluator(timeNeededToChange));

        return finder.findSinglePath(start, end);
    }

    @Override
    public PublicTransportRoute findPublicTransportRouteBetween(MapEntry start, MapEntry end) {
        PublicTransportRoute route = mapEntryRepository.getRelationshipBetween(start, end, PublicTransportRoute.class,
                MapEntryRelationships.PUBLIC_TRANSPORT_TO.getValue());

        if (route != null) {
            route.setRouteFrom(start);
            route.setRouteTo(end);
        }

        return route;
    }

    @Override
    public PublicTransportRoute findPublicTransportRouteBetween(Long startId, Long endId) {
        return findPublicTransportRouteBetween(mapEntryRepository.findOne(startId), mapEntryRepository.findOne(endId));
    }
}

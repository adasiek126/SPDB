package pl.edu.pw.elka.spdb.evaluators;

import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import pl.edu.pw.elka.spdb.relationships.MapEntryRelationships;

import java.time.Duration;
import java.util.Iterator;

public class PublicTransportCostEvaluator implements CostEvaluator<Double> {
    private Duration timeNeededToChange;

    public PublicTransportCostEvaluator(Duration timeNeededToChange) {
        this.timeNeededToChange = timeNeededToChange;
    }

    @Override
    public Double getCost(Relationship relationship, Direction direction) {
        if (direction == Direction.INCOMING) {
            return 0.0;
        }

        Node startNode = relationship.getStartNode();

        Iterator<Relationship> relationshipIterator = startNode.getRelationships(DynamicRelationshipType.withName
                (MapEntryRelationships.PUBLIC_TRANSPORT_TO.getValue()), Direction.INCOMING).iterator();

        boolean needChange = true;

        while (relationshipIterator.hasNext()) {
            Relationship previousRelationship = relationshipIterator.next();

            if (previousRelationship.getProperty("line").equals(relationship.getProperty("line"))) {
                needChange = false;
                break;
            }
        }

        Double cost = ((Long) relationship.getProperty("duration")).doubleValue();

        if (needChange) {
            cost += timeNeededToChange.getSeconds();
        }

        return cost;
    }
}

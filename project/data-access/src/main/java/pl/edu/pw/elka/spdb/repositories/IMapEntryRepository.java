package pl.edu.pw.elka.spdb.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;
import org.springframework.data.neo4j.repository.SpatialRepository;
import pl.edu.pw.elka.spdb.model.MapEntry;

public interface IMapEntryRepository extends GraphRepository<MapEntry>, RelationshipOperationsRepository<MapEntry>,
        SpatialRepository<MapEntry> {
}

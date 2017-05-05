package pl.edu.pw.elka.spdb.dao.entry.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.elka.spdb.configuration.IConfigurationProvider;
import pl.edu.pw.elka.spdb.dao.entry.IMapEntryDAO;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.repositories.IMapEntryRepository;

import java.util.Iterator;

@Service
@Transactional
public class Neo4jMapEntryDAO implements IMapEntryDAO {
    @Autowired
    private IMapEntryRepository mapEntryRepository;
    @Autowired
    private IConfigurationProvider configurationProvider;

    @Override
    public MapEntry insertMapEntry(MapEntry mapEntry) {
        return mapEntryRepository.save(mapEntry);
    }

    @Override
    public MapEntry findMapEntryById(Long id) {
        return mapEntryRepository.findOne(id);
    }

    @Override
    public MapEntry findNearestMapEntry(double latitude, double longitude) {
        EndResult<MapEntry> entriesWithinDistance = mapEntryRepository.findWithinDistance("MapEntryLocation", latitude,
                longitude, configurationProvider.getSearchRadius());

        Iterator<MapEntry> entryIterator = entriesWithinDistance.iterator();

        while (entryIterator.hasNext()) {
            return entryIterator.next();
        }

        return null;
    }

    @Override
    public MapEntry findNearestPublicTransportStop(double latitude, double longitude) {
        EndResult<MapEntry> entriesWithinDistance = mapEntryRepository.findWithinDistance("MapEntryLocation", latitude,
                longitude, configurationProvider.getSearchRadius());

        Iterator<MapEntry> entryIterator = entriesWithinDistance.iterator();

        while (entryIterator.hasNext()) {
            MapEntry entry = mapEntryRepository.findOne(entryIterator.next().getId());

            if (entry.getPublicTransportStop()) {
                return entry;
            }
        }

        return null;
    }
}

package pl.edu.pw.elka.spdb.population.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.elka.spdb.dao.entry.IMapEntryDAO;
import pl.edu.pw.elka.spdb.population.DataPopulatorException;
import pl.edu.pw.elka.spdb.population.IDataPopulator;
import pl.edu.pw.elka.spdb.population.repository.IDataPopulatorRepository;

@Transactional
public class DataPopulator implements IDataPopulator {
    @Autowired
    private IDataPopulatorRepository repository;

    @Autowired
    private IMapEntryDAO mapEntryDAO;

    @Autowired
    private Neo4jTemplate template;

    @Override
    public void clearData() {
        try {
            emptyDatabase();
        } catch (Exception e) {
            throw new DataPopulatorException("Could not clear database", e);
        }
    }

    @Override
    public void populate() {
        try {
            populateEntries();
            populateRoutes();
            populatePublicTransportRoutes();
        } catch (Exception e) {
            throw new DataPopulatorException("Could not populate database", e);
        }
    }

    private void emptyDatabase() {
        Neo4jHelper.cleanDb(template);
    }

    private void populateEntries() {
        repository.getEntries().forEach(mapEntryDAO::insertMapEntry);
    }

    private void populateRoutes() {
        repository.getRoutes().forEach(template::save);
    }

    private void populatePublicTransportRoutes() {
        repository.getPublicTransportRoutes().forEach(template::save);
    }
}

package pl.edu.pw.elka.spdb.tests.dao.entry.impl;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.dao.entry.IMapEntryDAO;
import pl.edu.pw.elka.spdb.model.MapEntry;

import java.time.Duration;

@ContextConfiguration(locations = "classpath:/spring/testContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class Neo4jMapEntryDAOTests extends TestCase {
    @Autowired
    private IMapEntryDAO mapEntryDAO;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void cleanUp() {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void testInsertAndFindLocationMethods() {
        MapEntry universityOfTechnology = new MapEntry(new Coordinates(52.2206062, 21.0105747));

        Long mapEntryId = mapEntryDAO.insertMapEntry(universityOfTechnology).getId();
        MapEntry foundMapEntry = mapEntryDAO.findMapEntryById(mapEntryId);

        assertNotNull(foundMapEntry);
        assertEquals(universityOfTechnology, foundMapEntry);
    }

    @Test
    public void testAddRouteMethod() {
        MapEntry universityOfTechnology = new MapEntry(new Coordinates(52.2206062, 21.0105747));
        MapEntry subway = new MapEntry(new Coordinates(52.2190664, 21.0153627));
        MapEntry mlocinyUndergroundStation = new MapEntry(new Coordinates(52.290513, 20.930355));
        universityOfTechnology = mapEntryDAO.insertMapEntry(universityOfTechnology);
        subway = mapEntryDAO.insertMapEntry(subway);
        mlocinyUndergroundStation = mapEntryDAO.insertMapEntry(mlocinyUndergroundStation);
        template.save(universityOfTechnology.addRoute(subway, Duration.ofMinutes(5)));
        template.save(subway.addRoute(universityOfTechnology, Duration.ofMinutes(5)));

        universityOfTechnology = mapEntryDAO.findMapEntryById(universityOfTechnology.getId());
        subway = mapEntryDAO.findMapEntryById(subway.getId());
        mlocinyUndergroundStation = mapEntryDAO.findMapEntryById(mlocinyUndergroundStation.getId());

        assertTrue(universityOfTechnology.routesTo(subway));
        assertTrue(subway.routesTo(universityOfTechnology));
        assertFalse(universityOfTechnology.routesTo(mlocinyUndergroundStation));
        assertFalse(mlocinyUndergroundStation.routesTo(universityOfTechnology));
        assertFalse(subway.routesTo(mlocinyUndergroundStation));
        assertFalse(mlocinyUndergroundStation.routesTo(subway));
    }

    @Test
    public void testGetTravelTimeMethod() {
        MapEntry universityOfTechnology = new MapEntry(new Coordinates(52.2206062, 21.0105747));
        MapEntry subway = new MapEntry(new Coordinates(52.2190664, 21.0153627));
        MapEntry mlocinyUndergroundStation = new MapEntry(new Coordinates(52.290513, 20.930355));
        universityOfTechnology = mapEntryDAO.insertMapEntry(universityOfTechnology);
        subway = mapEntryDAO.insertMapEntry(subway);
        mlocinyUndergroundStation = mapEntryDAO.insertMapEntry(mlocinyUndergroundStation);
        template.save(universityOfTechnology.addRoute(subway, Duration.ofMinutes(5)));

        universityOfTechnology = mapEntryDAO.findMapEntryById(universityOfTechnology.getId());
        subway = mapEntryDAO.findMapEntryById(subway.getId());
        mlocinyUndergroundStation = mapEntryDAO.findMapEntryById(mlocinyUndergroundStation.getId());

        assertTrue(universityOfTechnology.getTravelTime(subway).equals(Duration.ofMinutes(5)));
        assertNull(universityOfTechnology.getTravelTime(mlocinyUndergroundStation));
    }

    @Test
    public void testFindNearestMapEntryMethod() {
        MapEntry saviourSquare = new MapEntry(new Coordinates(52.219929, 21.017988));
        MapEntry subway = new MapEntry(new Coordinates(52.2190664, 21.0153627));
        MapEntry mlocinyUndergroundStation = new MapEntry(new Coordinates(52.290513, 20.930355));
        mapEntryDAO.insertMapEntry(saviourSquare);
        subway = mapEntryDAO.insertMapEntry(subway);
        mapEntryDAO.insertMapEntry(mlocinyUndergroundStation);

        MapEntry nearestToUniversityOfTechnology = mapEntryDAO.findNearestMapEntry(52.2206062, 21.0105747);

        assertNotNull(nearestToUniversityOfTechnology);
        assertEquals(subway.getId(), nearestToUniversityOfTechnology.getId());
    }

    @Test
    public void testFindNearestPublicTransportStopMethod() {
        MapEntry saviourSquare = new MapEntry(new Coordinates(52.219929, 21.017988), true);
        MapEntry subway = new MapEntry(new Coordinates(52.2190664, 21.0153627), false);
        MapEntry mlocinyUndergroundStation = new MapEntry(new Coordinates(52.290513, 20.930355), true);
        saviourSquare = mapEntryDAO.insertMapEntry(saviourSquare);
        mapEntryDAO.insertMapEntry(subway);
        mapEntryDAO.insertMapEntry(mlocinyUndergroundStation);

        MapEntry nearestStopToUniversityOfTechnology =
                mapEntryDAO.findNearestPublicTransportStop(52.2206062, 21.0105747);

        assertNotNull(nearestStopToUniversityOfTechnology);
        assertEquals(saviourSquare.getId(), nearestStopToUniversityOfTechnology.getId());
    }
}

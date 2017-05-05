package pl.edu.pw.elka.spdb.tests.dao.route.impl;

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
import pl.edu.pw.elka.spdb.dao.route.IRouteDAO;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.Route;

import java.time.Duration;
import java.util.List;

@ContextConfiguration(locations = "classpath:/spring/testContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class Neo4jRouteDAOTests extends TestCase {
    @Autowired
    private IMapEntryDAO mapEntryDAO;

    @Autowired
    private IRouteDAO routeDAO;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void cleanUp() {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void testFindRouteBetweenMethod() {
        MapEntry universityOfTechnology = new MapEntry(new Coordinates(52.2206062, 21.0105747));
        MapEntry saviourSquare = new MapEntry(new Coordinates(52.219929, 21.017988));
        universityOfTechnology = mapEntryDAO.insertMapEntry(universityOfTechnology);
        saviourSquare = mapEntryDAO.insertMapEntry(saviourSquare);
        Route universityToSaviourSquareRoute = universityOfTechnology.addRoute(saviourSquare, Duration.ofMinutes(3));
        universityToSaviourSquareRoute = template.save(universityToSaviourSquareRoute);

        Route foundFromUniversityToSaviourSquare = routeDAO.findRouteBetween(universityOfTechnology, saviourSquare);
        Route foundFromSaviourSquareToUniversity = routeDAO.findRouteBetween(saviourSquare, universityOfTechnology);

        assertNotNull(foundFromUniversityToSaviourSquare);
        assertEquals(universityToSaviourSquareRoute.getId(), foundFromUniversityToSaviourSquare.getId());
        assertEquals(universityToSaviourSquareRoute.getDuration(), foundFromUniversityToSaviourSquare.getDuration());
        assertEquals(universityOfTechnology.getWkt(), foundFromUniversityToSaviourSquare.getRouteFrom().getWkt());
        assertEquals(saviourSquare.getWkt(), foundFromUniversityToSaviourSquare.getRouteTo().getWkt());
        assertNull(foundFromSaviourSquareToUniversity);
    }

    @Test
    public void testFindRouteBetweenByIdMethod() {
        MapEntry universityOfTechnology = new MapEntry(new Coordinates(52.2206062, 21.0105747));
        MapEntry saviourSquare = new MapEntry(new Coordinates(52.219929, 21.017988));
        universityOfTechnology = mapEntryDAO.insertMapEntry(universityOfTechnology);
        saviourSquare = mapEntryDAO.insertMapEntry(saviourSquare);
        Route universityToSaviourSquareRoute = universityOfTechnology.addRoute(saviourSquare, Duration.ofMinutes(3));
        universityToSaviourSquareRoute = template.save(universityToSaviourSquareRoute);

        Route foundRouteFromUniversityToSaviourSquare = routeDAO.findRouteBetween(universityOfTechnology.getId(),
                saviourSquare.getId());
        Route foundRouteFromSaviourSquareToUniversity = routeDAO.findRouteBetween(saviourSquare.getId(),
                universityOfTechnology.getId());

        assertNotNull(foundRouteFromUniversityToSaviourSquare);
        assertEquals(universityToSaviourSquareRoute.getId(), foundRouteFromUniversityToSaviourSquare.getId());
        assertEquals(universityToSaviourSquareRoute.getDuration(),
                foundRouteFromUniversityToSaviourSquare.getDuration());
        assertEquals(universityOfTechnology.getWkt(), foundRouteFromUniversityToSaviourSquare.getRouteFrom().getWkt());
        assertEquals(saviourSquare.getWkt(), foundRouteFromUniversityToSaviourSquare.getRouteTo().getWkt());
        assertNull(foundRouteFromSaviourSquareToUniversity);
    }

    @Test
    public void testFindFastestRouteMethod() {
        MapEntry universityOfTechnology = new MapEntry(new Coordinates(52.2206062, 21.0105747));
        MapEntry saviourSquare = new MapEntry(new Coordinates(52.219929, 21.017988));
        MapEntry centralUndergroundStation = new MapEntry(new Coordinates(52.229896, 21.011701));
        MapEntry independenceStreet = new MapEntry(new Coordinates(52.220159, 21.005006));
        MapEntry goldenTerrace = new MapEntry(new Coordinates(52.227809, 21.001938));
        universityOfTechnology = mapEntryDAO.insertMapEntry(universityOfTechnology);
        saviourSquare = mapEntryDAO.insertMapEntry(saviourSquare);
        centralUndergroundStation = mapEntryDAO.insertMapEntry(centralUndergroundStation);
        independenceStreet = mapEntryDAO.insertMapEntry(independenceStreet);
        goldenTerrace = mapEntryDAO.insertMapEntry(goldenTerrace);
        template.save(universityOfTechnology.addRoute(saviourSquare, Duration.ofMinutes(5)));
        template.save(saviourSquare.addRoute(centralUndergroundStation, Duration.ofMinutes(10)));
        template.save(universityOfTechnology.addRoute(independenceStreet, Duration.ofMinutes(3)));
        template.save(independenceStreet.addRoute(goldenTerrace, Duration.ofMinutes(5)));
        template.save(goldenTerrace.addRoute(centralUndergroundStation, Duration.ofMinutes(3)));

        List<Route> fastestRoute = routeDAO.findFastestRoute(universityOfTechnology, centralUndergroundStation);

        assertEquals(3, fastestRoute.size());
        assertEquals(universityOfTechnology.getId(), fastestRoute.get(0).getRouteFrom().getId());
        assertEquals(independenceStreet.getId(), fastestRoute.get(0).getRouteTo().getId());
        assertEquals(independenceStreet.getId(), fastestRoute.get(1).getRouteFrom().getId());
        assertEquals(goldenTerrace.getId(), fastestRoute.get(1).getRouteTo().getId());
        assertEquals(goldenTerrace.getId(), fastestRoute.get(2).getRouteFrom().getId());
        assertEquals(centralUndergroundStation.getId(), fastestRoute.get(2).getRouteTo().getId());
    }
}

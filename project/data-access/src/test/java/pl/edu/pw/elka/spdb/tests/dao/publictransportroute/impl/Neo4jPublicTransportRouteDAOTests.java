package pl.edu.pw.elka.spdb.tests.dao.publictransportroute.impl;

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
import pl.edu.pw.elka.spdb.dao.publictransportroute.IPublicTransportRouteDAO;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.model.PublicTransportRoute;
import pl.edu.pw.elka.spdb.model.Route;

import java.time.Duration;
import java.util.List;

@ContextConfiguration(locations = "classpath:/spring/testContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class Neo4jPublicTransportRouteDAOTests extends TestCase {
    @Autowired
    private IMapEntryDAO mapEntryDAO;

    @Autowired
    private IPublicTransportRouteDAO publicTransportRouteDAO;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void cleanUp() {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void testFindPublicTransportRouteBetweenMethod() {
        MapEntry universitySquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2200113, 21.0120177),
                true));
        MapEntry saviourSquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.219929, 21.017988), true));
        MapEntry constitutionSquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.222285, 21.016180),
                true));
        PublicTransportRoute universityToSaviourSquareRoute = new PublicTransportRoute(15,
                universitySquare.addRoute(saviourSquare, Duration.ofMinutes(3)));
        template.save(universityToSaviourSquareRoute);

        PublicTransportRoute foundRouteFromUniversityToSaviourSquare =
                publicTransportRouteDAO.findPublicTransportRouteBetween(universitySquare, saviourSquare);
        PublicTransportRoute foundRouteFromSaviourToUniversitySquare =
                publicTransportRouteDAO.findPublicTransportRouteBetween(saviourSquare, universitySquare);
        PublicTransportRoute foundRouteFromConstitutionToUniversitySquare =
                publicTransportRouteDAO.findPublicTransportRouteBetween(constitutionSquare, universitySquare);

        assertNotNull(foundRouteFromUniversityToSaviourSquare);
        assertEquals(universitySquare, foundRouteFromUniversityToSaviourSquare.getRouteFrom());
        assertEquals(saviourSquare, foundRouteFromUniversityToSaviourSquare.getRouteTo());
        assertEquals(Duration.ofMinutes(3), foundRouteFromUniversityToSaviourSquare.getDuration());
        assertEquals(15, foundRouteFromUniversityToSaviourSquare.getLine());
        assertNull(foundRouteFromSaviourToUniversitySquare);
        assertNull(foundRouteFromConstitutionToUniversitySquare);
    }

    @Test
    public void testFindPublicTransportRouteBetweenByIdMethod() {
        MapEntry universitySquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2200113, 21.0120177),
                true));
        MapEntry saviourSquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.219929, 21.017988), true));
        MapEntry constitutionSquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.222285, 21.016180),
                true));
        PublicTransportRoute universityToSaviourSquareRoute = new PublicTransportRoute(15,
                universitySquare.addRoute(saviourSquare, Duration.ofMinutes(3)));
        template.save(universityToSaviourSquareRoute);

        PublicTransportRoute foundRouteFromUniversityToSaviourSquare =
                publicTransportRouteDAO.findPublicTransportRouteBetween(universitySquare.getId(),
                        saviourSquare.getId());
        PublicTransportRoute foundRouteFromSaviourToUniversitySquare =
                publicTransportRouteDAO.findPublicTransportRouteBetween(saviourSquare.getId(),
                        universitySquare.getId());
        PublicTransportRoute foundRouteFromConstitutionToUniversitySquare =
                publicTransportRouteDAO.findPublicTransportRouteBetween(constitutionSquare.getId(),
                        universitySquare.getId());

        assertNotNull(foundRouteFromUniversityToSaviourSquare);
        assertEquals(universitySquare, foundRouteFromUniversityToSaviourSquare.getRouteFrom());
        assertEquals(saviourSquare, foundRouteFromUniversityToSaviourSquare.getRouteTo());
        assertEquals(Duration.ofMinutes(3), foundRouteFromUniversityToSaviourSquare.getDuration());
        assertEquals(15, foundRouteFromUniversityToSaviourSquare.getLine());
        assertNull(foundRouteFromSaviourToUniversitySquare);
        assertNull(foundRouteFromConstitutionToUniversitySquare);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFastestPublicTransportRouteMethodStartValidation() {
        MapEntry universitySquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2200113, 21.0120177),
                true));
        MapEntry subway = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2190664, 21.0153627)));
        template.save(universitySquare.addRoute(subway, Duration.ofMinutes(1)));

        publicTransportRouteDAO.findFastestPublicTransportRoute(universitySquare, subway, Duration.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFastestPublicTransportRouteMethodEndValidation() {
        MapEntry subway = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2190664, 21.0153627)));
        MapEntry universitySquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2200113, 21.0120177),
                true));
        template.save(subway.addRoute(universitySquare, Duration.ofMinutes(1)));

        publicTransportRouteDAO.findFastestPublicTransportRoute(subway, universitySquare, Duration.ZERO);
    }

    @Test
    public void testFindFastestPublicTransportRouteMethod() {
        MapEntry universitySquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.2200113, 21.0120177),
                true));
        MapEntry universityUndergroundStation = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.219932,
                21.015511), true));
        MapEntry saviourSquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.219929, 21.017988), true));
        MapEntry constitutionSquare = mapEntryDAO.insertMapEntry(new MapEntry(new Coordinates(52.222285, 21.016180),
                true));
        Route universitySquareToUnderground = template.save(universitySquare.addRoute(universityUndergroundStation,
                Duration.ofMinutes(2)));
        Route universityUndergroundToSaviourSquare = template.save(universityUndergroundStation.addRoute
                (saviourSquare, Duration.ofMinutes(2)));
        Route universityUndergroundToConstitutionSquare = template.save(universityUndergroundStation.addRoute
                (constitutionSquare, Duration.ofMinutes(2)));
        Route saviourSquareToConstitutionSquare = template.save(saviourSquare.addRoute(constitutionSquare,
                Duration.ofMinutes(2)));
        template.save(new PublicTransportRoute(15, universitySquareToUnderground));
        template.save(new PublicTransportRoute(15, universityUndergroundToSaviourSquare));
        template.save(new PublicTransportRoute(502, universityUndergroundToConstitutionSquare));
        template.save(new PublicTransportRoute(15, saviourSquareToConstitutionSquare));

        List<PublicTransportRoute> routeFromUniversityToConstitutionSquareNoDelay = publicTransportRouteDAO
                .findFastestPublicTransportRoute(universitySquare, constitutionSquare, Duration.ofSeconds(0));
        List<PublicTransportRoute> routeFromUniversityToConstitutionSquareWithDelay = publicTransportRouteDAO
                .findFastestPublicTransportRoute(universitySquare, constitutionSquare, Duration.ofMinutes(3));

        assertNotNull(routeFromUniversityToConstitutionSquareNoDelay);
        assertEquals(2, routeFromUniversityToConstitutionSquareNoDelay.size());
        assertEquals(15, routeFromUniversityToConstitutionSquareNoDelay.get(0).getLine());
        assertEquals(universitySquare, routeFromUniversityToConstitutionSquareNoDelay.get(0).getRouteFrom());
        assertEquals(universityUndergroundStation, routeFromUniversityToConstitutionSquareNoDelay.get(0).getRouteTo());
        assertEquals(502, routeFromUniversityToConstitutionSquareNoDelay.get(1).getLine());
        assertEquals(universityUndergroundStation,
                routeFromUniversityToConstitutionSquareNoDelay.get(1).getRouteFrom());
        assertEquals(constitutionSquare, routeFromUniversityToConstitutionSquareNoDelay.get(1).getRouteTo());
        assertNotNull(routeFromUniversityToConstitutionSquareWithDelay);
        assertEquals(3, routeFromUniversityToConstitutionSquareWithDelay.size());
        assertEquals(15, routeFromUniversityToConstitutionSquareWithDelay.get(0).getLine());
        assertEquals(universitySquare, routeFromUniversityToConstitutionSquareWithDelay.get(0).getRouteFrom());
        assertEquals(universityUndergroundStation,
                routeFromUniversityToConstitutionSquareWithDelay.get(0).getRouteTo());
        assertEquals(15, routeFromUniversityToConstitutionSquareWithDelay.get(1).getLine());
        assertEquals(universityUndergroundStation,
                routeFromUniversityToConstitutionSquareWithDelay.get(1).getRouteFrom());
        assertEquals(saviourSquare, routeFromUniversityToConstitutionSquareWithDelay.get(1).getRouteTo());
        assertEquals(15, routeFromUniversityToConstitutionSquareWithDelay.get(2).getLine());
        assertEquals(saviourSquare, routeFromUniversityToConstitutionSquareWithDelay.get(2).getRouteFrom());
        assertEquals(constitutionSquare, routeFromUniversityToConstitutionSquareWithDelay.get(2).getRouteTo());
    }
}

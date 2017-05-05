package pl.edu.pw.elka.spdb.tests.model;

import junit.framework.TestCase;
import org.junit.Test;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;

public class MapEntryTests extends TestCase {
    @Test
    public void testSetCoordinatesMethod() {
        MapEntry mapEntry = new MapEntry();
        mapEntry.setCoordinates(new Coordinates(52.1234, 22.67891));

        String wkt = mapEntry.getWkt();

        assertEquals("POINT( 52.12340000 22.67891000 )", wkt);
    }

    @Test
    public void testGetCoordinatesMethod() {
        MapEntry mapEntry = new MapEntry(new Coordinates(52.224466, 22.6699));

        Coordinates coordinates = mapEntry.getCoordinates();

        assertEquals(52.224466, coordinates.getLatitude());
        assertEquals(22.6699, coordinates.getLongitude());
    }
}

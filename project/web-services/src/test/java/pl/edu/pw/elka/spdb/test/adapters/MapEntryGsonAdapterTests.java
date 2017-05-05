package pl.edu.pw.elka.spdb.test.adapters;

import junit.framework.TestCase;
import org.junit.Test;
import pl.edu.pw.elka.spdb.adapters.gson.MapEntryGsonAdapter;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;

public class MapEntryGsonAdapterTests extends TestCase {
    @Test
    public void testConstructor() {
        MapEntry mapEntry = new MapEntry(14L, new Coordinates(51.1382, 29.2621));

        MapEntryGsonAdapter mapEntryGsonAdapter = new MapEntryGsonAdapter(mapEntry);

        assertNotNull(mapEntryGsonAdapter.getId());
        assertEquals(14L, mapEntryGsonAdapter.getId().longValue());
        assertEquals(51.1382, mapEntryGsonAdapter.getLatitude());
        assertEquals(29.2621, mapEntryGsonAdapter.getLongitude());
    }

    @Test
    public void testToMapEntryMethod() {
        MapEntry mapEntry = new MapEntry(15L, new Coordinates(52.1823, 22.12763));
        MapEntryGsonAdapter mapEntryGsonAdapter = new MapEntryGsonAdapter(mapEntry);

        MapEntry generatedMapEntry = mapEntryGsonAdapter.toMapEntry();

        assertNotNull(generatedMapEntry.getId());
        assertEquals(15L, generatedMapEntry.getId().longValue());
        assertNotNull(generatedMapEntry.getCoordinates());
        assertEquals(52.1823, generatedMapEntry.getCoordinates().getLatitude());
        assertEquals(22.12763, generatedMapEntry.getCoordinates().getLongitude());
        assertEquals("POINT( 52.18230000 22.12763000 )", generatedMapEntry.getWkt());
    }
}

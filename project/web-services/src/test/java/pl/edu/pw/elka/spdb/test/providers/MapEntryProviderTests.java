package pl.edu.pw.elka.spdb.test.providers;

import junit.framework.TestCase;
import org.junit.Test;
import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.providers.MapEntryProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;

public class MapEntryProviderTests extends TestCase {
    @Test
    public void testWriteToMethod() {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            MapEntryProvider provider = new MapEntryProvider();
            MapEntry mapEntry = new MapEntry(new Coordinates(52.1234, 21.6789));
            mapEntry.setId(16L);
            mapEntry.addRoute(mapEntry, Duration.ofMinutes(5));

            provider.writeTo(mapEntry, null, null, null, null, null, outputStream);
            String mapEntryAsJson = outputStream.toString();

            assertEquals("{\"id\":16,\"latitude\":52.1234,\"longitude\":21.6789}", mapEntryAsJson);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown");
        }
    }

    @Test
    public void testReadFromMethod() {
        try (InputStream inputStream = new ByteArrayInputStream((
                "{\"id\":16,\"latitude\":52.1234,\"longitude\":21.6789}").getBytes())) {
            MapEntryProvider provider = new MapEntryProvider();
            MapEntry readEntry = provider.readFrom(MapEntry.class, null, null, null, null, inputStream);

            assertEquals(16L, readEntry.getId().longValue());
            assertEquals(52.1234, readEntry.getCoordinates().getLatitude());
            assertEquals(21.6789, readEntry.getCoordinates().getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown");
        }
    }
}

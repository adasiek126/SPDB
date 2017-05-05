package pl.edu.pw.elka.spdb.tests.population.reader.entry.impl;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.population.reader.StreamReaderException;
import pl.edu.pw.elka.spdb.population.reader.entry.IStreamMapEntryReader;
import pl.edu.pw.elka.spdb.population.reader.entry.impl.StreamMapEntryReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@RunWith(JUnit4.class)
public class StreamMapEntryReaderTests extends TestCase {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testReadEntriesMethodTooFewColumnsInFileValidation() {
        String entries = "#\tid \t\tlatitude\tlongitude\tbus_stop\tdescription \n" +
                "EN_0001\t\t52.220067\t21.012119\ttrue\t\tPlac Politechniki\n" +
                "52.220146\t21.004913\tfalse\t\tSkrzyżowanie Nowowiejskiej i Niepodległości\n" +
                "EN_0003\t\t52.223008\t21.004934\ttrue\t\tSkrzyżowanie Koszykowej i Chałubińskiego";

        InputStream entriesStream = new ByteArrayInputStream(entries.getBytes());
        IStreamMapEntryReader streamMapEntryReader = new StreamMapEntryReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("Malformed input file, detected too few columns.");

        streamMapEntryReader.readEntries(entriesStream);
    }

    @Test
    public void testReadEntriesMethodTooManyColumnsInFileValidation() {
        String entries = "#\tid \t\tlatitude\tlongitude\tbus_stop\tdescription \n" +
                "EN_0001\t\t52.220067\t21.012119\ttrue\t\tPlac Politechniki\n" +
                "EN_0001\t\t52.220146\t21.004913\tfalse\t\tSkrzyżowanie Nowowiejskiej i Niepodległości\tfails_test\n" +
                "EN_0003\t\t52.223008\t21.004934\ttrue\t\tSkrzyżowanie Koszykowej i Chałubińskiego";

        InputStream entriesStream = new ByteArrayInputStream(entries.getBytes());
        IStreamMapEntryReader streamMapEntryReader = new StreamMapEntryReader();

        thrown.expect(StreamReaderException.class);
        thrown.expectMessage("Malformed input file, detected too many columns.");

        streamMapEntryReader.readEntries(entriesStream);
    }

    @Test
    public void testReadEntriesMethod() {
        String entries =    "#\tid \t\tlatitude\tlongitude\tbus_stop\tdescription \n" +
                            "EN_0001\t\t52.220067\t21.012119\ttrue\t\tPlac Politechniki\n" +
                            "EN_0002\t\t52.220146\t21.004913\tfalse\t\tSkrzyżowanie Nowowiejskiej i Niepodległości\n" +
                            "EN_0003\t\t52.223008\t21.004934\ttrue\t\tSkrzyżowanie Koszykowej i Chałubińskiego";

        InputStream entriesStream = new ByteArrayInputStream(entries.getBytes());
        IStreamMapEntryReader streamMapEntryReader = new StreamMapEntryReader();

        Map<String, MapEntry> mapEntries = streamMapEntryReader.readEntries(entriesStream);

        assertNotNull(mapEntries);
        assertEquals(3, mapEntries.size());
        assertNotNull(mapEntries.get("EN_0001"));
        assertEquals(52.220067, mapEntries.get("EN_0001").getCoordinates().getLatitude());
        assertEquals(21.012119, mapEntries.get("EN_0001").getCoordinates().getLongitude());
        assertTrue(mapEntries.get("EN_0001").getPublicTransportStop());
        assertNotNull(mapEntries.get("EN_0002"));
        assertEquals(52.220146, mapEntries.get("EN_0002").getCoordinates().getLatitude());
        assertEquals(21.004913, mapEntries.get("EN_0002").getCoordinates().getLongitude());
        assertFalse(mapEntries.get("EN_0002").getPublicTransportStop());
        assertNotNull(mapEntries.get("EN_0003"));
        assertEquals(52.223008, mapEntries.get("EN_0003").getCoordinates().getLatitude());
        assertEquals(21.004934, mapEntries.get("EN_0003").getCoordinates().getLongitude());
        assertTrue(mapEntries.get("EN_0003").getPublicTransportStop());
    }
}

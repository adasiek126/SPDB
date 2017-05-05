package pl.edu.pw.elka.spdb.population.reader.entry;

import pl.edu.pw.elka.spdb.model.MapEntry;

import java.io.InputStream;
import java.util.Map;

public interface IStreamMapEntryReader {
    Map<String, MapEntry> readEntries(InputStream entriesStream);
}

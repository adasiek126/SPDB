package pl.edu.pw.elka.spdb.population.reader.entry.impl;

import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;
import pl.edu.pw.elka.spdb.population.reader.StreamReaderException;
import pl.edu.pw.elka.spdb.population.reader.entry.IStreamMapEntryReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class StreamMapEntryReader implements IStreamMapEntryReader {
    @Override
    public Map<String, MapEntry> readEntries(InputStream entriesStream) {
        Map<String, MapEntry> entries = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entriesStream))) {
            for (String line; (line = reader.readLine()) != null; ) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] splitLine = line.split("\\t+");

                if (splitLine.length != 5) {
                    String exceptionMessage = "Malformed input file, detected too " + (splitLine.length < 5 ?
                            "few" : "many") + " columns.";

                    throw new StreamReaderException(exceptionMessage);
                }

                String id = splitLine[0];
                double latitude = Double.parseDouble(splitLine[1]);
                double longitude = Double.parseDouble(splitLine[2]);
                boolean busStop = Boolean.parseBoolean(splitLine[3]);

                entries.put(id, new MapEntry(new Coordinates(latitude, longitude), busStop));
            }
        } catch (IOException e) {
            throw new StreamReaderException("Could not read entries", e);
        }

        return entries;
    }
}

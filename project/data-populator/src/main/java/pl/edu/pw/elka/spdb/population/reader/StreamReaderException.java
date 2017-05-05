package pl.edu.pw.elka.spdb.population.reader;

public class StreamReaderException extends RuntimeException {
    public StreamReaderException(String message) {
        super(message);
    }

    public StreamReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}

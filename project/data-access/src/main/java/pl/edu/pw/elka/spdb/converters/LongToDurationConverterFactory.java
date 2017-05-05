package pl.edu.pw.elka.spdb.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.time.Duration;

public class LongToDurationConverterFactory implements ConverterFactory<Long, Duration> {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Duration> Converter<Long, T> getConverter(Class<T> type) {
        return seconds -> (T) Duration.ofSeconds(seconds);
    }
}

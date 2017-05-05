package pl.edu.pw.elka.spdb.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.time.Duration;

public class DurationToLongConverterFactory implements ConverterFactory<Duration, Long> {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Long> Converter<Duration, T> getConverter(Class<T> type) {
        return duration -> (T) new Long(duration.getSeconds());
    }
}

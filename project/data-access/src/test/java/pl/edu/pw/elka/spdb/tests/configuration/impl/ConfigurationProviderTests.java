package pl.edu.pw.elka.spdb.tests.configuration.impl;

import junit.framework.TestCase;
import org.junit.Test;
import pl.edu.pw.elka.spdb.configuration.IConfigurationProvider;
import pl.edu.pw.elka.spdb.configuration.impl.ConfigurationProvider;

import java.util.Properties;

public class ConfigurationProviderTests extends TestCase {
    @Test
    public void testGetSearchRadiusMethod() {
        Properties properties = new Properties();
        properties.setProperty("search_radius", "5");
        IConfigurationProvider configurationProvider = new ConfigurationProvider(properties);

        double searchRadius = configurationProvider.getSearchRadius();

        assertEquals(5.0, searchRadius);
    }
}

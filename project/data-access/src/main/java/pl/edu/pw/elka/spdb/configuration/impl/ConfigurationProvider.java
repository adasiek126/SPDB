package pl.edu.pw.elka.spdb.configuration.impl;

import pl.edu.pw.elka.spdb.configuration.ConfigurationProviderException;
import pl.edu.pw.elka.spdb.configuration.IConfigurationProvider;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationProvider implements IConfigurationProvider {
    private Properties properties;

    public ConfigurationProvider() {
        Properties properties = getDefaultProperties();

        this.properties = properties;
    }

    private Properties getDefaultProperties() {
        try {
            Properties properties = new Properties();
            properties.load(ConfigurationProvider.class.getClassLoader().getResourceAsStream("config.properties"));

            return properties;
        } catch (IOException e) {
            throw new ConfigurationProviderException("Could not fetch properties from config.properties file!", e);
        }
    }

    public ConfigurationProvider(Properties properties) {
        this.properties = properties;
    }

    @Override
    public double getSearchRadius() {
        return Double.valueOf(properties.getProperty("search_radius"));
    }
}

package ec.gob.senescyt;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ServicioConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private final DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public String getDefaultSchema() {
        return database.getProperties().get("hibernate.default_schema");
    }

    public String getHttpsPort() {
        DefaultServerFactory serverFactory = (DefaultServerFactory) getServerFactory();
        HttpConnectorFactory httpConnectorFactory = (HttpConnectorFactory) serverFactory.getApplicationConnectors().get(1);

        return String.valueOf(httpConnectorFactory.getPort());
    }
}

package ec.gob.senescyt;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ServicioConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private final DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("configuracionSchema")
    private String nombreSchema;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public String getNombreSchema() {
        return nombreSchema;
    }
}

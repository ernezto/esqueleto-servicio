package ec.gob.senescyt.ejemplos.bundles;

import ec.gob.senescyt.ServicioConfiguration;
import ec.gob.senescyt.ejemplos.core.Ejemplo;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

public class ServicioHibernateBundle extends HibernateBundle<ServicioConfiguration> {

    public ServicioConfiguration getServicioConfiguration() {
        return servicioConfiguration;
    }

    private ServicioConfiguration servicioConfiguration;

    public ServicioHibernateBundle() {
        super(Ejemplo.class);
    }

    @Override
    public DataSourceFactory getDataSourceFactory(ServicioConfiguration configuration) {
        this.servicioConfiguration = configuration;
        return configuration.getConfiguracionPersistente().getDatabase();
    }

}

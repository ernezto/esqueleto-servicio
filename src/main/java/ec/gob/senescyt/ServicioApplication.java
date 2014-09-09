package ec.gob.senescyt;

import com.google.common.annotations.VisibleForTesting;
import com.sun.jersey.api.core.ResourceConfig;
import ec.gob.senescyt.commons.bundles.DBMigrationsBundle;
import ec.gob.senescyt.commons.exceptions.DBConstraintViolationMapper;
import ec.gob.senescyt.commons.exceptions.ValidacionExceptionMapper;
import ec.gob.senescyt.ejemplo.core.Ejemplo;
import ec.gob.senescyt.ejemplo.resources.EjemploResource;
import ec.gob.senescyt.microservicios.commons.MicroservicioAplicacion;
import ec.gob.senescyt.microservicios.commons.MicroservicioConfiguracion;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServicioApplication extends MicroservicioAplicacion<ServicioConfiguration> {

    private final DBMigrationsBundle flywayBundle = new DBMigrationsBundle();
    private final HibernateBundle<MicroservicioConfiguracion> hibernate = new HibernateBundle<MicroservicioConfiguracion>(Ejemplo.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(MicroservicioConfiguracion configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new ServicioApplication().run(args);
    }

    @Override
    public String getName() {
        return "servicio";
    }

    @Override
    public void inicializar(Bootstrap<ServicioConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(flywayBundle);
    }

    @Override
    public void ejecutar(ServicioConfiguration servicioConfiguration, Environment environment) {
        JerseyEnvironment jerseyEnvironment = environment.jersey();

        EjemploResource ejemploResource = new EjemploResource();
        jerseyEnvironment.register(ejemploResource);

        registrarValidacionExceptionMapper(environment);
    }

    @Override
    protected HibernateBundle<MicroservicioConfiguracion> getHibernate() {
        return hibernate;
    }

    @VisibleForTesting
    public SessionFactory getSessionFactory() {
        return hibernate.getSessionFactory();
    }

    private void registrarValidacionExceptionMapper(Environment environment) {
        eliminarDefaultConstraintValidationMapper(environment);
        ValidacionExceptionMapper validacionExceptionMapper = new ValidacionExceptionMapper();
        environment.jersey().register(validacionExceptionMapper);
        environment.jersey().register(new DBConstraintViolationMapper());
    }

    private void eliminarDefaultConstraintValidationMapper(Environment environment) {
        ResourceConfig jrConfig = environment.jersey().getResourceConfig();
        Set<Object> dwSingletons = jrConfig.getSingletons();

        List<Object> singletonsToRemove = dwSingletons
                .stream()
                .filter(s -> s instanceof ExceptionMapper &&
                        s.getClass().getName().equals("io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper"))
                .collect(Collectors.toList());

        for (Object s : singletonsToRemove) {
            jrConfig.getSingletons().remove(s);
        }
    }
}


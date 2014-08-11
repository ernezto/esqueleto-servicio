package ec.gob.senescyt;

import com.google.common.annotations.VisibleForTesting;
import com.sun.jersey.api.core.ResourceConfig;
import ec.gob.senescyt.commons.bundles.DBMigrationsBundle;
import ec.gob.senescyt.commons.exceptions.DBConstraintViolationMapper;
import ec.gob.senescyt.commons.exceptions.ValidacionExceptionMapper;
import ec.gob.senescyt.commons.filters.HeaderResponseFilter;
import ec.gob.senescyt.commons.filters.RedirectFilter;
import ec.gob.senescyt.ejemplo.core.Ejemplo;
import ec.gob.senescyt.ejemplo.resources.EjemploResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.SessionFactory;

import javax.servlet.DispatcherType;
import javax.ws.rs.ext.ExceptionMapper;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServicioApplication extends Application<ServicioConfiguration> {

    private final DBMigrationsBundle flywayBundle = new DBMigrationsBundle();
    private final HibernateBundle<ServicioConfiguration> hibernate = new HibernateBundle<ServicioConfiguration>(Ejemplo.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ServicioConfiguration configuration) {
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
    public void initialize(Bootstrap<ServicioConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(flywayBundle);
    }

    @Override
    public void run(ServicioConfiguration configuration, Environment environment) throws Exception {
        JerseyEnvironment jerseyEnvironment = environment.jersey();

        EjemploResource ejemploResource = new EjemploResource();
        jerseyEnvironment.register(ejemploResource);

        registrarFiltros(environment);
        registrarValidacionExceptionMapper(environment);
    }

    @VisibleForTesting
    public SessionFactory getSessionFactory() {
        return hibernate.getSessionFactory();
    }

    private void registrarFiltros(Environment environment) {
        environment.jersey().getResourceConfig().getContainerResponseFilters().add(new HeaderResponseFilter(StandardCharsets.UTF_8.name()));

        environment.servlets().addFilter("cors-filter", CrossOriginFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        environment.servlets().addFilter("redirect-filter", RedirectFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
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


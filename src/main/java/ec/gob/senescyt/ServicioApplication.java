package ec.gob.senescyt;

import com.google.common.annotations.VisibleForTesting;
import ec.gob.senescyt.ejemplos.bundles.ServicioHibernateBundle;
import ec.gob.senescyt.ejemplos.resources.EjemploResource;
import ec.gob.senescyt.sniese.commons.applications.AplicacionPersistente;
import ec.gob.senescyt.sniese.commons.applications.AplicacionSegura;
import ec.gob.senescyt.sniese.commons.applications.AplicacionSniese;
import ec.gob.senescyt.sniese.commons.applications.DecoradorAplicacion;
import ec.gob.senescyt.sniese.commons.security.PrincipalProvider;
import ec.gob.senescyt.sniese.commons.security.PrincipalProviderImpl;
import io.dropwizard.Application;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;

public class ServicioApplication extends Application<ServicioConfiguration> {

    private HibernateBundle hibernate = new ServicioHibernateBundle();
    private JerseyEnvironment jerseyEnvironment;
    private DecoradorAplicacion decoradorAplicacion;

    public ServicioApplication() {
        AplicacionSegura aplicacionSegura = new AplicacionSegura(null, new PrincipalProviderImpl());
        AplicacionPersistente aplicacionPersistente =  new AplicacionPersistente(aplicacionSegura, hibernate);
        decoradorAplicacion = new AplicacionSniese(aplicacionPersistente);
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public static void main(String[] args) throws Exception {
        new ServicioApplication().run(args);
    }

    @Override
    public String getName() {
        return "servicio";
    }

    @Override
    public void initialize(Bootstrap<ServicioConfiguration> bootstrap) {
        decoradorAplicacion.initialize(bootstrap);
    }

    @Override
    public void run(ServicioConfiguration servicioConfiguration, Environment environment) throws Exception {
        jerseyEnvironment = environment.jersey();
        String defaultSchema = servicioConfiguration.getConfiguracionPersistente().getDefaultSchema();
        PrincipalProvider principalProvider = new PrincipalProviderImpl();
        EjemploResource ejemploResource = new EjemploResource();
        jerseyEnvironment.register(ejemploResource);
        decoradorAplicacion.run(servicioConfiguration, environment);
    }

    @VisibleForTesting
    public SessionFactory getSessionFactory() {
        return hibernate.getSessionFactory();
    }
}


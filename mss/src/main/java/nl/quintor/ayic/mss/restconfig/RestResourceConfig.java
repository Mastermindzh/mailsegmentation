package nl.quintor.ayic.mss.restconfig;

import com.google.inject.Guice;
import nl.quintor.ayic.mss.guice.AppBinding;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class RestResourceConfig extends ResourceConfig {
    public static final String JSON_SERIALIZER = "jersey.config.server.provider.packages";
    public static final String JACKSON_JSON_SERIALIZER = "com.fasterxml.jackson.jaxrs.json;service";

    @Inject
    public RestResourceConfig(ServiceLocator serviceLocator) {
        packages(true, "nl.quintor.ayic.mss.contact.services;nl.quintor.ayic.mss.category.services;nl.quintor.ayic.mss.domain.services;nl.quintor.ayic.mss.export.services;nl.quintor.ayic.mss.authentication.services;nl.quintor.ayic.mss.statistics.services");
        property(JSON_SERIALIZER, JACKSON_JSON_SERIALIZER);
        register(CORSFilter.class);

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(Guice.createInjector(new AppBinding()));
    }
}
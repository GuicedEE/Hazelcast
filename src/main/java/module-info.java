import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.guicedee.client.services.lifecycle.IGuicePreDestroy;
import com.guicedee.client.services.lifecycle.IGuicePreStartup;
import com.guicedee.guicedhazelcast.implementations.*;
import com.guicedee.guicedhazelcast.services.*;

module com.guicedee.guicedhazelcast {
    exports com.guicedee.guicedhazelcast;
    exports com.guicedee.guicedhazelcast.services;
    exports com.guicedee.guicedhazelcast.implementations;

    requires transitive cache.annotations.ri.guice;
    requires transitive com.guicedee.vertx;
    requires transitive com.hazelcast.all;
    requires transitive io.vertx.clustermanager.hazelcast;

    requires java.xml;
    requires static lombok;
    requires io.github.classgraph;

    requires transitive com.guicedee.client;
    requires org.apache.commons.lang3;

    uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
    uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;

    provides IGuicePreDestroy with HazelcastPreDestroy;
    provides IGuiceModule with HazelcastBinderGuice;
    provides IGuicePreStartup with HazelcastPreStartup, HazelcastClientPreStartup;
    provides com.guicedee.vertx.spi.VertxConfigurator with HazelcastClusterConfigurator;

    opens com.guicedee.guicedhazelcast to com.google.guice, com.fasterxml.jackson.databind;
    opens com.guicedee.guicedhazelcast.implementations to com.google.guice, com.fasterxml.jackson.databind;
    opens com.guicedee.guicedhazelcast.services to com.google.guice, com.fasterxml.jackson.databind;
}

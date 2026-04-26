package com.guicedee.guicedhazelcast.implementations;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;
import com.guicedee.vertx.spi.ClusterVertxConfigurator;
import com.hazelcast.config.Config;
import io.vertx.core.VertxBuilder;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.log4j.Log4j2;

/**
 * Vert.x cluster configurator that uses Hazelcast as the cluster manager.
 * <p>
 * Register via SPI:
 * <pre>
 * provides com.guicedee.vertx.spi.VertxConfigurator with HazelcastClusterConfigurator;
 * </pre>
 * <p>
 * Or in META-INF/services/com.guicedee.vertx.spi.VertxConfigurator.
 * <p>
 * If a Hazelcast server instance has been started (via {@code HazelcastProperties.setStartLocal(true)}),
 * this configurator will use that instance. Otherwise, it creates a new {@link HazelcastClusterManager}
 * using the server config from {@link HazelcastPreStartup}.
 */
@Log4j2
public class HazelcastClusterConfigurator implements ClusterVertxConfigurator
{
    @Override
    public ClusterManager getClusterManager()
    {
        if (HazelcastPreStartup.getInstance() != null)
        {
            log.info("Using existing Hazelcast instance for Vert.x cluster manager");
            return new HazelcastClusterManager(HazelcastPreStartup.getInstance());
        }

        Config config = HazelcastPreStartup.getConfig();
        if (config != null)
        {
            log.info("Creating Vert.x Hazelcast cluster manager with server config (cluster={})",
                    config.getClusterName());
            return new HazelcastClusterManager(config);
        }

        log.info("Creating Vert.x Hazelcast cluster manager with default config");
        return new HazelcastClusterManager();
    }

    @Override
    public VertxBuilder builder(VertxBuilder builder)
    {
        log.info("Configuring Vert.x with Hazelcast cluster manager");
        return ClusterVertxConfigurator.super.builder(builder);
    }
}


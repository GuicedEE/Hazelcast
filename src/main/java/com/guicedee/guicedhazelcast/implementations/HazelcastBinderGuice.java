package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.hazelcast.core.HazelcastInstance;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import lombok.extern.log4j.Log4j2;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Guice module that binds Hazelcast instance, JCache providers, and cache annotations.
 */
@Log4j2
public class HazelcastBinderGuice
        extends AbstractModule
        implements IGuiceModule<HazelcastBinderGuice>
{
    @Override
    protected void configure()
    {
        log.info("Configuring Hazelcast Guice bindings");
        System.setProperty("hazelcast.logging.type", "log4j2");

        Set<CachingProvider> providers = new HashSet<>();
        for (CachingProvider provider : Caching.getCachingProviders())
        {
            providers.add(provider);
        }

        if (providers.isEmpty())
        {
            log.info("There are no known JCache providers on the classpath.");
        }
        else
        {
            bind(CachingProvider.class)
                    .toProvider(() -> providers.iterator().next())
                    .in(Singleton.class);

            bind(CacheManager.class)
                    .toProvider(() -> providers.iterator().next().getCacheManager())
                    .in(Singleton.class);

            install(new CacheAnnotationsModule());
        }

        log.info("Binding HazelcastInstance.class");
        bind(HazelcastInstance.class)
                .toProvider(new HazelcastClientProvider())
                .in(Singleton.class);
    }
}

package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.hazelcast.core.HazelcastInstance;
import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;
import lombok.extern.log4j.Log4j2;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;


/**
 * Guice module that binds Hazelcast instance, JCache providers, and cache annotations.
 */
@Log4j2
public class HazelcastBinderGuice
        extends AbstractModule
        implements IGuiceModule<HazelcastBinderGuice>
{
    @Override public boolean enabled() {
        return HazelcastClusterConfigurator.member() != null
                || com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup.getClientInstance() != null;
    }

    @Override
    protected void configure()
    {
        log.info("Configuring Hazelcast Guice bindings");
        System.setProperty("hazelcast.logging.type", "log4j2");

        var instance = new HazelcastClientProvider().get();
        var provider = new OwnedHazelcastCachingProvider(instance);
        bind(CachingProvider.class).toInstance(provider);
        bind(CacheManager.class).toProvider(() -> provider.getCacheManager())
                .in(Singleton.class);
        bind(org.jsr107.ri.annotations.DefaultCacheResolverFactory.class).toProvider(() ->
                new org.jsr107.ri.annotations.DefaultCacheResolverFactory(provider.getCacheManager())).in(Singleton.class);
        install(new CacheAnnotationsModule());

        log.info("Binding HazelcastInstance.class");
        bind(HazelcastInstance.class)
                .toProvider(new HazelcastClientProvider())
                .in(Singleton.class);
    }
}

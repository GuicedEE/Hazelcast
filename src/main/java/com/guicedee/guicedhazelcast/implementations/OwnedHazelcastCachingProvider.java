package com.guicedee.guicedhazelcast.implementations;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cache.HazelcastCachingProvider;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

/** Every JCache overload, including the annotation resolver's default, reuses the lifecycle owner. */
final class OwnedHazelcastCachingProvider implements CachingProvider {
    private final HazelcastCachingProvider delegate = new HazelcastCachingProvider();
    private final HazelcastInstance instance;
    OwnedHazelcastCachingProvider(HazelcastInstance instance) { this.instance = instance; }
    public CacheManager getCacheManager(URI uri, ClassLoader loader, Properties properties) {
        Properties owned = new Properties();
        if (properties != null) owned.putAll(properties);
        owned.putAll(HazelcastCachingProvider.propertiesByInstanceItself(instance));
        return delegate.getCacheManager(uri, loader, owned);
    }
    public CacheManager getCacheManager(URI uri, ClassLoader loader) { return getCacheManager(uri, loader, null); }
    public CacheManager getCacheManager() { return getCacheManager(getDefaultURI(), getDefaultClassLoader(), null); }
    public URI getDefaultURI() { return delegate.getDefaultURI(); }
    public ClassLoader getDefaultClassLoader() { return delegate.getDefaultClassLoader(); }
    public Properties getDefaultProperties() { return HazelcastCachingProvider.propertiesByInstanceItself(instance); }
    public boolean isSupported(OptionalFeature feature) { return delegate.isSupported(feature); }
    public void close() { delegate.close(); }
    public void close(ClassLoader loader) { delegate.close(loader); }
    public void close(URI uri, ClassLoader loader) { delegate.close(uri, loader); }
}

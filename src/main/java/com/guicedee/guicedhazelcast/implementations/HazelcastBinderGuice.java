package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import com.hazelcast.core.HazelcastInstance;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import lombok.extern.java.Log;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Binds Caching Annotations to the Hazelcast Provider
 */
@SuppressWarnings("unused")
@Log
public class HazelcastBinderGuice
				extends AbstractModule
				implements IGuiceModule<HazelcastBinderGuice>
{
	
	/**
	 * Returns and/or creates the generated hazelcast instance
	 *
	 * @param hzClient The client
	 * @return
	 */
	static HazelcastInstance getHzClient(HazelcastInstance hzClient)
	{
		return GuiceContext.get(HazelcastInstance.class);
	}
	
	@Override
	public void configure()
	{
		log.config("Configuring Hazelcast");
		// Setup Hazelcast logging to JDK with diagnostics
		System.setProperty("hazelcast.logging.type", "jdk");
		
		Set<CachingProvider> providers = new HashSet<>();
		for (CachingProvider provider : Caching.getCachingProviders())
		{
			providers.add(provider);
		}
		
		if (providers.isEmpty())
		{
			log.config("There are no known JCache providers on the classpath.");
			// XXX JCache provider based on Guava would be really cool
			// @see https://github.com/ben-manes/caffeine/issues/6
		} else
		{
			bind(CachingProvider.class)
							.toProvider(() -> providers.iterator()
											.next())
							.in(Singleton.class);
			
			bind(CacheManager.class)
							.toProvider(() -> providers.iterator()
											.next()
											.getCacheManager())
							.in(Singleton.class);
			
			install(new CacheAnnotationsModule());
		}
		
		log.config("Binding HazelcastInstance.class");
		bind(HazelcastInstance.class)
						.toProvider(new HazelcastClientProvider())
						.in(Singleton.class);
	}
	
}

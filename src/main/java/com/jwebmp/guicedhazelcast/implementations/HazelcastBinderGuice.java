package com.jwebmp.guicedhazelcast.implementations;

import com.google.inject.Singleton;
import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.abstractions.GuiceInjectorModule;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.jwebmp.logger.LogFactory;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Binds Caching Annotations to the Hazelcast Provider
 */
@SuppressWarnings("unused")
public class HazelcastBinderGuice
		implements IGuiceDefaultBinder<HazelcastBinderGuice, GuiceInjectorModule>
{

	private static final Logger log = LogFactory.getLog("HazelcastBinderGuice");


	/**
	 * Returns and/or creates the generated hazelcast instance
	 *
	 * @param hzClient
	 * 		The client
	 *
	 * @return
	 */
	static HazelcastInstance getHzClient(HazelcastInstance hzClient)
	{
		return GuiceContext.get(HazelcastInstance.class);
	}

	@Override
	public void onBind(GuiceInjectorModule module)
	{
		// Setup Hazelcast logging to JDK
		System.setProperty("hazelcast.logging.type", "jdk");
		System.setProperty("hazelcast.diagnostics.enabled", "true");

		// setup the known cache providers
		Map<String, String> knownProviders = new HashMap<>();
		knownProviders.put("org.infinispan.jcache.JCachingProvider", "infinispan");
		knownProviders.put("org.ehcache.jcache.JCacheCachingProvider", "ehcache");
		knownProviders.put("com.hazelcast.cache.HazelcastCachingProvider", "hazelcast");

		List<CachingProvider> providers = new ArrayList<>();
		for (CachingProvider provider : Caching.getCachingProviders())
		{
			providers.add(provider);
		}

		if (providers.isEmpty())
		{
			log.config("There are no known JCache providers on the classpath.");
			// XXX JCache provider based on Guava would be really cool
			// @see https://github.com/ben-manes/caffeine/issues/6
		}
		else
		{
			module.install(new CacheAnnotationsModule());
		}

		module.bind(HazelcastInstance.class)
		      .toProvider(new HazelcastClientProvider())
		      .in(Singleton.class);
	}

}

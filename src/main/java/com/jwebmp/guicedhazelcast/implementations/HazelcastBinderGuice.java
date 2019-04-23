package com.jwebmp.guicedhazelcast.implementations;

import com.google.inject.Singleton;
import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.abstractions.GuiceInjectorModule;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.jwebmp.logger.LogFactory;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static com.jwebmp.guicedhazelcast.HazelcastConfigHandler.*;

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
		log.config("Configuring Hazelcast");
		// Setup Hazelcast logging to JDK with diagnostics
		System.setProperty("hazelcast.logging.type", "jdk");
		System.setProperty("hazelcast.diagnostics.enabled", String.valueOf(diagnosticsEnabled));

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
		}
		else
		{
			module.bind(CachingProvider.class)
			      .toProvider(() -> providers.iterator()
			                                 .next())
			      .in(Singleton.class);

			module.bind(CacheManager.class)
			      .toProvider(() -> providers.iterator()
			                                 .next()
			                                 .getCacheManager())
			      .in(Singleton.class);

			module.install(new CacheAnnotationsModule());
		}

		log.config("Bound HazelcastInstance.class");
		module.bind(HazelcastInstance.class)
		      .toProvider(new HazelcastClientProvider())
		      .in(Singleton.class);
	}

}

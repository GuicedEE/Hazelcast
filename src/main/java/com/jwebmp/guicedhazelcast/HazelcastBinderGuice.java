package com.jwebmp.guicedhazelcast;

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

	@Override
	public void onBind(GuiceInjectorModule module)
	{

		// Setup Hazelcast logging
		System.setProperty("hazelcast.logging.type", "jdk");

		// setup the known cache providers
		Map<String, String> knownProviders = new HashMap<String, String>()
		{{
			put("org.infinispan.jcache.JCachingProvider", "infinispan");
			put("org.ehcache.jcache.JCacheCachingProvider", "ehcache");
			put("com.hazelcast.cache.HazelcastCachingProvider", "hazelcast");
		}};

		List<CachingProvider> providers = new ArrayList<>();
		for (CachingProvider provider : Caching.getCachingProviders())
		{
			providers.add(provider);
		}

		if (providers.isEmpty())
		{

			log.config("There are no JCache providers on the classpath.");
			// XXX JCache provider based on Guava would be really cool
			// @see https://github.com/ben-manes/caffeine/issues/6
		}
		else
		{
			module.install(new CacheAnnotationsModule());
			CachingProvider cp = Caching.getCachingProvider();
		}
	}
}

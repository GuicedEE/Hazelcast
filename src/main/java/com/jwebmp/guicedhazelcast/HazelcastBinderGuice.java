package com.jwebmp.guicedhazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.abstractions.GuiceInjectorModule;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.jwebmp.logger.LogFactory;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.inject.Provider;
import java.util.*;
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
	 * The connected hazelcast client for JCache
	 */
	private static HazelcastInstance hzClient;

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
			log.config("There are no JCache providers on the classpath.");
			// XXX JCache provider based on Guava would be really cool
			// @see https://github.com/ben-manes/caffeine/issues/6
		}
		else
		{
			module.install(new CacheAnnotationsModule());
		}

		module.bind(HazelcastInstance.class)
		      .toProvider((Provider<HazelcastInstance>) () ->
		      {
			      if (hzClient == null)
			      {
				      ClientConfig config = new ClientConfig();
				      Set<IGuicedHazelcastClientConfig> configSet = GuiceContext.instance()
				                                                                .getLoader(IGuicedHazelcastClientConfig.class, true, ServiceLoader.load(
						                                                                IGuicedHazelcastClientConfig.class));
				      for (IGuicedHazelcastClientConfig iGuicedHazelcastClientConfig : configSet)
				      {
					      config = iGuicedHazelcastClientConfig.buildConfig(config);
				      }

				      if (config.getGroupConfig() == null)
				      {
					      GroupConfig groupConfig = config.getGroupConfig();
					      if (HazelcastEntityManagerProperties.getGroupName() != null)
					      {
						      groupConfig.setName(HazelcastEntityManagerProperties.getGroupName());
					      }
					      if (HazelcastEntityManagerProperties.getGroupPass() != null)
					      {
						      groupConfig.setPassword(HazelcastEntityManagerProperties.getGroupPass());
					      }
				      }
				      setHzClient(HazelcastClient.newHazelcastClient(config));
			      }
			      return hzClient;
		      });
	}

	/**
	 * Sets the hazelcast client
	 *
	 * @param hzClient
	 * 		The actual client to use
	 */
	static void setHzClient(HazelcastInstance hzClient)
	{
		HazelcastBinderGuice.hzClient = hzClient;
	}
}

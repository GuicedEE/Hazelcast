package com.guicedee.guicedhazelcast.services;

import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.logger.LogFactory;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Logger;

import static com.guicedee.guicedhazelcast.HazelcastProperties.*;

public class HazelcastPreStartup
		implements IGuicePreStartup<HazelcastPreStartup>, IGuicePreDestroy<HazelcastPreStartup>
{

	private static final Logger log = LogFactory.getLog("HazelcastPreStartup");
	public static HazelcastInstance instance;
	public static Config config;

	@Override
	public void onStartup()
	{
		
		if (config == null)
		{
			config = new Config();
		}

		if (config.getNetworkConfig() == null)
		{
			config.setNetworkConfig(new NetworkConfig());
		}

		if (isStartLocal())
		{
			config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(false);

			@SuppressWarnings("rawtypes")
			Set<IGuicedHazelcastServerConfig> configSet = GuiceContext.instance()
			                                                          .getLoader(IGuicedHazelcastServerConfig.class, true, ServiceLoader.load(IGuicedHazelcastServerConfig.class));
			for (IGuicedHazelcastServerConfig<?> iGuicedHazelcastClientConfig : configSet)
			{
				config = iGuicedHazelcastClientConfig.buildConfig(config);
			}
			System.setProperty("hazelcast.jcache.provider.type", "member");
			log.config("Final Hazelcast Server Configuration - " + config.toString());
			instance = Hazelcast.newHazelcastInstance(config);
		}
	}

	@Override
	public Integer sortOrder()
	{
		return 45;
	}

	@Override
	public void onDestroy()
	{
		if (instance != null)
		{
			instance.shutdown();
			instance = null;
		}
	}
}

package com.guicedee.guicedhazelcast.services;

import com.guicedee.client.*;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.guicedinjection.properties.GlobalProperties;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.java.Log;

import java.util.ServiceLoader;
import java.util.Set;

import static com.guicedee.guicedhazelcast.HazelcastProperties.isStartLocal;

@Log
public class HazelcastPreStartup implements IGuicePreStartup<HazelcastPreStartup>, IGuicePreDestroy<HazelcastPreStartup>
{
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
		HazelcastProperties.setAddress(GlobalProperties.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", "localhost"));
		config
				.getNetworkConfig()
				.setPublicAddress(HazelcastProperties.getAddress());
		HazelcastProperties.setGroupName(GlobalProperties.getSystemPropertyOrEnvironment("GROUP_NAME", "dev"));
		config.setClusterName(HazelcastProperties.getGroupName());
		config.setInstanceName(HazelcastProperties.getGroupName());
		
		@SuppressWarnings("rawtypes")
		Set<IGuicedHazelcastServerConfig> configSet = IGuiceContext
				                                              .instance()
				                                              .getLoader(IGuicedHazelcastServerConfig.class, true, ServiceLoader.load(IGuicedHazelcastServerConfig.class));
		for (IGuicedHazelcastServerConfig<?> iGuicedHazelcastClientConfig : configSet)
		{
			config = iGuicedHazelcastClientConfig.buildConfig(config);
		}
		
		if (isStartLocal())
		{
			log.fine("Setting hazelcast to local instance startup. Disabling auto detection and multi cast pickup");
			config
					.getNetworkConfig()
					.getJoin()
					.getMulticastConfig()
					.setEnabled(false);
			config
					.getNetworkConfig()
					.getJoin()
					.getAutoDetectionConfig()
					.setEnabled(false);
			log.config("Final Hazelcast Server Configuration - " + config.toString());
			instance = Hazelcast.getOrCreateHazelcastInstance(config);
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

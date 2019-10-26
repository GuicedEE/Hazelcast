package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.Provider;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.guicedee.guicedhazelcast.HazelcastEntityManagerProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Logger;

public class HazelcastClientProvider
		implements Provider<HazelcastInstance>
{
	private static final Logger log = LogFactory.getLog("HazelcastClientProvider");

	@Override
	public HazelcastInstance get()
	{
		if (HazelcastPreStartup.instance != null)
		{
			return HazelcastPreStartup.instance;
		}

		ClientConfig config = new ClientConfig();
		Set<IGuicedHazelcastClientConfig> configSet = GuiceContext.instance()
		                                                          .getLoader(IGuicedHazelcastClientConfig.class, true, ServiceLoader.load(IGuicedHazelcastClientConfig.class));
		for (IGuicedHazelcastClientConfig iGuicedHazelcastClientConfig : configSet)
		{
			config = iGuicedHazelcastClientConfig.buildConfig(config);
		}
		log.config("Final Hazelcast Client Configuration - " + config.toString());
		if (config.getGroupConfig() == null)
		{
			GroupConfig groupConfig = new GroupConfig();
			if (HazelcastEntityManagerProperties.getGroupName() != null)
			{
				groupConfig.setName(HazelcastEntityManagerProperties.getGroupName());
			}
		}
		return HazelcastClient.newHazelcastClient(config);
	}
}

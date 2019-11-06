package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.Provider;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
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

		try
		{
			config.addLabel(InetAddress.getLocalHost()
			                           .getHostAddress());
		}
		catch (UnknownHostException e)
		{
			log.log(Level.SEVERE, "Unable to make an inet address from localhost", e);
		}

		ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
		clientNetworkConfig.addAddress(HazelcastProperties.getAddress());

		GroupConfig groupConfig = new GroupConfig();
		groupConfig.setName(HazelcastProperties.getGroupName());
		config.setGroupConfig(groupConfig);

		config.setInstanceName(HazelcastProperties.getInstanceName());

		return HazelcastClient.newHazelcastClient(config);
	}
}

package com.guicedee.guicedhazelcast.services;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HazelcastClientPreStartup
		implements IGuicePreStartup<HazelcastClientPreStartup>, IGuicePreDestroy<HazelcastClientPreStartup>
{

	public static HazelcastInstance clientInstance;
	public static ClientConfig config;

	private static final Logger log = LogFactory.getLog("HazelcastPreStartup");

	public void onStartup()
	{
		if (clientInstance != null)
		{
			return;
		}
		if (config == null)
		{
			config = new ClientConfig();
		}
		@SuppressWarnings("rawtypes")
		Set<IGuicedHazelcastClientConfig> configSet = GuiceContext.instance()
		                                                          .getLoader(IGuicedHazelcastClientConfig.class, true, ServiceLoader.load(IGuicedHazelcastClientConfig.class));
		for (IGuicedHazelcastClientConfig<?> iGuicedHazelcastClientConfig : configSet)
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

		if (HazelcastPreStartup.instance != null)
		{
			config.getNetworkConfig()
			      .getAddresses()
			      .clear();
			config.getNetworkConfig()
			      .addAddress(HazelcastPreStartup.config.getNetworkConfig()
			                                            .getPublicAddress());
		}

		ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
		System.getProperties()
		      .setProperty("system.hazelcast.groupname", config.getClusterName());
		System.getProperties()
		      .setProperty("system.hazelcast.clustername", config.getClusterName());
		System.getProperties()
		      .setProperty("system.hazelcast.cluster_name", config.getClusterName());
		if (config.getNetworkConfig() != null && config.getNetworkConfig()
		                                               .getAddresses() != null && !config.getNetworkConfig()
		                                                                                 .getAddresses()
		                                                                                 .isEmpty())
		{
			String addy = config.getNetworkConfig()
			                    .getAddresses()
			                    .get(0);
			clientNetworkConfig.addAddress(addy);
			System.getProperties()
			      .setProperty("hazelcast.socket.client.bind", addy);
			System.getProperties()
			      .setProperty("system.hazelcast.address", addy);
			System.setProperty("client.address", addy);
		}
		System.setProperty("group.name", config.getClusterName());
		System.setProperty("cluster.name", config.getClusterName());

		clientInstance = HazelcastClient.newHazelcastClient(config);
	}

	@Override
	public void onDestroy()
	{
		if (clientInstance != null)
		{
			clientInstance.shutdown();
			clientInstance = null;
		}
	}

	@Override
	public Integer sortOrder()
	{
		return 46;
	}
}

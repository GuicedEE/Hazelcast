package com.guicedee.guicedhazelcast.services;

import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
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

	private static final Logger log = LogFactory.getLog("HazelcastPreStartup");
	public static HazelcastInstance clientInstance;
	public static ClientConfig config;

	@Override
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

		config.setProperty("hazelcast.client.shuffle.member.list", "true");
		config.setProperty("hazelcast.client.heartbeat.timeout", "60000");
		config.setProperty("hazelcast.client.heartbeat.interval", "5000");
		config.setProperty("hazelcast.client.event.thread.count", "5");
		config.setProperty("hazelcast.client.event.queue.capacity", "1000000");
		config.setProperty("hazelcast.client.invocation.timeout.seconds", "120");

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
	}

	@Override
	public Integer sortOrder()
	{
		return 46;
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
}

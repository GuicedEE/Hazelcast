package com.guicedee.guicedhazelcast.services;

import com.google.common.base.Strings;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.guicedinjection.properties.*;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
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
		
		GlobalProperties.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", "localhost");
		HazelcastProperties.setAddress(System.getProperty("CLIENT_ADDRESS"));
		config.getNetworkConfig().addAddress(HazelcastProperties.getAddress());
		
		GlobalProperties.getSystemPropertyOrEnvironment("GROUP_NAME", "dev");
		HazelcastProperties.setGroupName(System.getProperty("GROUP_NAME"));
		
		config.setClusterName(HazelcastProperties.getGroupName());
		config.setInstanceName(HazelcastProperties.getGroupName());
		
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
		
		if (Strings.isNullOrEmpty(config.getClusterName()))
		{
			config.setClusterName("dev");
		}
		if (Strings.isNullOrEmpty(config.getInstanceName()))
		{
			config.setInstanceName("dev");
		}
		
		ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
		if (config.getNetworkConfig() != null && config.getNetworkConfig()
		                                               .getAddresses() != null && !config.getNetworkConfig()
		                                                                                 .getAddresses()
		                                                                                 .isEmpty())
		{
			String addy = config.getNetworkConfig()
			                    .getAddresses()
			                    .get(0);
			clientNetworkConfig.addAddress(addy);
			GlobalProperties.getSystemPropertyOrEnvironment("hazelcast.socket.client.bind", addy);
			GlobalProperties.getSystemPropertyOrEnvironment("system.hazelcast.address", addy);
			GlobalProperties.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", addy);
		}
		else
		{
			System.getProperties()
			      .setProperty("system.hazelcast.address", "127.0.0.1");
		}
		if (!Strings.isNullOrEmpty(config.getClusterName()))
		{
			GlobalProperties.getSystemPropertyOrEnvironment("GROUP_NAME", config.getClusterName());
			GlobalProperties.getSystemPropertyOrEnvironment("cluster.name", config.getClusterName());
			GlobalProperties.getSystemPropertyOrEnvironment("instance.name", config.getInstanceName());
			GlobalProperties.getSystemPropertyOrEnvironment("system.hazelcast.groupname", config.getClusterName());
			GlobalProperties.getSystemPropertyOrEnvironment("system.hazelcast.clustername", config.getClusterName());
			GlobalProperties.getSystemPropertyOrEnvironment("system.hazelcast.cluster_name", config.getClusterName());
		}
		GlobalProperties.getSystemPropertyOrEnvironment("hazelcast.jcache.provider.type", "client");
		
		//TODO this requires more than a blind code implmementation
		config.setInstanceName(config.getInstanceName());
		clientInstance = HazelcastClient.getOrCreateHazelcastClient(config);
		
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

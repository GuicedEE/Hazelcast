package com.jwebmp.guicedhazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedinjection.interfaces.IGuicePostStartup;
import com.jwebmp.logger.LogFactory;

import java.util.logging.Logger;

public class HazelcastInitializer
		implements IGuicePostStartup
{
	private static final Logger log = LogFactory.getLog("HazelcastInitializer");

	@Override
	public void postLoad()
	{
		ClientConfig config = new ClientConfig();
		GroupConfig groupConfig = config.getGroupConfig();
		groupConfig.setName("dev");
		groupConfig.setPassword("dev-pass");
		HazelcastInstance hzClient
				= HazelcastClient.newHazelcastClient(config);
	}
}


package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.InvalidConfigurationException;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

import static com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup.*;

public class HazelcastClientProvider
		implements Provider<HazelcastInstance>, IGuicePreDestroy<HazelcastClientProvider>
{

	@Override
	public HazelcastInstance get()
	{
		try
		{
			clientInstance = HazelcastClient.newHazelcastClient(config);
		}
		catch (ProvisionException | InvalidConfigurationException pe)
		{
			config.setInstanceName(config.getInstanceName() + "_" + RandomUtils.nextInt(1, 100));
			clientInstance = HazelcastClient.newHazelcastClient(config);
		}
		return clientInstance;
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

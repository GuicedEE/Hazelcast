package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.Provider;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup.*;

public class HazelcastClientProvider
		implements Provider<HazelcastInstance>, IGuicePreDestroy<HazelcastClientProvider>
{

	@Override
	public HazelcastInstance get()
	{
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

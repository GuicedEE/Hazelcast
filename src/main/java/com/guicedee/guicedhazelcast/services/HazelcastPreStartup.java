package com.guicedee.guicedhazelcast.services;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.logger.LogFactory;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Logger;

public class HazelcastPreStartup
		implements IGuicePreStartup<HazelcastPreStartup>, IGuicePreDestroy<HazelcastPreStartup>
{

	public static HazelcastInstance instance;
	private static final Logger log = LogFactory.getLog("HazelcastPreStartup");

	public void onStartup() {
		if (HazelcastProperties.isStartLocal()) {
			Config config = new Config();

            config.setProperty("hazelcast.client.shuffle.member.list", "true");
            config.setProperty("hazelcast.client.heartbeat.timeout", "60000");
            config.setProperty("hazelcast.client.heartbeat.interval", "5000");
            config.setProperty("hazelcast.client.event.thread.count", "5");
            config.setProperty("hazelcast.client.event.queue.capacity", "1000000");
            config.setProperty("hazelcast.client.invocation.timeout.seconds", "120");

            if(config.getNetworkConfig() == null)
                config.setNetworkConfig(new NetworkConfig());

			Set<IGuicedHazelcastServerConfig> configSet = GuiceContext.instance()
			                                                          .getLoader(IGuicedHazelcastServerConfig.class, true, ServiceLoader.load(IGuicedHazelcastServerConfig.class));
			for (IGuicedHazelcastServerConfig iGuicedHazelcastClientConfig : configSet)
			{
				config = iGuicedHazelcastClientConfig.buildConfig(config);
			}

			log.config("Final Hazelcast Server Configuration - " + config.toString());
			instance = Hazelcast.newHazelcastInstance(config);
		}
	}

	@Override
	public void onDestroy()
	{
		if(instance != null)
		{
			instance.shutdown();
			instance = null;
		}
	}
}

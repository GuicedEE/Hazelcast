package com.guicedee.guicedhazelcast.services;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePreDestroy;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.logger.LogFactory;
import com.hazelcast.config.Config;
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

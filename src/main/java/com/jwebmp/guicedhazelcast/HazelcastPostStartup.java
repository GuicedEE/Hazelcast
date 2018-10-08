package com.jwebmp.guicedhazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.IGuicePostStartup;
import com.jwebmp.logger.LogFactory;

import java.util.logging.Logger;

public class HazelcastPostStartup
		implements IGuicePostStartup
{
	private static final Logger log = LogFactory.getLog("HazelcastPostStartup");

	@Override
	public void postLoad()
	{
		log.fine("Starting JSR107 @Cache Bindings. Configure with IGuicedHazelcastClientConfig");
		HazelcastInstance clientInstance = GuiceContext.get(HazelcastInstance.class);
		log.fine("Bound HazelcastInstance.class");
	}

}

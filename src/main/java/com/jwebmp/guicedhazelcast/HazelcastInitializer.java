package com.jwebmp.guicedhazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedinjection.GuiceContext;
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
		log.config("Starting up JCache Hazelcast Instance");
		GuiceContext.get(HazelcastInstance.class);
	}
}


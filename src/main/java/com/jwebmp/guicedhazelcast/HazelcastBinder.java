package com.jwebmp.guicedhazelcast;

import com.jwebmp.guiceinjection.abstractions.GuiceInjectorModule;
import com.jwebmp.guiceinjection.interfaces.GuiceDefaultBinder;
import com.jwebmp.logger.LogFactory;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Binds Caching Annotations to the Hazelcast Provider
 */
@SuppressWarnings("unused")
public class HazelcastBinder
		extends GuiceDefaultBinder
{
	private static final Logger log = LogFactory.getLog("HazelcastBinder");

	@Override
	public void onBind(GuiceInjectorModule module)
	{
		log.log(Level.CONFIG, "Registering Cache Annotations Module");
		module.install(new CacheAnnotationsModule());
	}
}

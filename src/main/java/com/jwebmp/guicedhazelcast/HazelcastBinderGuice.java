package com.jwebmp.guicedhazelcast;

import com.jwebmp.guicedinjection.abstractions.GuiceInjectorModule;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.jwebmp.logger.LogFactory;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;

import java.util.logging.Logger;

/**
 * Binds Caching Annotations to the Hazelcast Provider
 */
@SuppressWarnings("unused")
public class HazelcastBinderGuice
		implements IGuiceDefaultBinder<GuiceInjectorModule>
{
	private static final Logger log = LogFactory.getLog("HazelcastBinderGuice");

	@Override
	public void onBind(GuiceInjectorModule module)
	{
		module.install(new CacheAnnotationsModule());
	}
}

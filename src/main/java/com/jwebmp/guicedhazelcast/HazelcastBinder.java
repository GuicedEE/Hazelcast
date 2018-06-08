package com.jwebmp.guicedhazelcast;

import com.jwebmp.guiceinjection.abstractions.GuiceSiteInjectorModule;
import com.jwebmp.guiceinjection.interfaces.GuiceSiteBinder;

/**
 * Binds Caching Annotations to the Hazelcast Provider
 */
@SuppressWarnings("unused")
public class HazelcastBinder
		extends GuiceSiteBinder
{
	@Override
	public void onBind(GuiceSiteInjectorModule module)
	{
		System.out.println("Stuff");
		//module.install(new CacheAnnotationsModule());
	}

}

package com.jwebmp.guicedhazelcast.implementations;

import com.hazelcast.core.ManagedContext;
import com.jwebmp.guicedinjection.GuiceContext;

public class HazelcastGuiceManagedContext
		implements ManagedContext
{

	public HazelcastGuiceManagedContext()
	{
		//No config required
	}

	@Override
	public Object initialize(Object instance)
	{
		GuiceContext.inject()
		            .injectMembers(instance);
		return instance;
	}
}

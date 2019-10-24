package com.guicedee.guicedhazelcast.implementations;

import com.hazelcast.core.ManagedContext;
import com.guicedee.guicedinjection.GuiceContext;

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

package com.jwebmp.guicedhazelcast.implementations;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.hazelcast.core.ManagedContext;

public class HazelcastGuiceManagedContext
		implements ManagedContext
{

	private final Injector injector;

	@Inject
	public HazelcastGuiceManagedContext(Injector injector)
	{
		this.injector = injector;
	}

	@Override
	public Object initialize(Object instance)
	{
		injector.injectMembers(instance);
		return instance;
	}
}

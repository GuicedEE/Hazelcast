package com.guicedee.guicedhazelcast.implementations;

import com.guicedee.guicedinjection.interfaces.IGuicePostStartup;

public class HazelcastPostStartup
		implements IGuicePostStartup<HazelcastPostStartup>
{
	@Override
	public void postLoad()
	{
		//No config
	}
}

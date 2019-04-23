package com.jwebmp.guicedhazelcast.implementations;

import com.jwebmp.guicedinjection.interfaces.IGuicePostStartup;

public class HazelcastPostStartup
		implements IGuicePostStartup<HazelcastPostStartup>
{
	@Override
	public void postLoad()
	{
		//No config
	}
}

package com.jwebmp.guicedhazelcast.implementations;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.jwebmp.guicedhazelcast.HazelcastConfigHandler;
import com.jwebmp.guicedinjection.interfaces.IGuicePreStartup;

public class HazelcastPreStartup
		implements IGuicePreStartup<HazelcastPreStartup>
{
	public static HazelcastInstance instance;

	public void onStartup()
	{
		if (HazelcastConfigHandler.startLocal)
		{
			instance = Hazelcast.newHazelcastInstance();
		}
	}
}

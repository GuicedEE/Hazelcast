package com.guicedee.guicedhazelcast.implementations;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HazelcastPreStartup
		implements IGuicePreStartup<HazelcastPreStartup> {
	public static HazelcastInstance instance;

	public void onStartup() {
		if (HazelcastProperties.isStartLocal()) {
			String hostname;
			try {
				hostname = InetAddress.getLocalHost()
									  .getHostName();
			} catch (UnknownHostException e) {
				hostname = "localhost";
			}
			Config cfg = new Config();
		//	cfg.getGroupConfig()
		//	   .setName(hostname);
			instance = Hazelcast.newHazelcastInstance(cfg);
		}
	}
}

package com.guicedee.guicedhazelcast.services;

import com.hazelcast.config.Config;

/**
 * Configures the client config when called upon
 */
public interface IGuicedHazelcastServerConfig
{
	/**
	 * Sets up the configuration suite
	 *
	 * @param serverConfig
	 * 		The incoming and current configuration of the client ocnfiguration
	 *
	 * @return Whatever config you want..
	 */
	Config buildConfig(Config serverConfig);
}

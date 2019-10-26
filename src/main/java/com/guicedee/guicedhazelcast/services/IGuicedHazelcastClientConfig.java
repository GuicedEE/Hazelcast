package com.guicedee.guicedhazelcast.services;

import com.hazelcast.client.config.ClientConfig;

/**
 * Configures the client config when called upon
 */
public interface IGuicedHazelcastClientConfig
{
	/**
	 * Sets up the configuration suite
	 *
	 * @param clientConfig
	 * 		The incoming and current configuration of the client ocnfiguration
	 *
	 * @return Whatever config you want..
	 */
	ClientConfig buildConfig(ClientConfig clientConfig);
}

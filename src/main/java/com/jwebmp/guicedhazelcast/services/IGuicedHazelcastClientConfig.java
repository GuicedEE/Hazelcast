package com.jwebmp.guicedhazelcast.services;

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
	 *
	 * @return
	 */
	ClientConfig buildConfig(ClientConfig clientConfig);
}
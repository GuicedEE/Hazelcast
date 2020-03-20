package com.guicedee.guicedhazelcast.services;

import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.hazelcast.client.config.ClientConfig;

/**
 * Configures the client config when called upon
 */
public interface IGuicedHazelcastClientConfig<J extends IGuicedHazelcastClientConfig<J>> extends IDefaultService<J>
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

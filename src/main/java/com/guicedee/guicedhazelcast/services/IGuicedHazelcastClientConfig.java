package com.guicedee.guicedhazelcast.services;

import com.guicedee.client.services.IDefaultService;
import com.hazelcast.client.config.ClientConfig;

/**
 * SPI for configuring the Hazelcast client configuration.
 */
public interface IGuicedHazelcastClientConfig<J extends IGuicedHazelcastClientConfig<J>> extends IDefaultService<J>
{
    /**
     * Customizes the Hazelcast client configuration.
     *
     * @param clientConfig The current client configuration.
     * @return The modified client configuration.
     */
    ClientConfig buildConfig(ClientConfig clientConfig);
}

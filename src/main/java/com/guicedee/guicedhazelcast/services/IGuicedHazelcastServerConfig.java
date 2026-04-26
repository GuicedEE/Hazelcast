package com.guicedee.guicedhazelcast.services;

import com.guicedee.client.services.IDefaultService;
import com.hazelcast.config.Config;

/**
 * SPI for configuring the Hazelcast server configuration.
 */
public interface IGuicedHazelcastServerConfig<J extends IGuicedHazelcastServerConfig<J>> extends IDefaultService<J>
{
    /**
     * Customizes the Hazelcast server configuration.
     *
     * @param serverConfig The current server configuration.
     * @return The modified server configuration.
     */
    Config buildConfig(Config serverConfig);
}

package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.InvalidConfigurationException;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.log4j.Log4j2;
import java.util.concurrent.ThreadLocalRandom;

import static com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup.clientInstance;
import static com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup.config;

/**
 * Guice provider for the Hazelcast client instance.
 */
@Log4j2
public class HazelcastClientProvider implements Provider<HazelcastInstance>
{
    @Override
    public HazelcastInstance get()
    {
        if (clientInstance != null)
        {
            return clientInstance;
        }
        try
        {
            clientInstance = HazelcastClient.newHazelcastClient(config);
        }
        catch (ProvisionException | InvalidConfigurationException pe)
        {
            log.warn("Hazelcast client creation failed, retrying with modified instance name: {}", pe.getMessage());
            config.setInstanceName(config.getInstanceName() + "_" + ThreadLocalRandom.current().nextInt(1, 100));
            clientInstance = HazelcastClient.newHazelcastClient(config);
        }
        return clientInstance;
    }
}

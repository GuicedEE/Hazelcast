package com.guicedee.guicedhazelcast.implementations;

import com.guicedee.client.services.lifecycle.IGuicePreDestroy;
import com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;
import lombok.extern.log4j.Log4j2;

/**
 * Shuts down Hazelcast instances on application shutdown.
 */
@Log4j2
public class HazelcastPreDestroy implements IGuicePreDestroy<HazelcastPreDestroy>
{
    @Override
    public void onDestroy()
    {
        log.info("Shutting down Hazelcast instances...");

        if (HazelcastClientPreStartup.clientInstance != null)
        {
            try
            {
                HazelcastClientPreStartup.clientInstance.shutdown();
                log.debug("Hazelcast client instance shut down.");
            }
            catch (Exception e)
            {
                log.error("Error shutting down Hazelcast client instance", e);
            }
            HazelcastClientPreStartup.clientInstance = null;
        }

        if (HazelcastPreStartup.instance != null)
        {
            try
            {
                HazelcastPreStartup.instance.shutdown();
                log.debug("Hazelcast server instance shut down.");
            }
            catch (Exception e)
            {
                log.error("Error shutting down Hazelcast server instance", e);
            }
            HazelcastPreStartup.instance = null;
        }

        log.info("Hazelcast shutdown complete.");
    }

    @Override
    public Integer sortOrder()
    {
        return Integer.MAX_VALUE - 100;
    }
}


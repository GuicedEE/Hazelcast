package com.guicedee.guicedhazelcast.tests;

import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;
import com.hazelcast.core.HazelcastInstance;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HazelcastBinderTest
{
    @BeforeAll
    static void init()
    {
        System.setProperty("VERTX_CLUSTER_ENABLED", "true");
        System.setProperty("HAZELCAST_CLIENT_ENABLED", "false");
        System.setProperty("hazelcast.jcache.provider.type", "server");
        var isolated = new com.hazelcast.config.Config();
        isolated.setClusterName("test");
        isolated.getNetworkConfig().setPort(0);
        isolated.getNetworkConfig().getInterfaces().setEnabled(true).addInterface("127.0.0.1");
        isolated.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        isolated.getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(false);
        isolated.setProperty("hazelcast.operation.thread.count", "2");
        isolated.setProperty("hazelcast.operation.generic.thread.count", "2");
        isolated.setProperty("hazelcast.shutdownhook.enabled", "false");
        HazelcastPreStartup.config = isolated;
        HazelcastProperties.setStartLocal(true);
        System.setProperty("GROUP_NAME", "test");
        IGuiceContext.registerModule("com.guicedee.guicedhazelcast.tests");
        IGuiceContext.instance().inject();
    }

    @AfterAll
    static void destroy()
    {
        IGuiceContext.instance().destroy();
    }

    @Test
    @Order(1)
    void testHazelcastInstanceBound()
    {
        HazelcastInstance instance = IGuiceContext.get(HazelcastInstance.class);
        assertSame(HazelcastPreStartup.getInstance(), instance);
        assertEquals(1, com.hazelcast.core.Hazelcast.getAllHazelcastInstances().size());
        var provider = IGuiceContext.get(javax.cache.spi.CachingProvider.class);
        var manager = IGuiceContext.get(javax.cache.CacheManager.class);
        assertSame(manager, provider.getCacheManager());
        assertEquals(1, com.hazelcast.core.Hazelcast.getAllHazelcastInstances().size(), "JCache must reuse the lifecycle member");
    }

    @Test
    @Order(2)
    void testServerInstanceStarted()
    {
        assertNotNull(HazelcastPreStartup.getInstance(), "Server instance should be started in local mode");
        assertEquals("test", HazelcastPreStartup.getConfig().getClusterName());
    }

    @Test
    @Order(3)
    void testHazelcastDistributedMap()
    {
        HazelcastInstance instance = IGuiceContext.get(HazelcastInstance.class);
        var map = instance.getMap("test-map");
        map.put("key1", "value1");
        assertEquals("value1", map.get("key1"));
        map.remove("key1");
        assertNull(map.get("key1"));
    }
}

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
        assertNotNull(instance, "HazelcastInstance should be bound");
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

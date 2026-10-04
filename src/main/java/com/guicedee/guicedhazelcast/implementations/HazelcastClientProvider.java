package com.guicedee.guicedhazelcast.implementations;

import com.google.inject.Provider;
import com.hazelcast.core.HazelcastInstance;
import com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;

/** Supplies the lifecycle-owned client or member; injection never creates another instance. */
public class HazelcastClientProvider implements Provider<HazelcastInstance> {
    @Override public HazelcastInstance get() {
        var client = HazelcastClientPreStartup.getClientInstance();
        if (client != null && client.getLifecycleService().isRunning()) return client;
        var member = HazelcastClusterConfigurator.member();
        if (member == null) throw new IllegalStateException("No enabled Hazelcast client or member");
        return member;
    }
}

package com.guicedee.guicedhazelcast.services;

import com.guicedee.client.Environment;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.services.lifecycle.IGuicePreStartup;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.HazelcastServerOptions;
import com.guicedee.vertx.spi.VertXPreStartup;
import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.vertx.core.Future;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

@Log4j2
public class HazelcastPreStartup implements IGuicePreStartup<HazelcastPreStartup>
{
    @Getter
    public static HazelcastInstance instance;
    @Getter
    public static Config config;
    @Getter
    private static HazelcastServerOptions serverOptions;

    @Override
    public List<Future<Boolean>> onStartup()
    {
        if (config == null)
        {
            config = new Config();
        }
        if (config.getNetworkConfig() == null)
        {
            config.setNetworkConfig(new NetworkConfig());
        }

        return List.of(VertXPreStartup.getVertx().executeBlocking(() -> {
            // Scan for @HazelcastServerOptions on classes and package-info
            ScanResult scanResult = IGuiceContext.instance().getScanResult();
            discoverServerOptions(scanResult);

            // Apply annotation-driven configuration (if found)
            if (serverOptions != null)
            {
                applyServerOptions(config, serverOptions);
            }
            else
            {
                // Fallback to old env-based config
                HazelcastProperties.setAddress(Environment.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", "localhost"));
                config.getNetworkConfig().setPublicAddress(HazelcastProperties.getAddress());
                HazelcastProperties.setGroupName(Environment.getSystemPropertyOrEnvironment("GROUP_NAME", "dev"));
                config.setClusterName(HazelcastProperties.getGroupName());
                config.setInstanceName(HazelcastProperties.getGroupName());
            }

            // Run SPI hooks for programmatic customization
            @SuppressWarnings("rawtypes")
            Set<IGuicedHazelcastServerConfig> configSet = IGuiceContext
                    .instance()
                    .getLoader(IGuicedHazelcastServerConfig.class, true, ServiceLoader.load(IGuicedHazelcastServerConfig.class));
            for (IGuicedHazelcastServerConfig<?> spiConfig : configSet)
            {
                config = spiConfig.buildConfig(config);
            }

            // Start local instance if requested
            boolean shouldStartLocal = serverOptions != null ? serverOptions.startLocal() : HazelcastProperties.isStartLocal();
            if (shouldStartLocal)
            {
                log.info("Starting embedded Hazelcast instance (cluster={})", config.getClusterName());
                if (serverOptions != null && serverOptions.joinType() == HazelcastServerOptions.JoinType.NONE)
                {
                    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
                    config.getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(false);
                }
                instance = Hazelcast.getOrCreateHazelcastInstance(config);
            }
            return true;
        }));
    }

    private void discoverServerOptions(ScanResult scanResult)
    {
        // Check classes
        ClassInfoList annotatedClasses = scanResult.getClassesWithAnnotation(HazelcastServerOptions.class);
        for (var ci : annotatedClasses)
        {
            var ann = ci.loadClass().getAnnotation(HazelcastServerOptions.class);
            if (ann != null)
            {
                serverOptions = wrapServerOptions(ann);
                log.debug("Found @HazelcastServerOptions on class: {}", ci.getName());
                return;
            }
        }

        // Check package-info
        var packageInfoList = scanResult.getPackageInfo();
        if (packageInfoList != null)
        {
            for (var pkgInfo : packageInfoList)
            {
                try
                {
                    Class<?> pkgClass = Class.forName(pkgInfo.getName() + ".package-info");
                    var ann = pkgClass.getAnnotation(HazelcastServerOptions.class);
                    if (ann != null)
                    {
                        serverOptions = wrapServerOptions(ann);
                        log.debug("Found @HazelcastServerOptions on package: {}", pkgInfo.getName());
                        return;
                    }
                }
                catch (ClassNotFoundException ignored) {}
            }
        }
    }

    /**
     * Wraps the annotation with environment variable resolution.
     */
    private HazelcastServerOptions wrapServerOptions(HazelcastServerOptions ann)
    {
        return new HazelcastServerOptions()
        {
            @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return HazelcastServerOptions.class; }

            @Override public String clusterName() { return env("CLUSTER_NAME", ann.clusterName()); }
            @Override public String instanceName() { return env("INSTANCE_NAME", ann.instanceName()); }

            @Override public int port() { return Integer.parseInt(env("PORT", String.valueOf(ann.port()))); }
            @Override public boolean portAutoIncrement() { return Boolean.parseBoolean(env("PORT_AUTO_INCREMENT", String.valueOf(ann.portAutoIncrement()))); }
            @Override public int portCount() { return Integer.parseInt(env("PORT_COUNT", String.valueOf(ann.portCount()))); }
            @Override public String publicAddress() { return env("PUBLIC_ADDRESS", ann.publicAddress()); }
            @Override public String interfaces() { return env("INTERFACES", ann.interfaces()); }
            @Override public boolean interfacesEnabled() { return Boolean.parseBoolean(env("INTERFACES_ENABLED", String.valueOf(ann.interfacesEnabled()))); }

            @Override public JoinType joinType() { return JoinType.valueOf(env("JOIN_TYPE", ann.joinType().name())); }
            @Override public boolean autoDetection() { return Boolean.parseBoolean(env("AUTO_DETECTION", String.valueOf(ann.autoDetection()))); }
            @Override public String multicastGroup() { return env("MULTICAST_GROUP", ann.multicastGroup()); }
            @Override public int multicastPort() { return Integer.parseInt(env("MULTICAST_PORT", String.valueOf(ann.multicastPort()))); }
            @Override public int multicastTtl() { return Integer.parseInt(env("MULTICAST_TTL", String.valueOf(ann.multicastTtl()))); }
            @Override public int multicastTimeoutSeconds() { return Integer.parseInt(env("MULTICAST_TIMEOUT_SECONDS", String.valueOf(ann.multicastTimeoutSeconds()))); }
            @Override public String tcpMembers() { return env("TCP_MEMBERS", ann.tcpMembers()); }
            @Override public int tcpConnectionTimeoutSeconds() { return Integer.parseInt(env("TCP_CONNECTION_TIMEOUT_SECONDS", String.valueOf(ann.tcpConnectionTimeoutSeconds()))); }

            @Override public String kubernetesServiceDns() { return env("KUBERNETES_SERVICE_DNS", ann.kubernetesServiceDns()); }
            @Override public String kubernetesNamespace() { return env("KUBERNETES_NAMESPACE", ann.kubernetesNamespace()); }

            @Override public boolean liteMember() { return Boolean.parseBoolean(env("LITE_MEMBER", String.valueOf(ann.liteMember()))); }
            @Override public boolean startLocal() { return Boolean.parseBoolean(env("START_LOCAL", String.valueOf(ann.startLocal()))); }
            @Override public int maxNoHeartbeatSeconds() { return Integer.parseInt(env("MAX_NO_HEARTBEAT_SECONDS", String.valueOf(ann.maxNoHeartbeatSeconds()))); }
            @Override public int cpMemberCount() { return Integer.parseInt(env("CP_MEMBER_COUNT", String.valueOf(ann.cpMemberCount()))); }
        };
    }

    /**
     * Applies the annotation values to the Hazelcast Config.
     */
    static void applyServerOptions(Config config, HazelcastServerOptions opts)
    {
        // Cluster identity
        config.setClusterName(opts.clusterName());
        if (!opts.instanceName().isBlank())
        {
            config.setInstanceName(opts.instanceName());
        }
        else
        {
            config.setInstanceName(opts.clusterName());
        }

        // Also update HazelcastProperties for backward compat
        HazelcastProperties.setGroupName(opts.clusterName());
        HazelcastProperties.setStartLocal(opts.startLocal());

        // Network
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(opts.port());
        network.setPortAutoIncrement(opts.portAutoIncrement());
        network.setPortCount(opts.portCount());

        if (!opts.publicAddress().isBlank())
        {
            network.setPublicAddress(opts.publicAddress());
            HazelcastProperties.setAddress(opts.publicAddress());
        }

        // Interfaces
        if (opts.interfacesEnabled() || !opts.interfaces().isBlank())
        {
            InterfacesConfig ifConfig = network.getInterfaces();
            ifConfig.setEnabled(true);
            for (String iface : opts.interfaces().split(","))
            {
                String trimmed = iface.trim();
                if (!trimmed.isEmpty())
                {
                    ifConfig.addInterface(trimmed);
                }
            }
        }

        // Join configuration
        JoinConfig join = network.getJoin();
        join.getAutoDetectionConfig().setEnabled(opts.autoDetection());

        switch (opts.joinType())
        {
            case MULTICAST ->
            {
                join.getMulticastConfig().setEnabled(true);
                join.getTcpIpConfig().setEnabled(false);
                join.getMulticastConfig().setMulticastGroup(opts.multicastGroup());
                join.getMulticastConfig().setMulticastPort(opts.multicastPort());
                join.getMulticastConfig().setMulticastTimeToLive(opts.multicastTtl());
                join.getMulticastConfig().setMulticastTimeoutSeconds(opts.multicastTimeoutSeconds());
            }
            case TCP ->
            {
                join.getMulticastConfig().setEnabled(false);
                join.getTcpIpConfig().setEnabled(true);
                join.getTcpIpConfig().setConnectionTimeoutSeconds(opts.tcpConnectionTimeoutSeconds());
                if (!opts.tcpMembers().isBlank())
                {
                    for (String member : opts.tcpMembers().split(","))
                    {
                        String trimmed = member.trim();
                        if (!trimmed.isEmpty())
                        {
                            join.getTcpIpConfig().addMember(trimmed);
                        }
                    }
                }
            }
            case KUBERNETES ->
            {
                join.getMulticastConfig().setEnabled(false);
                join.getAutoDetectionConfig().setEnabled(false);
                var k8s = join.getKubernetesConfig();
                k8s.setEnabled(true);
                if (!opts.kubernetesServiceDns().isBlank())
                {
                    k8s.setProperty("service-dns", opts.kubernetesServiceDns());
                }
                if (!opts.kubernetesNamespace().isBlank())
                {
                    k8s.setProperty("namespace", opts.kubernetesNamespace());
                }
            }
            case NONE ->
            {
                join.getMulticastConfig().setEnabled(false);
                join.getTcpIpConfig().setEnabled(false);
                join.getAutoDetectionConfig().setEnabled(false);
            }
        }

        // Lite member
        config.setLiteMember(opts.liteMember());

        // Heartbeat
        if (opts.maxNoHeartbeatSeconds() > 0)
        {
            config.setProperty("hazelcast.max.no.heartbeat.seconds", String.valueOf(opts.maxNoHeartbeatSeconds()));
        }

        // CP subsystem (via system property since CPSubsystemConfig is not exported)
        if (opts.cpMemberCount() > 0)
        {
            config.setProperty("hazelcast.cp-subsystem.cp-member-count", String.valueOf(opts.cpMemberCount()));
        }

        log.info("Hazelcast server configured: cluster={}, port={}, join={}, lite={}, startLocal={}",
                opts.clusterName(), opts.port(), opts.joinType(), opts.liteMember(), opts.startLocal());
    }

    /**
     * Environment variable resolution: HAZELCAST_{PROPERTY} → default.
     */
    static String env(String property, String defaultValue)
    {
        String value = Environment.getSystemPropertyOrEnvironment("HAZELCAST_" + property, null);
        if (value != null && !value.isBlank())
        {
            return value;
        }
        return defaultValue;
    }

    @Override
    public Integer sortOrder()
    {
        return Integer.MIN_VALUE + 70;
    }
}

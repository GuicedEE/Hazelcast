package com.guicedee.guicedhazelcast.services;

import com.google.common.base.Strings;
import com.guicedee.client.Environment;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.services.lifecycle.IGuicePreStartup;
import com.guicedee.guicedhazelcast.HazelcastClientOptions;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.vertx.spi.VertXPreStartup;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.client.config.ConnectionRetryConfig;
import com.hazelcast.core.HazelcastInstance;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.vertx.core.Future;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

@Log4j2
public class HazelcastClientPreStartup implements IGuicePreStartup<HazelcastClientPreStartup>
{
    @Getter
    public static HazelcastInstance clientInstance;
    @Getter
    public static ClientConfig config;
    @Getter
    private static HazelcastClientOptions clientOptions;

    @Override
    public List<Future<Boolean>> onStartup()
    {
        if (clientInstance != null)
        {
            return List.of(Future.succeededFuture(true));
        }
        if (config == null)
        {
            config = new ClientConfig();
        }

        return List.of(
                VertXPreStartup.getVertx().executeBlocking(() -> {
                    // Scan for @HazelcastClientOptions
                    ScanResult scanResult = IGuiceContext.instance().getScanResult();
                    discoverClientOptions(scanResult);

                    if (clientOptions != null)
                    {
                        applyClientOptions(config, clientOptions);
                    }
                    else
                    {
                        // Fallback to old env-based config
                        config.setProperty("hazelcast.client.shuffle.member.list", "true");
                        config.setProperty("hazelcast.client.heartbeat.timeout", "60000");
                        config.setProperty("hazelcast.client.heartbeat.interval", "5000");
                        config.setProperty("hazelcast.client.event.thread.count", "5");
                        config.setProperty("hazelcast.client.event.queue.capacity", "1000000");
                        config.setProperty("hazelcast.client.invocation.timeout.seconds", "120");
                        HazelcastProperties.setAddress(Environment.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", "localhost"));
                        config.getNetworkConfig().addAddress(HazelcastProperties.getAddress());
                        HazelcastProperties.setGroupName(Environment.getSystemPropertyOrEnvironment("GROUP_NAME", "dev"));
                        config.setClusterName(HazelcastProperties.getGroupName());
                        config.setInstanceName(HazelcastProperties.getGroupName());
                    }

                    // Run SPI hooks
                    @SuppressWarnings("rawtypes")
                    Set<IGuicedHazelcastClientConfig> configSet = IGuiceContext
                            .instance()
                            .getLoader(IGuicedHazelcastClientConfig.class, true, ServiceLoader.load(IGuicedHazelcastClientConfig.class));
                    for (IGuicedHazelcastClientConfig<?> spiConfig : configSet)
                    {
                        config = spiConfig.buildConfig(config);
                    }

                    log.info("Final Hazelcast Client Configuration - cluster={}, instance={}",
                            config.getClusterName(), config.getInstanceName());

                    try
                    {
                        config.addLabel(InetAddress.getLocalHost().getHostAddress());
                    }
                    catch (UnknownHostException e)
                    {
                        log.error("Unable to resolve local host address", e);
                    }

                    if (Strings.isNullOrEmpty(config.getClusterName()))
                    {
                        config.setClusterName("dev");
                    }
                    if (Strings.isNullOrEmpty(config.getInstanceName()))
                    {
                        config.setInstanceName("dev");
                    }

                    clientInstance = HazelcastClient.getOrCreateHazelcastClient(config);
                    return true;
                })
        );
    }

    private void discoverClientOptions(ScanResult scanResult)
    {
        // Check classes
        ClassInfoList annotatedClasses = scanResult.getClassesWithAnnotation(HazelcastClientOptions.class);
        for (var ci : annotatedClasses)
        {
            var ann = ci.loadClass().getAnnotation(HazelcastClientOptions.class);
            if (ann != null)
            {
                clientOptions = wrapClientOptions(ann);
                log.debug("Found @HazelcastClientOptions on class: {}", ci.getName());
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
                    var ann = pkgClass.getAnnotation(HazelcastClientOptions.class);
                    if (ann != null)
                    {
                        clientOptions = wrapClientOptions(ann);
                        log.debug("Found @HazelcastClientOptions on package: {}", pkgInfo.getName());
                        return;
                    }
                }
                catch (ClassNotFoundException ignored) {}
            }
        }
    }

    private HazelcastClientOptions wrapClientOptions(HazelcastClientOptions ann)
    {
        return new HazelcastClientOptions()
        {
            @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return HazelcastClientOptions.class; }

            @Override public String clusterName() { return envClient("CLUSTER_NAME", ann.clusterName()); }
            @Override public String instanceName() { return envClient("INSTANCE_NAME", ann.instanceName()); }
            @Override public String addresses() { return envClient("ADDRESSES", ann.addresses()); }
            @Override public int connectionTimeoutMs() { return Integer.parseInt(envClient("CONNECTION_TIMEOUT_MS", String.valueOf(ann.connectionTimeoutMs()))); }
            @Override public int heartbeatIntervalMs() { return Integer.parseInt(envClient("HEARTBEAT_INTERVAL_MS", String.valueOf(ann.heartbeatIntervalMs()))); }
            @Override public int heartbeatTimeoutMs() { return Integer.parseInt(envClient("HEARTBEAT_TIMEOUT_MS", String.valueOf(ann.heartbeatTimeoutMs()))); }
            @Override public int invocationTimeoutSeconds() { return Integer.parseInt(envClient("INVOCATION_TIMEOUT_SECONDS", String.valueOf(ann.invocationTimeoutSeconds()))); }
            @Override public int eventThreadCount() { return Integer.parseInt(envClient("EVENT_THREAD_COUNT", String.valueOf(ann.eventThreadCount()))); }
            @Override public int eventQueueCapacity() { return Integer.parseInt(envClient("EVENT_QUEUE_CAPACITY", String.valueOf(ann.eventQueueCapacity()))); }
            @Override public boolean shuffleMemberList() { return Boolean.parseBoolean(envClient("SHUFFLE_MEMBER_LIST", String.valueOf(ann.shuffleMemberList()))); }
            @Override public boolean smartRouting() { return Boolean.parseBoolean(envClient("SMART_ROUTING", String.valueOf(ann.smartRouting()))); }
            @Override public ReconnectMode reconnectMode() { return ReconnectMode.valueOf(envClient("RECONNECT_MODE", ann.reconnectMode().name())); }
            @Override public int reconnectInitialBackoffMs() { return Integer.parseInt(envClient("RECONNECT_INITIAL_BACKOFF_MS", String.valueOf(ann.reconnectInitialBackoffMs()))); }
            @Override public int reconnectMaxBackoffMs() { return Integer.parseInt(envClient("RECONNECT_MAX_BACKOFF_MS", String.valueOf(ann.reconnectMaxBackoffMs()))); }
            @Override public double reconnectMultiplier() { return Double.parseDouble(envClient("RECONNECT_MULTIPLIER", String.valueOf(ann.reconnectMultiplier()))); }
            @Override public long clusterConnectTimeoutMs() { return Long.parseLong(envClient("CLUSTER_CONNECT_TIMEOUT_MS", String.valueOf(ann.clusterConnectTimeoutMs()))); }
            @Override public String labels() { return envClient("LABELS", ann.labels()); }
        };
    }

    static void applyClientOptions(ClientConfig config, HazelcastClientOptions opts)
    {
        config.setClusterName(opts.clusterName());
        if (!opts.instanceName().isBlank())
        {
            config.setInstanceName(opts.instanceName());
        }
        else
        {
            config.setInstanceName(opts.clusterName());
        }

        HazelcastProperties.setGroupName(opts.clusterName());

        // Addresses
        for (String addr : opts.addresses().split(","))
        {
            String trimmed = addr.trim();
            if (!trimmed.isEmpty())
            {
                config.getNetworkConfig().addAddress(trimmed);
            }
        }
        HazelcastProperties.setAddress(opts.addresses().split(",")[0].trim());

        // Connection
        config.getNetworkConfig().setConnectionTimeout(opts.connectionTimeoutMs());
        config.getNetworkConfig().setSmartRouting(opts.smartRouting());

        // Properties
        config.setProperty("hazelcast.client.heartbeat.timeout", String.valueOf(opts.heartbeatTimeoutMs()));
        config.setProperty("hazelcast.client.heartbeat.interval", String.valueOf(opts.heartbeatIntervalMs()));
        config.setProperty("hazelcast.client.invocation.timeout.seconds", String.valueOf(opts.invocationTimeoutSeconds()));
        config.setProperty("hazelcast.client.event.thread.count", String.valueOf(opts.eventThreadCount()));
        config.setProperty("hazelcast.client.event.queue.capacity", String.valueOf(opts.eventQueueCapacity()));
        config.setProperty("hazelcast.client.shuffle.member.list", String.valueOf(opts.shuffleMemberList()));

        // Reconnection strategy
        ClientConnectionStrategyConfig strategy = config.getConnectionStrategyConfig();
        switch (opts.reconnectMode())
        {
            case OFF -> strategy.setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.OFF);
            case ON -> strategy.setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ON);
            case ASYNC -> strategy.setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);
        }

        ConnectionRetryConfig retry = strategy.getConnectionRetryConfig();
        retry.setInitialBackoffMillis(opts.reconnectInitialBackoffMs());
        retry.setMaxBackoffMillis(opts.reconnectMaxBackoffMs());
        retry.setMultiplier(opts.reconnectMultiplier());
        retry.setClusterConnectTimeoutMillis(opts.clusterConnectTimeoutMs());

        // Labels
        if (!opts.labels().isBlank())
        {
            for (String label : opts.labels().split(","))
            {
                String trimmed = label.trim();
                if (!trimmed.isEmpty())
                {
                    config.addLabel(trimmed);
                }
            }
        }

        log.info("Hazelcast client configured: cluster={}, addresses={}, smartRouting={}, reconnect={}",
                opts.clusterName(), opts.addresses(), opts.smartRouting(), opts.reconnectMode());
    }

    static String envClient(String property, String defaultValue)
    {
        String value = Environment.getSystemPropertyOrEnvironment("HAZELCAST_CLIENT_" + property, null);
        if (value != null && !value.isBlank())
        {
            return value;
        }
        return defaultValue;
    }

    @Override
    public Integer sortOrder()
    {
        return Integer.MIN_VALUE + 71;
    }
}

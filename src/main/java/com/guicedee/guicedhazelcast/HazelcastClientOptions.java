package com.guicedee.guicedhazelcast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares Hazelcast client-side configuration for connecting to a remote cluster.
 * Place on a class or {@code package-info.java}.
 * <p>
 * All attributes can be overridden via environment variables:
 * <ul>
 *   <li>{@code HAZELCAST_CLIENT_{PROPERTY}} — global</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface HazelcastClientOptions
{
    // ── Cluster identity ──────────────────────────────

    /**
     * @return Cluster name to connect to.
     */
    String clusterName() default "dev";

    /**
     * @return Client instance name (unique per JVM).
     */
    String instanceName() default "";

    // ── Network ───────────────────────────────────────

    /**
     * @return Comma-separated member addresses (e.g. "localhost:5701,192.168.1.10:5701").
     */
    String addresses() default "localhost";

    /**
     * @return Connection timeout in milliseconds (default 5000).
     */
    int connectionTimeoutMs() default 5000;

    // ── Heartbeat ────────────────────────────────────

    /**
     * @return Heartbeat interval in milliseconds (default 5000).
     */
    int heartbeatIntervalMs() default 5000;

    /**
     * @return Heartbeat timeout in milliseconds (default 60000).
     */
    int heartbeatTimeoutMs() default 60000;

    // ── Invocation ───────────────────────────────────

    /**
     * @return Invocation timeout in seconds (default 120).
     */
    int invocationTimeoutSeconds() default 120;

    // ── Threading ────────────────────────────────────

    /**
     * @return Number of event handler threads (default 5).
     */
    int eventThreadCount() default 5;

    /**
     * @return Event queue capacity (default 1000000).
     */
    int eventQueueCapacity() default 1000000;

    // ── Shuffle ──────────────────────────────────────

    /**
     * @return Whether to shuffle the member list for load balancing.
     */
    boolean shuffleMemberList() default true;

    // ── Smart routing ────────────────────────────────

    /**
     * @return Enable smart routing (route to data owner, default true).
     */
    boolean smartRouting() default true;

    // ── Reconnection ─────────────────────────────────

    /**
     * @return Reconnect mode: OFF, ON, ASYNC.
     */
    ReconnectMode reconnectMode() default ReconnectMode.ON;

    /**
     * @return Initial backoff for reconnection in milliseconds.
     */
    int reconnectInitialBackoffMs() default 1000;

    /**
     * @return Max backoff for reconnection in milliseconds.
     */
    int reconnectMaxBackoffMs() default 30000;

    /**
     * @return Multiplier for backoff (default 1.05).
     */
    double reconnectMultiplier() default 1.05;

    /**
     * @return Cluster connect timeout in milliseconds (-1 = infinite, 0 = no retry, default 20000).
     */
    long clusterConnectTimeoutMs() default 20000;

    // ── Labels ───────────────────────────────────────

    /**
     * @return Comma-separated labels for this client instance.
     */
    String labels() default "";

    enum ReconnectMode
    {
        OFF,
        ON,
        ASYNC
    }
}


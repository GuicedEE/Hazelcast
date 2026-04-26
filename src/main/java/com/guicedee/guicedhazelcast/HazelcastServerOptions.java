package com.guicedee.guicedhazelcast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares Hazelcast server-side (embedded member) configuration.
 * Place on a class or {@code package-info.java}.
 * <p>
 * All attributes can be overridden via environment variables:
 * <ul>
 *   <li>{@code HAZELCAST_{PROPERTY}} — global</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface HazelcastServerOptions
{
    // ── Cluster identity ──────────────────────────────

    /**
     * @return Cluster name. All members must share the same name.
     */
    String clusterName() default "dev";

    /**
     * @return Instance name (unique per JVM).
     */
    String instanceName() default "";

    // ── Network ───────────────────────────────────────

    /**
     * @return Port the member listens on (default 5701).
     */
    int port() default 5701;

    /**
     * @return Whether to auto-increment port if already in use.
     */
    boolean portAutoIncrement() default true;

    /**
     * @return Max range for port auto-increment (default 100).
     */
    int portCount() default 100;

    /**
     * @return Public address advertised to other members (e.g. external IP).
     */
    String publicAddress() default "";

    /**
     * @return Interfaces to bind to (comma-separated, e.g. "192.168.1.*,10.0.0.*").
     */
    String interfaces() default "";

    /**
     * @return Whether to enable interface filtering.
     */
    boolean interfacesEnabled() default false;

    // ── Discovery / Join ─────────────────────────────

    /**
     * @return Join mechanism: MULTICAST, TCP, KUBERNETES, NONE.
     */
    JoinType joinType() default JoinType.MULTICAST;

    /**
     * @return Enable auto-detection of join mechanism.
     */
    boolean autoDetection() default true;

    /**
     * @return Multicast group address (default 224.2.2.3).
     */
    String multicastGroup() default "224.2.2.3";

    /**
     * @return Multicast port (default 54327).
     */
    int multicastPort() default 54327;

    /**
     * @return Multicast time-to-live (default 32).
     */
    int multicastTtl() default 32;

    /**
     * @return Multicast timeout in seconds (default 2).
     */
    int multicastTimeoutSeconds() default 2;

    /**
     * @return TCP member addresses for TCP-IP join (comma-separated, e.g. "192.168.1.10,192.168.1.11").
     */
    String tcpMembers() default "";

    /**
     * @return Connection timeout for TCP-IP join in milliseconds.
     */
    int tcpConnectionTimeoutSeconds() default 5;

    // ── Kubernetes ────────────────────────────────────

    /**
     * @return Kubernetes service DNS name for DNS lookup discovery.
     */
    String kubernetesServiceDns() default "";

    /**
     * @return Kubernetes namespace.
     */
    String kubernetesNamespace() default "default";

    // ── Lite member ──────────────────────────────────

    /**
     * @return If true, this member is a lite member (no data partitions).
     */
    boolean liteMember() default false;

    // ── Startup ──────────────────────────────────────

    /**
     * @return If true, start a local embedded Hazelcast instance.
     * If false, only the configuration is prepared (for client mode or deferred start).
     */
    boolean startLocal() default false;

    // ── Heartbeat ────────────────────────────────────

    /**
     * @return Max time without heartbeat before member is considered dead (seconds, 0 = default 60s).
     */
    int maxNoHeartbeatSeconds() default 0;

    // ── CP subsystem ────────────────────────────────

    /**
     * @return CP member count for CP subsystem (0 = disabled).
     */
    int cpMemberCount() default 0;

    enum JoinType
    {
        MULTICAST,
        TCP,
        KUBERNETES,
        NONE
    }
}


package com.guicedee.guicedhazelcast;

import com.guicedee.logger.LogFactory;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class HazelcastProperties

{
	/**
	 * The property to enable native client mode (this client mode)
	 */
	public static final String HazelcastNativeClientProperty = "hibernate.cache.hazelcast.use_native_client";
	private static final Logger log = LogFactory.getLog(HazelcastProperties.class);
	/**
	 * The specific region name to apply the configuration to
	 */
	public static String regionName;
	/**
	 * If the local region factory must be used
	 */
	public static boolean useLocalRegionFactory;

	/**
	 * The given address to contact Hazelcast
	 */
	public static String address;
	/**
	 * The given group name to contact Hazelcast
	 */
	public static String groupName = "dev";
	/**
	 * The instance for hazelcast
	 */
	public static String instanceName;
	public static boolean startLocal;

	/**
	 * The specific region name to apply the configuration to
	 *
	 * @return
	 */
	public static String getRegionName()
	{
		return HazelcastProperties.regionName;
	}

	/**
	 * The specific region name to apply the configuration to
	 *
	 * @param regionName
	 */
	public static void setRegionName(String regionName)
	{
		HazelcastProperties.regionName = regionName;
	}

	/**
	 * The given address to contact Hazelcast
	 *
	 * @return
	 */
	public static String getAddress()
	{
		return HazelcastProperties.address;
	}

	/**
	 * The given address to contact Hazelcast
	 *
	 * @param address
	 */
	public static void setAddress(String address)
	{
		HazelcastProperties.address = address;
	}

	/**
	 * The given group name to contact Hazelcast
	 *
	 * @return
	 */
	public static String getGroupName()
	{
		return HazelcastProperties.groupName;
	}

	/**
	 * The given group name to contact Hazelcast
	 *
	 * @param groupName
	 */
	public static void setGroupName(String groupName)
	{
		HazelcastProperties.groupName = groupName;
	}

	public static boolean isStartLocal()
	{
		return startLocal;
	}

	public static void setStartLocal(boolean startLocal)
	{
		HazelcastProperties.startLocal = startLocal;
	}

	/**
	 * The instance for hazelcast
	 *
	 * @return
	 */
	public static String getInstanceName()
	{
		return HazelcastProperties.instanceName;
	}

	/**
	 * The instance for hazelcast
	 *
	 * @param instanceName
	 */
	public static void setInstanceName(String instanceName)
	{
		HazelcastProperties.instanceName = instanceName;
	}

	/**
	 * If the local region factory must be used
	 *
	 * @return
	 */
	public static boolean isUseLocalRegionFactory()
	{
		return HazelcastProperties.useLocalRegionFactory;
	}

	/**
	 * If the local region factory must be used
	 *
	 * @param useLocalRegionFactory
	 */
	public static void setUseLocalRegionFactory(boolean useLocalRegionFactory)
	{
		HazelcastProperties.useLocalRegionFactory = useLocalRegionFactory;
	}
}

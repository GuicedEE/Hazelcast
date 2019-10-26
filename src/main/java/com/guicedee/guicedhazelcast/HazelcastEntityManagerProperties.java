package com.guicedee.guicedhazelcast;

import com.guicedee.guicedpersistence.services.IPropertiesEntityManagerReader;
import com.oracle.jaxb21.PersistenceUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("unused")
public class HazelcastEntityManagerProperties
		implements IPropertiesEntityManagerReader
{
	/**
	 * The property to enable native client mode (this client mode)
	 */
	private static final String HazelcastNativeClientProperty = "hibernate.cache.hazelcast.use_native_client";

	/**
	 * The specific region name to apply the configuration to
	 */
	private static String regionName;
	/**
	 * If the local region factory must be used
	 */
	private static boolean useLocalRegionFactory;

	/**
	 * The given address to contact Hazelcast
	 */
	private static String address;
	/**
	 * The given group name to contact Hazelcast
	 */
	private static String groupName;
	/**
	 * The instance for hazelcast
	 */
	private static String instanceName;

	/**
	 * The specific region name to apply the configuration to
	 *
	 * @return
	 */
	public static String getRegionName()
	{
		return HazelcastEntityManagerProperties.regionName;
	}

	/**
	 * The specific region name to apply the configuration to
	 *
	 * @param regionName
	 */
	public static void setRegionName(String regionName)
	{
		HazelcastEntityManagerProperties.regionName = regionName;
	}

	/**
	 * If the local region factory must be used
	 *
	 * @return
	 */
	public static boolean isUseLocalRegionFactory()
	{
		return HazelcastEntityManagerProperties.useLocalRegionFactory;
	}

	/**
	 * If the local region factory must be used
	 *
	 * @param useLocalRegionFactory
	 */
	public static void setUseLocalRegionFactory(boolean useLocalRegionFactory)
	{
		HazelcastEntityManagerProperties.useLocalRegionFactory = useLocalRegionFactory;
	}

	/**
	 * The given address to contact Hazelcast
	 *
	 * @return
	 */
	public static String getAddress()
	{
		return HazelcastEntityManagerProperties.address;
	}

	/**
	 * The given address to contact Hazelcast
	 *
	 * @param address
	 */
	public static void setAddress(String address)
	{
		HazelcastEntityManagerProperties.address = address;
	}

	/**
	 * The given group name to contact Hazelcast
	 *
	 * @return
	 */
	public static String getGroupName()
	{
		return HazelcastEntityManagerProperties.groupName;
	}

	/**
	 * The given group name to contact Hazelcast
	 *
	 * @param groupName
	 */
	public static void setGroupName(String groupName)
	{
		HazelcastEntityManagerProperties.groupName = groupName;
	}

	/**
	 * The instance for hazelcast
	 *
	 * @return
	 */
	public static String getInstanceName()
	{
		return HazelcastEntityManagerProperties.instanceName;
	}

	/**
	 * The instance for hazelcast
	 *
	 * @param instanceName
	 */
	public static void setInstanceName(String instanceName)
	{
		HazelcastEntityManagerProperties.instanceName = instanceName;
	}

	@Override
	public Map<String, String> processProperties(PersistenceUnit persistenceUnit, Properties incomingProperties)
	{
		Map<String, String> props = new HashMap<>();

		props.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
		props.put("hibernate.javax.cache.provider", "com.hazelcast.client.cache.impl.HazelcastClientCachingProvider");

		if (!incomingProperties.containsKey("hibernate.cache.use_second_level_cache"))
		{
			props.put("hibernate.cache.use_second_level_cache", "true");
		}
		if (!incomingProperties.containsKey("hibernate.cache.use_query_cache"))
		{
			props.put("hibernate.cache.use_query_cache", "true");
		}
		if (!incomingProperties.containsKey("hibernate.cache.use_minimal_puts"))
		{
			props.put("hibernate.cache.use_minimal_puts", "true");
		}

		if (HazelcastEntityManagerProperties.address != null)
		{
			props.put(HazelcastNativeClientProperty, "true");
			props.put("hibernate.cache.hazelcast.native_client_hosts", HazelcastEntityManagerProperties.address);
			props.put("hibernate.cache.hazelcast.native_client_address", HazelcastEntityManagerProperties.address);
		}
		if (HazelcastEntityManagerProperties.groupName != null)
		{
			props.put(HazelcastNativeClientProperty, "true");
			props.put("hibernate.cache.hazelcast.native_client_group", HazelcastEntityManagerProperties.groupName);
		}
		if (HazelcastEntityManagerProperties.instanceName != null)
		{
			props.put(HazelcastNativeClientProperty, "true");
			props.put("hibernate.cache.hazelcast.instance_name", HazelcastEntityManagerProperties.instanceName);
		}

		if (HazelcastEntityManagerProperties.regionName != null)
		{
			props.put("hibernate.cache.region_prefix", HazelcastEntityManagerProperties.regionName);
		}

		return props;
	}
}

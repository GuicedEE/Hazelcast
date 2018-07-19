package com.jwebmp.guicedhazelcast;

import com.jwebmp.guicedpersistence.db.PropertiesEntityManagerReader;
import com.oracle.jaxb21.PersistenceUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("unused")
public class HazelcastEntityManagerProperties
		implements PropertiesEntityManagerReader
{
	private static String regionName = null;
	private static boolean useLocalRegionFactory = false;

	private static String address;
	private static String groupName;
	private static String groupPass;
	private static String instanceName;

	@Override
	public Map<String, String> processProperties(PersistenceUnit persistenceUnit, Properties incomingProperties)
	{
		Map<String, String> props = new HashMap<>();

		props.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
		props.put("hibernate.javax.cache.provider", "com.hazelcast.client.cache.impl.HazelcastClientCachingProvider");

		props.put("hibernate.cache.use_second_level_cache", "true");
		props.put("hibernate.cache.use_query_cache", "true");
		props.put("hibernate.cache.use_minimal_puts", "true");

		if (address != null)
		{
			props.put("hibernate.cache.hazelcast.use_native_client", "true");
			props.put("hibernate.cache.hazelcast.native_client_hosts", address);
			props.put("hibernate.cache.hazelcast.native_client_address", address);
		}
		if (groupName != null)
		{
			props.put("hibernate.cache.hazelcast.use_native_client", "true");
			props.put("hibernate.cache.hazelcast.native_client_group", groupName);
		}
		if (groupPass != null)
		{
			props.put("hibernate.cache.hazelcast.use_native_client", "true");
			props.put("hibernate.cache.hazelcast.native_client_password", groupPass);
		}
		if (instanceName != null)
		{
			props.put("hibernate.cache.hazelcast.use_native_client", "true");
			props.put("hibernate.cache.hazelcast.instance_name", instanceName);
		}

		if (regionName != null)
		{
			props.put("hibernate.cache.region_prefix", regionName);
		}

		return props;
	}

	/**
	 * Sets a region name if required
	 *
	 * @return The applied region name or null
	 */
	public static String getRegionName()
	{
		return regionName;
	}

	/**
	 * Sets the region name if required
	 *
	 * @param regionName
	 * 		The region name to apply
	 */
	public static void setRegionName(String regionName)
	{
		HazelcastEntityManagerProperties.regionName = regionName;
	}

	public static boolean isUseLocalRegionFactory()
	{
		return useLocalRegionFactory;
	}

	public static void setUseLocalRegionFactory(boolean useLocalRegionFactory)
	{
		HazelcastEntityManagerProperties.useLocalRegionFactory = useLocalRegionFactory;
	}

	public static String getAddress()
	{
		return address;
	}

	public static void setAddress(String address)
	{
		HazelcastEntityManagerProperties.address = address;
	}

	public static String getGroupName()
	{
		return groupName;
	}

	public static void setGroupName(String groupName)
	{
		HazelcastEntityManagerProperties.groupName = groupName;
	}

	public static String getGroupPass()
	{
		return groupPass;
	}

	public static void setGroupPass(String groupPass)
	{
		HazelcastEntityManagerProperties.groupPass = groupPass;
	}

	public static String getInstanceName()
	{
		return instanceName;
	}

	public static void setInstanceName(String instanceName)
	{
		HazelcastEntityManagerProperties.instanceName = instanceName;
	}
}

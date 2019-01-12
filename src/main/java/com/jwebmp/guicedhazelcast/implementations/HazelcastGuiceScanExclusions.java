package com.jwebmp.guicedhazelcast.implementations;

import com.jwebmp.guicedinjection.interfaces.IGuiceScanJarExclusions;
import com.jwebmp.guicedinjection.interfaces.IGuiceScanModuleExclusions;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class HazelcastGuiceScanExclusions
		implements IGuiceScanJarExclusions<HazelcastGuiceScanExclusions>,
				           IGuiceScanModuleExclusions<HazelcastGuiceScanExclusions>
{
	@Override
	public @NotNull Set<String> excludeJars()
	{
		Set<String> strings = new HashSet<>();
		strings.add("hazelcast-*");
		strings.add("cache-annotations-ri-guice-*");
		strings.add("hibernate-jcache-*");
		strings.add("jboss-logging-*");
		strings.add("cache-annotations-ri-common-*");
		strings.add("cache-api-*");
		return strings;
	}

	@Override
	public @NotNull Set<String> excludeModules()
	{
		Set<String> strings = new HashSet<>();
		strings.add("com.jwebmp.guicedhazelcast");
		strings.add("hazelcast.all");
		strings.add("cache.annotations.ri.guice");
		strings.add("org.hibernate.orm.jcache");
		strings.add("cache.api");
		return strings;
	}
}

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

		strings.add("com.google.guice");
		strings.add("hazelcast.all");

		strings.add("com.jwebmp.guicedinjection");
		strings.add("com.jwebmp.logmaster");
		strings.add("java.logging");
		strings.add("cache.annotations.ri.guice");
		strings.add("java.validation");

		strings.add("com.jwebmp.guicedpersistence");
		strings.add("org.hibernate.orm.jcache");
		strings.add("cache.api");
		strings.add("io.github.classgraph");
		strings.add("org.apache.commons.io");

		strings.add("com.google.common");

		strings.add("com.jwebmp.guicedpersistence.readers.hibernateproperties");
		strings.add("javax.inject");

		return strings;
	}
}

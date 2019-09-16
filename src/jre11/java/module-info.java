module com.jwebmp.guicedhazelcast {
	exports com.jwebmp.guicedhazelcast;

	requires com.google.guice;
	requires hazelcast.all;

	requires com.jwebmp.guicedinjection;
	requires com.jwebmp.logmaster;
	requires java.logging;
	requires cache.annotations.ri.guice;
	requires java.validation;

	requires com.jwebmp.guicedpersistence;
	requires org.hibernate.orm.jcache;
	requires cache.api;
	requires io.github.classgraph;

	requires transitive org.apache.commons.io;

	requires com.google.common;

	requires transitive jdk.unsupported;

	requires com.jwebmp.guicedpersistence.readers.hibernateproperties;
	requires javax.inject;

	uses com.jwebmp.guicedhazelcast.services.IGuicedHazelcastClientConfig;

	provides com.jwebmp.guicedpersistence.services.IPropertiesEntityManagerReader with com.jwebmp.guicedhazelcast.HazelcastEntityManagerProperties;

	provides com.jwebmp.guicedinjection.interfaces.IGuiceScanModuleExclusions with com.jwebmp.guicedhazelcast.implementations.HazelcastGuiceScanExclusions;
	provides com.jwebmp.guicedinjection.interfaces.IGuiceScanJarExclusions with com.jwebmp.guicedhazelcast.implementations.HazelcastGuiceScanExclusions;

	provides com.jwebmp.guicedinjection.interfaces.IFileContentsScanner with com.jwebmp.guicedhazelcast.HazelcastConfigHandler;
	provides com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder with com.jwebmp.guicedhazelcast.implementations.HazelcastBinderGuice;

	provides com.jwebmp.guicedinjection.interfaces.IGuicePostStartup with com.jwebmp.guicedhazelcast.implementations.HazelcastPostStartup;
	provides com.jwebmp.guicedinjection.interfaces.IGuicePreStartup with com.jwebmp.guicedhazelcast.implementations.HazelcastPreStartup;

	opens com.jwebmp.guicedhazelcast.implementations to com.google.guice;
	opens com.jwebmp.guicedhazelcast to com.google.guice;
	opens com.jwebmp.guicedhazelcast.services to com.google.guice;
}


module com.guicedee.guicedhazelcast {
	exports com.guicedee.guicedhazelcast;
	exports com.guicedee.guicedhazelcast.services;

	requires com.google.guice;
	requires com.hazelcast.all;

	requires com.guicedee.guicedinjection;
	requires com.guicedee.logmaster;
	requires java.logging;
	requires cache.annotations.ri.guice;
	requires java.validation;

	requires com.guicedee.guicedpersistence;
	requires org.hibernate.orm.jcache;
	requires cache.api;
	requires io.github.classgraph;

	requires transitive org.apache.commons.io;

	requires com.google.common;

	requires jdk.unsupported;

	requires com.guicedee.guicedpersistence.readers.hibernateproperties;
	requires javax.inject;

	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;

	provides com.guicedee.guicedpersistence.services.IPropertiesEntityManagerReader with com.guicedee.guicedhazelcast.HazelcastEntityManagerProperties;

	provides com.guicedee.guicedinjection.interfaces.IGuiceScanModuleExclusions with com.guicedee.guicedhazelcast.implementations.HazelcastGuiceScanExclusions;
	provides com.guicedee.guicedinjection.interfaces.IGuiceScanJarExclusions with com.guicedee.guicedhazelcast.implementations.HazelcastGuiceScanExclusions;

	provides com.guicedee.guicedinjection.interfaces.IFileContentsScanner with com.guicedee.guicedhazelcast.HazelcastConfigHandler;
	provides com.guicedee.guicedinjection.interfaces.IGuiceDefaultBinder with com.guicedee.guicedhazelcast.implementations.HazelcastBinderGuice;

	provides com.guicedee.guicedinjection.interfaces.IGuicePostStartup with com.guicedee.guicedhazelcast.implementations.HazelcastPostStartup;
	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedhazelcast.implementations.HazelcastPreStartup;

	opens com.guicedee.guicedhazelcast to com.google.guice;
	opens com.guicedee.guicedhazelcast.annotations to com.google.guice;
	opens com.guicedee.guicedhazelcast.implementations to com.google.guice;
	opens com.guicedee.guicedhazelcast.services to com.google.guice;
}

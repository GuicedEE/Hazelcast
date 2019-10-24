import com.guicedee.guicedhazelcast.HazelcastConfigHandler;
import com.guicedee.guicedhazelcast.HazelcastEntityManagerProperties;
import com.guicedee.guicedhazelcast.implementations.HazelcastBinderGuice;
import com.guicedee.guicedhazelcast.implementations.HazelcastGuiceScanExclusions;
import com.guicedee.guicedhazelcast.implementations.HazelcastPostStartup;
import com.guicedee.guicedhazelcast.implementations.HazelcastPreStartup;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
import com.guicedee.guicedinjection.interfaces.*;
import com.guicedee.guicedpersistence.services.IPropertiesEntityManagerReader;

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

	uses IGuicedHazelcastClientConfig;
	uses IGuicedHazelcastServerConfig;

	provides IPropertiesEntityManagerReader with HazelcastEntityManagerProperties;

	provides IGuiceScanModuleExclusions with HazelcastGuiceScanExclusions;
	provides IGuiceScanJarExclusions with HazelcastGuiceScanExclusions;

	provides IFileContentsScanner with HazelcastConfigHandler;
	provides IGuiceDefaultBinder with HazelcastBinderGuice;

	provides IGuicePostStartup with HazelcastPostStartup;
	provides IGuicePreStartup with HazelcastPreStartup;

	opens com.guicedee.guicedhazelcast to com.google.guice;
	opens com.guicedee.guicedhazelcast.annotations to com.google.guice;
	opens com.guicedee.guicedhazelcast.implementations to com.google.guice;
	opens com.guicedee.guicedhazelcast.services to com.google.guice;
}

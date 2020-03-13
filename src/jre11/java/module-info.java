import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;

module com.guicedee.guicedhazelcast {
	exports com.guicedee.guicedhazelcast;
	exports com.guicedee.guicedhazelcast.services;

	requires com.google.guice;

	//requires com.hazelcast.all;

	requires com.hazelcast.all;

	requires com.guicedee.guicedinjection;
	requires com.guicedee.logmaster;
	requires java.logging;
	requires cache.annotations.ri.guice;
	requires java.validation;

	requires com.guicedee.guicedpersistence;
	requires org.hibernate.orm.jcache;
	requires io.github.classgraph;

	requires transitive org.apache.commons.io;

	requires com.guicedee.guicedpersistence.readers.hibernateproperties;

	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;

	provides com.guicedee.guicedpersistence.services.IPropertiesEntityManagerReader with HazelcastProperties;

	provides com.guicedee.guicedinjection.interfaces.IGuicePreDestroy with com.guicedee.guicedhazelcast.implementations.HazelcastClientProvider,
															com.guicedee.guicedhazelcast.services.HazelcastPreStartup;
	provides com.guicedee.guicedinjection.interfaces.IGuiceDefaultBinder with com.guicedee.guicedhazelcast.implementations.HazelcastBinderGuice;

	provides com.guicedee.guicedinjection.interfaces.IGuicePostStartup with com.guicedee.guicedhazelcast.implementations.HazelcastPostStartup;
	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with HazelcastPreStartup;

	opens com.guicedee.guicedhazelcast to com.google.guice;
	opens com.guicedee.guicedhazelcast.annotations to com.google.guice;
	opens com.guicedee.guicedhazelcast.implementations to com.google.guice;
	opens com.guicedee.guicedhazelcast.services to com.google.guice;
}

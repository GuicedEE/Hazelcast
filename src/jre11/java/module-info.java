import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;

module com.guicedee.guicedhazelcast {
	exports com.guicedee.guicedhazelcast;
	exports com.guicedee.guicedhazelcast.services;


	requires cache.annotations.ri.guice;
	requires java.logging;
	requires org.hibernate.orm.jcache;

	requires transitive com.hazelcast.all;
	requires transitive com.guicedee.guicedpersistence;
	requires transitive org.apache.commons.io;
	requires transitive com.guicedee.guicedpersistence.readers.hibernateproperties;




	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;

	provides com.guicedee.guicedpersistence.services.IPropertiesEntityManagerReader with HazelcastProperties;

	provides com.guicedee.guicedinjection.interfaces.IGuicePreDestroy with com.guicedee.guicedhazelcast.implementations.HazelcastClientProvider,
															com.guicedee.guicedhazelcast.services.HazelcastPreStartup, HazelcastClientPreStartup;
	provides com.guicedee.guicedinjection.interfaces.IGuiceDefaultBinder with com.guicedee.guicedhazelcast.implementations.HazelcastBinderGuice;

	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with HazelcastPreStartup,HazelcastClientPreStartup;

	opens com.guicedee.guicedhazelcast to com.google.guice;
	opens com.guicedee.guicedhazelcast.annotations to com.google.guice;
	opens com.guicedee.guicedhazelcast.implementations to com.google.guice;
	opens com.guicedee.guicedhazelcast.services to com.google.guice;
}

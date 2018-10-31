import com.jwebmp.guicedhazelcast.HazelcastBinderGuice;
import com.jwebmp.guicedhazelcast.HazelcastConfigHandler;
import com.jwebmp.guicedhazelcast.HazelcastEntityManagerProperties;
import com.jwebmp.guicedhazelcast.HazelcastPostStartup;
import com.jwebmp.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.jwebmp.guicedinjection.interfaces.IFileContentsScanner;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.jwebmp.guicedinjection.interfaces.IGuicePostStartup;

module com.jwebmp.guicedhazelcast {
	exports com.jwebmp.guicedhazelcast;

	requires transitive hazelcast.all;

	requires transitive cache.annotations.ri.guice;

	requires transitive com.jwebmp.guicedpersistence;
	requires transitive org.hibernate.orm.jcache;
	requires transitive cache.api;

	requires transitive org.apache.commons.io;

	requires transitive com.jwebmp.guicedpersistence.readers.hibernateproperties;

	requires jdk.unsupported;

	uses IGuicedHazelcastClientConfig;

	provides com.jwebmp.guicedpersistence.services.PropertiesEntityManagerReader with HazelcastEntityManagerProperties;

	provides IFileContentsScanner with HazelcastConfigHandler;
	provides IGuiceDefaultBinder with HazelcastBinderGuice;
	provides IGuicePostStartup with HazelcastPostStartup;
}

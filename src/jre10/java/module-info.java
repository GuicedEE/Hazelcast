import com.jwebmp.guicedhazelcast.HazelcastBinderGuice;
import com.jwebmp.guicedhazelcast.HazelcastConfigHandler;
import com.jwebmp.guicedhazelcast.HazelcastEntityManagerProperties;
import com.jwebmp.guicedinjection.interfaces.IFileContentsScanner;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;

module com.jwebmp.guicedhazelcast {
	exports com.jwebmp.guicedhazelcast;

	requires com.jwebmp.guicedinjection;
	requires com.jwebmp.logmaster;
	requires java.logging;
	requires cache.annotations.ri.guice;
	requires java.validation;

	requires com.jwebmp.guicedpersistence;
	requires org.hibernate.orm.jcache;
	requires cache.api;
	requires io.github.classgraph;
	requires org.apache.commons.io;

	provides com.jwebmp.guicedpersistence.db.PropertiesEntityManagerReader with HazelcastEntityManagerProperties;

	provides IFileContentsScanner with HazelcastConfigHandler;
	provides IGuiceDefaultBinder with HazelcastBinderGuice;
}

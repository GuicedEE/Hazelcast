import com.jwebmp.guicedhazelcast.HazelcastBinderGuice;
import com.jwebmp.guicedhazelcast.HazelcastConfigHandler;
import com.jwebmp.guicedhazelcast.HazelcastEntityManagerProperties;
import com.jwebmp.guicedhazelcast.config.HazelcastPackageContentsScanner;
import com.jwebmp.guicedinjection.interfaces.IFileContentsScanner;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.jwebmp.guicedinjection.interfaces.IPackageContentsScanner;

module com.jwebmp.guicedhazelcast {
	exports com.jwebmp.guicedhazelcast;

	exports com.jwebmp.guicedhazelcast.config to io.github.lukehutch.fastclasspathscanner,com.jwebmp.guicedinjection;

	requires com.jwebmp.guicedinjection;
	requires com.jwebmp.logmaster;
	requires java.logging;
	requires cache.annotations.ri.guice;
	requires io.github.lukehutch.fastclasspathscanner;
	requires java.validation;
	requires commons.io;
	requires com.jwebmp.guicedpersistence;
	requires org.hibernate.orm.jcache;
	requires cache.api;

	provides com.jwebmp.guicedpersistence.db.PropertiesEntityManagerReader with HazelcastEntityManagerProperties;
	provides IPackageContentsScanner with HazelcastPackageContentsScanner;
	provides IFileContentsScanner with HazelcastConfigHandler;
	provides IGuiceDefaultBinder with HazelcastBinderGuice;
}

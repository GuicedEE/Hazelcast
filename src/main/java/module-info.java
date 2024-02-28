open module com.guicedee.guicedhazelcast {
	exports com.guicedee.guicedhazelcast;
	exports com.guicedee.guicedhazelcast.services;
	
	requires transitive cache.annotations.ri.guice;
	
	requires java.xml;
	
	requires static lombok;
	
	requires transitive com.hazelcast.all;
	requires org.apache.commons.io;
	
	requires transitive com.guicedee.client;
	requires org.apache.commons.lang3;
	
	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
	uses com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
	
	provides com.guicedee.guicedinjection.interfaces.IGuicePreDestroy with com.guicedee.guicedhazelcast.implementations.HazelcastClientProvider, com.guicedee.guicedhazelcast.services.HazelcastPreStartup, com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;
	provides com.guicedee.guicedinjection.interfaces.IGuiceModule with com.guicedee.guicedhazelcast.implementations.HazelcastBinderGuice;
	
	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedhazelcast.services.HazelcastPreStartup, com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;
	
	//opens com.guicedee.guicedhazelcast to com.google.guice;
	//opens com.guicedee.guicedhazelcast.annotations to com.google.guice;
	
	exports com.guicedee.guicedhazelcast.implementations;
	//opens com.guicedee.guicedhazelcast.implementations to com.google.guice;
	
	//opens com.guicedee.guicedhazelcast.services to com.google.guice;
}

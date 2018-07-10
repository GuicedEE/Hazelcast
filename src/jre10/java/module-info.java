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
}

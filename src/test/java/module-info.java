module guiced.hazelcast.tests {
	requires com.guicedee.guicedhazelcast;
	
	requires com.hazelcast.all;
	
	requires org.junit.jupiter.api;
	requires org.slf4j;
	requires org.slf4j.simple;
	
	
	opens com.guicedee.guicedhazelcast.tests to org.junit.platform.commons,com.google.guice;
	
}
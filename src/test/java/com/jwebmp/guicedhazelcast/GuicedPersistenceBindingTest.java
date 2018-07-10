package com.jwebmp.guicedhazelcast;

import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.logger.LogFactory;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

class GuicedPersistenceBindingTest
{
	@Test
	public void testMe()
	{
		LogFactory.configureConsoleSingleLineOutput(Level.FINE);
		GuiceContext.inject();

	}
}

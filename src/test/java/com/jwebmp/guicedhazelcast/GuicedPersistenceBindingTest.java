package com.jwebmp.guicedhazelcast;

import com.google.inject.persist.UnitOfWork;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.btm.implementation.BTMAutomatedTransactionHandler;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.logger.logging.LogColourFormatter;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.logging.Level;

public class GuicedPersistenceBindingTest
{
	@Test
	public void testMe() throws InterruptedException
	{
		LogFactory.configureConsoleSingleLineOutput(Level.FINE);
		LogColourFormatter.setRenderBlack(false);
		HazelcastConfigHandler.startLocal=true;

		GuiceContext.inject();

		UnitOfWork uw = GuiceContext.get(UnitOfWork.class, TestCustomPersistenceLoader.class);
		EntityManager em = GuiceContext.get(EntityManager.class, TestCustomPersistenceLoader.class);
		System.out.println("open : " + em.isOpen());

		BTMAutomatedTransactionHandler.setActive(true);
	}
}

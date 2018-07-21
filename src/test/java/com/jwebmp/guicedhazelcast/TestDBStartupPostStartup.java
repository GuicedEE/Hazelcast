package com.jwebmp.guicedhazelcast;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

public class TestDBStartupPostStartup
{
	@Inject
	public TestDBStartupPostStartup(@TestCustomPersistenceLoader PersistService ps)
	{
		ps.start();
	}
}

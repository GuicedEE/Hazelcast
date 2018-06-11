package com.jwebmp.guicedhazelcast;

import com.jwebmp.guiceinjection.GuiceContext;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.logger.logging.LogColourFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.logging.Level;

class HazelcastBinderTest
{

	private static HazelcastBinderTest test;

	private String returnTest = "Not this one";


	@Test
	public void testCaching()
	{
		LogFactory.configureConsoleColourOutput(Level.FINE);
		LogColourFormatter.setRenderBlack(false);

		HazelcastBinderTest c = GuiceContext.getInstance(HazelcastBinderTest.class);
		test = c;
		System.out.println(test.test("1"));
		System.out.println(test.test("2"));
		System.out.println(test.test("Cache Test"));
		test.returnTest = "Test the Cache Pull";
		String result = test.test("Cache Test");
		System.out.println(test.test("Cache Test"));

		if (test.test("Cache Test")
		        .equals(test.returnTest))
		{
			Assertions.fail("Cache is not being hit");
		}
		else
		{

		}
	}

	@CacheResult
	public String test(@CacheKey String key)
	{
		switch (key)
		{
			case "1":
				return "One";
			case "2":
				return "Two";
			default:
				return returnTest;
		}
	}
}


package com.guicedee.guicedhazelcast.tests;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.HazelcastClientPreStartup;
import com.guicedee.guicedhazelcast.services.HazelcastPreStartup;
import com.guicedee.guicedinjection.GuiceContext;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HazelcastBinderTest
{

	private HazelcastBinderTest test;

	private String returnTest = "Not this one";

	@Test
	public void testCaching()
	{
		HazelcastProperties.setStartLocal(true);
		
		System.setProperty("GROUP_NAME", "test");
		
		HazelcastBinderTest c = GuiceContext.get(HazelcastBinderTest.class);
		test = c;
		System.out.println(test.test("1"));
		System.out.println(test.test("2"));
		System.out.println(test.test("Cache Test"));

		test.returnTest = "Test the Cache Pull";

		String result = test.test("Cache Test");
		System.out.println("Output from cache : " + test.test("Cache Test"));
		System.out.println("Output from result : " + test.test("Cache Test"));

		assertEquals("Not this one", test.test("Cache Test"));

		String shouldBeCached = test.test("Cache Test");

		if (shouldBeCached
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


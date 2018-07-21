package com.jwebmp.guicedhazelcast.config;

import com.jwebmp.guicedinjection.interfaces.IPackageContentsScanner;

import java.util.HashSet;
import java.util.Set;

public class HazelcastPackageContentsScanner
		implements IPackageContentsScanner
{
	@Override
	public Set<String> searchFor()
	{
		Set<String> output = new HashSet<>();
		output.add("com.jwebmp.guicedhazelcast");
		return output;
	}
}

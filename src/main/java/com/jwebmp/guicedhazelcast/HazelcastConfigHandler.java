package com.jwebmp.guicedhazelcast;

import com.jwebmp.guicedinjection.interfaces.IFileContentsScanner;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import org.apache.commons.io.FileUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operates on a Hazelcast Client file. Creates a copy of it in the temp directory and points hazelcast to it to allow programmatic configuration
 */
@SuppressWarnings("unused")
public class HazelcastConfigHandler
		implements IFileContentsScanner
{
	private static final Logger log = Logger.getLogger("HazelcastConfigHandler");
	public static boolean diagnosticsEnabled = false;
	public static boolean startLocal = false;
	private static String hazelcastConfigFileName = "hazelcast-client.xml";
	private static byte[] hazelcastConfig;

	/**
	 * Returns any found hazel cast config file
	 *
	 * @return the byte array or null
	 */
	public static byte[] getHazelcastConfig()
	{
		return hazelcastConfig;
	}

	/**
	 * Sets the given hazelcast configuration byte []
	 *
	 * @param hazelcastConfig
	 * 		The configuration byte []
	 */
	@SuppressWarnings("WeakerAccess")
	public static void setHazelcastConfig(byte[] hazelcastConfig)
	{
		HazelcastConfigHandler.hazelcastConfig = hazelcastConfig;
	}

	@NotNull
	public static String getHazelcastConfigFileName()
	{
		return hazelcastConfigFileName;
	}

	public static void setHazelcastConfigFileName(String hazelcastConfigFileName)
	{
		HazelcastConfigHandler.hazelcastConfigFileName = hazelcastConfigFileName;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public Map<String, ResourceList.ByteArrayConsumer> onMatch()
	{
		Map<String, ResourceList.ByteArrayConsumer> map = new HashMap<>();
		ResourceList.ByteArrayConsumer processor = (Resource resource, byte[] bytearray) ->
		{
			log.log(Level.FINE, "Hazelcast client found in class path - " + resource.getPathRelativeToClasspathElement() + ". Sending to temp directory [" + getTempDir() + "]");
			setHazelcastConfig(bytearray);
			File hazelTempFile = new File(getTempDir() + hazelcastConfigFileName);
			try
			{
				FileUtils.writeByteArrayToFile(hazelTempFile, hazelcastConfig, false);
				log.config("Setting Hazelcast Client Config filename to [" + hazelTempFile.getCanonicalPath() + "]");
				System.setProperty("hazelcast.client.config", getTempDir() + hazelcastConfigFileName);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "Unable to create Hazelcast Temporary File", e);
			}
			resource.close();
		};
		map.put(hazelcastConfigFileName, processor);
		return map;
	}

	@SuppressWarnings("WeakerAccess")
	@NotNull
	public static String getTempDir()
	{
		return System.getProperty("java.io.tmpdir");
	}
}

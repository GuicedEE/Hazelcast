package com.jwebmp.guicedhazelcast;

import com.jwebmp.guiceinjection.scanners.FileContentsScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.FileMatchContentsProcessorWithContext;
import org.apache.commons.io.IOUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operates on a Hazelcast Client file. Creates a copy of it in the temp directory and points hazelcast to it to allow programmatic configuration
 */
@SuppressWarnings("unused")
public class HazelcastConfigHandler
		implements FileContentsScanner
{
	private static final Logger log = Logger.getLogger("HazelcastConfigHandler");

	private static String tempDir;
	private static String hazelcastConfigFileName = "hazelcast-client.xml";
	private static byte[] hazelcastConfig = null;

	/**
	 * Returns any found hazel cast config file
	 *
	 * @return
	 */
	public static byte[] getHazelcastConfig()
	{
		return hazelcastConfig;
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

	@Override
	public Map<String, FileMatchContentsProcessorWithContext> onMatch()
	{

		Map<String, FileMatchContentsProcessorWithContext> map = new HashMap<>();
		FileMatchContentsProcessorWithContext processor = (classpathElt, relativePath, fileContents) ->
		{
			log.log(Level.INFO, "Hazelcast client found in class path - " + classpathElt.getCanonicalPath() + ". Sending to temp directory [" + getTempDir() + "]");
			hazelcastConfig = fileContents;
			File hazelTempFile = new File(getTempDir() + hazelcastConfigFileName);
			if (hazelTempFile.exists())
			{
				hazelTempFile.delete();
			}
			hazelTempFile.createNewFile();
			try (FileOutputStream fw = new FileOutputStream(hazelTempFile))
			{
				IOUtils.write(hazelcastConfig, fw);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "Unable to write down hazelcast to temp directory [" + getTempDir() + hazelcastConfigFileName + "]", e);
			}
			log.config("Setting Hazelcast Client Config filename to [" + getTempDir() + hazelcastConfigFileName + "]");
			System.setProperty("hazelcast.client.config", getTempDir() + hazelcastConfigFileName);
		};

		map.put(hazelcastConfigFileName, processor);

		return map;
	}

	@NotNull
	public static String getTempDir() throws IOException
	{
		if (tempDir == null)
		{
			tempDir = File.createTempFile("jwebmp", "sfx")
			              .getPath();
			int lastIndex = tempDir.lastIndexOf("/") == -1 ? tempDir.lastIndexOf("\\") : tempDir.lastIndexOf("/");
			tempDir = tempDir.substring(0, lastIndex);
			tempDir += "/";
		}
		return tempDir;
	}

	public static void setTempDir(String tempDir)
	{
		HazelcastConfigHandler.tempDir = tempDir;
	}
}

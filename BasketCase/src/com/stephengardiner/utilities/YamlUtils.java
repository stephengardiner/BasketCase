package com.stephengardiner.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.yaml.snakeyaml.Yaml;

/**
 * @author Stephen Gardiner
 *
 */
public class YamlUtils {

	/**
	 * Dumb and very thin layer on Snakeyaml.
	 * 
	 * @param pathToFile
	 * @return
	 */
	public static final Iterable<Object> loadYaml(File pathToFile) {
		Yaml yaml = new Yaml();
		try {
			Iterable<Object> data = yaml.loadAll(new FileInputStream(pathToFile));
			return data;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}

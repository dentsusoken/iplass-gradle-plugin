/*
 * Copyright 2024 DENTSU SOKEN INC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iplass.mtp.dev.gradle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Properties;

/**
 * Property file utility.
 */
public class PropertyFileUtil {
	/**
	 * private constructor.
	 */
	private PropertyFileUtil() {
	}

	/**
	 * Save the property file.
	 * @param file destination file.
	 * @param propValues property key and value.
	 */
	public static void save(File file, Map<String, String> propValues) {
		Properties prop = new Properties();
		propValues.forEach((k, v) -> prop.put(k, v));
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
			prop.store(out, ZonedDateTime.now().toString());

		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to create file '" + file.getAbsolutePath() + "'.", e);

		} catch (IOException e) {
			throw new RuntimeException("Failed during file operation.", e);
		}
	}

	/**
	 * Load the property file.
	 * @param file read file.
	 * @return An instance of a property file read.
	 */
	public static Properties load(File file) {
		try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
			return load(input);

		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to read file '" + file.getAbsolutePath() + "'.", e);

		} catch (IOException e) {
			throw new RuntimeException("Failed during file operation.", e);
		}
	}

	/**
	 * Load the property file.
	 * @param input read input stream.
	 * @return An instance of a property file read.
	 */
	public static Properties load(InputStream input) {
		Properties prop = new Properties();
		try {
			prop.load(input);
			return prop;
		} catch (IOException e) {
			throw new RuntimeException("Failed during file operation.", e);
		}
	}
}

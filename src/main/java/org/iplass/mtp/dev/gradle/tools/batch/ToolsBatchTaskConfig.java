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
package org.iplass.mtp.dev.gradle.tools.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * ToolsBatch task configuration.
 *
 * <p>
 * Manage configuration information to run batches.
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class ToolsBatchTaskConfig {
	private Properties props;

	/** main class for javaexec. */
	private String mainClass;
	/** task description. */
	private String description;
	/** arguments for javaexec. */
	private List<String> args;
	/** use stdin */
	private boolean useStandardInput;

	/**
	 * constructor
	 *
	 * <p>
	 * property to get configuration information.
	 * The available property keys are as follows, setting values to items with the same key name.
	 * </p>
	 *
	 * <ul>
	 * <li>mainClass = require. main class for javaexec.</li>
	 * <li>description = require. task description.</li>
	 * <li>args.n = optional. If multiple arguments exist, set them with sequential numbers. <br> e.g. args.0=ONE args.1=TWO</li>
	 * <li>useStandardInput = optional. Set a boolean value. Default value is false.</li>
	 * </ul>
	 *
	 *
	 * @param props properties instance.
	 */
	public ToolsBatchTaskConfig(Properties props) {
		this.props = props;

		this.mainClass = require("mainClass");
		this.description = require("description");

		this.args = list("args", Collections.emptyList());
		this.useStandardInput = getValue("useStandardInput", v -> Boolean.valueOf(v), Boolean.FALSE);

	}

	/**
	 * @return main class for javaexec
	 */
	public String getMainClass() {
		return mainClass;
	}

	/**
	 * @return task description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return arguments for javaexec.
	 */
	public List<String> getArgs() {
		return args;
	}

	/**
	 * Use standard input.
	 *
	 * <p>
	 * if true, use standard input.
	 * </p>
	 *
	 * @return use standard input
	 */
	public boolean isUseStandardInput() {
		return useStandardInput;
	}

	/**
	 * Read the required value from the property.
	 *
	 * <p>
	 * If the value does not exist, an exception is thrown.
	 * </p>
	 *
	 * @param key key of property
	 * @return value of property
	 */
	protected String require(String key) {
		String value = props.getProperty(key);
		if (null != value) {
			return value;
		}

		throw new RuntimeException("The key \"" + key + "\" does not exist in the property.");
	}

	/**
	 * Get value from a property.
	 *
	 * <p>
	 * If the specified key value does not exist, the default value is returned.
	 * </p>
	 *
	 * @param <T> Return data type.
	 * @param key key of property.
	 * @param valueConverter Converts from a string to the specified data type.
	 * @param defaultValue Default value if not present in the property.
	 * @return value of property or default value.
	 */
	protected <T> T getValue(String key, Function<String, T> valueConverter, T defaultValue) {
		String value = props.getProperty(key);
		return null != value ? valueConverter.apply(value) : defaultValue;
	}

	/**
	 * Get list value from a property.
	 *
	 * <p>
	 * In the following example, you can get list ["ONE", "TWO"].
	 * </p>
	 * <pre>
	 * prop.0=ONE
	 * prop.1=TWO
	 * </pre>
	 *
	 * @param baseKey Base key for the property.
	 * @param defaultValue Default value if not present in the property.
	 * @return list value of property or default value.
	 */
	protected List<String> list(String baseKey, List<String> defaultValue) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; true; i++) {
			String k = new StringBuilder(baseKey).append(".").append(i).toString();
			String v = props.getProperty(k);
			if (null == v) {
				break;
			}

			list.add(v);
		}

		return 0 < list.size() ? list : defaultValue;
	}

	/**
	 * Get map value from a property.
	 *
	 * <p>
	 * In the following example, you can get map {"KEY1": "A", "KEY2": "", "KEY3": "CCC"}.
	 * </p>
	 * <pre>
	 * prop.key.0=KEY1
	 * prop.val.0=A
	 * prop.key.1=KEY2
	 * prop.val.1=
	 * prop.key.2=KEY3
	 * prop.val.2=CCC
	 * </pre>
	 *
	 * @param baseKey Base key for the property.
	 * @param defaultValue Default value if not present in the property.
	 * @return map value of property or default value.
	 */
	protected Map<String, String> map(String baseKey, Map<String,String> defaultValue) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; true; i++) {
			String keyKey = new StringBuilder(baseKey).append(".key.").append(i).toString();
			String valKey = new StringBuilder(baseKey).append(".val.").append(i).toString();

			String key = props.getProperty(keyKey);
			String val = props.getProperty(valKey);

			if (null == key || null == val) {
				break;
			}

			map.put(key, val);
		}

		return 0 < map.size() ? map : defaultValue;
	}
}

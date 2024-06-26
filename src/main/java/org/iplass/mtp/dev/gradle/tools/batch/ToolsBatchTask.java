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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.gradle.api.tasks.Internal;
import org.gradle.process.JavaExecSpec;
import org.iplass.mtp.dev.gradle.JavaBatchTask;
import org.iplass.mtp.dev.gradle.PropertyFileUtil;

/**
 * Tasks to perform processing in the tools batch
 *
 * <p>
 * The task configuration is defined in a property file with the same name as the task name.
 * </p>
 *
 * @param <T> task configuration type.
 * @author SEKIGUCHI Naoya
 */
public abstract class ToolsBatchTask<T extends ToolsBatchTaskConfig> extends JavaBatchTask {
	/** task configuration */
	private T taskConfig;

	/**
	 * constructor
	 */
	public ToolsBatchTask() {
		super();

		getLogger().info("load task {}.", getName());

		Properties props = loadProperties(getName());
		getLogger().info("properties value = {}", props);
		taskConfig = createToolsBatchConfig(props);

		setDescription(taskConfig.getDescription());
	}

	@Override
	protected void configure(JavaExecSpec spec) {
		spec.getMainClass().set(taskConfig.getMainClass());

		ifExists(taskConfig.getArgs(), v -> spec.args(v));
		if (taskConfig.isUseStandardInput()) {
			spec.setStandardInput(System.in);
		}
	}

	/**
	 * If the value is not null, the process is executed.
	 * @param <V> value type.
	 * @param value the value.
	 * @param process execute process.
	 */
	protected <V> void ifNotNull(V value, Consumer<V> process) {
		if (null != value) {
			process.accept(value);
		}
	}

	/**
	 * If the value exists, the process is executed.
	 * @param <V> value type.
	 * @param value the value.
	 * @param process execute process.
	 */
	protected <V> void ifExists(List<V> value, Consumer<List<V>> process) {
		if (null != value && 0 < value.size()) {
			process.accept(value);
		}
	}

	/**
	 * get a configuration instance.
	 * @return configuration instance.
	 */
	@Internal
	protected T getTaskConfig() {
		return taskConfig;
	}

	/**
	 * Create ToolsBatchTaskConfig instance.
	 *
	 * <p>
	 * The file to be read as the task properties file is the ${taskName}.properties file that exists under the package of this class.
	 * See the ToolsBatchTaskConfig class for information on configuring property files.
	 * </p>
	 *
	 * @see org.iplass.mtp.dev.gradle.tools.batch.ToolsBatchTaskConfig
	 * @param  props task properties file.
	 * @return task config instance.
	 */
	@SuppressWarnings("unchecked")
	protected T createToolsBatchConfig(Properties props) {
		return (T) new ToolsBatchTaskConfig(props);
	}

	/**
	 * load property files.
	 *
	 * <p>
	 * Reads the property file of the task name that exists in the package location of this class.
	 * </p>
	 *
	 * @param taskName gradle task name.
	 * @return properties instance.
	 */
	protected Properties loadProperties(String taskName) {
		String packageLocation = "/" + ToolsBatchTask.class.getPackage().getName().replaceAll("\\.", "/");
		String propertyResource = packageLocation + "/" + taskName + ".properties";

		getLogger().info("read properties resource. resource file: {}.", propertyResource);
		try (InputStream input = getClass().getResourceAsStream(propertyResource)) {
			return PropertyFileUtil.load(input);
		} catch (IOException e) {
			throw new RuntimeException("An exception occurred while reading the property file. resource file: " + propertyResource, e);
		}
	}
}

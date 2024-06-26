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

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.process.JavaExecSpec;
import org.iplass.mtp.dev.gradle.PropertyFileUtil;

/**
 * A task that executes the processing of the tools batch that sets the vmargs in meta.config.
 *
 * <p>
 * If any of the following keywords are present in the property value, the value is replaced.
 * </p>
 *
 * <ul>
 * <li>{tenantId} - extension tenantId</li>
 * <li>{source} - source of task properties</li>
 * </ul>
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class ToolsBatchMetaConfigTask extends ToolsBatchTask<ToolsBatchMetaConfigTaskConfig> {
	/**
	 * @return meta.soruce property value in the meta configuration file.
	 */
	@Input
	@Optional
	abstract protected ListProperty<String> getSource();

	@Override
	protected void beforeTask() {
		getLogger().info("inputs.properties = {}", getInputs().getProperties());

		Map<String, String> config = new HashMap<>(getTaskConfig().getMetaConfig());

		getLogger().info("before config = {}", config);
		// replacement "{tenantId}", "{source}" keywords.
		replaceConfigValue(config, ReplaceKeys.TENANT_ID, String.valueOf(getPluginExtension().getTenantId().get()));
		replaceConfigValue(config, ReplaceKeys.SOURCE, String.join(",", getSource().getOrElse(Collections.emptyList())));
		getLogger().info("after config = {}", config);

		PropertyFileUtil.save(getPropertyFile(), config);
	}

	@Override
	protected void configure(JavaExecSpec spec) {
		super.configure(spec);

		spec.jvmArgs("-Dmeta.config=" + getPropertyFile().getAbsolutePath());
	}

	private File getPropertyFile() {
		// /temp/dir/taskname_config.properties
		return Paths.get(getTemporaryDir().getAbsolutePath(), getName() + "_config.properties").toFile();
	}

	@Override
	protected ToolsBatchMetaConfigTaskConfig createToolsBatchConfig(Properties props) {
		return new ToolsBatchMetaConfigTaskConfig(props);
	}

	private void replaceConfigValue(Map<String, String> config, String target, String replaceValue) {
		String checkKey = "{" + target + "}";
		String replaceKey = "\\{" + target + "\\}";

		for (String key : config.keySet()) {
			String value = config.get(key);

			if (null == value || !value.contains(checkKey)) {
				continue;
			}

			config.put(key, value.replaceAll(replaceKey, replaceValue));
		}
	}

	/**
	 * replace keys
	 */
	public static final class ReplaceKeys {
		/** tenant id */
		public static final String TENANT_ID = "tenantId";
		/** Metadata Target Path. Comma-separated if there are multiple, or all if not specified. */
		public static final String SOURCE = "source";
	}

}

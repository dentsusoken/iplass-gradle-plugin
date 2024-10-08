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

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Set vmargs in meta.config tools batch processing settings
 *
 * @author SEKIGUCHI Naoya
 */
public class ToolsBatchMetaConfigTaskConfig extends ToolsBatchTaskConfig {
	/** property key, value */
	private Map<String, String> metaConfig;

	/**
	 * <p>
	 * property to get configuration information.
	 * The available property keys are as follows, setting values to items with the same key name.
	 * </p>
	 *
	 * <ul>
	 * <li>metaConfig.key.n = optional. key of the property file specified in meta.config.</li>
	 * <li>metaConfig.val.n = optional. Value of the property file specified in meta.config.</li>
	 * </ul>
	 * <p>* key and val must be set to the same number.</p>
	 *
	 *
	 * @param props properties instance.
	 */
	public ToolsBatchMetaConfigTaskConfig(Properties props) {
		super(props);

		metaConfig = map("metaConfig", Collections.emptyMap());
	}

	/**
	 * @return property key, value
	 */
	public Map<String, String> getMetaConfig() {
		return metaConfig;
	}
}

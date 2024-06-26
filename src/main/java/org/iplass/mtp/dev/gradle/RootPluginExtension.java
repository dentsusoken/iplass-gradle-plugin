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

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.provider.Property;

/**
 * root plugin extension for plugin
 *
 * @author SEKIGUCHI Naoya
 */
public interface RootPluginExtension extends ExtensionAware {
	/**
	 * get service-config xml path.
	 *
	 * <p>
	 * File path or classpath resource.
	 * </p>
	 *
	 * @return service-config xml path.
	 */
	Property<String> getServiceConfig();

	/**
	 * Get tenant id.
	 * @return tenant id
	 */
	Property<Integer> getTenantId();

	/**
	 * Get display language.
	 * @return display language
	 */
	Property<String> getLanguage();

	/**
	 * Get classpath
	 *
	 * <p>
	 * Used as classpath when executing JavaBatchTask.
	 * </p>
	 *
	 * @return classpath
	 */
	ConfigurableFileCollection getClasspath();
}

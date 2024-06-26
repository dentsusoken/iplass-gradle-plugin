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

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.iplass.mtp.dev.gradle.tools.batch.ToolsBatchPlugin;

/**
 * root for plugin
 *
 * @author SEKIGUCHI Naoya
 */
public class RootPlugin implements org.gradle.api.Plugin<Project> {
	/** plugin extension name */
	public static final String EXTENSION_NAME = "iplass";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(JavaPlugin.class);
		project.getExtensions().create(EXTENSION_NAME, RootPluginExtension.class);

		project.getPlugins().apply(ToolsBatchPlugin.class);
	}

}

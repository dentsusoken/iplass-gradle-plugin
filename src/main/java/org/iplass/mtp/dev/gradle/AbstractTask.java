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

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.JavaExec;

/**
 * Abstract base class for plugin tasks.
 * <p>
 * When a task is configured, instances of JavaPluginExtension and RootPluginExtension are initialized.
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractTask extends JavaExec {
	/** JavaPluginExtension instance */
	private JavaPluginExtension javaPluginExtension;
	/** RootPluginExtension instance */
	private RootPluginExtension rootPluginExtension;

	/**
	 * Called when the task is configured.
	 * <p>
	 * This is a method for internal configuration by the plugin.
	 * </p>
	 *
	 * @param project the project to which the task belongs.
	 */
	public void onConfigureTask(Project project) {
		this.javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
		this.rootPluginExtension = project.getExtensions().getByType(RootPluginExtension.class);

		setGroup("iPLAss develop");
	}

	/**
	 * Get {@link JavaPluginExtension} instance.
	 * @return {@link JavaPluginExtension} instance.
	 */
	@Internal
	protected JavaPluginExtension getJavaPluginExtension() {
		return javaPluginExtension;
	}

	/**
	 * Get {@link RootPluginExtension} instance.
	 * @return {@link RootPluginExtension} instance.
	 */
	@Internal
	protected RootPluginExtension getRootPluginExtension() {
		return rootPluginExtension;
	}

	/**
	 * @return {@link FileSystemOperations} instance.
	 */
	@Inject
	protected abstract FileSystemOperations getFileSystemOperations();
}

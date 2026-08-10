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
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.JavaExec;

/**
 * task abstract class.
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractTask extends JavaExec {
	/**
	 * constructor
	 */
	public AbstractTask() {
		setGroup("iPLAss develop");

		getProject().afterEvaluate(project -> {
			projectAfterEvaluate(project);
		});
	}

	/**
	 * Implement individual processing after project evaluation.
	 * @param project Project instance.
	 */
	protected abstract void projectAfterEvaluate(Project project);

	/**
	 * Get {@link RootPluginExtension} instance.
	 * @return {@link RootPluginExtension} instance.
	 */
	@Internal
	protected RootPluginExtension getPluginExtension() {
		return getProject().getExtensions().getByType(RootPluginExtension.class);
	}

	/**
	 * Get the child Extension of {@link RootPluginExtension}.
	 * @param <T> Type of child extension.
	 * @param type Class of child extension.
	 * @return Instance of child extension.
	 */
	@Internal
	protected <T> T getChildExtension(Class<T> type) {
		return getPluginExtension().getExtensions().getByType(type);
	}

	/**
	 * @return {@link FileSystemOperations} instance.
	 */
	@Inject
	protected abstract FileSystemOperations getFileSystemOperations();
}

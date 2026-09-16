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
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.SourceSet;
import org.gradle.process.JavaExecSpec;

/**
 * Superclass of the JavaExec task for executing batches.
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class JavaBatchTask extends AbstractTask {
	/**
	 * Get service-config xml path.
	 *
	 * <p>
	 * File path or classpath resource.
	 * </p>
	 *
	 * @return service-config xml path
	 */
	@Input
	@Optional
	protected abstract Property<String> getServiceConfig();

	@Override
	public void onConfigureTask(Project project) {
		super.onConfigureTask(project);

		// depends classes task. because use classpath resource service-config.
		dependsOn(project.getTasks().named(JavaPlugin.CLASSES_TASK_NAME));
	}

	@Override
	public void exec() {
		try {
			beforeTask();

			super.exec();

			if (getExecutionResult().isPresent()) {
				getExecutionResult().get().assertNormalExitValue().rethrowFailure();

			} else {
				getLogger().warn(getName() + " task execution result is not available.");
			}

		} finally {
			afterTask();
		}
	}

	/**
	 * Configure Java execution settings on task action.
	 *
	 * @param spec JavaExecSpec
	 */
	protected void configureJavaExecSpecOnTaskAction(JavaExecSpec spec) {
		spec.getJvmArguments().add("-Dbatch.language=" + getLanguage());

		String serviceConfigPath = getServiceConfigPath();
		if (null != serviceConfigPath) {
			spec.getJvmArguments().add("-Dmtp.config=" + serviceConfigPath);
		}

		if (getJvmArgs() != null && !getJvmArgs().isEmpty()) {
			spec.getJvmArguments().addAll(getJvmArgs());
		}

		spec.setClasspath(getTaskRuntimeClasspath());
	}

	/**
	 * Implement task execution pre-processing as needed.
	 */
	protected void beforeTask() {
		removeTemporaryDir();
		if (!getTemporaryDir().mkdirs()) {
			getLogger().warn("Failed to create temporary directory: " + getTemporaryDir().getAbsolutePath());
		}

		configureJavaExecSpecOnTaskAction(this);
	}

	/**
	 * Implement post-task execution processing as needed.
	 *
	 * <p>
	 * It is executed even if an exception occurs during pre-processing or processing.
	 * </p>
	 */
	protected void afterTask() {
	}


	/**
	 * Get display language.
	 *
	 * <p>
	 * Default value: "system"
	 * </p>
	 *
	 * @return display language
	 */
	@UseWithinTaskAction
	@Internal
	protected String getLanguage() {
		return getRootPluginExtension().getLanguage().getOrElse("system");
	}

	/**
	 * Get service-config xml path.
	 *
	 * <p>
	 * If set as a task property, the task property takes precedence.
	 * </p>
	 *
	 * @return service-config xml path
	 */
	@UseWithinTaskAction
	@Internal
	protected String getServiceConfigPath() {
		return getServiceConfig().getOrElse(getRootPluginExtension().getServiceConfig().getOrNull());
	}

	/**
	 * Get task runtime classpath.
	 *
	 * <p>
	 * If the classpath is set in the {@link RootPluginExtension} extension, it takes precedence.
	 * </p>
	 *
	 * @return runtime classpath
	 */
	@UseWithinTaskAction
	@Internal
	protected FileCollection getTaskRuntimeClasspath() {
		FileCollection extensionClasspath = getRootPluginExtension().getClasspath();

		if (extensionClasspath != null && !extensionClasspath.isEmpty()) {
			return extensionClasspath;
		}

		return getJavaPluginExtension().getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
	}

	/**
	 * Delete temporary directories.
	 */
	private void removeTemporaryDir() {
		if (getTemporaryDir().exists()) {
			getFileSystemOperations().delete(action -> {
				action.delete(getTemporaryDir());
			});
		}
	}
}

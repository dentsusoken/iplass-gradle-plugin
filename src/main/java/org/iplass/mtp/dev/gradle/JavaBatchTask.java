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
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.SourceSet;
import org.gradle.process.JavaExecSpec;

/**
 * Superclass of the javaexec task for executing batches.
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class JavaBatchTask extends AbstractTask {
	/**
	 * get service-config xml path.
	 *
	 * <p>
	 * File path or classpath resource.
	 * </p>
	 *
	 * @return service-config xml path
	 */
	@Input
	@Optional
	abstract protected Property<String> getServiceConfig();

	/**
	 * default constructor.
	 */
	public JavaBatchTask() {
		super();

		// depends classes task. because use classpath resource service-config.
		dependsOn(getProject().getTasks().getByPath(JavaPlugin.CLASSES_TASK_NAME));
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

	@Override
	protected void projectAfterEvaluate(Project project) {
		configureJavaExecSpecInner(this);
	}

	/**
	 * javaexec configure. internal use.
	 * @param spec JavaExecSpec
	 */
	private void configureJavaExecSpecInner(JavaExecSpec spec) {
		JavaPluginExtension javaPluginExtension = getProject().getExtensions().getByType(JavaPluginExtension.class);

		spec.getJvmArguments().add("-Dbatch.language=" + getLanguage());

		String serviceConfigPath = getServiceConfigPath();
		if (null != serviceConfigPath) {
			spec.getJvmArguments().add("-Dmtp.config=" + getServiceConfigPath());
		}

		if (getJvmArgs() != null && !getJvmArgs().isEmpty()) {
			spec.getJvmArguments().addAll(getJvmArgs());
		}

		FileCollection classpathFiles = getPluginExtension().getClasspath().isEmpty()
				// src/main/* and configurations.runtimeClasspath
				? javaPluginExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath()
						// set extension value
						: getPluginExtension().getClasspath();

		classpathFiles.forEach(f -> spec.classpath(f));

		configureJavaExecSpec(spec);
	}

	/**
	 * Implement task execution pre-processing as needed.
	 */
	protected void beforeTask() {
		removeTemporaryDir();
		if (!getTemporaryDir().mkdirs()) {
			getLogger().warn("Failed to create temporary directory: " + getTemporaryDir().getAbsolutePath());
		}
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
	 * Configure Java execution settings.
	 * @param spec JavaExecSpec
	 */
	protected abstract void configureJavaExecSpec(JavaExecSpec spec);

	/**
	 * Get display language.
	 *
	 * <p>
	 * Default value: "system"
	 * </p>
	 *
	 * @return display language
	 */
	@Internal
	protected String getLanguage() {
		return getPluginExtension().getLanguage().getOrElse("system");
	}

	/**
	 * Get Service-Config file path.
	 *
	 * <p>
	 * If set as a task property, the task property takes precedence.
	 * </p>
	 *
	 * @return Service-Config file path
	 */
	@Internal
	protected String getServiceConfigPath() {
		return getServiceConfig().getOrElse(getPluginExtension().getServiceConfig().getOrNull());
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

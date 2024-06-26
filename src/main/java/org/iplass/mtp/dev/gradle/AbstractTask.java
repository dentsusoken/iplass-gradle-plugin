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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

/**
 * task abstract class.
 *
 * <p>
 * If this class is inherited, {@link #doTask()} must be implemented.
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractTask extends DefaultTask {
	/**
	 * constructor
	 */
	public AbstractTask() {
		setGroup("iPLAss develop");
	}

	/**
	 * task entry point
	 */
	@TaskAction
	public void taskAction() {
		removeTemporaryDir();
		getTemporaryDir().mkdirs();

		doTask();
	}

	/**
	 * Implement individual processing of tasks.
	 */
	public abstract void doTask();

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
		return getProject().getExtensions().getByType(RootPluginExtension.class).getExtensions().getByType(type);
	}

	/**
	 * Delete temporary directories.
	 */
	private void removeTemporaryDir() {
		if (getTemporaryDir().exists()) {
			try {
				Files.walkFileTree(Paths.get(getTemporaryDir().toURI()), new FileVisitor<Path>() {

					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

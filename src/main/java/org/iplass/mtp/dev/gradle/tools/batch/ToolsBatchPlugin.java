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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Plugin for tools-batch.
 *
 * @author SEKIGUCHI Naoya
 */
public class ToolsBatchPlugin implements Plugin<Project> {
	/** serviceConfigView task name */
	public static final String SERVICE_CONFIG_VIEW_TASK_NAME = "serviceConfigView";
	/** entityViewDdl task name */
	public static final String ENTITY_VIEW_DDL_TASK_NAME = "entityViewDdl";
	/** metaConvertRdbToFile task name */
	public static final String META_CONVERT_RDB_TO_FILE_TASK_NAME = "metaConvertRdbToFile";
	/** metaSyncRdbToFile task name */
	public static final String META_SYNC_RDB_TO_FILE_TASK_NAME = "metaSyncRdbToFile";

	@Override
	public void apply(Project project) {
		// register tasks.
		project.getTasks().register(SERVICE_CONFIG_VIEW_TASK_NAME, ToolsBatchTask.class);
		project.getTasks().register(ENTITY_VIEW_DDL_TASK_NAME, ToolsBatchTask.class);

		project.getTasks().register(META_CONVERT_RDB_TO_FILE_TASK_NAME, ToolsBatchMetaConfigTask.class);
		project.getTasks().register(META_SYNC_RDB_TO_FILE_TASK_NAME, ToolsBatchMetaConfigTask.class);
	}
}

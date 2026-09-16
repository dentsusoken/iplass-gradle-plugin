/*
 * Copyright 2026 DENTSU SOKEN INC.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This is a marker annotation that indicates that a method is available only within the call stack of a method annotated with {@link org.gradle.api.tasks.TaskAction}.
 * <p>
 * This annotation is applied to getter methods that retrieve values from a Plugin Extension. <br>
 * Since Plugin Extension values can only be evaluated accurately during task execution, they must be used within a block annotated with {@link org.gradle.api.tasks.TaskAction}. <br>
 * This annotation has no effect on the operation.
 * </p>
 *
 * <h3>Target Extension</h3>
 * <ul>
 * <li>{@link org.iplass.mtp.dev.gradle.RootPluginExtension}</li>
 * </ul>
 *
 * @author SEKIGUCHI Naoya
 */
@Target(ElementType.METHOD)
public @interface UseWithinTaskAction {
}

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.engine.impl.bpmn.behavior;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.task.ITaskListEntity;

/**
 * Parent class for all BPMN 2.0 task types such as ServiceTask, ScriptTask,
 * UserTask, etc.
 * 
 * When used on its own, it behaves just as a pass-through activity.
 * 
 * @author Joram Barrez
 */
public class TaskActivityBehavior extends AbstractBpmnActivityBehavior {

	/**
	 * 系统自动执行环境
	 * liyb add 
	 * @param execution
	 * @throws Exception
	 */
	public void doSysTask(ActivityExecution execution, String taskTemplateId, boolean isBlocked) throws Exception {
 
		ITaskListEntity te = (ITaskListEntity) Class.forName(TASK_ENTITY_STR)
				.newInstance();
		te.createSysTask(execution, taskTemplateId, isBlocked);
		System.out.println("UserTask doUserTask");
	};
	
	/**
	 * 系统自动执行环境
	 * liyb add 
	 * @param execution
	 * @throws Exception
	 */
	public void doSysExTask(ActivityExecution execution, String taskTemplateId, String exceptionMsg) throws Exception {
 
		ITaskListEntity te = (ITaskListEntity) Class.forName(TASK_ENTITY_STR)
				.newInstance();
		te.createSysExTask(execution, taskTemplateId, exceptionMsg);
		System.out.println("UserTask doUserTask");
	};

	/**
	 * liyb add userTask 
	 * @param task
	 * @param execution
	 * @throws Exception
	 */
	public void doUserTask(TaskEntity task, ActivityExecution execution)
			throws Exception {
		System.out.println("==doUserTask===" + task.getName());
	};
}

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

import javax.script.ScriptException;

import com.ztesoft.zsmart.core.utils.StringUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.bpmn.helper.ErrorPropagation;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.scripting.ScriptingEngines;


/**
 * activity implementation of the BPMN 2.0 script task.
 * 
 * @author Joram Barrez
 * @author Christian Stettler
 * @author Falko Menge
 */
public class ScriptTaskActivityBehavior extends TaskActivityBehavior {

	protected final String script;
	protected final String language;
	protected final String resultVariable;

	public ScriptTaskActivityBehavior(String script, String language,
			String resultVariable) {
		this.script = script;
		this.language = language;
		this.resultVariable = resultVariable;
	}

	public void execute(ActivityExecution execution) throws Exception {
		ScriptingEngines scriptingEngines = Context
				.getProcessEngineConfiguration().getScriptingEngines();

		boolean noErrors = true;
		String errorMsg = "";
		Object result = null;
		try {
			// modify by liyb,暂时没有jUEL解析器，仅支持groovy和javascript

			if (script.indexOf("<![CDATA[") != -1) {
				script.replaceAll("<![CDATA[", "").replaceAll("]]>", "");
			}

			result = scriptingEngines.evaluate(script, "groovy", execution);

			if (StringUtil.isNotEmpty(resultVariable) ) {
				execution.setVariable(resultVariable, result);
			}

		} catch (ActivitiException e) {
			noErrors = false;
			errorMsg = e.toString();
			if (e.getCause() instanceof ScriptException
					&& e.getCause().getCause() instanceof ScriptException
					&& e.getCause().getCause().getCause() instanceof BpmnError) {
				ErrorPropagation.propagateError((BpmnError) e.getCause()
						.getCause().getCause(), execution);
			} else {
				//throw e;
			}
		}

		this.doSysTask(execution, null, false);

		if (noErrors) {
			leave(execution);
		} else {
			this.doSysExTask(execution, "", errorMsg);
		}
	}

}

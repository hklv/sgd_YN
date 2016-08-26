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
package org.activiti.uboss.behavior;

import java.util.Iterator;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.scripting.ScriptingEngines;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.ztesoft.uboss.bpm.utils.ULogger;

/**
 * activeMQcom.ztesoft.uboss.bpm.utils
 * 
 * @author Liyb
 * 
 */
public class ZMQSendActivitiBehavior extends TaskActivityBehavior {

	private Expression endpointUrl;
	private Expression queueName;
	private Expression payloadExpression;
	private Expression resultVariable;

	private Expression userName;
	private Expression password;
	private ZSmartLogger logger = ZSmartLogger.getLogger(ZMQSendActivitiBehavior.class);

	public void execute(ActivityExecution execution) throws Exception {

		String endpointUrlValue = this.getStringFromField(this.endpointUrl,
				execution);
		String queueNameValue = this.getStringFromField(this.queueName,
				execution);
		String userNameValue = this
				.getStringFromField(this.userName, execution);
		String passwordValue = this
				.getStringFromField(this.password, execution);

		String payloadExpressionValue = this.getStringFromField(
				this.payloadExpression, execution);

		// String payloadExpressionValue = "String a = 1";
		// queueNameValue = "queue.uboss";

		logger.debug("Queue Url = \t" + endpointUrlValue);
		logger.debug("Queue Name = \t" + queueNameValue);
		logger.debug("Queue user name = \t" + userNameValue);
		logger.debug("Queue password = \t" + passwordValue);
		logger.debug("Queue payload = \t" + payloadExpressionValue);

		ScriptingEngines scriptingEngines = Context
				.getProcessEngineConfiguration().getScriptingEngines();

		Object payload = scriptingEngines.evaluate(payloadExpressionValue,
				"groovy", execution);
		
		logger.debug("Queue payload value = \t" + payload);
		
		Object exceptionMessage = null;
		if (payload != null) {
			Connection connection = null;
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
						endpointUrlValue);
				if (userNameValue != null) {
					connectionFactory.setUserName(userNameValue);
					connectionFactory.setPassword(passwordValue);
				}

				connection = (Connection) connectionFactory.createConnection();
				connection.start();
				Session session = (Session) connection.createSession(true,
						Session.AUTO_ACKNOWLEDGE);
				Destination destination = session.createQueue(queueNameValue);
				MessageProducer producer = session.createProducer(destination);
				/* 消息持久化 */
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
				TextMessage message = session.createTextMessage(payload
						.toString());
				Set<String> vars = execution.getVariableNames();
				Iterator<String> it = vars.iterator();

				while (it.hasNext()) {
					String key = it.next();
					Object v = execution.getVariable(key);
					if (v != null) {
						message.setStringProperty(key, v.toString());
					}
				}

				producer.send(message);

				session.commit();
			} catch (Exception e) {
				exceptionMessage = e;
				e.printStackTrace();
			} finally {
				try {
					if (null != connection)
						connection.close();
				} catch (Throwable ignore) {
				}
			}
		}
		if (exceptionMessage == null) {
			this.doSysTask(execution, null, false);
			this.leave(execution);
		} else {
			this.doSysExTask(execution, null, exceptionMessage.toString());
		}

	}

	protected String getStringFromField(Expression expression,
			DelegateExecution execution) {
		if (expression != null) {
			Object value = expression.getValue(execution);
			if (value != null) {
				return value.toString();
			}
		}
		return null;
	}

	public Expression getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(Expression endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public Expression getPayloadExpression() {
		return payloadExpression;
	}

	public void setPayloadExpression(Expression payloadExpression) {
		this.payloadExpression = payloadExpression;
	}

	public Expression getResultVariable() {
		return resultVariable;
	}

	public void setResultVariable(Expression resultVariable) {
		this.resultVariable = resultVariable;
	}

	public Expression getQueueName() {
		return queueName;
	}

	public void setQueueName(Expression queueName) {
		this.queueName = queueName;
	}

	public Expression getUserName() {
		return userName;
	}

	public void setUserName(Expression userName) {
		this.userName = userName;
	}

	public Expression getPassword() {
		return password;
	}

	public void setPassword(Expression password) {
		this.password = password;
	}

}

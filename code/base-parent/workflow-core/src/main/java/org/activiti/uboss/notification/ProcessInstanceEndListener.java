package org.activiti.uboss.notification;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstanceEndListener implements ExecutionListener {

	@SuppressWarnings("unchecked")
	public void notify(DelegateExecution execution) throws Exception {
		
		Object _observerEntry_ = execution.getVariable(ObserverNames.ObserverEntryName);
		if (_observerEntry_ == null) {
			return;
		}

		Map<String, ProcessObserver> entries = (Map<String, ProcessObserver>) _observerEntry_;
		ProcessObserver processObserver = entries.get(ObserverNames.ProcessInstanceEndObserverName);
		if(processObserver == null){
			return;
		}
		
		Notification notification = new ProcessInstanceEndNotification((ProcessInstance)execution);
		processObserver.invoke(notification);
	}
}

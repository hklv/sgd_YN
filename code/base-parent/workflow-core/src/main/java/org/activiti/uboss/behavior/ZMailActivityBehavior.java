package org.activiti.uboss.behavior;

import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;

public class ZMailActivityBehavior extends MailActivityBehavior{
	
	
	public void execute(ActivityExecution execution) throws Exception{
	    
	   /* DynamicDict dict = new DynamicDict();
	    dict.set(MailUtil.to, getStringFromField(to, execution));
	    dict.set(MailUtil.from, getStringFromField(from, execution));
	    dict.set(MailUtil.cc, getStringFromField(cc, execution));
	    dict.set(MailUtil.bcc, getStringFromField(bcc, execution));
	    dict.set(MailUtil.subject, getStringFromField(subject, execution));
	    dict.set(MailUtil.text, getStringFromField(text, execution));
	    dict.set(MailUtil.html, getStringFromField(html, execution));
	    dict.set(MailUtil.charset, getStringFromField(charset, execution) == null ? "UTF-8" : getStringFromField(charset, execution));
	    
	    dict.serviceName = "MailService";
	    dict.set("method", "sendMail");
	    ServiceFlow.callService(dict, true);
	    this.doSysTask(execution, null, false);
	    leave(execution);*/
	}
}

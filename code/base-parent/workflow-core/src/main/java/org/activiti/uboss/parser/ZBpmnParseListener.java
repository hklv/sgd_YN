package org.activiti.uboss.parser;

import org.activiti.engine.impl.bpmn.parser.BpmnParseListener;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.uboss.model.ZProperty;

public interface ZBpmnParseListener extends BpmnParseListener {
	void parseZProperty(Element propertyElement, ZProperty property, ActivityImpl activity);
}

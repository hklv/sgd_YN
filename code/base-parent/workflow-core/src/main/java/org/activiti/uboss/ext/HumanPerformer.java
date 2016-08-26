package org.activiti.uboss.ext;

public class HumanPerformer implements Performer {
	private String assignee;

	private ZServiceDefinition serviceDefinition;

	public HumanPerformer(String assignee, String serviceName) {
		this.assignee = assignee;
		if (serviceName == null) {
			serviceDefinition = null;
		} else {
			serviceDefinition = new ZServiceDefinition(serviceName);
		}
	}

	public String getAssignee() {
		return assignee;
	}

	public ZServiceDefinition getServiceDefinition() {
		return serviceDefinition;
	}
}

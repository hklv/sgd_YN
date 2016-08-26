package org.activiti.uboss.ext;

public class PotentialOwner implements Performer {
	private String candidateUsers;

	private String candidateGroups;

	private ZServiceDefinition serviceDefinition;

	public PotentialOwner(String candidateUsers, String candidateGroups, String serviceName) {
		this.candidateUsers = candidateUsers;
		this.candidateGroups = candidateGroups;
		if (serviceName == null) {
			serviceDefinition = null;
		} else {
			serviceDefinition = new ZServiceDefinition(serviceName);
		}
	}

	public String getCandidateUsers() {
		return candidateUsers;
	}

	public String getCandidateGroups() {
		return candidateGroups;
	}

	public ZServiceDefinition getServiceDefinition() {
		return serviceDefinition;
	}
}

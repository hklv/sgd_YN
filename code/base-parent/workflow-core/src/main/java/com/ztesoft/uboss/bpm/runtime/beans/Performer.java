package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.uboss.ext.HumanPerformer;
import org.activiti.uboss.ext.PotentialOwner;

import com.ztesoft.uboss.bpm.runtime.client.PerformerType;

public class Performer {
	private final String assignee;

	private final String candidateUsers;

	private final String candidateGroups;

	private final PerformerType performerType;

	public Performer(PotentialOwner potentialOwner) {
		this.performerType = PerformerType.PotentialOwner;
		candidateUsers = potentialOwner.getCandidateUsers();
		candidateGroups = potentialOwner.getCandidateGroups();
		assignee = null;
	}

	public Performer(HumanPerformer humanPerformer) {
		this.performerType = PerformerType.HumanPerformer;
		assignee = humanPerformer.getAssignee();
		candidateUsers = null;
		candidateGroups = null;
	}

	public PerformerType getPerformerType() {
		return performerType;
	}

	public String getAssignee() {
		return assignee;
	}

	public String getCandidateUsers() {
		return candidateUsers;
	}

	public String getCandidateGroups() {
		return candidateGroups;
	}
}

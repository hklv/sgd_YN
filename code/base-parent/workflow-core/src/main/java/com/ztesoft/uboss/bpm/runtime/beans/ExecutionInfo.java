package com.ztesoft.uboss.bpm.runtime.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.model.ZProperty;

import com.ztesoft.uboss.bpm.runtime.client.ActivityType;

public abstract class ExecutionInfo {
	private final String id;

	private final String processInstanceId;

	private final ActivityType activityType;

	private final String activityId;

	private final List<Property> propertyList;
	// 环节编码，不可重复
	private final String activityCode;
	// 是否活跃状态
	private final boolean isActive;
	// 是否并发状态，拥有多个实例
	private final boolean isConcurrent;
	// 是否是流程或子流程
	private final boolean isScope;

	public ExecutionInfo(Execution execution, ActivityImpl activity,
			ActivityType activityType) {
		ExecutionEntity executionEntity = (ExecutionEntity) execution;
		id = executionEntity.getId();
		processInstanceId = executionEntity.getProcessInstanceId();
		this.activityType = activityType;
		this.activityId = activity.getId();
		this.activityCode = ZExtendsionUtil.getActivityCode(activity);
		this.isActive = executionEntity.isActive();
		this.isConcurrent = executionEntity.isConcurrent();
		this.isScope = executionEntity.isScope();

		List<Property> propertyList = new ArrayList<Property>();
		List<ZProperty> propertyDeclarations = ZExtendsionUtil
				.getPropertyDeclarations(activity);
		if (propertyDeclarations != null) {
			for (ZProperty z : propertyDeclarations) {
				Property p = new Property(z);
				propertyList.add(p);
			}
		}
		this.propertyList = Collections.unmodifiableList(propertyList);
	}

	public String getId() {
		return id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public String getActivityId() {
		return activityId;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public boolean isActive() {
		return isActive;
	}

	public boolean isConcurrent() {
		return isConcurrent;
	}

	public boolean isScope() {
		return isScope;
	}

	public List<Property> getPropertyList() {
		return propertyList;
	}

	public Property getProperty(String id) {
		if (propertyList != null) {
			for (Property property : propertyList) {
				if (property.getId().equals(id)) {
					return property;
				}
			}
		}
		return null;
	}

	public String toString() {
		return this.getClass().getSimpleName() + "[" + activityId + "@" + id
				+ "]";
	}
}

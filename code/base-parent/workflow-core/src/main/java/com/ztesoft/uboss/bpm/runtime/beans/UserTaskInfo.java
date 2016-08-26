package com.ztesoft.uboss.bpm.runtime.beans;

import java.util.Collections;
import java.util.List;

public class UserTaskInfo {
	// list按执行顺序排序
	private List<ActivityInfo> prev = Collections.EMPTY_LIST;
	private UserTaskExecution current;
	private List<ActivityInfo> next = Collections.EMPTY_LIST;

	public List<ActivityInfo> getPrev() {
		return prev;
	}

	public void setPrev(List<ActivityInfo> prev) {
		if (prev != null) {
			this.prev = prev;
		}
	}

	public UserTaskExecution getCurrent() {
		return current;
	}

	public void setCurrent(UserTaskExecution current) {
		this.current = current;
	}

	public List<ActivityInfo> getNext() {
		return next;
	}

	public void setNext(List<ActivityInfo> next) {
		if (next != null) {
			this.next = next;
		}
	}

	public boolean canStepForward() {
		return next.size() > 0 ? true : false;
	}

	public boolean canStepBackward() {
		if (prev.size() > 0) {
			if (current == null) {
				return false;
			}
			ActivityInfo last = prev.get(prev.size() - 1);
			// 如果和前一个任务不在一个流程/子流程中，则不允许回退
			if (!last.getExecutionId().equals(current.getId())) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}

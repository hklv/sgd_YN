package com.ztesoft.uboss.bpm.setup.model;

import java.util.List;

public class FlowCatgTreeDto {

	private String id;
	
	private String text;
	
	private List<FlowCatgTreeDto> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<FlowCatgTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<FlowCatgTreeDto> children) {
		this.children = children;
	}
	
}

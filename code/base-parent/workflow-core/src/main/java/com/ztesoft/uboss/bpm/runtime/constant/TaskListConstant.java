package com.ztesoft.uboss.bpm.runtime.constant;

public enum TaskListConstant {
	
	ACTIVE("A"),
	BACK("B"),
	DELETE("X"),
	HOLDER("H"),
	PENDING("I"),
	EXCEPTION("E"),
	JUMP("J"),
	COMPLETED("C"),
	BLOCKED("K"),
	
	DIRECTION_BACK("B"),
	DIRECTION_FRONT("F"),
	
	TYPE_SERVICE("S"),
	TYPE_USER("U"),
	
	A("正在处理"),
	B("回退"),
	X("已删除"),
	H("挂起"),
	I("待处理"),
	E("异常"),
	J("跳转"),
	C("完成"),
	K("堵塞"),
	
	D_B("反向"),
	D_F("正向"),
	
	T_S("服务任务"),
	T_U("人工任务"),
	
	H_COMPLETED("C"),
	H_C("完成");
	
	private String taskstate;
	
	private TaskListConstant(String taskstate)
	{
		this.taskstate = taskstate;
	}
	
	public String state()
	{
		return taskstate;
	}

}
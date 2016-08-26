package com.ztesoft.uboss.bpm.runtime.constant;

public class TaskHolderConstant {
	
	//==============流程动作=======================//
	
	//创建
	public static String HOLDER_ACTION_CREATE = "C";
	
	//挂起
	public static String HOLDER_ACTION_BLOCK = "B";
	
	//解挂
	public static String HOLDER_ACTION_UNBLOCK = "U";
	
	//停止/终止
	public static String HOLDER_ACTION_STOP = "S";
	
	//撤回(单)
	public static String HOLDER_ACTION_WITHDRAW = "W";
	
	//撤回(单)完成
	public static String HOLDER_ACTION_WITHDRAW_COMPLETE = "M";
	
	//完成/结束
	public static String HOLDER_ACTION_FINISH = "F";
	
	//回退
	public static String HOLDER_ACTION_BACK = "K";
	
	//回退结束
	public static String HOLDER_ACTION_BACK_COMPLETE = "N";
	
	//==============流程动作=======================//
	
	
	//==============流程状态=======================//
	
	//激活/处理中
	public static String HOLDER_STATE_ACTIVE = "A";
	
	//挂起
	public static String HOLDER_STATE_BLOCK = "B";
	
	//终止/废弃/删除
	public static String HOLDER_STATE_TERMINATE = "T";
	
	//完成
	public static String HOLDER_STATE_COMPLETE = "C";
	
	//回退
	public static String HOLDER_STATE_BACK = "K";
	
	//撤回(单) 途中
	public static String HOLDER_STATE_WITH_DRAW = "F";
	
	//==============流程状态=======================//
}

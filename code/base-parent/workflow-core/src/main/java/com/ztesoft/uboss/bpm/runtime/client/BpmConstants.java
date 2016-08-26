package com.ztesoft.uboss.bpm.runtime.client;

/**
 * 常量定义
 * @author chen.gang71
 *
 */
public final class BpmConstants {
    private BpmConstants(){
        
    }
    
    /*
     * 流程变量名 
     */
    public static final String VAR_NAME_COMMON_DATA_ID = "__common_data_id";
    public static final String VAR_NAME_BO_INSTANCE = "bo";
    public static final String VAR_NAME_DEBUG_MODE = "__z_Debug_Mode";
    
    /*
     * 布尔值
     */
    public static final String YES = "Y";
    public static final String NO = "N";
    
    /*
     * 状态编码
     */
    public static final String STATE_ENABLE = "A";
    public static final String STATE_DISABLE = "X";
    
    /*
     * 流程版本状态 
     */
    public static final String VER_STATE_ACTIVE = "A";
    public static final String VER_STATE_EDIT = "B";
    public static final String VER_STATE_INACTIVE = "C";  
    public static final String  INIT_VER= "1.0.0";
    public static final String VER_EXPIRED_DATE = "2038-01-19";
    
    /*
     * 流程节点状态
     */
    public static final String ACTIVITY_STATE_PROCESSING = "processing";
    public static final String ACTIVITY_STATE_COMPLETED = "completed";
    public static final String ACTIVITY_STATE_FAILED = "failed";
    public static final String ACTIVITY_STATE_UNEXECUTED = "unexecuted";
}

package com.ztesoft.uboss.form.sz600.formEngine2;

public class DynFormFieldInfo {

	  private String tableId = "";
	  private String tableName = "";  
	  private String ZDID = "";  //输入框id
	  private String ZDSM = "";
	  private String ZDMC = "";  //字段名
	  private String ZDLX = "";  //字段类型 int varchar..
	  private String ZDLC = "0";  //是否传给流程，默认0不传
	  private String ZDCD = "";
	  private int ZDZT = 0;
	  private int ZDLB = 0;
	  private int ZDBJ = 0;
	  private int ZDKK = 0;

	  private String ZDGLBM = "";
	  private String ZDGLID = "";
	  private String ZDGLMC = "";
	  
	  private int ZDCX = 0;
	  private int ZDDR = 0;
	  private int ZDDC = 0;
	  
	  private String ZDKZ = "";
	  private String ZDBQ = "";

	  //备用字段
	  private String ZDBY = "";
	  //初始值
	  private String ZDCSZ = "";
	  //函数
	  private String ZDHS = "";
	  
	  //		----字段增加属性：对齐方式
	  private String Align = "";
		//----字段增加属性：是否加粗
	  private String Bold = "";
		//----字段增加属性：颜色选择
	  private String Color = "";
		//----字段增加属性：列表字数
	  private String Maxlen = "";
	  
	  //
	  private String ZDQTB ="";

	  public int getZDBJ() {
	    return ZDBJ;
	  }
	  public String getZDCD() {
	    return ZDCD;
	  }
	  public String getZDGLBM() {
	    return ZDGLBM;
	  }
	  public String getZDGLID() {
	    return ZDGLID;
	  }
	  public String getZDGLMC() {
	    return ZDGLMC;
	  }
	  public String getZDID() {
	    return ZDID;
	  }
	  public int getZDKK() {
	    return ZDKK;
	  }
	  public int getZDLB() {
	    return ZDLB;
	  }
	  public String getZDLX() {
	    return ZDLX;
	  }
	  public String getZDMC() {
	    return ZDMC;
	  }
	  public String getZDSM() {
	    return ZDSM;
	  }
	  public int getZDZT() {
	    return ZDZT;
	  }
	  public void setZDBJ(int ZDBJ) {
	    this.ZDBJ = ZDBJ;
	  }
	  public void setZDCD(String ZDCD) {
	    this.ZDCD = ZDCD;
	  }
	  public void setZDGLBM(String ZDGLBM) {
	    this.ZDGLBM = ZDGLBM;
	  }
	  public void setZDGLID(String ZDGLID) {
	    this.ZDGLID = ZDGLID;
	  }
	  public void setZDGLMC(String ZDGLMC) {
	    this.ZDGLMC = ZDGLMC;
	  }
	  public void setZDID(String ZDID) {
	    this.ZDID = ZDID;
	  }
	  public void setZDKK(int ZDKK) {
	    this.ZDKK = ZDKK;
	  }
	  public void setZDLB(int ZDLB) {
	    this.ZDLB = ZDLB;
	  }
	  public void setZDLX(String ZDLX) {
	    this.ZDLX = ZDLX;
	  }
	  public void setZDMC(String ZDMC) {
	    this.ZDMC = ZDMC;
	  }
	  public void setZDSM(String ZDSM) {
	    this.ZDSM = ZDSM;
	  }
	  public void setZDZT(int ZDZT) {
	    this.ZDZT = ZDZT;
	  }
	  public String getTableId() {
	    return tableId;
	  }
	  public void setTableId(String tableId) {
	    this.tableId = tableId;
	  }
	  public String getTableName() {
	    return tableName;
	  }
	  public void setTableName(String tableName) {
	    this.tableName = tableName;
	  }  
	  public String getZDBY() {
	    return ZDBY;
	  }
	  public void setZDBY(String ZDBY) {
	    this.ZDBY = ZDBY;
	  }
	  
	  //20080902，增加查询、导入、导出
	  public int getZDCX() {
	    return ZDCX;
	  }
	  public void setZDCX(int ZDCX) {
	    this.ZDCX = ZDCX;
	  }    
	  public int getZDDR() {
	    return ZDDR;
	  }
	  public void setZDDR(int ZDDR) {
	    this.ZDDR = ZDDR;
	  }    
	  public int getZDDC() {
	    return ZDDC;
	  }
	  public void setZDDC(int ZDDC) {
	    this.ZDDC = ZDDC;
	  }  
	  
	  public String getZDKZ() {
	    return ZDKZ;
	  }
	  public void setZDKZ(String ZDKZ) {
	    this.ZDKZ = ZDKZ;
	  }  
	  
	  public String getZDBQ() {
	    return ZDBQ;
	  }
	  public void setZDBQ(String ZDBQ) {
	    this.ZDBQ = ZDBQ;
	  }   

	  public String getZDCSZ() {
	    return ZDCSZ;
	  }
	  public void setZDCSZ(String ZDCSZ) {
	    this.ZDCSZ = ZDCSZ;
	  }   
	  
	  public String getZDHS() {
	    return ZDHS;
	  }
	  public void setZDHS(String ZDHS) {
	    this.ZDHS = ZDHS;
	  }     
	  
	  public String getAlign() {
	    return Align;
	  }
	  public void setAlign(String align) {
	    this.Align = align;
	  }  
	  
	  public String getBold() {
	    return Bold;
	  }
	  public void setBold(String bold) {
	    this.Bold = bold;
	  }
	  
	  public String getColor() {
	    return Color;
	  }
	  public void setColor(String color) {
	    this.Color = color;
	  }
	  
	  public String getMaxlen() {
	    return Maxlen;
	  }
	  public void setMaxlen(String maxlen) {
	    this.Maxlen = maxlen;
	  }
	public String getZDLC() {
		return ZDLC;
	}
	public void setZDLC(String zDLC) {
		ZDLC = zDLC;
	}
	public String getZDQTB() {
		return ZDQTB;
	}
	public void setZDQTB(String zDQTB) {
		ZDQTB = zDQTB;
	}  
	  

	}

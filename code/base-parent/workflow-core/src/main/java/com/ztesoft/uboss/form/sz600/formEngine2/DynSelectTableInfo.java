package com.ztesoft.uboss.form.sz600.formEngine2;

public class DynSelectTableInfo {
	private String GLBZ = "";
	  private String GLSM	="";			//关联名称，以这个为主键
	  private String GLBM = ""; 	//关联
	  private String GLTJ	="";			//关联条件
	  private String GLLX = "0";		//单项，多项，新增加
	  private String FieldName="";//字段
	  private String FieldDesc="";//控件
	  private String FieldID	="";	//员工ID字段
	  private String FieldType="";//关联分类ID
	  private String FieldPopedom = "0"; //关联权限，1

	  private String GLGLBM="";		//分类表
	  private String GLGLID="";		//分类ID
	  private String GLGLMC="";		//名称
	  private String GLGLTJ="";		//分类条件
	  private String GLDLID="";		//分类员工ID，新增加
	  
	  private String RelateField ="";	//员工ID字段
	  private String RelateObject="";//关联分类ID  
	  private String moduleName = "";
	  public String getModuleName() {
	  	return moduleName;
	  }
	  public void setModuleName(String sModuleName) {
	  	this.moduleName = sModuleName;
	  }
	  
	  public String getFieldDesc() {
	    return FieldDesc;
	  }
	  public String getFieldID() {
	    return FieldID;
	  }
	  public String getFieldName() {
	    return FieldName;
	  }
	  public String getFieldType() {
	    return FieldType;
	  }
	  public String getGLBM() {
	    return GLBM;
	  }
	  public String getGLGLBM() {
	    return GLGLBM;
	  }
	  public String getGLGLID() {
	    return GLGLID;
	  }
	  public String getGLGLMC() {
	    return GLGLMC;
	  }
	  public String getGLGLTJ() {
	    return GLGLTJ;
	  }
	  public String getGLSM() {
	    return GLSM;
	  }
	  public String getGLTJ() {
	    return GLTJ;
	  }
	  public void setFieldDesc(String FieldDesc) {
	    this.FieldDesc = FieldDesc;
	  }
	  public void setFieldID(String FieldID) {
	    this.FieldID = FieldID;
	  }
	  public void setFieldName(String FieldName) {
	    this.FieldName = FieldName;
	  }
	  public void setFieldType(String FieldType) {
	    this.FieldType = FieldType;
	  }
	  public void setGLBM(String GLBM) {
	    this.GLBM = GLBM;
	  }
	  public void setGLGLBM(String GLGLBM) {
	    this.GLGLBM = GLGLBM;
	  }
	  public void setGLGLID(String GLGLID) {
	    this.GLGLID = GLGLID;
	  }
	  public void setGLGLMC(String GLGLMC) {
	    this.GLGLMC = GLGLMC;
	  }
	  public void setGLGLTJ(String GLGLTJ) {
	    this.GLGLTJ = GLGLTJ;
	  }
	  public void setGLSM(String GLSM) {
	    this.GLSM = GLSM;
	  }
	  public void setGLTJ(String GLTJ) {
	    this.GLTJ = GLTJ;
	  }

	  //新增加
	  public String getGLLX() {
	    return GLLX;
	  }
	  public void setGLLX(String GLLX) {
	    this.GLLX = GLLX;
	  }  

	  public String getGLDLID() {
	    return GLDLID;
	  }
	  public void setGLDLID(String GLDLID) {
	    this.GLDLID = GLDLID;
	  }  
	  
	  public String getFieldPopedom() {
	    return FieldPopedom;
	  }
	  public void setFieldPopedom(String FieldPopedom) {
	    this.FieldPopedom = FieldPopedom;
	  }   
	  //新增加备注
	  public String getGLBZ() {
	    return GLBZ;
	  }
	  public void setGLBZ(String GLBZ) {
	    this.GLBZ = GLBZ;
	  }    
	  
	  public String getRelateField() {
	    return RelateField;
	  }
	  public void setRelateField(String RelateField) {
	    this.RelateField = RelateField;
	  }  
	  
	  public String getRelateObject() {
	    return RelateObject;
	  }
	  public void setRelateObject(String RelateObject) {
	    this.RelateObject = RelateObject;
	  } 
}

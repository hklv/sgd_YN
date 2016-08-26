package com.ztesoft.uboss.form.sz600.formEngine2;

public class DynFormInfo {

	  String formId = "";
	  String formName = "";
	  String formDesc = "";
	  String formPackage = "";

	  private String modulePopedom="0";
	  private String hasAnnex=""; //这里保存的是附件的名称
	  private String editor="";   //保存编辑器字段名称 
	  private String hasDepartLimit="0";
	  private String hasUserLimit="0";
	  private String hasReadRecord="0";
	  private String hasFlow="0";
	  private String hasUpdateRecord="0";
	  private String hasFlowSms="0";
	  private String printTemp = "";
	  private String relateFormID = "";	//查看关联表单ID
	  private String triggerName = "";		//触发关联
	  private String allograph = "";
	  private String printTempRelate = "";
	  private String selectName = "";
	  
	  int tableCount = 0;
	  int rowCount = 0;
	  int cellCount = 0;
	  int objectCount = 0;
	  int desktop = 0;
	  String toolbar = "";  
	  private String weboffice = "";
	  private String templateSelect = "";
	  
	  private String lock ="0";
	  private String defaultCond ="1=1";
	  
	  private String prefix = "";
	  //辅助设置
	  private String assistant = "";	//2010-05-05
	  //表单类型
	  private String formType = "";	//空白为系统表单，simple简单表单, engine引擎
	  
	  public String getAssistant() {
	    return this.assistant;
	  }
	  public void setAssistant(String sAssistant) {
	  	assistant = sAssistant;
	  }  
	  
	  public String getDefaultCond() {
	    return this.defaultCond;
	  }
	  public void setDefaultCond(String sDefaultCond) {
	  	defaultCond = sDefaultCond;
	  }    

	  public String getPrefix() {
	    return this.prefix;
	  }
	  public void setPrefix(String prefix) {
	  	this.prefix = prefix;
	  } 
	  
	  public int getCellCount() {
	    return cellCount;
	  }
	  public String getFormDesc() {
	    return formDesc;
	  }
	  public String getFormName() {
	    return formName;
	  }
	  public int getObjectCount() {
	    return objectCount;
	  }
	  public int getRowCount() {
	    return rowCount;
	  }
	  public int getTableCount() {
	    return tableCount;
	  }
	  public void setCellCount(int cellCount) {
	    this.cellCount = cellCount;
	  }
	  public void setFormDesc(String formDesc) {
	    this.formDesc = formDesc;
	  }
	  public void setFormName(String formName) {
	    this.formName = formName;
	  }
	  public void setRowCount(int rowCount) {
	    this.rowCount = rowCount;
	  }
	  public void setObjectCount(int objectCount) {
	    this.objectCount = objectCount;
	  }
	  public void setTableCount(int tableCount) {
	    this.tableCount = tableCount;
	  }
	  public String getFormId() {
	    return formId;
	  }
	  public void setFormId(String formId) {
	    this.formId = formId;
	  }
	  public String getFormPackage() {
	    return formPackage;
	  }
	  public void setFormPackage(String formPackage) {
	    this.formPackage = formPackage;
	  }
	  public String getEditor() {
	    return editor;
	  }
	  public String getHasAnnex() {
	    return hasAnnex;
	  }
	  public String getHasDepartLimit() {
	    return hasDepartLimit;
	  }
	  public String getHasFlow() {
	    return hasFlow;
	  }
	  public String getHasFlowSms() {
	    return hasFlowSms;
	  }  
	  public String getHasReadRecord() {
	    return hasReadRecord;
	  }
	  public String getHasUpdateRecord() {
	    return hasUpdateRecord;
	  }
	  public String getHasUserLimit() {
	    return hasUserLimit;
	  }
	  public String getModulePopedom() {
	    return modulePopedom;
	  }
	  public void setEditor(String editor) {
	    this.editor = editor;
	  }
	  public void setHasAnnex(String hasAnnex) {
	    this.hasAnnex = hasAnnex;
	  }
	  public void setHasDepartLimit(String hasDepartLimit) {
	    this.hasDepartLimit = hasDepartLimit;
	  }
	  public void setHasFlow(String hasFlow) {
	    this.hasFlow = hasFlow;
	  }
	  public void setHasFlowSms(String hasFlowSms) {
	    this.hasFlowSms = hasFlowSms;
	  }  
	  public void setHasReadRecord(String hasReadRecord) {
	    this.hasReadRecord = hasReadRecord;
	  }
	  public void setHasUpdateRecord(String hasUpdateRecord) {
	    this.hasUpdateRecord = hasUpdateRecord;
	  }
	  public void setHasUserLimit(String hasUserLimit) {
	    this.hasUserLimit = hasUserLimit;
	  }
	  public void setModulePopedom(String modulePopedom) {
	    this.modulePopedom = modulePopedom;
	  }

	  public String getLock() {
	    return lock;
	  }
	  public void setLock(String lock) {
	    this.lock = lock;
	  }
	  
	  
	  public int getDesktop() {
	    return desktop;
	  }
	  public void setDesktop(int desktop) {
	    this.desktop = desktop;
	  }  

	  public String getToolbar() {
	    return toolbar;
	  }
	  public void setToolbar(String toolbar) {
	    this.toolbar = toolbar;
	  }  
	  
	  public String getWeboffice() {
	    return weboffice;
	  }
	  public void setWeboffice(String weboffice) {
	    this.weboffice = weboffice;
	  }    
	  
	  public String getTemplateSelect() {
	    return templateSelect;
	  }
	  public void setTemplateSelect(String templateSelect) {
	    this.templateSelect = templateSelect;
	  }     
	  public String getPrintTemp() {
	    return printTemp;
	  }
	  public void setPrintTemp(String printTemp) {
	    this.printTemp = printTemp;
	  }      
	  public String getRelateFormID() {
	    return relateFormID;
	  }
	  public void setRelateFormID(String relateFormID) {
	    this.relateFormID = relateFormID;
	  } 
	  public String getTriggerName() {
	    return triggerName;
	  }
	  public void setTriggerName(String triggerName) {
	    this.triggerName = triggerName;
	  }  
	  
	  public String getAllograph() {
	    return allograph;
	  }
	  public void setAllograph(String allograph) {
	    this.allograph = allograph;
	  } 
	  public String getPrintTempRelate() {
	    return printTempRelate;
	  }
	  public void setPrintTempRelate(String printTempRelate) {
	    this.printTempRelate = printTempRelate;
	  }  
	  
	  public String getSelectName() {
	    return selectName;
	  }
	  public void setSelectName(String selectName) {
	    this.selectName = selectName;
	  }  
	  
	  public String getFormType() {
	    return formType;
	  }
	  public void setFormType(String formType) {
	    this.formType = formType;
	  }  
	  
	  public String getFormTypeName() {
	  	String s = "";
	  	if (formType.equalsIgnoreCase("")) {
	  		//s = "系统表单";
	  	} else if (formType.equalsIgnoreCase("formEngine")) {
	  		s = "引擎用户表单";
	  		
	  	} else if (formType.equalsIgnoreCase("simpleform")) {
	  		s = "自定义用户表单";
	  	}	else if (formType.equalsIgnoreCase("documentengine")) {
	  		s = "自定义公文表单";
	  	}
	  	
	  	return s;
	  }

	}

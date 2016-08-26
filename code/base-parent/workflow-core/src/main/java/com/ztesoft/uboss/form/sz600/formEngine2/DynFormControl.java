package com.ztesoft.uboss.form.sz600.formEngine2;

import java.util.*;


public class DynFormControl {
	
	//private static this formControl = new this();	
		
	//把表单、表、字段的信息读到static map
	private static ArrayList moduleList = null;
	private static HashMap formMap  = null;
	private static HashMap tableMap = null;
	private static HashMap fieldMap = null;
	
	//明细表：存储同个表单下明细表的详细[]
	public Map detailMap = null;	
	public ArrayList tableNameList = null; //目的是按照顺序:先列出主表,再列出附属表,再列出明细表
	private final int I_DETAIL_COUNT = 7;	
	
	/*
  public static this getInstance() {
    return formControl;
  }
  */
	
	//单个模块的名称、
	
	public String strTableId = "";
	public String strMainTable = "";	//主表	
	public String strKeyField = "";    //关键字段	

	public String strTableAnnex = "";  //附件表名
	public String strTableRead = "";   //阅读表名	
	public String strMainField = "";   //标题字段
	public String strMainFieldDesc = "";
	public String value_str[] = null;  //值
	

	public String strSearchCond = "";  //查询的条件
	public String strEditor = ""; //编辑器内容字段

	//分解后的信息
	public String strTable = "";       //表名	
	public String strCond = "";
	
	public String strField = "";       //字段组合
	public String strForeignField = ""; //外健字段	
	public int    iFieldCount = 0;          //字段的数量
	
	public String detailTableId = "";
	public String detailTableName = "";
	public String detailField = "";
	public String detailKeyField = "";
	public String detailForeign = "";
	public int    detailFieldCount = 0;
	
	//是否有导入、导出
	public boolean hasExport = false;
	public boolean hasImport = false;
	
	//用户ID字段
	public String strLoginField = "";
	public String strNameField = "";
	public String strDepartField = "";
	public String strDepartNameField = "";
	
	public String weboffice = "";
	public String templateSelect = "";
	public String strDateField = "";
	//部门索引
	public int indexDepart = -1;
	//员工索引
	public int indexLogin = -1;
	public int indexName = -1;
	
	public int indexLogin2 = -1;	
	public int indexName2 = -1;	
	public int indexKey = -1;
	
	public String printTemp = "";
	public String printTempRelate = "";
	public String allograph = "";
	public String selectName = "";
	public String assistant = "";
	public String formType = "";
		
	
	
	//构造函数，传入表单
	public DynFormControl() {
	}
	
	public ArrayList getModuleList() {
		return moduleList;
	}
	
	public HashMap getFormMap() {
		if (formMap == null) {
			this.initialFormMap();
		}
		return formMap;
	}
	
	public HashMap getTableMap() {
		return tableMap;
	}
	
	public HashMap getFieldMap() {
		return fieldMap;
	}
	
	/*
	public Map getDetailMap() {
		return this.detailMap;
	}
	*/
	
	public void getMapStatic(long dynFormId) {
		//跟新map的内容
		if (moduleList == null || formMap == null) {
			this.initialFormMap();
		}

		DynForm bdForm = new DynForm();
		//读取表单的内容
		if (formMap.get(dynFormId+"") == null) {
			DynFormInfo bdFormInfo = null;
			bdFormInfo = (DynFormInfo)bdForm.getFormInfo(dynFormId);
			formMap.put(dynFormId+"", bdFormInfo);
		}
		
    //读取表的信息
		if (tableMap.get(dynFormId+"") == null) {
			Collection collFormTable = null;
			collFormTable = bdForm.getTableList(dynFormId+"");    
			tableMap.put(dynFormId+"", collFormTable);
		}
		
    //字段信息
		if (fieldMap.get(dynFormId+"") == null) {
			Collection collFormField = null;
			collFormField = bdForm.getFieldList(dynFormId);  
			fieldMap.put(dynFormId+"", collFormField);
		}
	}	
	
	public void updateModuleStatic(String sModuleName, int iType) {
		if (moduleList == null || formMap == null) {
			this.initialFormMap();
		}		
		//增加
		if (iType == 0) {
			moduleList.add(sModuleName);
		} else if (iType == 1) {
			//删除
			moduleList.remove(sModuleName);
			//应该删除相应的表单、字段
		}
	}
	
	public void updateMapStatic(long formId) {
		this.updateMapStatic(formId, 0,-1);
	}
	
	public void updateMapStatic(long formId, long verId) {
		this.updateMapStatic(formId, 0,verId);
	}
	
	public void updateMapStatic(long dynFormId, int iType,long verId) {
		//跟新map的内容
		
		if (moduleList == null || formMap == null) {
			this.initialFormMap();
		}
		
		String strKey = dynFormId+"";
		if (iType == 0) {
		
			//读取表单的内容
			DynForm bdForm = new DynForm();
			DynFormInfo bdFormInfo = null;
			bdFormInfo = (DynFormInfo)bdForm.getFormInfo(dynFormId,verId);
			formMap.put(strKey, bdFormInfo);
    
			//读取表的信息
			Collection collFormTable = null;
			collFormTable = bdForm.getTableList(dynFormId,verId);    
			tableMap.put(strKey, collFormTable);
    
			//字段信息
			Collection collFormField = null;
			collFormField = bdForm.getFieldList(dynFormId,verId);  
			fieldMap.put(strKey, collFormField);
		} else if (iType == 1) {
			//删除字段、删除表单
			if (formMap != null && formMap.containsKey(strKey)) {
				formMap.remove(strKey);
			} 
			if (tableMap != null && tableMap.containsKey(strKey)) {
				tableMap.remove(strKey);
			}
			if (fieldMap != null && fieldMap.containsKey(strKey)) {
				fieldMap.remove(strKey);
			}			

		}
	}
	

	
	public void initialFormMap() {
		if (moduleList == null) {
			moduleList = new ArrayList();
		}
		if (formMap == null) {
			formMap = new HashMap();
		}
		if (tableMap == null) {
			tableMap = new HashMap();		
		}
		if (fieldMap == null) {
			fieldMap = new HashMap();
		}  
	}
	
	
	public void getFormInfo(long dynFormId) {
		this.getMapStatic(dynFormId);
		//2010-09-03增加,避免formName被改变.
	
		//先初始化为空
       // 分解出需要使用的信息
		StringBuffer sbField = null;
		StringBuffer sbCond = new StringBuffer("");
		StringBuffer sbSearch = null;
		this.strKeyField = "";    //关键字段
		this.strForeignField = ""; //外健字段
		this.strTable = "";       //表名
		this.strField = "";       //字段组合
		this.strTableAnnex = "";  //附件表名
		this.strTableRead = "";   //阅读表名	
		this.strMainField = "";   //标题字段
		this.strMainFieldDesc = "";
		//this.field_str[] = null;  //字段组合
		this.value_str = null;  //值
		this.iFieldCount = 0;     //字段的数量:主要是为了查询用
		int iFieldIndex = 0;
		this.strCond = "1=1";
		this.strSearchCond = "";
		this.strMainTable = "";
		String strFirstField = "";
		
		this.strEditor = "";
		this.strLoginField = "";
		this.strNameField = "";
		this.strDepartNameField = "";
		this.strDepartField = "";
		this.strDateField = "";
		//部门索引
		indexDepart = -1;
		//员工索引
		indexLogin = -1;	
		indexName = -1;
		indexLogin2 = -1;
		indexName2 = -1;
		this.indexKey = -1;
		
		this.hasImport = false;
		this.hasExport = false;
		this.weboffice = "";
		this.templateSelect = "";
		this.printTemp = "";
		this.printTempRelate = "";
		this.allograph = "";
		this.selectName = "";
		this.assistant = "";
		this.formType = "";
		
		//从Map里获取表信息
		DynFormInfo bdFormInfo = null;

		bdFormInfo = (DynFormInfo)formMap.get(dynFormId+"");

		if (bdFormInfo != null) {
			this.strTableAnnex = bdFormInfo.getHasAnnex();
			this.strTableRead = bdFormInfo.getHasReadRecord();	
			this.strEditor = bdFormInfo.getEditor();
			this.weboffice = bdFormInfo.getWeboffice();
			this.templateSelect = bdFormInfo.getTemplateSelect();
			this.printTemp = bdFormInfo.getPrintTemp();
			this.printTempRelate = bdFormInfo.getPrintTempRelate();
			this.allograph = bdFormInfo.getAllograph();
			this.selectName = bdFormInfo.getSelectName();
			this.assistant = bdFormInfo.getAssistant();
			this.formType = bdFormInfo.getFormType();
		}		
	  //读取表的信息
		DynFormTableInfo bdFormTableInfo = null;
		Collection collFormTable = null;

		collFormTable = (Collection)tableMap.get(dynFormId+"");
    Iterator iterFormTable = null;
    
    //字段信息
    DynFormFieldInfo bdFormFieldInfo = null;
    Collection collFormField = null;
    collFormField = (Collection)fieldMap.get(dynFormId+"");
    Iterator iterFormField = null;
    
    if (collFormField != null) {
      iterFormField = collFormField.iterator();
    } 
    String tableId = "";
    String tableName = "";

    //有没有主表
    //bdFormTableInfo.getGLZB() == 1
    detailMap = new HashMap();		//
    tableNameList = new ArrayList();
    String s_detail[] = null; //
    if (collFormTable != null) {
      iterFormTable = collFormTable.iterator();
    }     
    String sKeyField = "";
    String sField = "";

    while (iterFormTable != null && iterFormTable.hasNext()) {
      bdFormTableInfo = (DynFormTableInfo)iterFormTable.next();
      if (bdFormTableInfo.getGLID().equals("") || bdFormTableInfo.getGLBM().equals("")) {
          continue;
      }    
      if (!bdFormTableInfo.getGLLX().equals("0")) {
      		continue;
      }
      if (!bdFormTableInfo.getGLZB().equals("1")) {
      	continue;
      }
      sbField = new StringBuffer("");
      this.strTableId = bdFormTableInfo.getGLID();
      this.strMainTable = bdFormTableInfo.getGLBM();

      this.strTable = this.strMainTable;
      s_detail = new String[I_DETAIL_COUNT];
      s_detail[6] = "2";
      s_detail[0] = bdFormTableInfo.getGLID();
      s_detail[1] = bdFormTableInfo.getGLBM();

      iFieldIndex = 0;

      // 记录本次表，罗列字段的时候才可以使用
      tableName = bdFormTableInfo.getGLBM();      
      if (collFormField != null) {
        iterFormField = collFormField.iterator();
      } 
      while ((iterFormField != null && iterFormField.hasNext())) {
				bdFormFieldInfo = null;
				if (iterFormField != null && iterFormField.hasNext()) {
					bdFormFieldInfo = (DynFormFieldInfo) iterFormField.next();
				}
				if (!tableName.equals(bdFormFieldInfo.getTableName())) {
					continue;
				}

				sbField.append(",");
				sbField.append(bdFormFieldInfo.getZDMC());
				if (strFirstField.equals("")) {
					strFirstField = bdFormFieldInfo.getZDMC();
				}
				//关键字
				if(bdFormFieldInfo.getZDZT() == 2 && sKeyField.equals("") ) {
					sKeyField = bdFormFieldInfo.getZDMC();
					this.indexKey = this.iFieldCount;
				}				

				//标题字段
				if(bdFormFieldInfo.getZDZT() == 3 && this.strMainField.equals("") ) {
					this.strMainField = bdFormFieldInfo.getZDMC();					
					this.strMainFieldDesc = bdFormFieldInfo.getZDSM();
				}	
				//用户字段
				if (bdFormFieldInfo.getZDBJ() == 3 && this.strLoginField.equals("")) {
					this.strLoginField = bdFormFieldInfo.getZDMC();
					//员工索引
					indexLogin = this.iFieldCount;
				}
				//用户名称字段
				if (bdFormFieldInfo.getZDBJ() == 2 && this.strNameField.equals("")) {
					this.strNameField = bdFormFieldInfo.getZDMC();
					indexName = this.iFieldCount;
				}				
				//日期字段	4创建时间	44当前时间	42日期控件  43时间控件
				//4创建时间	44当前时间	42日期控件  43时间控件
				if (this.strDateField.equals("") && (bdFormFieldInfo.getZDBJ() == 42 ||  bdFormFieldInfo.getZDBJ() == 43)) {
					this.strDateField = bdFormFieldInfo.getZDMC();
				}	
				if (bdFormFieldInfo.getZDBJ() == 4 ||  bdFormFieldInfo.getZDBJ() == 44) {
					this.strDateField = bdFormFieldInfo.getZDMC();
				}				
								
				//部门索引
				if (bdFormFieldInfo.getZDBJ() == 34 && this.strDepartField.equals("")) {
					this.strDepartField = bdFormFieldInfo.getZDMC();
					//indexDepart = this.iFieldCount;
					//2011-06-08 增加
					indexDepart = iFieldIndex;
				}
				if (bdFormFieldInfo.getZDBJ() == 33 && this.strDepartNameField.equals("")) {
					this.strDepartNameField = bdFormFieldInfo.getZDMC();
					//indexDepart = this.iFieldCount;
				}				
					
	
				if (!hasImport && bdFormFieldInfo.getZDDR() == 1) {
					hasImport = true;
				}
				if (!hasExport && bdFormFieldInfo.getZDDC() == 1) {
					hasExport = true;
				}
				iFieldIndex ++;	
				
			} // while form field
      
      if (this.allograph.equals("1") && this.indexLogin2 == -1) {
				//增加所属人信息
      	this.indexLogin2 = iFieldIndex;
				sbField.append(",SZOA_BDDLID");
				iFieldIndex ++;
				
				this.indexName2 = iFieldIndex;
				sbField.append(",SZOA_BDDLXM");
				iFieldIndex ++;
      }      
      
    	sField = sbField.toString();
    	sbField = null;
    	if (sField.length() > 0) {
    		sField = sField.substring(1);
    	}
    	if (this.strKeyField.equals("")) {
    		this.strKeyField = strFirstField;
    	}
      
      s_detail[2] = sKeyField;
      s_detail[3] = this.strForeignField;
      s_detail[4] = sField;
      s_detail[5] = Integer.toString(iFieldIndex);
      this.iFieldCount = iFieldIndex;
      strField = sField;
      strKeyField = sKeyField;
      tableNameList.add(new String(s_detail[1]));
      detailMap.put(new String(s_detail[1]), s_detail);
			
      break; //保证只有一个
    } //while form table
    
    //查询非主表

    if (collFormTable != null) {
      iterFormTable = collFormTable.iterator();
    }     
    sKeyField = "";

    while (iterFormTable != null && iterFormTable.hasNext()) {
      bdFormTableInfo = (DynFormTableInfo)iterFormTable.next();
      
      if (bdFormTableInfo.getGLID().equals("") || bdFormTableInfo.getGLBM().equals("")) {
          continue;
      }    
      if (!bdFormTableInfo.getGLLX().equals("0")) {
      		continue;
      }
      if (!bdFormTableInfo.getGLZB().equals("0")) {
      	continue;
      }  
    	sbField = new StringBuffer("");

    	s_detail = new String[I_DETAIL_COUNT];
    	s_detail[6] = "0";
      s_detail[0] = bdFormTableInfo.getGLID();
      s_detail[1] = bdFormTableInfo.getGLBM();  
      iFieldIndex = 0;

      if (this.strMainTable.equals("")) {
      	this.strMainTable = bdFormTableInfo.getGLBM();
      	this.strTableId = bdFormTableInfo.getGLID();
      	this.strTable = this.strMainTable; 
      	s_detail[6] = "2";
      	    	
      } else {
      	this.strTable = this.strMainTable + "," + bdFormTableInfo.getGLBM();
      }

      // 记录本次表，罗列字段的时候才可以使用
      tableName = bdFormTableInfo.getGLBM();    

      if (collFormField != null) {
        iterFormField = collFormField.iterator();
      }       
      while ((iterFormField != null && iterFormField.hasNext())) {
				bdFormFieldInfo = null;
				if (iterFormField != null && iterFormField.hasNext()) {
					bdFormFieldInfo = (DynFormFieldInfo) iterFormField.next();
				}				
				if (!tableName.equals(bdFormFieldInfo.getTableName())) {
					continue;
				}
		
				sbField.append(",");
				sbField.append(bdFormFieldInfo.getZDMC());
				if (strFirstField.equals("")) {
					strFirstField = bdFormFieldInfo.getZDMC();
				}
				//关键字
				if(bdFormFieldInfo.getZDZT() == 2 && sKeyField.equals("") ) {
					sKeyField = bdFormFieldInfo.getZDMC();
					if (this.indexKey == -1) {
						this.indexKey = iFieldIndex;
					}
				}				
				//外健
				if(bdFormFieldInfo.getZDZT() == 4 && this.strForeignField.equals("") ) {
					this.strForeignField = bdFormFieldInfo.getZDMC();
					//遇到外健就要组合条件
					this.strCond = this.strKeyField + " = " + this.strForeignField;
				}	
				//标题字段
				if(bdFormFieldInfo.getZDZT() == 3 && this.strMainField.equals("") ) {
					this.strMainField = bdFormFieldInfo.getZDMC();
					this.strMainFieldDesc = bdFormFieldInfo.getZDSM();
				}	
				//用户字段
				if (bdFormFieldInfo.getZDBJ() == 3 && this.strLoginField.equals("")) {
					this.strLoginField = bdFormFieldInfo.getZDMC();
					//员工索引
					indexLogin = iFieldIndex;
				}
				//用户名称字段
				if (bdFormFieldInfo.getZDBJ() == 2 && this.strNameField.equals("")) {
					this.strNameField = bdFormFieldInfo.getZDMC();
					indexName = iFieldIndex;
				}					
				//日期字段	4创建时间	44当前时间	42日期控件  43时间控件
				//4创建时间	44当前时间	42日期控件  43时间控件
				if (this.strDateField.equals("") && (bdFormFieldInfo.getZDBJ() == 42 ||  bdFormFieldInfo.getZDBJ() == 43)) {
					this.strDateField = bdFormFieldInfo.getZDMC();
				}	
				if (bdFormFieldInfo.getZDBJ() == 4 ||  bdFormFieldInfo.getZDBJ() == 44) {
					this.strDateField = bdFormFieldInfo.getZDMC();
				}				
								
				//部门索引
				if (bdFormFieldInfo.getZDBJ() == 34 && this.strDepartField.equals("")) {
					this.strDepartField = bdFormFieldInfo.getZDMC();
					//indexDepart = this.iFieldCount;
					//indexDepart = this.iFieldCount;
					//2011-06-08 增加
					indexDepart = iFieldIndex;					
				}				
	
				if (!hasImport && bdFormFieldInfo.getZDDR() == 1) {
					hasImport = true;
				}
				if (!hasExport && bdFormFieldInfo.getZDDC() == 1) {
					hasExport = true;
				}
				iFieldIndex ++;				
				
      } // while form field
      
      if (this.allograph.equals("1") && this.indexLogin2 == -1) {
				//增加所属人信息
      	this.indexLogin2 = iFieldIndex;
				sbField.append(",SZOA_BDDLID");
				iFieldIndex ++;
				
				this.indexName2 = iFieldIndex;
				sbField.append(",SZOA_BDDLXM");
				iFieldIndex ++;
      }  
      
    	sField = sbField.toString();
    	sbField = null;
    	if (sField.length() > 0) {
    		sField = sField.substring(1);
    	}
    	if (this.strKeyField.equals("")) {
    		this.strKeyField = strFirstField;
    	}

      s_detail[2] = sKeyField;
      s_detail[3] = this.strForeignField;
      s_detail[4] = sField;
      s_detail[5] = Integer.toString(iFieldIndex);
      this.iFieldCount += iFieldIndex;
      if (this.strField.equals("")) {
      	this.strField = sField;
      } else {
      	this.strField += "," + sField;
      }
      
      if (this.strKeyField.equals("")) {
      	this.strKeyField = sKeyField;
      }
      tableNameList.add(new String(s_detail[1]));
      detailMap.put(new String(s_detail[1]), s_detail);

      break; //保证只有一个
    } //while form table
          
  	/////////////////以下做明细  	
  	strFirstField = "";
		this.detailTableId = "";
		this.detailTableName = "";
		this.detailKeyField = "";
		this.detailForeign = "";
		this.detailFieldCount = 0;  	
    if (collFormTable != null) {
      iterFormTable = collFormTable.iterator();
    }        
    while (iterFormTable != null && iterFormTable.hasNext()) {
      bdFormTableInfo = (DynFormTableInfo)iterFormTable.next();
      if (bdFormTableInfo.getGLID().equals("") || bdFormTableInfo.getGLBM().equals("")) {
      	continue;
      }    
      if (bdFormTableInfo.getGLLX().equals("0")) {
      	continue;      	
      }
      s_detail = new String[this.I_DETAIL_COUNT];
      sbField = new StringBuffer("");
      
      this.detailTableId = bdFormTableInfo.getGLID();
      this.detailTableName = bdFormTableInfo.getGLBM();
      this.detailForeign = "";
      this.detailKeyField = "";
      this.detailFieldCount = 0;
      s_detail[0] = this.detailTableId;
      s_detail[1] = this.detailTableName;
            
      
      // 记录本次表，罗列字段的时候才可以使用
      tableId = bdFormTableInfo.getGLID();  
      tableName = bdFormTableInfo.getGLBM(); 
      if (collFormField != null) {
        iterFormField = collFormField.iterator();
      } 
      while ((iterFormField != null && iterFormField.hasNext())) {
				bdFormFieldInfo = null;
				if (iterFormField != null && iterFormField.hasNext()) {
					bdFormFieldInfo = (DynFormFieldInfo) iterFormField.next();
				}
				if (!tableName.equals(bdFormFieldInfo.getTableName())) {
					continue;
				}
				this.detailFieldCount ++;
				sbField.append(",");
				sbField.append(bdFormFieldInfo.getZDMC());
				if (strFirstField.equals("")) {
					strFirstField = bdFormFieldInfo.getZDMC();
				}
				//关键字
				if(bdFormFieldInfo.getZDZT() == 2 && this.detailKeyField.equals("") ) {
					this.detailKeyField = bdFormFieldInfo.getZDMC();
				}
				//外健
				if(bdFormFieldInfo.getZDZT() == 4 && this.detailForeign.equals("") ) {
					this.detailForeign = bdFormFieldInfo.getZDMC();					
				}	
			} // while form field
    	this.detailField = sbField.toString();
    	sbField = null;
    	if (this.detailField.length() > 0) {
    		this.detailField = this.detailField.substring(1);
    	}
    	if (this.detailKeyField.equals("")) {
    		this.detailKeyField = strFirstField;
    	}  
    	
    	s_detail[2] = this.detailKeyField;
    	s_detail[3] = this.detailForeign;
    	s_detail[4] = this.detailField;
    	s_detail[5] = "" + this.detailFieldCount;
    	s_detail[6] = "1";
    	tableNameList.add(new String(s_detail[1]));
    	detailMap.put(new String(s_detail[1]), s_detail);
    } //while form table  	    
	}

}

package com.ztesoft.uboss.form.sz600.tool;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SzPageHelp {

  /**
   * 输出js函数，可以将form提交到指定的url
   * @param sFormName
   * @param sUrl
   * @return
   */
  public static String jsFormSubmit(String sFormName, String sUrl) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append(sFormName);
    sb.append(".action='");
    sb.append(sUrl);
    sb.append("';");

    sb.append(sFormName);
    sb.append(".target='_self';");

    sb.append(sFormName);
    sb.append(".submit();");

    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  public static String jsFormSubmitBlank(String sFormName, String sUrl) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append(sFormName);
    sb.append(".action='");
    sb.append(sUrl);
    sb.append("';");

    sb.append(sFormName);
    sb.append(".target='_blank';");

    sb.append(sFormName);
    sb.append(".submit();");

    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }


  /**
   * 输出js函数，可以将页面跳转到指定的url
   * @param sUrl
   * @return
   */
  public static String jsGoUrl(String sUrl) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("  document.location.href='");
    sb.append(sUrl);
    sb.append("';");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  public static String jsCloseWindow() {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("  window.close();");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }
  
  
  /**
   * 输出js函数，可以将页面跳转到指定的url
   * @param sUrl
   * @return
   */
  public static String jsGoUrlBlank(String sUrl) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("  document.location.href='");
    sb.append(sUrl);
    sb.append("';");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  /**
   * 输出js函数，可以在frame里调用，让整个frameset都跳转到指定的url
   * @param sUrl
   * @return
   */
  public static String jsTopGoUrl(String sUrl) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("  top.location.href='");
    sb.append(sUrl);
    sb.append("';");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  /**  ok
   * 输出js函数，可以将sMessage输出到alert提示框中
   * @param sMessage
   * @return
   */
  public static String jsAlert(String sMessage) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("  alert('");
    sb.append(sMessage);
    sb.append("');");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  /** ok
   * 输出js函数，可以将sMessage输出到alert提示框中,并且返回上一页面。用于
   * 添加、修改保存后出错，提示错误信息后返回。
   * @param sMessage
   * @return
   */
  public static String jsAlertBack(String sMessage) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("  alert('");
    sb.append(sMessage);
    sb.append("');");
    sb.append("history.go(-1);");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  public static String jsBack(String sMessage) {
    StringBuffer sb = new StringBuffer("<script language='javascript'>");
    sb.append("history.go(-1);");
    sb.append("</script>");
    String s = sb.toString();
    sb = null;
    return s;
  }


  /**
   * 输出js函数，用于信息列表中，补充足够的空行，让格式显得整齐！
   * @param iFirst
   * @param iLast
   * @return
   */
  public static String outIndexTableTr(int iFirst, int iLast) {
    if (iFirst == iLast) {
      return "";
    }
    StringBuffer sb = new StringBuffer("");
    for (int i=iFirst; i<iLast; i++) {
      sb.append("<tr>");
      sb.append("<td colspan='20'>&nbsp;</td>");
      sb.append("</tr>\n");
    }
    String s = sb.toString();
    sb = null;
    return s;
  }

  public static String outIndexTableTr(int iFirst, int iLast, int iTdCount) {
    if (iFirst == iLast) {
      return "";
    }
    StringBuffer sb = new StringBuffer("");
    int j = 0;
    for (int i=iFirst; i<iLast; i++) {
      sb.append("<tr>");
      for (j=0; j<iTdCount; j++) {
        sb.append("<td>&nbsp;</td>");
      }
      sb.append("</tr>\n");
    }
    String s = sb.toString();
    sb = null;
    return s;
  }
  
  public static String outIndexTableTr(int iFirst, int iLast, int iTdCount, int iHeight) {
	    if (iFirst == iLast) {
	        return "";
	      }
	      StringBuffer sb = new StringBuffer("");
	      int j = 0;
	      for (int i=iFirst; i<iLast; i++) {
	        sb.append("<tr>");
	        for (j=0; j<iTdCount; j++) {
	          sb.append("<td height=");
	          sb.append(iHeight);
	          sb.append(">&nbsp;</td>");
	        }
	        sb.append("</tr>\n");
	      }
	      String s = sb.toString();
	      sb = null;
	      return s;	  
  }


  //翻页
  public static String pageInclude(int iCurrentPage, int iPageCount,
                                   int iRecordCount, String sSearch,
                                   String formPage, String sUrl) {
    StringBuffer sb = new StringBuffer("<!----翻页---->");
    sb.append("\n");
    if (iCurrentPage>1) {
      sb.append("<a href=\"javascript:FormSubmit(");
      sb.append(formPage);
      sb.append(",'");
      sb.append(sUrl);
      sb.append("?page=1');\">首页</a>");
      sb.append("\n");

      sb.append("<a href=\"javascript:FormSubmit(");
      sb.append(formPage);
      sb.append(",'");
      sb.append(sUrl);
      sb.append("?page=");
      sb.append(iCurrentPage - 1);
      sb.append("');\">上页</a>");
      sb.append("\n");
    }
    if (iCurrentPage<iPageCount) {
      sb.append("<a href=\"javascript:FormSubmit(");
      sb.append(formPage);
      sb.append(",'");
      sb.append(sUrl);
      sb.append("?page=");
      sb.append(iCurrentPage + 1);
      sb.append("');\">下页</a>");
      sb.append("\n");

      sb.append("<a href=\"javascript:FormSubmit(");
      sb.append(formPage);
      sb.append(",'");
      sb.append(sUrl);
      sb.append("?page=");
      sb.append(iPageCount);
      sb.append("');\">尾页</a>");
      sb.append("\n");
    }

    sb.append("第<input type=\"text\" name=\"page\" class=\"txtClassPage\" value=\"");
    sb.append(iCurrentPage);
    sb.append("\">页");
    sb.append("\n");

    sb.append("<input type=\"button\" name=\"btnGoPage\" class=\"btnClass\" value=\"确定\" ");
    sb.append("OnClick=\"javascript:FormSubmit(");
    sb.append(formPage);
    sb.append(",'");
    sb.append(sUrl);
    sb.append("');\">");
    sb.append("\n");

    sb.append("共&nbsp;");
    sb.append(iPageCount);
    sb.append("&nbsp;页/共&nbsp;");
    sb.append(iRecordCount);
    sb.append("&nbsp;记录&nbsp;");
    sb.append("\n");

    sb.append("<!----快速查找---->");
    sb.append("\n");
    sb.append("<input type=\"text\" name=\"txtSearch\" class=\"txtClassSearch\" value=\"");
    sb.append(sSearch);
    sb.append("\">");
    sb.append("\n");
    sb.append("<input type=\"button\" name=\"btnSearch\"  class=\"btnClass\" value=\"快速查找\" ");
    sb.append("OnClick=\"javascript:FormSubmit(");
    sb.append(formPage);
    sb.append(",'");
    sb.append(sUrl);
    sb.append("');\">&nbsp;");
    sb.append("\n");

    String s = sb.toString();
    sb = null;
    return s;
  }


  //日历控件的frame，
  /**
   * 日历控件的frame，在使用日历前包含。这一系列都是包含时、分的
   * @return
   */
  public static String calendarFrame() {
    return SzPageHelp.calendarFrame("");
  }
  public static String calendarFrame1() {
    return SzPageHelp.calendarFrame("../");
  }
  public static String calendarFrame2() {
    return SzPageHelp.calendarFrame("../../");
  }

  public static String calendarFrame(String strLevel) {
    StringBuffer sb = new StringBuffer("");
    sb.append("<iframe width='168' height='190' scrolling='no' frameborder='0'");
    sb.append(" name='gToday:normal:");
    sb.append(strLevel);
    sb.append("share/calendar/agenda.js'");
    sb.append(" id='gToday:normal:");
    sb.append(strLevel);
    sb.append("share/calendar/agenda.js'");
    sb.append(" src='");
    sb.append(strLevel);
    sb.append("share/calendar/ipopeng.htm'");
    sb.append(" style='border:2px ridge; visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;'>");
    sb.append("</iframe>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  /**
   * 日历控件的调用，strObject为显示日期的input text
   * @param strObject
   * @return
   */
  public static String calendarSelect(String strObject) {
    return SzPageHelp.calendarSelect("", strObject);
  }
  public static String calendarSelect1(String strObject) {
    return SzPageHelp.calendarSelect("../", strObject);
  }
  public static String calendarSelect2(String strObject) {
    return SzPageHelp.calendarSelect("../../", strObject);
  }

  public static String calendarSelect(String strLevel, String strObject) {
    StringBuffer sb = new StringBuffer("");
    sb.append("<a href='javascript:void(0)' HIDEFOCUS onclick='gfPop.fPopCalendar(");
    sb.append(strObject);
    sb.append(");return false;'>");
    sb.append("<img name='pop1' align='absbottom' width='34' height='22' border='0' src='");
    sb.append(strLevel);
    sb.append("share/calendar/calbtn.gif'></a>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  //日历控件的frame，
	/**
	 * 日历控件的frame，在使用日历前包含。这一系列都是不包含时、分的。
	 * @return
	 */
	public static String calendar2Frame() {
	  return SzPageHelp.calendar2Frame("");
	}

	public static String calendar2Frame1() {
    return SzPageHelp.calendar2Frame("../");
  }
  public static String calendar2Frame2() {
    return SzPageHelp.calendar2Frame("../../");
  }

  public static String calendar2Frame(String strLevel) {
    StringBuffer sb = new StringBuffer("");
    sb.append("<iframe width='168' height='190' scrolling='no' frameborder='0'");
    sb.append(" name='gToday:normal2:");
    sb.append(strLevel);
    sb.append("share/calendar2/agenda2.js'");
    sb.append(" id='gToday:normal2:");
    sb.append(strLevel);
    sb.append("share/calendar2/agenda2.js'");
    sb.append(" src='");
    sb.append(strLevel);
    sb.append("share/calendar2/ipopeng2.htm'");
    sb.append(" style='border:2px ridge; visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;'>");
    sb.append("</iframe>");
    String s = sb.toString();
    sb = null;
    return s;
  }

  /**
   * 日历控件的调用，strObject为显示日期的input text
   * @param strObject
   * @return
   */
  public static String calendar2Select(String strObject) {
    return SzPageHelp.calendar2Select("", strObject);
  }
  public static String calendar2Select1(String strObject) {
    return SzPageHelp.calendar2Select("../", strObject);
  }
  public static String calendar2Select2(String strObject) {
    return SzPageHelp.calendar2Select("../../", strObject);
  }

  public static String calendar2Select(String strLevel, String strObject) {
    StringBuffer sb = new StringBuffer("");
    sb.append("<a href='javascript:void(0)' HIDEFOCUS onclick='gfPop2.fPopCalendar(");
    sb.append(strObject);
    sb.append(");return false;'>");
    sb.append("<img name='pop1' align='absbottom' width='34' height='22' border='0' src='");
    sb.append(strLevel);
    sb.append("share/calendar2/calbtn.gif'></a>");
    String s = sb.toString();
    sb = null;
    return s;
  }


  //按钮
  public static String addButton() {
    //<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"添加\" OnClick=\"javascript:GoUrl('update.jsp?OPERATOR=add&KEY=0');\">
    String s = "<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"添加\" OnClick=\"javascript:GoUrl('update.jsp?OPERATOR=add&KEY=0');\">";
    return s;
  }
  //按钮
  public static String addButtonSubmit() {
    String s = "<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"添加\" OnClick=\"javascript:FormSubmit(formBar, 'update.jsp?OPERATOR=add&KEY=0');\">";
    return s;
  }
  //按钮
  public static String addButtonSubmit(String sValue, String sUrl) {
    StringBuffer sb = new StringBuffer("<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"");
    sb.append(sValue);
    sb.append("\" OnClick=\"javascript:FormSubmit(formBar, '");
    sb.append(sUrl);
    sb.append("');\">");
    String s = sb.toString();
    sb = null;
    return s;
  }
  //按钮
  public static String addButtonSubmit(String sValue, String sUrl, String sClass) {
    StringBuffer sb = new StringBuffer("<input type=\"button\" name=\"btnAdd\" class=\"");
    sb.append(sClass);
    sb.append("\" value=\"");
    sb.append(sValue);
    sb.append("\" OnClick=\"javascript:FormSubmit(formBar, '");
    sb.append(sUrl);
    sb.append("');\">");
    String s = sb.toString();
    sb = null;
    return s;
  }

  //按钮
  public static String addButton(boolean flag) {
    if (!flag) {
      return "";
    }
    //<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"添加\" OnClick=\"javascript:GoUrl('update.jsp?OPERATOR=add&KEY=0');\">
    String s = "<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"添加\" OnClick=\"javascript:GoUrl('update.jsp?OPERATOR=add&KEY=0');\">";
    return s;
  }
  //按钮
  public static String addButtonSubmit(boolean flag) {
    if (!flag) {
      return "";
    }

    String s = "<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"添加\" OnClick=\"javascript:FormSubmit(formBar, 'update.jsp?OPERATOR=add&KEY=0');\">";
    return s;
  }
  //按钮
  public static String addButtonSubmit(String sValue, String sUrl, boolean flag) {
    if (!flag) {
      return "";
    }

    StringBuffer sb = new StringBuffer("<input type=\"button\" name=\"btnAdd\" class=\"btnClass\" value=\"");
    sb.append(sValue);
    sb.append("\" OnClick=\"javascript:FormSubmit(formBar, '");
    sb.append(sUrl);
    sb.append("');\">");
    String s = sb.toString();
    sb = null;
    return s;
  }
  //按钮
  public static String addButtonSubmit(String sValue, String sUrl, String sClass, boolean flag) {
    if (!flag) {
      return "";
    }

    StringBuffer sb = new StringBuffer("<input type=\"button\" name=\"btnAdd\" class=\"");
    sb.append(sClass);
    sb.append("\" value=\"");
    sb.append(sValue);
    sb.append("\" OnClick=\"javascript:FormSubmit(formBar, '");
    sb.append(sUrl);
    sb.append("');\">");
    String s = sb.toString();
    sb = null;
    return s;
  }


  //删除按钮
  public static String deleteButton() {
    //<input type=\"button\" name=\"btnDel\" class=\"btnClass\" value=\"删除\" OnClick=\"javascript:FormSubmit(formList, 'control.jsp?OPERATOR=delete');\">
    String s = "<input type=\"button\" name=\"btnDel\" class=\"btnClass\" value=\"删除\" OnClick=\"javascript:FormSubmitConfirm(formList, 'control.jsp?OPERATOR=delete', '确定要删除？');\">";
    return s;
  }
  //当有上传附件的时候
  public static String deleteButtonUpload() {
    String s = "<input type=\"button\" name=\"btnDel\" class=\"btnClass\" value=\"删除\" OnClick=\"javascript:FormSubmitConfirm(formList, 'delete.jsp?OPERATOR=delete', '确定要删除？');\">";
    return s;
  }

  //删除按钮
  public static String deleteButton(boolean flag) {
    if (!flag) {
      return "";
    }
    //<input type=\"button\" name=\"btnDel\" class=\"btnClass\" value=\"删除\" OnClick=\"javascript:FormSubmit(formList, 'control.jsp?OPERATOR=delete');\">
    String s = "<input type=\"button\" name=\"btnDel\" class=\"btnClass\" value=\"删除\" OnClick=\"javascript:FormSubmitConfirm(formList, 'control.jsp?OPERATOR=delete', '确定要删除？');\">";
    return s;
  }
  //当有上传附件的时候
  public static String deleteButtonUpload(boolean flag) {
    if (!flag) {
      return "";
    }

    String s = "<input type=\"button\" name=\"btnDel\" class=\"btnClass\" value=\"删除\" OnClick=\"javascript:FormSubmitConfirm(formList, 'delete.jsp?OPERATOR=delete', '确定要删除？');\">";
    return s;
  }

  //修改、详细
  public static String viewButton(String sType, int iKey) {
    String sUrl = "detail.jsp";
    String sOperator = "detail";
    String sName = "查看";
    return SzPageHelp.operatorButton(sType, iKey, sUrl, sOperator, sName);
  }


  public static String detailButton(String sType, int iKey) {
    String sUrl = "update.jsp";
    String sOperator = "detail";
    String sName = "详细";
    return SzPageHelp.operatorButton(sType, iKey, sUrl, sOperator, sName);
  }

  public static String checkButton(String sName, String sValue, boolean canCheck) {
  	StringBuffer sb = new StringBuffer("<input type=\"checkbox\" name=\"");
  	sb.append(sName);
  	sb.append("\" id=\"");
  	sb.append(sName);
  	sb.append("\" ");
  	sb.append(" value=\"");
  	sb.append(sValue);
  	sb.append("\"");
  	if (!canCheck) {
  		sb.append(" disabled ");
  	}
  	sb.append(">");
  	String s = sb.toString();
  	sb = null;
  	return s;
  }
  
  public static String updateButton(String sType, int iKey) {
    String sUrl = "update.jsp";
    String sOperator = "update";
    String sName = "修改";
    return SzPageHelp.operatorButton(sType, iKey, sUrl, sOperator, sName);
  }

  public static String updateButton(String sType, int iKey, boolean canUpdate) {
    if (!canUpdate) {
      return "";
    }
    String sUrl = "update.jsp";
    String sOperator = "update";
    String sName = "修改";
    return SzPageHelp.operatorButton(sType, iKey, sUrl, sOperator, sName);
  }

  public static String updateButton(String sType, int iKey, boolean isSelf, boolean canUpdate, int iDLID, int iXXDL) {
    //如果是个人文档，只有自己的文档才能修改
    if (isSelf && iDLID != iXXDL) {
      return "";
    }
    //如果是管理文档，有修改权限才可以修改
    if (!isSelf && !canUpdate) {
      return "";
    }
    String sUrl = "update.jsp";
    String sOperator = "update";
    String sName = "修改";
    return SzPageHelp.operatorButton(sType, iKey, sUrl, sOperator, sName);
  }


  public static String operatorButton(String sType, int iKey, String sUrl, String sOperator, String sName) {
    StringBuffer sb = new StringBuffer("<a href=\"javascript:FormSubmit(formPage,'");
    sb.append(sUrl);
    sb.append("?OPERATOR=");
    sb.append(sOperator);
    sb.append("&type=");
    sb.append(sType);
    sb.append("&KEY=");
    sb.append(iKey);
    sb.append("');\">");
    sb.append(sName);
    sb.append("</a>");
    String s = sb.toString();
    sb = null;
    return s;
  }
  
  public static String operatorFunc(String sName, String sFunc) {
    StringBuffer sb = new StringBuffer("<a href=\"#\" OnClick=\"javascript:");
    sb.append(sFunc);
    sb.append(";\">");
    sb.append(sName);
    sb.append("</a>");
    String s = sb.toString();
    sb = null;
    return s;
  }  

  //2011-12-05增加,可设置名称,方便控制
  public static String button(String sClass, String sValue, String sJsFunction, String sName) {
	  if (sJsFunction.startsWith("javascript:")) {
	  	sJsFunction = sJsFunction.substring(11);
	  }
    String s = "<input type=\"button\" class=\"" + sClass + "\"  name=\"" + sName + "\"  id=\"" + sName + "\" " 
             + "value=\"" + sValue + "\" OnClick=\"javascript:" + sJsFunction + ";\">";
    return s;  	
  }  
  
  public static String button(String sClass, String sValue, String sJsFunction) {
	  if (sJsFunction.startsWith("javascript:")) {
	  	sJsFunction = sJsFunction.substring(11);
	  }
    String s = "<input type=\"button\" class=\"" + sClass + "\" "
             + "value=\"" + sValue + "\" OnClick=\"javascript:" + sJsFunction + ";\">";
    return s;  	
  }
  
  public static String button(String sValue, String sJsFunction) {
	  if (sJsFunction.startsWith("javascript:")) {
	  	sJsFunction = sJsFunction.substring(11);
	  }  	
    String s = "<input type=\"button\" class=\"btnClass\" "
             + "value=\"" + sValue + "\" OnClick=\"javascript:" + sJsFunction + ";\">";
    return s;  	
  }  
  
  //保存按钮
  public static String saveButton() {
    //<input type=\"button\" name=\"btnSave\" class=\"btnClass\" value=\"保存\" OnClick=\"javascript:FormSave(formUpdate, 'control.jsp');\">
    String s = "<input type=\"button\" name=\"btnSave\" class=\"btnClass\" value=\"保存\" OnClick=\"javascript:FormSave(formUpdate, 'control.jsp');\">";
    return s;
  }
  //关闭按钮
  public static String closeButton() {
    String s = "<input type=\"button\" name=\"btnClose\" class=\"btnClass\" value=\"关闭\" OnClick=\"javascript:window.close();\">";
    return s;
  }
  //返回按钮
  public static String backButton() {
    String s = "<input type=\"button\" name=\"btnBack\" class=\"btnClass\" value=\"返回\" OnClick=\"javascript:history.go(-1);\">";
    return s;
  }
  //返回按钮
  public static String backButtonSubmit() {
    String s = "<input type=\"button\" name=\"btnBack\" class=\"btnClass\" value=\"返回\" OnClick=\"javascript:FormSave(formUpdate, 'index.jsp');\">";
    return s;
  }
  //返回按钮
  public static String backButtonSubmit(String sUrl) {
    String s = "<input type=\"button\" name=\"btnBack\" class=\"btnClass\" value=\"返回\" OnClick=\"javascript:FormSave(formUpdate, '" + sUrl + "');\">";
    return s;
  }
  //返回按钮
  public static String backButtonHref() {
    String s = "<input type=\"button\" name=\"btnBack\" class=\"btnClass\" value=\"返回\" OnClick=\"javascript:document.location.href='index.jsp';\">";
    return s;
  }
  //返回按钮
  public static String backButtonHref(String sUrl) {
    String s = "<input type=\"button\" name=\"btnBack\" class=\"btnClass\" value=\"返回\" OnClick=\"javascript:document.location.href='" + sUrl + "';\">";
    return s;
  }


  //select
  public static String selectArraySubmit(String s_name[], String s_id[],
      String sName, String sValue, String sClass) {
  	return SzPageHelp.selectArraySubmit(s_name, s_id, sName, sValue, sClass, "");
  }
  
  public static String selectArraySubmit(String s_name[], String s_id[],
                                     String sName, String sValue, String sClass, String strExcept) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("<select name=\"");
    sb.append(sName);
    sb.append("\" class=\"");
    sb.append(sClass);
    sb.append("\" onChange=\"this.form.submit();\">");
    sb.append("\n");
    sb.append("<option value=\"\">===请选择===</option>");
    int iLen = s_id.length;
    for (int i=0; i<iLen; i++) {
    	if (("," + strExcept + ",").indexOf("," + s_id[i] + ",") >=0) {
    		continue;
    	}
      sb.append("<option value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.equals(s_id[i])) {
        sb.append(" selected");
      }
      sb.append(">");
      sb.append(s_name[i]);
      sb.append("</option>");
      sb.append("\n");
    }
    sb.append("</select>");
    s = sb.toString();
    sb = null;
    return s;
  }


  //select
  public static String idSelectArray(String s_name[], String s_id[],
                                     String sName, String sValue, String sClass) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("<select name=\"");
    sb.append(sName);
    sb.append("\" class=\"");
    sb.append(sClass);
    sb.append("\">");
    sb.append("\n");
    int iLen = s_id.length;
    for (int i=0; i<iLen; i++) {
      sb.append("<option value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.equals(s_id[i])) {
        sb.append(" selected");
      }
      sb.append(">");
      sb.append(s_name[i]);
      sb.append("</option>");
      sb.append("\n");
    }
    sb.append("</select>");
    s = sb.toString();
    sb = null;
    return s;
  }

  //select  ok
  public static String idSelectArrayFunc(String s_name[], String s_id[],
                                     String sName, String sValue, String sClass, String sFunc) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("<select name=\"");
    sb.append(sName);
    sb.append("\" class=\"");
    sb.append(sClass);
    sb.append("\" onChange=\"");
    sb.append(sFunc);
    sb.append("\">");
    sb.append("\n");
    sb.append("<option value=\"\">==请选择==</option>");
    int iLen = s_id.length;
    for (int i=0; i<iLen; i++) {
      sb.append("<option value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.equals(s_id[i])) {
        sb.append(" selected");
      }
      sb.append(">");
      sb.append(s_name[i]);
      sb.append("</option>");
      sb.append("\n");
    }
    sb.append("</select>");
    s = sb.toString();
    sb = null;
    return s;
  }
  
  //select
  public static String idSelectArraySubmit(String s_name[], String s_id[],
                                     String sName, String sValue, String sClass) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("<select name=\"");
    sb.append(sName);
    sb.append("\" class=\"");
    sb.append(sClass);
    sb.append("\" onChange=\"this.form.submit();\">");
    sb.append("\">");
    sb.append("\n");
    int iLen = s_id.length;
    for (int i=0; i<iLen; i++) {
      sb.append("<option value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.equals(s_id[i])) {
        sb.append(" selected");
      }
      sb.append(">");
      sb.append(s_name[i]);
      sb.append("</option>");
      sb.append("\n");
    }
    sb.append("</select>");
    s = sb.toString();
    sb = null;
    return s;
  }  
  
  /**
   * 根据iCount的数量打印空格的个数
   * @param iCount
   * @return
   */
  public static String getLevelSpace(int iCount) {
    String s = "";

    StringBuffer sb = new StringBuffer("");
    for (int i=0; i<iCount; i++) {
      sb.append("&nbsp;&nbsp;");
    }
    s = sb.toString();
    sb = null;
    return s;
  }


  /**
   * 附件图标
   * @param strSkin
   * @param hasAnnex
   * @return
   */
  public static String annexImage(String strSkin, boolean hasAnnex) {
    if (!hasAnnex) {
      return "&nbsp;";
    }
    String s = "";
    StringBuffer sb = new StringBuffer("<img src=\"");
    sb.append(strSkin);
    sb.append("/annex.gif\" border=\"0\">");

    
    s = sb.toString();
    sb = null;
    return s;
  }

  public static String newImage(String strSkin, boolean hasRead) {
    if (hasRead) {
      return "";
    }
    String s = "<font color='red'>[新]</font>";
    return s;
  }

  public static String titleHelp(String strSkin, int iType, String sValue) {
    if (iType == 0) {
      return "";
    }
    String s = "<font color='red'>[" + sValue + "]</font>";
    return s;
  }

  public static String selectCond(String condName, String condValue) {
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("              <select name=\"");
    sb.append(condName);
    sb.append("\">");

    sb.append("                <option value=\"like\"");
    if (condValue.equals("like")) {
      sb.append(" selected ");
    }
    sb.append(">包含</option>");

    sb.append("                <option value=\"=\"");
    if (condValue.equals("=")) {
      sb.append(" selected ");
    }
    sb.append(">等于</option>");

    sb.append("                <option value=\">\"");
    if (condValue.equals(">")) {
      sb.append(" selected ");
    }
    sb.append(">大于</option>");

    sb.append("                <option value=\"<\"");
    if (condValue.equals("<")) {
      sb.append(" selected ");
    }
    sb.append(">小于</option>");

    sb.append("                <option value=\">=\"");
    if (condValue.equals(">=")) {
      sb.append(" selected ");
    }
    sb.append(">大于等于</option>");

    sb.append("                <option value=\"<=\"");
    if (condValue.equals("<=")) {
      sb.append(" selected ");
    }
    sb.append(">小于等于</option>");
    sb.append("              </select>");
    s = sb.toString();
    sb = null;
    return s;
  }

  //综合查询条件
  public static String searchCond(String desc,
                                  String condName, String condValue,
                                  String valueName, String valueValue) {
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append(desc);
    sb.append("&nbsp;");
    sb.append(SzPageHelp.selectCond(condName, condValue));
    sb.append("              <input type=\"text\" name=\"");
    sb.append(valueName);
    sb.append("\" value=\"");
    sb.append(valueValue);
    sb.append("\">&nbsp");
    s = sb.toString();
    sb = null;
    return s;
  }

  //组合查询条件
  public static String compSql(String cond1, String value1, String searchType, String field) {
    String s = "";
    StringBuffer sb = new StringBuffer("");

    if (!cond1.equals("") && !value1.equals("")) {
        if (cond1.equals("like")) {
            sb.append(searchType);
            sb.append(" ");
            sb.append(field);
            sb.append(" ");
            sb.append(cond1);
            sb.append(" '%");
            sb.append(value1);
            sb.append("%'");
        } else {
            sb.append(searchType);
            sb.append(" ");
            sb.append(field);
            sb.append(" ");
            sb.append(cond1);
            sb.append(" '");
            sb.append(value1);
            sb.append("'");
        }
    }

    s = sb.toString();
    sb = null;
    return s;

  }
  
  public static String selectCondJava(String condName, String condValue) {
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("              <select name=\"");
    sb.append(condName);
    sb.append("\">");
    
    sb.append("                <option value=\"0\"");
    if (condValue.equals("")) {
      sb.append("  ");
    }
    sb.append(">==请选择==</option>");    

    sb.append("                <option value=\"7\"");
    if (condValue.equals("7")) {
      sb.append(" selected ");
    }
    sb.append(">包含</option>");

    sb.append("                <option value=\"1\"");
    if (condValue.equals("1")) {
      sb.append(" selected ");
    }
    sb.append(">等于</option>");

    sb.append("                <option value=\"2\"");
    if (condValue.equals("2")) {
      sb.append(" selected ");
    }
    sb.append(">大于</option>");

    sb.append("                <option value=\"3\"");
    if (condValue.equals("3")) {
      sb.append(" selected ");
    }
    sb.append(">小于</option>");

    sb.append("                <option value=\"4\"");
    if (condValue.equals("4")) {
      sb.append(" selected ");
    }
    sb.append(">大于等于</option>");

    sb.append("                <option value=\"5\"");
    if (condValue.equals("5")) {
      sb.append(" selected ");
    }
    sb.append(">小于等于</option>");
    
    sb.append("                <option value=\"6\"");
    if (condValue.equals("6")) {
      sb.append(" selected ");
    }
    sb.append(">不等于</option>");    
    
    sb.append("              </select>");
    s = sb.toString();
    sb = null;
    return s;
  }
  

  //disable check
  public static String checkboxDisabled(int iValue) {
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("<input type=\"checkbox\" disabled");
    if (iValue >0) {
      sb.append(" checked ");
    }
    sb.append(">");

    s = sb.toString();
    sb = null;
    return s;
  }
  
  public static String checkbox(String sName, int iValue) {
    String s = "";
    StringBuffer sb = new StringBuffer("");
    sb.append("<input type=\"checkbox\" ");
    sb.append(" name=\"");
    sb.append(sName);
    sb.append("\"");
    sb.append(" value=1");
    if (iValue >0) {
      sb.append(" checked ");
    }
    sb.append(">");

    s = sb.toString();
    sb = null;
    return s;
  } 
  
  public static String checkbox(String sName, String sValue) {
	    String s = "";
	    StringBuffer sb = new StringBuffer("");
	    sb.append("<input type=\"checkbox\" ");
	    sb.append(" name=\"");
	    sb.append(sName);
	    sb.append("\"");
	    sb.append(" value=1");
	    if (sValue.equals("1")) {
	      sb.append(" checked ");
	    }
	    sb.append(">");

	    s = sb.toString();
	    sb = null;
	    return s;
	  }   
  
  //多选
  public static String checkbox(String sName, String s_id[], String s_name[], 
  		String sValue) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    int iLen = s_id.length;
    //<input type=radio name=relateType value=0 checked>
    sValue = sValue.replaceAll(",", ";");
    sValue = ";" + sValue + ";";
    for (int i=0; i<iLen; i++) {
      sb.append("<input type=checkbox name=");
      sb.append(sName);
      sb.append(" value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.indexOf(";" + s_id[i] + ";") >=0) {
        sb.append(" checked");
      }
      sb.append(">");
      sb.append(s_name[i]);
      sb.append("\n");
    }
    s = sb.toString();
    sb = null;
    return s;  	
  }    
  
  public static String radioFunc(String sName, String s_id[], String s_name[], 
  		String sValue, String sFunc) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    int iLen = s_id.length;
    //<input type=radio name=relateType value=0 checked>
    for (int i=0; i<iLen; i++) {
      sb.append("<input type=radio name=");
      sb.append(sName);
      sb.append(" value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.equals(s_id[i])) {
        sb.append(" checked");
      }
      sb.append(" OnClick='javascript:");
      sb.append(sFunc);
      sb.append(";'>");
      sb.append(s_name[i]);
      sb.append("\n");
    }
    s = sb.toString();
    sb = null;
    return s;    	
  }
  
  // ok
  public static String radio(String sName, String s_id[], String s_name[], 
  		String sValue) {
    if (s_id == null || s_name == null || s_id.length != s_name.length) {
      return null;
    }
    String s = "";
    StringBuffer sb = new StringBuffer("");
    int iLen = s_id.length;
    //<input type=radio name=relateType value=0 checked>
    for (int i=0; i<iLen; i++) {
      sb.append("<input type=radio name=");
      sb.append(sName);
      sb.append(" value=\"");
      sb.append(s_id[i]);
      sb.append("\"");
      if (sValue.equals(s_id[i])) {
        sb.append(" checked");
      }
      sb.append(">");
      sb.append(s_name[i]);
      sb.append("\n");
    }
    s = sb.toString();
    sb = null;
    return s;  	
  }
  
  public static String radioSubmit(String sName, String s_id[], String s_name[],
			String sValue) {
		if (s_id == null || s_name == null || s_id.length != s_name.length) {
			return null;
		}
		String s = "";
		StringBuffer sb = new StringBuffer("");
		int iLen = s_id.length;
		// <input type=radio name=relateType value=0 checked>
		for (int i = 0; i < iLen; i++) {
			sb.append("<input type=radio name=");
			sb.append(sName);
			sb.append(" value=\"");
			sb.append(s_id[i]);
			sb.append("\"");
			if (sValue.equals(s_id[i])) {
				sb.append(" checked");
			}
			sb.append(" OnClick='javascript:this.form.submit();'>");
			sb.append(s_name[i]);
			sb.append("\n");
		}
		s = sb.toString();
		sb = null;
		return s;
  }    
  
  //ok
  public static String selectOption(String sOption, String sTag) {
    String s = "";
    StringBuffer sb = new StringBuffer("");  	
    String s_option[] = sOption.split(sTag);
    int iLen = 0;
    String sTemp = "";
    if (s_option != null) {
    	iLen = s_option.length;
    	for (int i=0; i<iLen; i++ ) {
    		sTemp = s_option[i];
    		if (sTemp.equals("")) {
    			continue;
    		}
    		sb.append("<option value=");
    		sb.append(sTemp);
    		sb.append(">");
    		sb.append(sTemp);
    		sb.append("</option>\n");
    	}
    }
    
    s = sb.toString();
    sb = null;
    return s;      
  }
  
  public static String indexerButton(String moduleType, String moduleId, boolean isBarManager) {
  	return SzPageHelp.indexerButton(moduleType, moduleId, isBarManager, "");
  }  

  public static String indexerButton(String moduleType, String moduleId, boolean isBarManager, String asstant) {
  	String s = "";
  	if (isBarManager && (";" + asstant + ";").indexOf(";1;") >=0) {
  		s = SzPageHelp.button("全文检索", "document.location.href='../../searcher/index.jsp?moduleType=" + moduleType + "&moduleId=" + moduleId + "'");
  	}
  	return s;
  }
  
/*
  public static void main(String args[]) {
    //System.out.println(SzPageHelp.pageInclude(1, 10, 20, "aa", "formPage", "index.jsp"));
    System.out.println(SzPageHelp.idSelect("SZKC_KHFL","XXID","XXBT","KHFL",1, "txtClass1"));
  }
*/

}
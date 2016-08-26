package com.ztesoft.uboss.form.sz600.formEngine2;

import com.ztesoft.uboss.form.bll.FormManager;
import com.ztesoft.uboss.form.model.DynFormDto;
import com.ztesoft.uboss.form.model.DynFormVerDto;
import com.ztesoft.uboss.form.sz600.tool.SzConvert;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

public class DynFormCreate {

    /**
     * 修改字段的属性
     *
     * @param dynFormId
     * @param dynFormDto
     * @param request
     * @param iType      ：0编辑界面；1修改属性，2修改属性但是不重新创建表
     * @return
     */
    public boolean updateXMLField(String dynFormId, DynFormDto dynFormDto,
                                  HttpServletRequest request, int iType) throws BaseAppException {
        return updateXMLField(dynFormId, dynFormDto, request, iType, -1);
    }

    public boolean updateXMLField(String dynFormId, DynFormDto dynFormDto,
                                  HttpServletRequest request, int iType, long lastVerId)
            throws BaseAppException {
        int iReCreate = 0;
        if (iType == 0) {
            iReCreate = 0;
        } else {
            iReCreate = 1;
        }
        Enumeration rnames = request.getParameterNames();

        for (Enumeration e = rnames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            System.out.println(thisName + "-------" + thisValue);
        }
        DynFormControl bdFormControl = new DynFormControl();
        boolean flag;

        DynForm bdForm = new DynForm();

        Collection collTable1 = null;
        Collection collField1 = null;
        try {
            DynFormFieldInfo bdFormFieldInfo = null;
            Document document = null;
            DynFormVerDto dynFormVerDto = null;
            if (lastVerId > 0) {
                dynFormVerDto = new FormManager()
                        .queryFormVerDetailByVerId(lastVerId);
            } else {
                dynFormVerDto = new FormManager().queryFormVerDetail(dynFormId);
            }

            if (dynFormVerDto != null
                    && dynFormVerDto.getDesignXmlStr() != null
                    && dynFormVerDto.getDesignXmlStr().length() > 0) {
                document = XMLDom4jUtils.createDocument(
                        dynFormVerDto.getDesignXmlStr(), false, null);
            } else {

				/*
                 * document = XMLDom4jUtils.createDocument(
				 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT></ROOT>",
				 * false, null);
				 */
                createXMLField(dynFormId, dynFormDto, request, iType);
                return true;
            }

            // 查找名字为formName的element
            Element eleForm = (Element) document
                    .selectSingleNode("/ROOT/FORM[@formId='" + dynFormId + "']");

            if (eleForm == null) {
                eleForm = document.getRootElement().addElement("FORM");
            }
            String tableCount = SzConvert.toZeroStr(request
                    .getParameter("tableCount"));
            String rowCount = SzConvert.toZeroStr(request
                    .getParameter("rowCount"));
            String cellCount = SzConvert.toZeroStr(request
                    .getParameter("cellCount"));
            String objectCount = SzConvert.toZeroStr(request
                    .getParameter("objectCount"));

            // 设置eleForm
            eleForm.addAttribute("tableCount", tableCount);
            eleForm.addAttribute("rowCount", rowCount);
            eleForm.addAttribute("cellCount", cellCount);
            eleForm.addAttribute("objectCount", objectCount);

            // 到底有多少个表？
            int iCountTable = Integer.parseInt(tableCount);
            int i = 0;
            int j = 0;

            // 字段
            int iCount = Integer.parseInt(objectCount);
            String objectId = "";
            String field = "";
            String name = "";
            String type = "";
            Element eleField = null;
            int iControl = 0;
            int iZDZT = 1;
            int iZDLB = 1;
            String sZDLB = "1";
            String sZDLX = "";

            // 2010-06-01修改,增加排序功能:先对所有需要进行排序
            String order1 = "";
            String order2 = "";
            int iTemp = 0;
            ArrayList indexList = new ArrayList();
            String s[] = null;
            String s2[] = null;
            for (i = 1; i <= iCount; i++) {
                order1 = SzConvert
                        .toSpaceStr(request.getParameter("order" + i));
                s = new String[2];
                s[0] = Integer.toString(i);
                s[1] = order1;
                indexList.add(s);
            }

            int jIndex = 0;

            // 表
            String tableId = "";
            String tableName = "";
            String tableClass = "";
            String tableType = "0";
            String objectStr = "";
            String tableKey = "";
            String tableForeign = "";
            String tableMain = "0";
            Element eleTable = null;

            for (i = 0; i <= iCountTable; i++) {

                tableId = SzConvert.strWriteData(
                        request.getParameter("tableId" + i)).trim();
                tableName = SzConvert.strWriteData(
                        request.getParameter("table" + i)).trim();
                tableClass = SzConvert.strWriteData(
                        request.getParameter("tableClass" + i)).trim();
                objectStr = SzConvert.toSpaceStr(
                        request.getParameter("object" + i)).trim()
                        + ",";
                tableType = SzConvert.toZeroStr(
                        request.getParameter("tableType" + i)).trim();
                tableKey = SzConvert.toSpaceStr(
                        request.getParameter("tableKey" + i)).trim();
                tableForeign = SzConvert.toSpaceStr(
                        request.getParameter("tableForeign" + i)).trim();
                tableMain = SzConvert.toZeroStr(
                        request.getParameter("tableMain" + i)).trim();

                // 表是否已经存在？存在的话删除掉
                eleTable = (Element) document
                        .selectSingleNode("/ROOT/FORM[@formId='" + dynFormId
                                + "']/TABLE[@GLID='" + tableId + "']");

                if (tableName.equals("") || objectStr.equals("")) {
                    // 删除该表格
                    if (eleTable != null) {
                        eleForm.remove(eleTable);
                    }
                    continue;
                }

                tableName = tableName.toUpperCase();
                if (eleTable != null) {
                    if (iReCreate == 1) {
                        eleForm.remove(eleTable);
                        eleTable = eleForm.addElement("TABLE");
                    }
                    // eleForm.remove(eleTable);
                    SzConvert.setText(eleTable, "GLID", tableId);
                    SzConvert.setText(eleTable, "GLSM", "");
                    SzConvert.setText(eleTable, "GLBM", tableName);
                    SzConvert.setText(eleTable, "GLMS", "");
                    SzConvert.setText(eleTable, "GLLM", tableClass);
                    SzConvert.setText(eleTable, "GLLBM", "");
                    SzConvert.setText(eleTable, "GLFJ", "");
                    SzConvert.setText(eleTable, "GLLX", tableType);

                } else {
                    // 增加一个新的table
                    eleTable = eleForm.addElement("TABLE");
                    eleTable.addAttribute("GLID", tableId);
                    eleTable.addAttribute("GLSM", "");
                    eleTable.addAttribute("GLBM", tableName);
                    eleTable.addAttribute("GLMS", "");
                    eleTable.addAttribute("GLLM", tableClass);
                    eleTable.addAttribute("GLLBM", "");
                    eleTable.addAttribute("GLFJ", "");
                    eleTable.addAttribute("GLLX", tableType);
                }

                SzConvert.setText(eleTable, "GLZB", tableMain);
                // 保存字段信息
                // for (j =0; j<=iCount; j++) {
                for (jIndex = 0; jIndex < iCount; jIndex++) {
                    s = (String[]) indexList.get(jIndex);
                    j = SzConvert.toInt(s[0]);

                    objectId = SzConvert.toSpaceStr(request
                            .getParameter("objectId" + j));
                    field = SzConvert.strWriteData(request.getParameter("field"
                            + j));
                    name = SzConvert.strWriteData(request.getParameter("name"
                            + j));
                    type = SzConvert.toSpaceStr(request
                            .getParameter("type" + j));

                    iControl = Integer.parseInt(SzConvert.toZeroStr(request
                            .getParameter("ZDBJ" + j)));
                    sZDLX = SzConvert.toSpaceStr(request.getParameter("ZDLX"
                            + j));

                    // 查找该字段是否存在
                    eleField = (Element) document
                            .selectSingleNode("/ROOT/FORM[@formId='"
                                    + dynFormId + "']/TABLE[@GLID='" + tableId
                                    + "']/FIELD[@ZDID='" + objectId + "']");
                    // System.out.println(objectId + "==" + field);
                    if (field.equals("") || field.equals("0")) {
                        // 删除该字段
                        if (eleField != null) {
                            eleTable.remove(eleField);
                        }
                        continue;
                    }

                    // 字段是否属于该表？，不属于的话，直接跳过
                    if (objectStr.indexOf("," + objectId + ",") == -1) {
                        continue;
                    }

                    // 这里要换成switch，效率高些。
                    if (type.equals("0")) { // 普通text
                        type = "nvarchar";
                        iControl = 0;
                    } else if (type.equals("1")) { // 普通hidden
                        type = "nvarchar";
                        iControl = 1;
                    } else if (type.equals("7")) { // 普通hidden
                        type = "ntext";
                        iControl = 7;
                    } else if (type.equals("51")) { // 自动流水编号
                        type = "nvarchar";
                        iControl = 51;
                    } else if (type.equals("2")) { // 本人姓名
                        type = "nvarchar";
                        iControl = 2;
                    } else if (type.equals("3")) { // 本人ID
                        type = "int";
                        iControl = 3;
                    } else if (type.equals("33")) { // 本人部门名称
                        type = "nvarchar";
                        iControl = 33;
                    } else if (type.equals("34")) { // 本人部门ID
                        type = "int";
                        iControl = 34;
                    } else if (type.equals("4")) { // 创建时间
                        type = "nvarchar";
                        iControl = 4;
                    } else if (type.equals("44")) { // 当前时间
                        type = "nvarchar";
                        iControl = 44;
                    } else if (type.equals("31")) { // radio 单选
                        type = "nvarchar";
                        iControl = 31;
                    } else if (type.equals("32")) { // check 多选
                        type = "nvarchar";
                        iControl = 32;
                    } else if (type.equals("42")) { // 日期控件
                        type = "nvarchar";
                        iControl = 42;
                    } else if (type.equals("43")) { // 时间控件
                        type = "nvarchar";
                        iControl = 43;
                    } else if (type.equals("6")) { // 弹出窗口
                        type = "nvarchar";
                        iControl = 6;
                    } else if (type.equals("133")) { // select 单选
                        type = "nvarchar";
                        iControl = 133;
                    } else {
                        type = "nvarchar";
                        iControl = 0;
                    }

                    if (sZDLX.equals("")) {
                        sZDLX = type;
                    }
                    if (sZDLX.equals("")) {
                        sZDLX = "nvarchar";
                    }
                    // System.out.println("objectId===" + sZDLX);
                    bdFormFieldInfo = new DynFormFieldInfo();
                    iZDZT = Integer.parseInt(SzConvert.toZeroStr(request
                            .getParameter("ZDZT" + j)));
                    // 关键字
                    if (tableKey.equals(objectId)) {
                        iZDZT = 2;
                        bdFormFieldInfo.setZDKK(1);
                        bdFormFieldInfo.setZDLX("int");
                    } else if (tableForeign.equals(objectId)) {
                        iZDZT = 4;
                        bdFormFieldInfo.setZDKK(1);
                        bdFormFieldInfo.setZDLX("int");
                    } else {
                        bdFormFieldInfo.setZDKK(Integer.parseInt(SzConvert
                                .toZeroStr(request.getParameter("ZDKK" + j))));
                        bdFormFieldInfo.setZDLX(sZDLX);
                    }
                    sZDLB = SzConvert.toSpaceStr(request.getParameter("ZDLB"
                            + j));
                    if (sZDLB.equals("")
                            && bdFormFieldInfo.getZDLX().equals("nvarchar")) {
                        bdFormFieldInfo.setZDLB(1);
                    } else {
                        iZDLB = Integer.parseInt(SzConvert.toZeroStr(sZDLB));
                        bdFormFieldInfo.setZDLB(iZDLB);
                    }

                    // 如果是checkbox字段，要更换ID
                    bdFormFieldInfo.setZDID(objectId);
                    bdFormFieldInfo.setZDMC(field);
                    bdFormFieldInfo.setZDSM(name);

                    bdFormFieldInfo.setZDCD(SzConvert.toSpaceStr(request
                            .getParameter("ZDCD" + j)));

                    bdFormFieldInfo.setZDZT(iZDZT);

                    bdFormFieldInfo.setZDBJ(iControl);
                    bdFormFieldInfo.setZDBY(SzConvert.strWriteData(request
                            .getParameter("ZDBY" + j)));

                    bdFormFieldInfo.setZDGLBM(SzConvert.strWriteData(request
                            .getParameter("ZDGLBM" + j)));
                    bdFormFieldInfo.setZDGLID(SzConvert.strWriteData(request
                            .getParameter("ZDGLID" + j)));
                    bdFormFieldInfo.setZDGLMC(SzConvert.strWriteData(request
                            .getParameter("ZDGLMC" + j)));

                    bdFormFieldInfo.setZDCX(Integer.parseInt(SzConvert
                            .toZeroStr(request.getParameter("ZDCX" + j))));
                    bdFormFieldInfo.setZDDR(Integer.parseInt(SzConvert
                            .toZeroStr(request.getParameter("ZDDR" + j))));
                    bdFormFieldInfo.setZDDC(Integer.parseInt(SzConvert
                            .toZeroStr(request.getParameter("ZDDC" + j))));
                    bdFormFieldInfo.setZDCSZ(SzConvert.strWriteData(request
                            .getParameter("ZDCSZ" + j)));
                    bdFormFieldInfo.setZDHS(SzConvert.strWriteData(request
                            .getParameter("ZDHS" + j)));

                    // 2010-06-02增加
                    bdFormFieldInfo.setZDKZ(SzConvert.strWriteData(request
                            .getParameter("ZDKZ" + j)));
                    bdFormFieldInfo.setZDBQ(SzConvert.strWriteData(request
                            .getParameter("ZDBQ" + j)));

                    // 2011-06-07增加
                    bdFormFieldInfo.setAlign(SzConvert.strWriteData(request
                            .getParameter("Align" + j)));
                    bdFormFieldInfo.setBold(SzConvert.strWriteData(request
                            .getParameter("Bold" + j)));
                    bdFormFieldInfo.setColor(SzConvert.strWriteData(request
                            .getParameter("Color" + j)));
                    bdFormFieldInfo.setMaxlen(SzConvert.strWriteData(request
                            .getParameter("Maxlen" + j)));

                    bdFormFieldInfo.setZDLC(SzConvert.strWriteData(request
                            .getParameter("ZDLC" + j)));
                    bdFormFieldInfo.setZDQTB(SzConvert.strWriteData(request
                            .getParameter("ZDQTB" + j)));

                    if (bdFormFieldInfo.getZDLX() == null
                            || bdFormFieldInfo.getZDLX().equals("")) {
                        bdFormFieldInfo.setZDLX("nvarchar");
                    }
                    if (bdFormFieldInfo.getZDCD().equals("")) {
                        bdFormFieldInfo.setZDCD("200");
                    }

                    if (eleField != null) { // 已经有的字段
                        eleField.attribute("ZDMC").setValue(
                                bdFormFieldInfo.getZDMC());
                        eleField.attribute("ZDSM").setValue(
                                bdFormFieldInfo.getZDSM());
                        eleField.attribute("ZDLX").setValue(
                                bdFormFieldInfo.getZDLX());
                        if (iType == 1 || iType == 2) {
                            eleField.attribute("ZDCD").setValue(
                                    bdFormFieldInfo.getZDCD());
                            eleField.attribute("ZDKK")
                                    .setValue(
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDKK()));
                            eleField.attribute("ZDZT")
                                    .setValue(
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDZT()));
                            eleField.attribute("ZDLB")
                                    .setValue(
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDLB()));
                            eleField.attribute("ZDBJ")
                                    .setValue(
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDBJ()));
                            // eleField.attribute("ZDBY").setValue(bdFormFieldInfo.getZDBY());
                            // setText作用，如果属性不存在，创建一个新的
                            // 备用做为公式，在其他地方用，这里只要增加就好了！
                            // this.setText(eleField, "ZDBY",
                            // bdFormFieldInfo.getZDBY());

                            SzConvert.setText(eleField, "ZDGLBM",
                                    bdFormFieldInfo.getZDGLBM());
                            SzConvert.setText(eleField, "ZDGLID",
                                    bdFormFieldInfo.getZDGLID());
                            // 屏蔽,当已经设置了关联后,修改不能再设置,否则关联再次被设置为空
                            // SzConvert.setText(eleField, "ZDGLMC",
                            // bdFormFieldInfo.getZDGLMC());

                            SzConvert
                                    .setText(eleField, "ZDCX",
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDCX()));
                            SzConvert
                                    .setText(eleField, "ZDDR",
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDDR()));
                            SzConvert
                                    .setText(eleField, "ZDDC",
                                            Integer.toString(bdFormFieldInfo
                                                    .getZDDC()));
                            SzConvert.setText(eleField, "ZDCSZ",
                                    bdFormFieldInfo.getZDCSZ());
                            SzConvert.setText(eleField, "ZDHS",
                                    bdFormFieldInfo.getZDHS());

                            SzConvert.setText(eleField, "Align",
                                    bdFormFieldInfo.getAlign());
                            SzConvert.setText(eleField, "Bold",
                                    bdFormFieldInfo.getBold());
                            SzConvert.setText(eleField, "Color",
                                    bdFormFieldInfo.getColor());
                            SzConvert.setText(eleField, "Maxlen",
                                    bdFormFieldInfo.getMaxlen());
                            SzConvert.setText(eleField, "ZDLC",
                                    bdFormFieldInfo.getZDLC());
                            SzConvert.setText(eleField, "ZDQTB",
                                    bdFormFieldInfo.getZDQTB());

                        }
                    } else {
                        eleField = eleTable.addElement("FIELD");
                        eleField.setText(bdFormFieldInfo.getZDSM());
                        eleField.addAttribute("ZDID", bdFormFieldInfo.getZDID());
                        eleField.addAttribute("ZDMC", bdFormFieldInfo.getZDMC());
                        eleField.addAttribute("ZDSM", bdFormFieldInfo.getZDSM());
                        eleField.addAttribute("ZDLX", bdFormFieldInfo.getZDLX());
                        eleField.addAttribute("ZDCD", bdFormFieldInfo.getZDCD());
                        eleField.addAttribute("ZDKK",
                                Integer.toString(bdFormFieldInfo.getZDKK()));
                        eleField.addAttribute("ZDZT",
                                Integer.toString(bdFormFieldInfo.getZDZT()));
                        eleField.addAttribute("ZDLB",
                                Integer.toString(bdFormFieldInfo.getZDLB()));
                        eleField.addAttribute("ZDBJ",
                                Integer.toString(bdFormFieldInfo.getZDBJ()));
                        eleField.addAttribute("ZDBY", bdFormFieldInfo.getZDBY());

                        eleField.addAttribute("ZDGLBM",
                                bdFormFieldInfo.getZDGLBM());
                        eleField.addAttribute("ZDGLID",
                                bdFormFieldInfo.getZDGLID());
                        eleField.addAttribute("ZDGLMC",
                                bdFormFieldInfo.getZDGLMC());

                        eleField.addAttribute("ZDCX",
                                Integer.toString(bdFormFieldInfo.getZDCX()));
                        eleField.addAttribute("ZDDR",
                                Integer.toString(bdFormFieldInfo.getZDDR()));
                        eleField.addAttribute("ZDDC",
                                Integer.toString(bdFormFieldInfo.getZDDC()));
                        eleField.addAttribute("ZDCSZ",
                                bdFormFieldInfo.getZDCSZ());
                        eleField.addAttribute("ZDHS", bdFormFieldInfo.getZDHS());
                        // 2010-06-02增加
                        eleField.addAttribute("ZDKZ", bdFormFieldInfo.getZDKZ());
                        eleField.addAttribute("ZDBQ", bdFormFieldInfo.getZDBQ());

                        eleField.addAttribute("Align",
                                bdFormFieldInfo.getAlign());
                        eleField.addAttribute("Bold", bdFormFieldInfo.getBold());
                        eleField.addAttribute("Color",
                                bdFormFieldInfo.getColor());
                        eleField.addAttribute("Maxlen",
                                bdFormFieldInfo.getMaxlen());
                        eleField.addAttribute("ZDLC", bdFormFieldInfo.getZDLC());
                        eleField.addAttribute("ZDQTB",
                                bdFormFieldInfo.getZDQTB());
                    }
                } // for (j=0);
            } // for (i=0);

            bdFormControl.updateMapStatic(Long.valueOf(dynFormId), lastVerId);

            Map tableMap1 = bdFormControl.getTableMap();
            Map fieldMap1 = bdFormControl.getFieldMap();
            collTable1 = (Collection) tableMap1.get(dynFormId);
            collField1 = (Collection) fieldMap1.get(dynFormId);

            bdForm.saveDynXml(dynFormId, document.asXML());

            if (request.getParameter("isCreateTable") != null
                    && request.getParameter("isCreateTable").toString()
                    .equals("0"))
                return true;
            // 根据生成的XML创建新的数据表

            bdFormControl.updateMapStatic(Long.valueOf(dynFormId));
            Map tableMap2 = bdFormControl.getTableMap();
            Map fieldMap2 = bdFormControl.getFieldMap();
            Collection collTable2 = (Collection) tableMap2.get(dynFormId);
            Collection collField2 = (Collection) fieldMap2.get(dynFormId);

            if (iType != 2) {
                DynFormGenerator bdFormGenerator = new DynFormGenerator(
                        Long.valueOf(dynFormId));
                // bdFormGenerator.genTable();
                bdFormGenerator.modifyTable(collTable1, collField1, collTable2,
                        collField2);
            }
            //

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    public boolean createXMLField(String dynFormId, DynFormDto dynFormDto,
                                  HttpServletRequest request, int iType) throws BaseAppException {
        int iReCreate = 0;
        if (iType == 0) {
            iReCreate = 0;
        } else {
            iReCreate = 1;
        }
        Enumeration rnames = request.getParameterNames();

        for (Enumeration e = rnames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            System.out.println(thisName + "-------" + thisValue);
        }
        DynFormControl bdFormControl = new DynFormControl();
        boolean flag;

        DynForm bdForm = new DynForm();

        // System.out.println("============sPath==" + sPath);
        try {
            DynFormFieldInfo bdFormFieldInfo = null;
            Document document = XMLDom4jUtils.createDocument(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT></ROOT>",
                    false, null);

            Element rootElement = document.getRootElement();
            // 查找名字为formName的element
            Element eleForm = rootElement.addElement("FORM");

            String tableCount = SzConvert.toZeroStr(request
                    .getParameter("tableCount"));
            String rowCount = SzConvert.toZeroStr(request
                    .getParameter("rowCount"));
            String cellCount = SzConvert.toZeroStr(request
                    .getParameter("cellCount"));
            String objectCount = SzConvert.toZeroStr(request
                    .getParameter("objectCount"));

            // 设置eleForm
            eleForm.addAttribute("tableCount", tableCount);
            eleForm.addAttribute("rowCount", rowCount);
            eleForm.addAttribute("cellCount", cellCount);
            eleForm.addAttribute("objectCount", objectCount);

            eleForm.addAttribute("formId", dynFormDto.getDynFormId() + "");

            eleForm.addAttribute("formName", dynFormDto.getFormName());
            eleForm.addAttribute("formDesc", dynFormDto.getFormDesc());
            eleForm.addAttribute("formPackage", "");
            eleForm.addAttribute("hasAnnex", "");
            eleForm.addAttribute("hasFlow", "");
            eleForm.addAttribute("hasFlowSms", "");
            eleForm.addAttribute("relateFormID", "");
            eleForm.addAttribute("triggerName", "");
            eleForm.addAttribute("selectName", "");
            eleForm.addAttribute("allograph", "");
            eleForm.addAttribute("editor", "");
            eleForm.addAttribute("weboffice", "");
            eleForm.addAttribute("modulePopedom", "");
            eleForm.addAttribute("lock", "0");
            eleForm.addAttribute("defaultCond", "");
            eleForm.addAttribute("prefix", "");
            eleForm.addAttribute("printTemp", "");
            eleForm.addAttribute("printTempRelate", "");
            eleForm.addAttribute("assistant", "");
            eleForm.addAttribute("formType", dynFormDto.getFormType());

            // 到底有多少个表？
            int iCountTable = Integer.parseInt(tableCount);
            int i = 0;
            int j = 0;

            // 字段
            int iCount = Integer.parseInt(objectCount);
            String objectId = "";
            String field = "";
            String name = "";
            String type = "";
            Element eleField = null;
            int iControl = 0;
            int iZDZT = 1;
            int iZDLB = 1;
            String sZDLB = "1";
            String sZDLX = "";

            // 2010-06-01修改,增加排序功能:先对所有需要进行排序
            String order1 = "";
            String order2 = "";
            int iTemp = 0;
            ArrayList indexList = new ArrayList();
            String s[] = null;
            String s2[] = null;
            for (i = 1; i <= iCount; i++) {
                order1 = SzConvert
                        .toSpaceStr(request.getParameter("order" + i));
                s = new String[2];
                s[0] = Integer.toString(i);
                s[1] = order1;
                indexList.add(s);
            }

            int jIndex = 0;

            // 表
            String tableId = "";
            String tableName = "";
            String tableClass = "";
            String tableType = "0";
            String objectStr = "";
            String tableKey = "";
            String tableForeign = "";
            String tableMain = "0";
            Element eleTable = null;

            for (i = 0; i <= iCountTable; i++) {

                tableId = SzConvert.strWriteData(
                        request.getParameter("tableId" + i)).trim();
                tableName = SzConvert.strWriteData(
                        request.getParameter("table" + i)).trim();
                tableClass = SzConvert.strWriteData(
                        request.getParameter("tableClass" + i)).trim();
                objectStr = SzConvert.toSpaceStr(
                        request.getParameter("object" + i)).trim()
                        + ",";
                tableType = SzConvert.toZeroStr(
                        request.getParameter("tableType" + i)).trim();
                tableKey = SzConvert.toSpaceStr(
                        request.getParameter("tableKey" + i)).trim();
                tableForeign = SzConvert.toSpaceStr(
                        request.getParameter("tableForeign" + i)).trim();
                tableMain = SzConvert.toZeroStr(
                        request.getParameter("tableMain" + i)).trim();

                // 增加一个新的table
                eleTable = eleForm.addElement("TABLE");
                eleTable.addAttribute("GLID", tableId);
                eleTable.addAttribute("GLSM", "");
                eleTable.addAttribute("GLBM", tableName);
                eleTable.addAttribute("GLMS", "");
                eleTable.addAttribute("GLLM", tableClass);
                eleTable.addAttribute("GLLBM", "");
                eleTable.addAttribute("GLFJ", "");
                eleTable.addAttribute("GLLX", tableType);

                SzConvert.setText(eleTable, "GLZB", tableMain);
                // 保存字段信息
                // for (j =0; j<=iCount; j++) {
                for (jIndex = 0; jIndex < iCount; jIndex++) {
                    s = (String[]) indexList.get(jIndex);
                    j = SzConvert.toInt(s[0]);

                    objectId = SzConvert.toSpaceStr(request
                            .getParameter("objectId" + j));
                    field = SzConvert.strWriteData(request.getParameter("field"
                            + j));
                    name = SzConvert.strWriteData(request.getParameter("name"
                            + j));
                    type = SzConvert.toSpaceStr(request
                            .getParameter("type" + j));
                    iControl = Integer.parseInt(SzConvert.toZeroStr(request
                            .getParameter("ZDBJ" + j)));
                    sZDLX = SzConvert.toSpaceStr(request.getParameter("ZDLX"
                            + j));

                    if (objectStr.indexOf("," + objectId + ",") == -1) {
                        continue;
                    }

                    // 这里要换成switch，效率高些。
                    if (type.equals("0")) { // 普通text
                        type = "nvarchar";
                        iControl = 0;
                    } else if (type.equals("1")) { // 普通hidden
                        type = "nvarchar";
                        iControl = 1;
                    } else if (type.equals("7")) { // 普通hidden
                        type = "ntext";
                        iControl = 7;
                    } else if (type.equals("51")) { // 自动流水编号
                        type = "nvarchar";
                        iControl = 51;
                    } else if (type.equals("2")) { // 本人姓名
                        type = "nvarchar";
                        iControl = 2;
                    } else if (type.equals("3")) { // 本人ID
                        type = "int";
                        iControl = 3;
                    } else if (type.equals("33")) { // 本人部门名称
                        type = "nvarchar";
                        iControl = 33;
                    } else if (type.equals("34")) { // 本人部门ID
                        type = "int";
                        iControl = 34;
                    } else if (type.equals("4")) { // 创建时间
                        type = "nvarchar";
                        iControl = 4;
                    } else if (type.equals("44")) { // 当前时间
                        type = "nvarchar";
                        iControl = 44;
                    } else if (type.equals("31")) { // radio 单选
                        type = "nvarchar";
                        iControl = 31;
                    } else if (type.equals("32")) { // check 多选
                        type = "nvarchar";
                        iControl = 32;
                    } else if (type.equals("42")) { // 日期控件
                        type = "nvarchar";
                        iControl = 42;
                    } else if (type.equals("43")) { // 时间控件
                        type = "nvarchar";
                        iControl = 43;
                    } else if (type.equals("6")) { // 弹出窗口
                        type = "nvarchar";
                        iControl = 6;
                    } else if (type.equals("133")) { // select 单选
                        type = "nvarchar";
                        iControl = 133;
                    } else {
                        type = "nvarchar";
                        iControl = 0;
                    }

                    if (sZDLX.equals("")) {
                        sZDLX = type;
                    }
                    if (sZDLX.equals("")) {
                        sZDLX = "nvarchar";
                    }
                    // System.out.println("objectId===" + sZDLX);
                    bdFormFieldInfo = new DynFormFieldInfo();
                    iZDZT = Integer.parseInt(SzConvert.toZeroStr(request
                            .getParameter("ZDZT" + j)));
                    // 关键字
                    if (tableKey.equals(objectId)) {
                        iZDZT = 2;
                        bdFormFieldInfo.setZDKK(1);
                        bdFormFieldInfo.setZDLX("int");
                    } else if (tableForeign.equals(objectId)) {
                        iZDZT = 4;
                        bdFormFieldInfo.setZDKK(1);
                        bdFormFieldInfo.setZDLX("int");
                    } else {
                        bdFormFieldInfo.setZDKK(Integer.parseInt(SzConvert
                                .toZeroStr(request.getParameter("ZDKK" + j))));
                        bdFormFieldInfo.setZDLX(sZDLX);
                    }
                    sZDLB = SzConvert.toSpaceStr(request.getParameter("ZDLB"
                            + j));
                    if (sZDLB.equals("")
                            && bdFormFieldInfo.getZDLX().equals("nvarchar")) {
                        bdFormFieldInfo.setZDLB(1);
                    } else {
                        iZDLB = Integer.parseInt(SzConvert.toZeroStr(sZDLB));
                        bdFormFieldInfo.setZDLB(iZDLB);
                    }

                    // 如果是checkbox字段，要更换ID
                    bdFormFieldInfo.setZDID(objectId);
                    bdFormFieldInfo.setZDMC(field);
                    bdFormFieldInfo.setZDSM(name);

                    bdFormFieldInfo.setZDCD(SzConvert.toSpaceStr(request
                            .getParameter("ZDCD" + j)));

                    bdFormFieldInfo.setZDZT(iZDZT);

                    bdFormFieldInfo.setZDBJ(iControl);
                    bdFormFieldInfo.setZDBY(SzConvert.strWriteData(request
                            .getParameter("ZDBY" + j)));

                    bdFormFieldInfo.setZDGLBM(SzConvert.strWriteData(request
                            .getParameter("ZDGLBM" + j)));
                    bdFormFieldInfo.setZDGLID(SzConvert.strWriteData(request
                            .getParameter("ZDGLID" + j)));
                    bdFormFieldInfo.setZDGLMC(SzConvert.strWriteData(request
                            .getParameter("ZDGLMC" + j)));

                    bdFormFieldInfo.setZDCX(Integer.parseInt(SzConvert
                            .toZeroStr(request.getParameter("ZDCX" + j))));
                    bdFormFieldInfo.setZDDR(Integer.parseInt(SzConvert
                            .toZeroStr(request.getParameter("ZDDR" + j))));
                    bdFormFieldInfo.setZDDC(Integer.parseInt(SzConvert
                            .toZeroStr(request.getParameter("ZDDC" + j))));
                    bdFormFieldInfo.setZDCSZ(SzConvert.strWriteData(request
                            .getParameter("ZDCSZ" + j)));
                    bdFormFieldInfo.setZDHS(SzConvert.strWriteData(request
                            .getParameter("ZDHS" + j)));

                    // 2010-06-02增加
                    bdFormFieldInfo.setZDKZ(SzConvert.strWriteData(request
                            .getParameter("ZDKZ" + j)));
                    bdFormFieldInfo.setZDBQ(SzConvert.strWriteData(request
                            .getParameter("ZDBQ" + j)));

                    // 2011-06-07增加
                    bdFormFieldInfo.setAlign(SzConvert.strWriteData(request
                            .getParameter("Align" + j)));
                    bdFormFieldInfo.setBold(SzConvert.strWriteData(request
                            .getParameter("Bold" + j)));
                    bdFormFieldInfo.setColor(SzConvert.strWriteData(request
                            .getParameter("Color" + j)));
                    bdFormFieldInfo.setMaxlen(SzConvert.strWriteData(request
                            .getParameter("Maxlen" + j)));

                    bdFormFieldInfo.setZDLC(SzConvert.strWriteData(request
                            .getParameter("ZDLC" + j)));
                    bdFormFieldInfo.setZDQTB(SzConvert.strWriteData(request
                            .getParameter("ZDQTB" + j)));

                    if (bdFormFieldInfo.getZDLX() == null
                            || bdFormFieldInfo.getZDLX().equals("")) {
                        bdFormFieldInfo.setZDLX("nvarchar");
                    }
                    if (bdFormFieldInfo.getZDCD().equals("")) {
                        bdFormFieldInfo.setZDCD("200");
                    }

                    eleField = eleTable.addElement("FIELD");
                    eleField.setText(bdFormFieldInfo.getZDSM());
                    eleField.addAttribute("ZDID", bdFormFieldInfo.getZDID());
                    eleField.addAttribute("ZDMC", bdFormFieldInfo.getZDMC());
                    eleField.addAttribute("ZDSM", bdFormFieldInfo.getZDSM());
                    eleField.addAttribute("ZDLX", bdFormFieldInfo.getZDLX());
                    eleField.addAttribute("ZDCD", bdFormFieldInfo.getZDCD());
                    eleField.addAttribute("ZDKK",
                            Integer.toString(bdFormFieldInfo.getZDKK()));
                    eleField.addAttribute("ZDZT",
                            Integer.toString(bdFormFieldInfo.getZDZT()));
                    eleField.addAttribute("ZDLB",
                            Integer.toString(bdFormFieldInfo.getZDLB()));
                    eleField.addAttribute("ZDBJ",
                            Integer.toString(bdFormFieldInfo.getZDBJ()));
                    eleField.addAttribute("ZDBY", bdFormFieldInfo.getZDBY());

                    eleField.addAttribute("ZDGLBM", bdFormFieldInfo.getZDGLBM());
                    eleField.addAttribute("ZDGLID", bdFormFieldInfo.getZDGLID());
                    eleField.addAttribute("ZDGLMC", bdFormFieldInfo.getZDGLMC());

                    eleField.addAttribute("ZDCX",
                            Integer.toString(bdFormFieldInfo.getZDCX()));
                    eleField.addAttribute("ZDDR",
                            Integer.toString(bdFormFieldInfo.getZDDR()));
                    eleField.addAttribute("ZDDC",
                            Integer.toString(bdFormFieldInfo.getZDDC()));
                    eleField.addAttribute("ZDCSZ", bdFormFieldInfo.getZDCSZ());
                    eleField.addAttribute("ZDHS", bdFormFieldInfo.getZDHS());
                    // 2010-06-02增加
                    eleField.addAttribute("ZDKZ", bdFormFieldInfo.getZDKZ());
                    eleField.addAttribute("ZDBQ", bdFormFieldInfo.getZDBQ());

                    eleField.addAttribute("Align", bdFormFieldInfo.getAlign());
                    eleField.addAttribute("Bold", bdFormFieldInfo.getBold());
                    eleField.addAttribute("Color", bdFormFieldInfo.getColor());
                    eleField.addAttribute("Maxlen", bdFormFieldInfo.getMaxlen());

                    eleField.addAttribute("ZDLC", bdFormFieldInfo.getZDLC());
                    eleField.addAttribute("ZDQTB", bdFormFieldInfo.getZDQTB());

                } // for (j=0);
            } // for (i=0);

            bdForm.saveDynXml(dynFormId, document.asXML());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (request.getParameter("isCreateTable") != null
                && request.getParameter("isCreateTable").toString().equals("0"))
            return true;
        // 根据生成的XML创建新的数据表

        try {
            bdFormControl.updateMapStatic(Long.valueOf(dynFormId));
            Map tableMap2 = bdFormControl.getTableMap();
            Map fieldMap2 = bdFormControl.getFieldMap();
            Collection collTable2 = (Collection) tableMap2.get(dynFormId);
            Collection collField2 = (Collection) fieldMap2.get(dynFormId);

            if (iType != 2) {
                DynFormGenerator bdFormGenerator = new DynFormGenerator(
                        Long.valueOf(dynFormId));
                // bdFormGenerator.genTable();
                bdFormGenerator.modifyTable(null, null, collTable2, collField2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //

        return true;

    }
}

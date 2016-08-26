package com.ztesoft.uboss.bpm.setup.servlet;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.HttpCall;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class FlowExportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LOCAL_CHARSET = "UTF-8";
    private static final String XML_CHARSET = "UTF-8";
    public static final ZSmartLogger logger = ZSmartLogger.getLogger(FlowExportServlet.class);

    public FlowExportServlet() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding(LOCAL_CHARSET);

        // 设置响应头和下载保存的文件名
        String name = request.getParameter("flowName");
        byte[] bs = name.getBytes("iso-8859-1");
        String flowName = new String(bs, LOCAL_CHARSET);
        String version = request.getParameter("version");
        String deployId = request.getParameter("deployId");
        HttpCall httpCall = new HttpCall();
        DynamicDict dict = new DynamicDict();
        String xml = null;
        try {
            dict.setServiceName("ProcessDefineService");
            dict.set("method", "qryBpmContent");
            dict.set("DEPLOY_ID", deployId);
            httpCall.callService(dict);
            xml = dict.getString("BPM_XML");

            Document defdDoc = XMLDom4jUtils.fromXML(xml, XML_CHARSET);
            Element definition = defdDoc.getRootElement();
            Document doc = XMLDom4jUtils.createDocument();
            Element root = doc.addElement("uboss");
            Element process = root.addElement("process");
            Element processName = process.addElement("process_name");
            processName.setText(flowName);
            Element processVersion = process.addElement("process_version");
            processVersion.setText(version);
            root.add(definition);

            Element procDefVarList = process.addElement("proc_def_var_list");
            dict.setServiceName("ProcessDefineService");
            dict.set("method", "queryPorcessVarDef");
            dict.set("DEPLOY_ID", deployId);
            httpCall.callService(dict);
            @SuppressWarnings("unchecked")
            List<DynamicDict> varList = dict.getList("PROC_DEF_VAR_LIST");
            for (DynamicDict var : varList) {
                Element procDefVar = procDefVarList.addElement("proc_def_var");
                procDefVar.addElement("var_name").setText(var.getString("VAR_NAME"));
                procDefVar.addElement("var_type").setText(var.getString("VAR_TYPE"));
                procDefVar.addElement("default_value").setText(var.getString("DEFAULT_VALUE"));
                procDefVar.addElement("var_comments").setText(var.getString("VAR_COMMENTS"));
            }

            byte[] xmlByte = XMLDom4jUtils.toXML(doc, XML_CHARSET);
            xml = new String(xmlByte, XML_CHARSET);
            logger.debug("export flow-->" + xml);
        } catch (BaseAppException ex) {
            logger.error("查询流程版本xml出错:" + ex.getMessage());
        }

        String resFileName = new String((flowName + version + ".xml").getBytes("GB2312"), "ISO-8859-1");

        response.setContentType("APPLICATION/OCTET-STREAM;charset=" + LOCAL_CHARSET);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resFileName + "\"");


        ByteArrayInputStream in = null;
        in = new ByteArrayInputStream(xml.getBytes(LOCAL_CHARSET));

        int i = 0;
        byte[] bt = new byte[8192];
        OutputStream out = response.getOutputStream();
        try {
            while ((i = in.read(bt)) != -1) {
                out.write(bt, 0, i);
            }
        } catch (Exception ex) {
            logger.error("Download file exception:" + ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    logger.error("Download file exception:" + ex.getMessage());
                }

                out = null;
            }
        }
    }
}

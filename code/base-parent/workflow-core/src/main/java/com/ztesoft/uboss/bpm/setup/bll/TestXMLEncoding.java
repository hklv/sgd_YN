package com.ztesoft.uboss.bpm.setup.bll;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

public class TestXMLEncoding {

    private static InputSource getInputSource(String src) {
        return new InputSource(new ByteArrayInputStream(src.getBytes()));
    }

    public static void main(String[] args) throws Exception {
        String encoding = System.getProperty("file.encoding");
//		
        String xml = "<?xml version=\"1.0\"?><test xmlns=\"\" name=\"名称\"></test>";
//		System.out.println(xmlStr);
//
//		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//		SAXParser saxParser = saxParserFactory.newSAXParser();
//		XMLReader xmlReader = saxParser.getXMLReader();
//
//		InputSource input = getInputSource(xmlStr);
//		xmlReader.parse(input);

//		String xml = FileUtil.loadTxtFile("c:\\flow.xml");
        if (!xml.startsWith("<?xml")) {
            xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + xml;
        }
        if (xml.startsWith("<?xml version=\"1.0\"?>")) {
            xml = xml.replaceAll("version=\"1.0\"",
                    "version='1.0' encoding='" + encoding + "'");
        }

		/*
         * 解决firefox浏览器初次创建流程在保存的时候删除xmlns=
		 * "http://www.omg.org/spec/BPMN/20100524/MODEL"属性的问题开始
		 */
        if (xml.indexOf("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"") == -1) {
            xml = xml
                    .replaceAll(
                            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                            " xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        }
		/*
		 * 解决firefox浏览器初次创建流程在保存的时候删除xmlns=
		 * "http://www.omg.org/spec/BPMN/20100524/MODEL"属性的问题结束
		 */
		/* 处理子流程浏览器自动增加如下属性早晨工作流无法部署的问题开始 */
        xml = xml.replaceAll("xmlns=\"\"", "");
        xml = xml.replaceAll("xmlns:bpmndi=\"BPMN/20100524/DI\"", "");
        xml = xml.replaceAll("xmlns:omgdc=\"DD/20100524/DC\"", "");
        xml = xml.replaceAll("xmlns:omgdi=\"DD/20100524/DI\"", "");
        xml = xml.replaceAll("xmlns:activiti=\"activiti.org/bpmn\"", "");
        xml = xml.replaceAll("&lt;", "<");
        xml = xml.replaceAll("&gt;", ">");
        //
        SAXReader saxReader = new SAXReader();
        Document document = (Document) saxReader.read(new StringReader(
                xml));
//		Document xmlDoc = XMLDom4jUtils.fromXML(xml, encoding);
        document.setXMLEncoding(encoding);
        String flowXML = document.asXML();
        System.out.println(flowXML);
    }
}

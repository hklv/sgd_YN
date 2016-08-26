package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.OutputStreamWriter;

/**
 * xml工具类
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/5/11
 */
public class MyXMLDom4jUtils {
    /**
     * 缺省字符集
     */
    public static final String DEFAULT_ENCODING = "UTF-8";// Common.DEFAULT_CHARSET;

    public static String element2XML(Element element, String encoding)
            throws BaseAppException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        element2XML(element, stream, encoding);
        if (stream != null) {
            try {
                stream.close();
            } catch (java.io.IOException ex) {
            }
        }
        return new String(stream.toByteArray());
    }

    public static void element2XML(Element element,
                                   java.io.OutputStream outStream, String encoding)
            throws BaseAppException {
        //
        OutputFormat outformat = new OutputFormat();
        outformat.setIndentSize(0);
        outformat.setNewlines(false);
        outformat.setTrimText(true);

        if (encoding == null || encoding.trim().equals("")) {
            encoding = DEFAULT_ENCODING;
        }
        // 设置编码类型
        outformat.setEncoding(encoding);
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(new OutputStreamWriter(outStream),
                    outformat);
            xmlWriter.write(element);
            xmlWriter.flush();
        } catch (java.io.IOException ex) {
            ExceptionHandler.publish("UTIL-0001", ex);
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (java.io.IOException ex) {
                }
            }
        }
    }
}

package com.ztesoft.zsmartcity.sgd.servlet;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.web.servlet.DownloadServlet;
import com.ztesoft.zsmartcity.sgd.attachment.domain.Attachment;
import com.ztesoft.zsmartcity.sgd.attachment.service.AttachmentService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by liuhao 2016-6-17.
 * 2016-6-17
 * sgd
 * com.ztesoft.zsmartcity.sgd.servlet
 */
public class DownloadServlet4Sgd extends DownloadServlet {
    /**
     * LOCAL_CHARSET
     */
    private static final String LOCAL_CHARSET = "UTF-8";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(LOCAL_CHARSET);
        String filename = "";
        String filePath = request.getParameter("filePath");
        if (!StringUtil.isEmpty(filePath)) {
            filePath = new String(filePath.getBytes("ISO-8859-1"), LOCAL_CHARSET);
        }
        filename = request.getParameter("fileName");
        if (!StringUtil.isEmpty(filename)) {
            filename = new String(filename.getBytes("ISO-8859-1"), LOCAL_CHARSET);
            if (StringUtil.isEmpty(filePath)) {
                filePath = filename;
            } else {
                filePath = filePath + File.separator + filename;
            }
        }

        logger.info("get filePath=" + filePath);
        if (!StringUtil.isEmpty(filePath)) {
            filename = FilenameUtils.getName(filePath);
        }

        // 设置响应头和下载保存的文件名

        AttachmentService serv = new AttachmentService();
        DynamicDict d = new DynamicDict();
        try {
            d.set("ALIAS_NAME", filename);
            Attachment dto = serv.qrySingleAttachmentByTrueName(d);
            filePath = dto.getPath();
            filename = dto.getTrueName();
        } catch (BaseAppException e) {
            e.printStackTrace();
        }
        // 文件名中文乱码的问题
        String userAgent = request.getHeader("USER-AGENT");
        if (userAgent != null && userAgent.contains("MSIE")) {
            filename = URLEncoder.encode(filename, "UTF-8");
            filename = filename.replace("+", "%20");
        } else {
            filename = new String(filename.getBytes(), "ISO-8859-1");
        }
        response.setContentType("APPLICATION/OCTET-STREAM;charset=" + LOCAL_CHARSET);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        // 打开指定文件的流信息
        File f = new File(filePath);
        // 写出流信息
        response.setContentLength((int) f.length());
        IOUtils.copy(new FileInputStream(f), response.getOutputStream());
    }
}

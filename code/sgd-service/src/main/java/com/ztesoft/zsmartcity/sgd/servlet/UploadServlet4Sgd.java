package com.ztesoft.zsmartcity.sgd.servlet;

import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.mvc.helper.JsonUtil;
import com.ztesoft.zsmart.web.resource.Common;
import com.ztesoft.zsmart.web.servlet.UploadServlet;
import com.ztesoft.zsmartcity.sgd.attachment.domain.Attachment;
import com.ztesoft.zsmartcity.sgd.attachment.service.AttachmentService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by liuhao 2016-6-13.
 * 2016-6-13
 * sgd
 * com.ztesoft.zsmartcity.sgd.servlet
 * 继承框架的servlet，做了部分修改。
 */
public class UploadServlet4Sgd extends UploadServlet {
    public UploadServlet4Sgd() {

    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * logger
     */
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(UploadServlet.class);

    /**
     * LOCAL_CHARSET
     */
    private static final String LOCAL_CHARSET = "UTF-8";


    /**
     * The doPost method of the servlet. <br>
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        upload(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        upload(request, response);
    }

    public void upload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding(LOCAL_CHARSET);
        response.setContentType("text/plain; charset=" + LOCAL_CHARSET);
        String modelName = request.getParameter("moduleName") == null ? "default" : request.getParameter("moduleName");
        // 上传文件
        String errCodeGlobal = null;
        String fileSizeMax = "";
        String fileSrc = "";
        String newFileName = "";
        String fileSize = "";
        String realName = "";
        Long attachment = 1L;
        String realPath = "";
        String extPath;
        String datePath;
        String fileType;
        // 框架那边使用
        boolean isSuccess = true;
        boolean isWindows = false;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //判断是否是linux
            if ("\\".equals(System.getProperties().getProperty("file.separator"))) {
                isWindows = true;
            }
            String thresholdSize = ConfigurationMgr.instance().getString("upload.uploadThresholdSize");
            String repository = isWindows ? ConfigurationMgr.instance().getString("uploadRepositorySgd") : ConfigurationMgr.instance().getString("upload.uploadRepository");
            if (!repository.endsWith("\\\\") && !repository.endsWith("/")) {
                repository += File.separator;
            }
            File dirRepository = new File(repository);
            if (!dirRepository.exists()) {
                dirRepository.mkdirs();
            }
            fileSizeMax = ConfigurationMgr.instance().getString("upload.uploadFileSizeMax");
            String fileDirectory = isWindows ? ConfigurationMgr.instance().getString("uploadFileDirectorySgd") : ConfigurationMgr.instance().getString("upload.uploadFileDirectory");

            if (!fileDirectory.endsWith("\\\\") && !fileDirectory.endsWith("/")) {
                fileDirectory += File.separator;
            }
            extPath = getModelPath(modelName);
            datePath = this.getDatePath();
            fileDirectory = fileDirectory + extPath + datePath;
            File dirDirectory = new File(fileDirectory);
            if (!dirDirectory.exists()) {
                dirDirectory.mkdirs();
            }

            // 设置最多只允许在内存中存储的数据,单位:字节
            factory.setSizeThreshold(Integer.parseInt(thresholdSize));
            factory.setRepository(dirRepository);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Set overall request size constraint
            upload.setSizeMax(Long.parseLong(fileSizeMax));

            // Parse the request
            List<FileItem> fileItems = upload.parseRequest(request);

            // 依次处理每个上传的文件
            Iterator iter = fileItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                // 忽略其他不是文件域的所有表单信息
                if (!item.isFormField()) {
                    String name = item.getName();
                    int lastIndex = name.lastIndexOf("\\");
                    String fileName = name.substring(lastIndex + 1);
                    //保存真实名称
                    realName = fileName;
                    //文件路径
                    fileSrc = fileDirectory + fileName;
                    //如果上传的文件不包含文件类型（暂时不考虑）
                    fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    fileSize = String.valueOf(item.getSize());
                    if ("0".equals(fileSize)) {
                        isSuccess = false;
                        errCodeGlobal = "S-UPLOAD-006";
                        logger.error("File can not be empty!");
                        break;
                    }
                    File newFile;

                    //加上时间戳+随机数
                    newFileName = "" + java.util.UUID.randomUUID().toString();
                    newFile = new File(fileDirectory + newFileName + "." + fileType);
                    realPath = dirDirectory.getPath() + File.separator + newFileName + "." + fileType;
                    int index = -1;
                    // 如果当前文件存在,文件名后缀计数累计
                    int intFileId = 0;
                    while (newFile.exists()) {
                        intFileId += 1;
                        index = fileName.lastIndexOf(".");
                        if (index != -1) {
                            newFileName = fileName.substring(0, index) + "_" + intFileId + "."
                                    + fileName.substring(index + 1);
                        } else {
                            newFileName = fileName + "_" + intFileId;
                        }
                        newFile = new File(fileDirectory + newFileName + "." + fileType);
                        realPath = fileDirectory + newFileName + "." + fileType;
                    }

                    // save file to directory of upload
                    item.write(newFile);
                    logger.info("UploadFile success:" + fileDirectory + newFileName);
                    extPath = extPath + newFileName;
                    //存表并且返回这个文件对应表里的Key
                    Attachment dto = new Attachment();
                    dto.setComments("");
                    dto.setAliasName(newFileName);
                    dto.setTrueName(fileName);
                    dto.setFileSize(fileSize);
                    dto.setFileType(fileType);
                    dto.setPath(realPath);
                    dto.setState("A");
                    Session ses = null;
                    try {
                        ses = SessionContext.currentSession();
                        ses.beginTrans();
                        AttachmentService serv = new AttachmentService();
                        serv.addAttachment(dto);
                        ses.commitTrans();
                    } finally {
                        ses.releaseTrans();
                    }

                    attachment = dto.getId();
                    System.out.printf("upload end");
                }
            }
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            isSuccess = false;
            logger.error("The file size exceeds the limitation of " + fileSizeMax + "kb", ex);
            errCodeGlobal = "S-UPLOAD-007";
        } catch (Exception e) {
            isSuccess = false;
            logger.error(e.getMessage(), e);
            errCodeGlobal = "S-UPLOAD-003";
        }
        // 返回json数据
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("isSuccess", isSuccess);
        if (!isSuccess) {
            res.put("MsgCode", errCodeGlobal);
            String msg = Common.getCommonRes(errCodeGlobal);
            if ("S-UPLOAD-007".equals(errCodeGlobal)) {
                long maxSize = Long.parseLong(fileSizeMax);
                String maxSizeStr = Common.convertFileSize(maxSize);
                msg = msg + " " + maxSizeStr;
                res.put("fileSizeMax", fileSizeMax);

            }
            res.put("Msg", msg);
        } else {
            res.put("Msg", "upload SUCCESS");
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("FILE_SRC", fileSrc);
            dataMap.put("FILE_SIZE", fileSize);
            dataMap.put("FILE_PATH", realPath);
            dataMap.put("TRUE_NAME", realName);
            dataMap.put("ID", attachment.toString());
            dataMap.put("ALIAS_NAME", newFileName);
            res.put("z_d_r", dataMap);
        }
        String str = JsonUtil.objToJson(res);

        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(str);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            pw.close();
        }

    }

    /**
     * Description: 根据模块名生成路径<br>
     *
     * @param modelName 模块名:如a/b/c<br>
     * @return <br>
     */
    private String getModelPath(String modelName) {
        if (!StringUtil.isEmpty(modelName) && !modelName.endsWith("\\\\") && !modelName.endsWith("/")) {
            modelName += File.separator;

        }
        if (modelName.startsWith("\\\\") || modelName.startsWith("/")) {
            modelName = modelName.substring(1);
        }
        return modelName;
    }


    /**
     * 得到/year/month/day/的文件夹物理路径
     *
     * @return
     */
    private String getDatePath() {
        Calendar cal = Calendar.getInstance();
        int date = cal.get(Calendar.DATE);
        int year = cal.get(Calendar.YEAR);
        //month+1
        int month = cal.get(Calendar.MONTH) + 1;
        System.out.printf("" + date);
        String path = "";
        path += this.getPackagePath(year + "");
        path += this.getPackagePath(month + "");
        path += this.getPackagePath(date + "");
        path = this.getPackagePath(path);
        return path;
    }

    /**
     * 传入包名返回带分隔符的路径
     *
     * @return
     */
    private String getPackagePath(String path) {
        if (!StringUtil.isEmpty(path) && !path.endsWith("\\\\") && !path.endsWith("/")) {
            path += File.separator;
        }
        return path;
    }
}

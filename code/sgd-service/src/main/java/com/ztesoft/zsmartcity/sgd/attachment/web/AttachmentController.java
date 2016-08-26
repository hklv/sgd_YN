package com.ztesoft.zsmartcity.sgd.attachment.web;

import com.ztesoft.sgd.base.web.ActionSupport;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmartcity.sgd.attachment.service.AttachmentService;

/**
 * Created by asus-pc on 2016-6-16.
 * 2016-6-16
 * sgd
 * com.ztesoft.zsmartcity.sgd.attachment.web
 */
public class AttachmentController  extends ActionSupport {
    @Override
    public int perform(DynamicDict dict) throws BaseAppException {
        String serviceName = dict.serviceName;
        if ("AddAttachment".equalsIgnoreCase(serviceName)) {
            return addAttachment(dict);
        } else {
            String msg = StringUtil.stringFormat("BO.serviceName = [{0}] can not match method.", dict.getServiceName(),
                    serviceName);
            throw new BaseAppException("S-ACT-00203", msg, BaseAppException.INNER_ERROR);
        }

    }
    public int addAttachment(DynamicDict dict) throws BaseAppException {
        AttachmentService serv = new AttachmentService();
        return serv.addAttachment(dict);
    }
}

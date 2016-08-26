package com.ztesoft.zsmartcity.sgd.attachment.service;

import com.ztesoft.sgd.base.beans.BaseDto;
import com.ztesoft.sgd.base.jdbc.GenericJdbcDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmartcity.sgd.enums.SgdTable;
import com.ztesoft.zsmartcity.sgd.attachment.domain.Attachment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2016-6-16.
 * 2016-6-16
 * sgd
 * com.ztesoft.zsmartcity.sgd.upload.service
 */
public class AttachmentService {
    public List<Attachment> qryAllAttachment(DynamicDict dict) throws BaseAppException {
        Map<String ,String > param = new HashMap<String, String>();
        param.put("STATE","A");
        return GenericJdbcDAO.getInstance().loadEntities(SgdTable.BFM_ATTACHMENT_TABLE,Attachment.class,param);
    }
    public Attachment qrySingleAttachment(DynamicDict dict) throws BaseAppException{
        return  GenericJdbcDAO.getInstance().loadEntity(SgdTable.BFM_ATTACHMENT_TABLE,Attachment.class,dict.getLong("ID"));
    }
    public int addAttachment(DynamicDict dict) throws BaseAppException{
        GenericJdbcDAO.getInstance().genericInsert((BaseDto) BoHelper.boToDto(dict,Attachment.class));
        return 0;
    }
    public int addAttachment(Attachment dto) throws BaseAppException{
        GenericJdbcDAO.getInstance().genericInsert(dto);
        return 0;
    }
    public Attachment qrySingleAttachmentByTrueName(DynamicDict dict) throws BaseAppException{
        String trueName = dict.getString("ALIAS_NAME");

        return  GenericJdbcDAO.getInstance().loadEntity(SgdTable.BFM_ATTACHMENT_TABLE,Attachment.class, Collections.singletonMap("ALIAS_NAME",trueName));
    }
}

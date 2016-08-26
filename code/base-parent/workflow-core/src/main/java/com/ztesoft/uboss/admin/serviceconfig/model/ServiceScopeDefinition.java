package com.ztesoft.uboss.admin.serviceconfig.model;

import com.ztesoft.uboss.admin.serviceconfig.bean.BeanWrapper;
import com.ztesoft.uboss.admin.serviceconfig.model.dto.TfmServiceScopeDefineDto;
import com.ztesoft.uboss.admin.serviceconfig.model.type.ServiceScopeDefinitionState;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

import java.sql.Date;

/**
 * @author ZEN 2012-4-26
 */
public class ServiceScopeDefinition extends
        BeanWrapper<TfmServiceScopeDefineDto> {

    ZSmartLogger logger = ZSmartLogger.getLogger(ServiceScopeDefinition.class);

    private ServiceScopeDefinitionState state;

    public ServiceScopeDefinition(TfmServiceScopeDefineDto dto) {
        super(dto);
        validate();
    }

    // validate dto content and do interpret
    private void validate() {
        state = ServiceScopeDefinitionState.forName(dto.getState());
        if (state == null) {
            logger.error("unknow state:"
                    + dto.getState() + "for serviceScopeDefinition[" + dto
                    + "]");
        }
    }

    public Long getId() {
        return dto.getId();
    }

    public String getServiceName() {
        return dto.getServiceName();
    }

    public String getScopeName() {
        return dto.getScopeName();
    }

    public ServiceScopeDefinitionState getState() {
        return state;
    }

    public Date getStateDate() {
        return dto.getStateDate();
    }

    public Date getCreateDate() {
        return dto.getCreateDate();
    }

    public void setDto(TfmServiceScopeDefineDto dto) {
        super.setDto(dto);
        validate();
    }
}

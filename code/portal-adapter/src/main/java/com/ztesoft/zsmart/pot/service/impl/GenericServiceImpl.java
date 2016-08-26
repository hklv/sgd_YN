package com.ztesoft.zsmart.pot.service.impl;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.HttpCall;
import com.ztesoft.zsmart.core.service.ServiceDynamicDict;
import com.ztesoft.zsmart.pot.service.GenericService;
import org.springframework.stereotype.Service;

@Service("genericServ")
public class GenericServiceImpl implements GenericService {

    public ServiceDynamicDict callService(ServiceDynamicDict bo) throws BaseAppException {
        new HttpCall().httpCallWithDbRtEvn(bo);
        return bo;
    }

}
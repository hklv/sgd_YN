package com.ztesoft.zsmart.pot.service;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.ServiceDynamicDict;


public interface GenericService {
    ServiceDynamicDict callService(ServiceDynamicDict bo) throws BaseAppException;

}
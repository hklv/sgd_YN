/******************************************************************************
 * All rights are reserved. Reproduction or transmission in whole or in part， in
 * any form or by any means， electronic， mechanical or otherwise， is prohibited
 * without the prior written consent of the copyright owner
 ********************************************************************************/
package com.ztesoft.uboss.admin.serviceconfig.dao;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;

/**
 * @Filename: ICatDAO.java
 * @Author: cao.shilei
 * @CreateDate: 2007-10-30
 * @decription:目录管理dao
 */
public interface ICatDAO {
    List<DynamicDict> qryAllCatTree() throws BaseAppException;
}

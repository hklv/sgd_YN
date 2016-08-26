package com.ztesoft.zsmartcity.sgd;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.zsmartcity.sgd.dict.dao.DictDAO;
import com.ztesoft.zsmartcity.sgd.dict.dao.impl.DictDAOImpl;
import org.junit.Test;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-6-15 11:09
 */
public class SgdDaoFactoryTest {
    @Test
    public void testGetDaoImpl() throws Exception {
        DictDAO dictDAO = SgdDaoFactory.getDaoImpl(DictDAOImpl.class);
        System.out.println(dictDAO.getClass().getName());
    }
}

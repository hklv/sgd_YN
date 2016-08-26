package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;


public class SkinManager {

    private ISkinDAO skinDAO = null;

    private ISkinDAO getISkinDAO() throws BaseAppException {
        if (skinDAO == null) {
            skinDAO = SgdDaoFactory.getDaoImpl(ISkinDAO.class);
        }
        return skinDAO;
    }

    /**
     * @param
     * @return
     * @throws BaseAppException <br>
     * @description 获取系统缺省皮肤<br>
     */
    public String getDefaultSkin() throws BaseAppException {
        Session ses = null;
        DynamicDict dict = null;
        try {

            ses = SessionContext.currentSession();
            ses.beginTrans();// 启动事务
            dict = getISkinDAO().getSkinConfigDefault();

            ses.commitTrans();// 提交事务
        } catch (Throwable ex) {
            ExceptionHandler.logErrorInfo("SkinManager getDefaultSkin error.", ex);
        } finally {
            try {
                ses.releaseTrans();
            } catch (BaseAppException e) {
                e.printStackTrace();
            }// 释放事务
        }
        if (dict == null || dict.getString("SKIN_DIR") == null) {
            Common.SYS_DEFAULT_SKIN = "default";
            return "default";

        } else {
            Common.SYS_DEFAULT_SKIN = dict.getString("SKIN_DIR");
            return dict.getString("SKIN_DIR");
        }
    }

    /**
     * @param userId
     * @return
     * @throws BaseAppException <br>
     * @description 获取USER 皮肤<br>
     */
    public String getUserSkinDir(String userId) throws BaseAppException {
        Session ses = null;
        String dir = "default";
        try {

            ses = SessionContext.currentSession();
            ses.beginTrans();// 启动事务
            dir = getISkinDAO().getUserSkinDir(userId);

            ses.commitTrans();// 提交事务
        } catch (Throwable ex) {
            ExceptionHandler.logErrorInfo("SkinManager getUserSkinDir error.", ex);
        } finally {
            try {
                ses.releaseTrans();
            } catch (BaseAppException e) {
                e.printStackTrace();
            }// 释放事务
        }
        return dir;
    }

    /**
     * @param userId
     * @return
     * @throws BaseAppException <br>
     * @description 获取USER 皮肤<br>
     */
    public DynamicDict getUserSkinDict(String userId) throws BaseAppException {
        Session ses = null;
        DynamicDict dict = null;

        try {

            ses = SessionContext.currentSession();
            ses.beginTrans();// 启动事务
            dict = getISkinDAO().getUserSkinDict(userId);

            ses.commitTrans();// 提交事务
        } catch (Throwable ex) {
            ExceptionHandler.logErrorInfo("SkinManager getUserSkinDir error.", ex);
        } finally {
            try {
                ses.releaseTrans();
            } catch (BaseAppException e) {
                e.printStackTrace();
            }// 释放事务
        }
        if (dict == null) {
            dict = new DynamicDict();
            dict.set("SKIN_DIR", "default");
            dict.set("MAIN_URL", "");
        }
        return dict;
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 增加 皮肤配置<br>
     */
    public DynamicDict addSkinConfig(DynamicDict dict) throws BaseAppException {
        return null;
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 修改皮肤配置<br>
     */
    public DynamicDict modifySkinConfig(DynamicDict dict) throws BaseAppException {
        return null;
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 查询皮肤配置<br>
     */
    public List<DynamicDict> querySkinConfig(DynamicDict dict) throws BaseAppException {
        return getISkinDAO().selectSkinConfig(dict);
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 增加USER皮肤<br>
     */
    public int addUserSkin(DynamicDict dict) throws BaseAppException {
        dict.set("SET_DATE", DateUtil.GetDBDateTime());
        if (getISkinDAO().isSameUser(dict) > 0) {
            getISkinDAO().updateUserSkin(dict);
        } else {
            getISkinDAO().insertUserSkin(dict);
        }
        getISkinDAO().updateSkinConfigTimes(dict.getLong("SKIN_ID").intValue());
        return 0;
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 删除USER皮肤<br>
     */
    public int deleteUserSkin(DynamicDict dict) throws BaseAppException {
        return getISkinDAO().deleteUserSkin(dict);
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 修改USER皮肤<br>
     */
    public int modifyUserSkin(DynamicDict dict) throws BaseAppException {
        dict.set("SET_DATE", DateUtil.GetDBDateTime());
        getISkinDAO().updateUserSkin(dict);
        getISkinDAO().updateSkinConfigTimes(dict.getLong("SKIN_ID").intValue());
        return 1;
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 查询USER皮肤<br>
     */
    public DynamicDict queryUserSkin(DynamicDict dict) throws BaseAppException {
        return getISkinDAO().selectUserSkin(dict);
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 查询USER皮肤<br>
     */
    public List<DynamicDict> queryUserSkinList(DynamicDict dict) throws BaseAppException {
        return getISkinDAO().selectUserSkinList(dict);
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 查询分页USER皮肤<br>
     */
    public List<DynamicDict> queryUserSkinStep(DynamicDict dict) throws BaseAppException {
        return getISkinDAO().selectUserSkinStep(dict);
    }

    /**
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description 查询USER皮肤数量<br>
     */
    public int queryUserSkinCount(DynamicDict dict) throws BaseAppException {
        return getISkinDAO().selectUserSkinCount(dict);
    }

    /**
     * @param
     * @return
     * @throws BaseAppException <br>
     * @description 获取系统缺省皮肤<br>
     */
    public void selectDefaultSkin(DynamicDict dict) throws BaseAppException {
        DynamicDict rs = getISkinDAO().getSkinConfigDefault();
        if (rs != null) {
            dict.set("SKIN_ID", rs.getLong("SKIN_ID"));
            dict.set("SKIN_NAME", rs.getString("SKIN_NAME"));
            dict.set("SKIN_DIR", rs.getString("SKIN_DIR"));
            dict.set("SKIN_URL", rs.getString("SKIN_URL"));
            dict.set("USE_TIMES", rs.getLong("USE_TIMES"));

        }
    }

}

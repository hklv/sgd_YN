package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.ArrayList;


public interface ISkinDAO {

    /**
     * 新增皮肤配置<br>
     *
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int insertSkinConfig(DynamicDict dict) throws BaseAppException;

    /**
     * 修改皮肤配置<br>
     *
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int updateSkinConfig(DynamicDict dict) throws BaseAppException;

    /**
     * 修改皮肤配置使用次数<br>
     *
     * @param skinId
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int updateSkinConfigTimes(int skinId) throws BaseAppException;

    /**
     * 设置皮肤缺省值<br>
     *
     * @param skinId <br>
     * @description <br>
     */
    public int setSkinConfigDefaultValue(int skinId) throws BaseAppException;

    /**
     * 查询缺省皮肤<br>
     *
     * @description <br>
     */
    public DynamicDict getSkinConfigDefault() throws BaseAppException;

    /**
     * 设置皮肤状态<br>
     *
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int setSkinConfigState(DynamicDict dict) throws BaseAppException;

    /**
     * 查询皮肤配置<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public ArrayList<DynamicDict> selectSkinConfig(DynamicDict dict) throws BaseAppException;

    /**
     * 判断是否有重名<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public int isSameName(DynamicDict dict) throws BaseAppException;

    /**
     * 新增皮肤设置<br>
     *
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int insertUserSkin(DynamicDict dict) throws BaseAppException;

    /**
     * 修改皮肤设置<br>
     *
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int updateUserSkin(DynamicDict dict) throws BaseAppException;

    /**
     * 删除皮肤设置<br>
     *
     * @param dict
     * @return
     * @throws BaseAppException <br>
     * @description <br>
     */
    public int deleteUserSkin(DynamicDict dict) throws BaseAppException;

    /**
     * 查询用户皮肤<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public DynamicDict selectUserSkin(DynamicDict dict) throws BaseAppException;

    /**
     * 查询皮肤设置<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public ArrayList<DynamicDict> selectUserSkinList(DynamicDict dict) throws BaseAppException;

    /**
     * 分页查询用户皮肤<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public ArrayList<DynamicDict> selectUserSkinStep(DynamicDict dict) throws BaseAppException;

    /**
     * 查询用户皮肤数量<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public int selectUserSkinCount(DynamicDict dict) throws BaseAppException;

    /**
     * 查询用户使用皮肤<br>
     *
     * @param userId <br>
     * @description <br>
     */
    public String getUserSkinDir(String userId) throws BaseAppException;

    /**
     * 查询用户使用皮肤<br>
     *
     * @param userId <br>
     * @description <br>
     */
    public DynamicDict getUserSkinDict(String userId) throws BaseAppException;

    /**
     * 判断是否有重名<br>
     *
     * @param dict <br>
     * @description <br>
     */
    public int isSameUser(DynamicDict dict) throws BaseAppException;
}

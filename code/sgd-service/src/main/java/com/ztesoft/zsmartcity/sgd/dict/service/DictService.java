package com.ztesoft.zsmartcity.sgd.dict.service;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.sgd.base.jdbc.GenericJdbcDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmartcity.sgd.dict.dao.DictDAO;
import com.ztesoft.zsmartcity.sgd.dict.domain.Dict;
import com.ztesoft.zsmartcity.sgd.dict.domain.DictData;
import com.ztesoft.zsmartcity.sgd.enums.SgdTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典业务逻辑处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-17 14:17
 */
public class DictService {
    private DictDAO dao = null;


    public DictService() throws BaseAppException {
        if (dao == null) {
            dao = SgdDaoFactory.getDaoImpl(DictDAO.class);
        }
    }

    //查询所有字典
    public int qryDictList(DynamicDict dict) throws BaseAppException {

        //进行模糊查询操作设置
        if (!StringUtil.isEmpty(dict.getString("CODE"))) {
            dict.set("CODE", "%" + dict.getString("CODE") + "%");
        }
        DynamicDict pageObj = dict.getBO(ActionDomain.BO_FIELD_QUERY_PAGE);
        //区分是查询count还是查询
        if (pageObj != null && Boolean.TRUE.equals(pageObj.getBoolean(ActionDomain.BO_FIELD_QUERY_COUNT))) {
            dict.set("CNT", this.dao.qryDictCount(dict));
        } else {
            dict.set("z_d_r", this.dao.qryDictDataByCode(dict));
        }
        return 0;

    }

    /**
     * 根据id查询一条字典数据
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int qrySingleDicData(DynamicDict dict) throws BaseAppException {
        String id = dict.getString("ID");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("ID",id);
        paramMap.put("STATE","A");
        DictData dictData =  GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_DATA_TABLE, DictData.class, paramMap);
        dict.set("DICTIONARY_DATA",dictData);
        return 0;
    }
    /**
     * 新增一个字典
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int addDict(DynamicDict dict) throws BaseAppException {

        String code = dict.getString("CODE");
        Map<String, String> map = new HashMap<String, String>();
        map.put("CODE", code);
        map.put("STATE", "A");
        List<Dict> dicts = GenericJdbcDAO.getInstance().loadEntities(SgdTable.DICT_TABLE, Dict.class, map);
        //Assert.isTrue(dicts == null || dicts.size() <= 0, "编码重复。");
        if (!(dicts == null || dicts.size() <= 0)) {
            //业务型异常
            ExceptionHandler.publish("DICTSERVICE_EXSITS_ERROR", "编码重复。", 0);
            return 0;
        }
        Dict dictDto = (Dict) BoHelper.boToDto(dict, Dict.class);
        dictDto.setState("A");
        GenericJdbcDAO.getInstance().genericInsert(dictDto);
        dict.set("ID", dictDto.getId());
        return 0;

    }

    /**
     * 删除一个字典
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int deleteDict(DynamicDict dict) throws BaseAppException {

        Long id = dict.getLong("ID");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("DICTIONARY_ID", id);
        map.put("STATE", "A");
        List<DictData> dictDatas = GenericJdbcDAO.getInstance().loadEntities(SgdTable.DICT_DATA_TABLE, DictData.class, map);
        //数据字典有数据就删除，否则不删除
        //Assert.isTrue(org.springframework.util.CollectionUtils.isEmpty(dictDatas), "该类型相关的数据字典有数据，不可删除。");
        if (!org.springframework.util.CollectionUtils.isEmpty(dictDatas)) {
            //业务型异常
            ExceptionHandler.publish("DICTSERVICE_EXSITS_ERROR", "该类型相关的数据字典有数据，不可删除。", 0);
            return 0;
        }
        Dict dictDto = (Dict) BoHelper.boToDto(dict, Dict.class);
        dictDto = GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_TABLE, Dict.class, dictDto.getId());
        dictDto.setState("X");
        GenericJdbcDAO.getInstance().genericUpdate(dictDto);
        return 0;


    }

    /**
     * 修改一个字典
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int updateDict(DynamicDict dict) throws BaseAppException {
        Long id = dict.getLong("ID");
        Dict newDict = GenericJdbcDAO.getInstance().genericLoad(Dict.class, id);
        Dict paramDict = (Dict) BoHelper.boToDto(dict, Dict.class);
        newDict.setState("A");
        newDict.setCode(paramDict.getCode());
        newDict.setComments(paramDict.getComments());
        newDict.setName(paramDict.getName());
        GenericJdbcDAO.getInstance().genericUpdate(newDict);
        dict.set("RESULT", "0");
        return 0;
    }

    /**
     * 查询数据字典数据，返回list
     *
     * @param dict CODE  ,DICTIONARY_ID
     * @return
     * @throws BaseAppException
     */
    public List<DictData> qryDictData(DynamicDict dict) throws BaseAppException {
        Long id = dict.getLong("DICTIONARY_ID");
        if (id == null || id <= 0) {
            String code = dict.getString("CODE");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("CODE", code);
            param.put("StATE", "A");
            Dict d = GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_TABLE, Dict.class, param);
            Map<String, Object> param2 = new HashMap<String, Object>();
            param2.put("DICTIONARY_ID", d.getId());
            param2.put("STATE", "A");
            return GenericJdbcDAO.getInstance().loadEntities(SgdTable.DICT_DATA_TABLE, DictData.class, param2);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DICTIONARY_ID", id);
        param.put("STATE", "A");
        return GenericJdbcDAO.getInstance().loadEntities(SgdTable.DICT_DATA_TABLE, DictData.class, param);
    }


    /**
     * 新增一条数据字典数据
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int addDictData(DynamicDict dict) throws BaseAppException {
        DictData newData = (DictData) BoHelper.boToDto(dict, DictData.class);
        newData.setState("A");
        newData.setIsLocked("N");
        GenericJdbcDAO.getInstance().genericInsert(newData);
        dict.set("ID", newData.getId());
        return 0;
    }

    /**
     * 修改一条数据字典
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int updateDictData(DynamicDict dict) throws BaseAppException {

        DictData dto = (DictData) BoHelper.boToDto(dict, DictData.class);
        DictData param = GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_DATA_TABLE, DictData.class, dto.getId());
        dto.setCreateDate(param.getCreateDate());
        dto.setCreateUser(param.getCreateUser());
        dto.setState("A");
        dto.setIsLocked("N");
        GenericJdbcDAO.getInstance().genericUpdate(dto);
        return 0;


    }

    /**
     * 逻辑删除一条数据字典
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int delDictData(DynamicDict dict) throws BaseAppException {

        DictData paramData = (DictData) BoHelper.boToDto(dict, DictData.class);
        paramData = GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_DATA_TABLE, DictData.class, paramData.getId());
        paramData.setState("X");
        GenericJdbcDAO.getInstance().genericUpdate(paramData);
        return 0;


    }


}

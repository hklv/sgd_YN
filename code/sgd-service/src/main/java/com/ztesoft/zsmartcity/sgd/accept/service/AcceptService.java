package com.ztesoft.zsmartcity.sgd.accept.service;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.sgd.base.id.SnowflakeIdGenerator;
import com.ztesoft.sgd.base.jdbc.GenericJdbcDAO;
import com.ztesoft.uboss.bpm.client.BpmClient;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmartcity.sgd.accept.dao.AcceptDAO;
import com.ztesoft.zsmartcity.sgd.accept.domain.*;
import com.ztesoft.zsmartcity.sgd.accept.domain.System;
import com.ztesoft.zsmartcity.sgd.attachment.domain.Attachment;
import com.ztesoft.zsmartcity.sgd.dict.domain.DictData;
import com.ztesoft.zsmartcity.sgd.enums.SgdTable;
import com.ztesoft.zsmartcity.sgd.items.domain.ItemMaterials;
import com.ztesoft.zsmartcity.sgd.items.domain.Items;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZhangLu on 2016/6/16.
 */
public class AcceptService {
    private static ZSmartLogger logger = ZSmartLogger.getLogger(AcceptService.class);
    private AcceptDAO acceptDAO = null;

    public AcceptService() throws BaseAppException {
        if (acceptDAO == null) {
            acceptDAO = SgdDaoFactory.getDaoImpl(AcceptDAO.class);
        }
    }
    public int qryPrivateNetWork(DynamicDict dict) throws BaseAppException {
        logger.debug("call qryPrivateNetWork service ...........");
        List<System> systemsList = GenericJdbcDAO.getInstance().loadEntities(SgdTable.SYSTEM_TABLE, System.class, Collections.singletonMap("STATE", "A"));
        List<Menu> menuList = new LinkedList<>();
        Menu menu = null;
        int i= 1;
        for (System system : systemsList) {
            menu = new Menu();
            menu.setParentId(system.getOrgId());
            menu.setState("A");
            menu.setPartyId(system.getId());
            menu.setPartyName(system.getName());
            menu.setUrl(system.getUrl());
            menu.setSeq(i++);
            menu.setSpId(0);
            menu.setType("1");
            menu.setPortalId(1);
            menu.setMenuType("I");
            menuList.add(menu);
        }
        dict.set("MENU_LIST",BoHelper.listDtoToListBO(menuList));
        return 0;
    }
    public int qryItemTree(DynamicDict dict) throws BaseAppException {
        logger.debug("call qryItemTree service ...........");
        List<Items> itemList = GenericJdbcDAO.getInstance().loadEntities(SgdTable.ITEMS_TABLE, Items.class, Collections.singletonMap("STATE", "A"));
        List<Org> itemOrgList = new ArrayList<Org>();
        for (Items item : itemList) {
            Org org = new Org();
            org.setOrgId("item-" + item.getId());//前端生成目录树,加前缀用以和部门id区分
            org.setOrgCode(item.getItemCode());
            org.setOrgName(item.getName());
            org.setParentOrgId(item.getOrgId());
            org.setOrgType("I");
            org.setProcDefId(item.getProcDefId());
            itemOrgList.add(org);
        }
        List<Org> orgSourceList = GenericJdbcDAO.getInstance().loadEntities(SgdTable.ORG_TABLE, Org.class, Collections.singletonMap("STATE", "A"));
        orgSourceList.addAll(itemOrgList);
        dict.set("z_d_r", BoHelper.listDtoToListBO(orgSourceList));

        return 0;
    }

    public int acceptItems(DynamicDict dict) throws BaseAppException {
        logger.debug("call acceptItems service ..........."+dict.toString());

        DynamicDict applyDynamicDict = dict.getBOByName("APPLY");
        dict.set("PROC_DEF_ID",applyDynamicDict.get("PROC_DEF_ID"));
        Apply apply = (Apply) BoHelper.boToDto(applyDynamicDict, Apply.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ITEM_ID", apply.getItemId());
        paramMap.put("ID_TYPE_ID", apply.getIdTypeId());
        paramMap.put("ID_NO", apply.getIdNo());
        paramMap.put("STATE", "A");
        List<Apply> applyList = GenericJdbcDAO.getInstance().loadEntities(SgdTable.APPLY_TABLE, Apply.class, paramMap);
        if (!CollectionUtils.isEmpty(applyList)) {
//            dict.set("z_d_r", "1");
//            dict.set("z_d_m", "该事项已经申请，请勿重复申请");
//            return -1;
            throw new BaseAppException("S-ACT-00203", "该事项已经申请，请勿重复申请", BaseAppException.BUSS_ERROR);
        }
        //受理
        acceptItem(apply,dict);

        return 0;
    }
    private void acceptItem(Apply apply,DynamicDict dict)throws BaseAppException {

        Session ses = null;
        try {
            logger.debug("begin  call acceptItem service!");
            ses = SessionContext.currentSession();
            ses.beginTrans();// 启动事务

            BpmClient bpmClient = new BpmClient();
            bpmClient.startProcess(dict);
            if(dict.get("PROCESS_HOLDER_NO") == null){
                throw new BaseAppException("流程启动失败");
            }
            apply.setState("A");
            apply.setSerialNum(getSerialNum());
            apply.setHolderNo(dict.getString("PROCESS_HOLDER_NO"));
            GenericJdbcDAO.getInstance().genericInsert(apply);//保存受理人信息

            saveAgent(dict, apply.getId());//保存代理人信息

            saveMaterials(dict, apply.getId());//保存材料
            logger.debug("end  call acceptItem service!");
            ses.commitTrans();// 提交事务
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (ses != null) {
                ses.releaseTrans();// 释放事务
            }
        }




    }
    /**
     * 查询申请单详情=申请人信息+代理人信息+要件材料信息
     *
     * @param dict
     * @return
     */
    public int queryApplyInfo(DynamicDict dict) throws BaseAppException {
        logger.debug("call queryApplyInfo service ...........");
       String holderNo = dict.getString("HOLDER_NO");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("HOLDER_NO", holderNo);
        map.put("STATE", "A");
        Apply apply = GenericJdbcDAO.getInstance().loadEntity(SgdTable.APPLY_TABLE, Apply.class, map);
        DictData dictData = getDicData(apply.getIdTypeId());
        apply.setIdTypeName(dictData.getValueName());
        if (null == apply) {
            dict.set("z_d_r", "-1");
            dict.set("z_d_m","无此申请单");
            return -1;
        }
        Agent agent = getAgentInfo(apply.getId());
        List<DynamicDict> dynamicDicts = BoHelper.listDtoToListBO(getMaterialsById(apply.getItemId(),apply.getId()));
        if(null == agent){
            dict.set("z_d_r", "-2");
            dict.set("z_d_m","无代理人");
        }
        if(CollectionUtils.isEmpty(dynamicDicts)){
            dict.set("z_d_r", "-3");
            dict.set("z_d_m","无申请材料");
        }
        if(null!=apply && null != agent && !CollectionUtils.isEmpty(dynamicDicts)){
            dict.set("z_d_r", "0");
        }
        dict.set("APPLY", BoHelper.dtoToBO(apply, null));
        dict.set("AGENT", null == agent?"": BoHelper.dtoToBO(agent, null));
        dict.set("ITEM_MATERIALS", CollectionUtils.isEmpty(dynamicDicts)?"":dynamicDicts);
        return 0;
    }

    private List<ItemMaterials> getMaterialsById(long itemId,long applyId) throws BaseAppException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ITEM_ID", itemId);
        map.put("STATE", "A");
        List<ItemMaterials> itemMaterialsList = GenericJdbcDAO.getInstance().loadEntities(SgdTable.ITEM_MATERIALS_TABLE, ItemMaterials.class, map);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("APPLY_ID", applyId);
        paramMap.put("STATE", "A");
        List<ApplyMaterials> applyMaterialsList = GenericJdbcDAO.getInstance().loadEntities(SgdTable.APPLY_MATERIALS_TABLE,ApplyMaterials.class,paramMap);
        for(ApplyMaterials applyMaterials : applyMaterialsList){
            Integer attchId = applyMaterials.getAttachId();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("ID", attchId);
            param.put("STATE", "A");
            Attachment attachment = GenericJdbcDAO.getInstance().loadEntity(SgdTable.BFM_ATTACHMENT_TABLE,Attachment.class,param);
            applyMaterials.setAliasName(attachment.getAliasName());
        }
        for (ItemMaterials itemMaterials : itemMaterialsList) {
            for(ApplyMaterials applyMaterials : applyMaterialsList){
                if(itemMaterials.getId().equals(applyMaterials.getMaterialsId())){
                    itemMaterials.setAliasName(applyMaterials.getAliasName());
                }
            }
        }
        return itemMaterialsList;
    }

    private Agent getAgentInfo(Long applyId) throws BaseAppException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("APPLY_ID", applyId);
        map.put("STATE", "A");
        Agent agent = GenericJdbcDAO.getInstance().loadEntity(SgdTable.AGENT, Agent.class, map);
        if(null == agent ){
            return null;
        }
        DictData dictData = getDicData(agent.getIdTypeId());
        agent.setIdTypeName(dictData.getValueName());
        return agent;
    }

    //保存代理人
    private void saveAgent(DynamicDict dict, Long applyId) throws BaseAppException {
        DynamicDict agentDynamicDict;
        try {
            agentDynamicDict = dict.getBOByName("AGENT");
        }catch (BaseAppException e){
            agentDynamicDict = null;
        }
        if (null == agentDynamicDict) {
            return;
        }
        Agent agent = (Agent) BoHelper.boToDto(agentDynamicDict, Agent.class);
        agent.setApplyId(applyId);
        agent.setState("A");
        GenericJdbcDAO.getInstance().genericInsert(agent);
    }

    //材料
    private void saveMaterials(DynamicDict dict, Long applyId) throws BaseAppException {
        DynamicDict materialsDynamicDict;
        try {
            materialsDynamicDict = dict.getBOByName("APPLY_MATERIALS");
        }catch (BaseAppException e){
            materialsDynamicDict = null;
        }
        if (null == materialsDynamicDict) {
            return;
        }
        List<Object> applyMaterialsList = BoHelper.boToListDto(dict, "APPLY_MATERIALS", ApplyMaterials.class);
        List<ApplyMaterials> applyMaterialsesList = new ArrayList<ApplyMaterials>();
        for (Object object : applyMaterialsList) {
            ApplyMaterials applyMaterials = (ApplyMaterials) object;
            applyMaterials.setApplyId(applyId);
            applyMaterials.setState("A");
            applyMaterialsesList.add(applyMaterials);
            GenericJdbcDAO.getInstance().genericInsert(applyMaterials);
        }
        //GenericJdbcDAO.getInstance().genericBatchInsert(applyMaterialsesList);
    }

    /**
     * 业务流水号=年月日+16位随机数
     */
    private String getSerialNum() throws BaseAppException {

        return new SimpleDateFormat("yyyymmdd").format(new Date()) + new SnowflakeIdGenerator().nextId("");
    }

    /**
     * 获取字典数据
     * @param id
     * @return
     * @throws BaseAppException
     */
    private DictData getDicData(long id) throws BaseAppException{
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("ID",id);
        paramMap.put("STATE","A");
        return GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_DATA_TABLE, DictData.class, paramMap);
    }
}

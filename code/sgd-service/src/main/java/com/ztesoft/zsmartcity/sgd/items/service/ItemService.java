package com.ztesoft.zsmartcity.sgd.items.service;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.sgd.base.id.MySQLIDGenerator;
import com.ztesoft.sgd.base.jdbc.GenericJdbcDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmartcity.sgd.attachment.domain.Attachment;
import com.ztesoft.zsmartcity.sgd.attachment.service.AttachmentService;
import com.ztesoft.zsmartcity.sgd.dict.domain.DictData;
import com.ztesoft.zsmartcity.sgd.enums.SgdTable;
import com.ztesoft.zsmartcity.sgd.items.dao.ItemDAO;
import com.ztesoft.zsmartcity.sgd.items.domain.ItemMaterials;
import com.ztesoft.zsmartcity.sgd.items.domain.Items;
import com.ztesoft.zsmartcity.sgd.items.domain.Laws;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by liuhao 2016-6-17.
 * 2016-6-17
 * sgd
 */
public class ItemService {

    private ItemDAO dao = null;

    public ItemService() throws BaseAppException {
        if (dao == null) {
            dao = SgdDaoFactory.getDaoImpl(ItemDAO.class);
        }
    }


    /**
     * 根据条件查询事项
     *
     * @param dict(NAME ITEM_CODE LEGAL_PERIOD CREATE_DATE_BEGIN CREATE_DATE_END)
     * @return
     * @throws BaseAppException
     */
    public int qryItemsByParams(DynamicDict dict) throws BaseAppException {
        //进行模糊查询操作设置
        if (!StringUtil.isEmpty(dict.getString("NAME"))) {
            dict.set("NAME", "%" + dict.getString("NAME") + "%");
        }
        if (!StringUtil.isEmpty(dict.getString("ITEM_CODE"))) {
            dict.set("ITEM_CODE", "%" + dict.getString("ITEM_CODE") + "%");
        }
        DynamicDict pageObj = dict.getBO(ActionDomain.BO_FIELD_QUERY_PAGE);
        //区分是查询count还是查询
        if (pageObj != null && Boolean.TRUE.equals(pageObj.getBoolean(ActionDomain.BO_FIELD_QUERY_COUNT))) {
            dict.set("CNT", dao.qryItemsCountByParams(dict));
        } else {
            //进行子集合数据设置
            List<DynamicDict> list = dao.qryItemsByParams(dict);
            List<DynamicDict> result = new ArrayList<DynamicDict>();

            for (int i = 0; i < list.size(); i++) {
                //设置数据字典类型
                DynamicDict d = list.get(i);
                Long spTypeId = d.getLong("SP_TYPE_ID");
                DictData data = GenericJdbcDAO.getInstance().loadEntity(SgdTable.DICT_DATA_TABLE, DictData.class, spTypeId);
                if (data != null) {
                    d.set("SP_TYPE_NAME", data.getValueName());
                }
                //设置要件材料库（多个List）
                Long pkId = d.getLong("ID");
                List<ItemMaterials> ims = GenericJdbcDAO.getInstance().loadEntities(SgdTable.ITEM_MATERIALS_TABLE, ItemMaterials.class, Collections.singletonMap("ITEM_ID", pkId));
                d.set("ITEM_MATERIALS", BoHelper.listDtoToListBO(ims));
                result.add(d);
            }
            dict.set("z_d_r", result);

        }
        return 0;


    }

    /**
     * 新增一个事项
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int addItem(DynamicDict dict) throws BaseAppException {

        Items item = (Items) BoHelper.boToDto(dict, Items.class);
        //state
        item.setState("A");
        GenericJdbcDAO.getInstance().genericInsert(item);
        Long itemPk = item.getId();
        List<DynamicDict> dicts = dict.getList("FILE_LIST");
        if (org.springframework.util.CollectionUtils.isEmpty(dicts)) {
            return 0;
        }
        for (int i = 0; i < dicts.size(); i++) {
            ItemMaterials im = new ItemMaterials();
            im.setName(dicts.get(i).getString("NAME"));
            im.setFormatId(dicts.get(i).getLong("FORMAT_ID"));
            im.setRequirement(dicts.get(i).getString("REQUIREMENT"));
            im.setNum(dicts.get(i).getLong("NUM"));
            im.setComments(dicts.get(i).getString("COMMENTS"));
            im.setId(new MySQLIDGenerator().nextId("ITEM_MATERIALS_SEQ"));
            im.setItemId(itemPk);
            //state
            im.setState("A");
            GenericJdbcDAO.getInstance().genericInsert(im);
        }
        return 0;

    }

    /**
     * 修改一个事项
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int updateItem(DynamicDict dict) throws BaseAppException {

        Items item = (Items) BoHelper.boToDto(dict, Items.class);
        //修改状态
        item.setState("A");
        //excuteUpdate
        GenericJdbcDAO.getInstance().genericUpdate(item);
        //先删掉主表数据
        String sql = " delete from sgd_item_materials where 1=1 and ITEM_ID=" + item.getId();
        GenericJdbcDAO.getInstance().executeUpdate(sql, new ParamMap());
        //再新增数据
        List<DynamicDict> dicts = dict.getList("FILE_LIST");
        if (org.springframework.util.CollectionUtils.isEmpty(dicts)) {
            return 0;
        }
        for (int i = 0; i < dicts.size(); i++) {
            ItemMaterials im = new ItemMaterials();
            im.setName(dicts.get(i).getString("NAME"));
            im.setFormatId(dicts.get(i).getLong("FORMAT_ID"));
            im.setRequirement(dicts.get(i).getString("REQUIREMENT"));
            im.setNum(dicts.get(i).getLong("NUM"));
            im.setComments(dicts.get(i).getString("COMMENTS"));
            im.setItemId(item.getId());
            //state
            im.setState("A");
            GenericJdbcDAO.getInstance().genericInsert(im);
        }
        return 0;

    }

    /**
     * 删除一个事项
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int deleteItem(DynamicDict dict) throws BaseAppException {

        Items item = (Items) BoHelper.boToDto(dict, Items.class);
        //查询原有对象
        Items newItem = GenericJdbcDAO.getInstance().genericLoad(Items.class, item.getId());
        //change state
        newItem.setState("X");
        //excuteUpdate
        GenericJdbcDAO.getInstance().genericUpdate(newItem);
        //删除要见材料库的数据
        String sql = " delete from sgd_item_materials where 1=1 and ITEM_ID=" + item.getId();
        GenericJdbcDAO.getInstance().executeUpdate(sql, new ParamMap());
        return 0;

    }

    /**
     * itemMaterials表增删改查
     * 查询所有要件材料库
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public List<ItemMaterials> qryAllItemsMaterials(DynamicDict dict) throws BaseAppException {
        return GenericJdbcDAO.getInstance().loadEntities(SgdTable.ITEM_MATERIALS_TABLE, ItemMaterials.class, Collections.singletonMap("STATE", "A"));
    }

    /**
     * 根据itemId查询对应的要见材料列表
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public List<ItemMaterials> qryMaterialsByItemId(DynamicDict dict) throws BaseAppException {
        Long id = dict.getLong("ITEM_ID");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ITEM_ID", id);
        map.put("STATE", "A");
        return GenericJdbcDAO.getInstance().loadEntities(SgdTable.ITEM_MATERIALS_TABLE, ItemMaterials.class, map);
    }

    /**
     * 新增一个要件材料库
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int addItemMaterials(DynamicDict dict) throws BaseAppException {

        ItemMaterials item = (ItemMaterials) BoHelper.boToDto(dict, ItemMaterials.class);
        //state
        item.setState("A");
        GenericJdbcDAO.getInstance().genericInsert(item);
        return 0;

    }

    /**
     * 修改一个要件材料库
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int updateItemMaterials(DynamicDict dict) throws BaseAppException {

        ItemMaterials item = (ItemMaterials) BoHelper.boToDto(dict, ItemMaterials.class);
        //修改状态
        item.setState("A");
        //excuteUpdate
        GenericJdbcDAO.getInstance().genericUpdate(item);
        return 0;

    }

    /**
     * 逻辑删除一个要件材料库
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int deleteItemMaterials(DynamicDict dict) throws BaseAppException {
        ItemMaterials item = (ItemMaterials) BoHelper.boToDto(dict, ItemMaterials.class);
        //查询原有对象
        ItemMaterials newItem = GenericJdbcDAO.getInstance().genericLoad(ItemMaterials.class, item.getId());
        //change state
        newItem.setState("X");
        //excuteUpdate
        GenericJdbcDAO.getInstance().genericUpdate(newItem);
        return 0;
    }

    /**
     * 法律法规库（通用增删改查）
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */

    public List<Laws> qryAllLaws(DynamicDict dict) throws BaseAppException {
        //模糊参数设置
        if (StringUtil.isNotEmpty(dict.getString("NAME"))) {
            dict.set("NAME", "%" + dict.getString("NAME") + "%");
        }
        if (StringUtil.isNotEmpty(dict.getString("FILE_NO"))) {
            dict.set("FILE_NO", "%" + dict.getString("FILE_NO") + "%");
        }
        //是否查询count
        DynamicDict pageObj = dict.getBO(ActionDomain.BO_FIELD_QUERY_PAGE);
        if (pageObj != null && Boolean.TRUE.equals(pageObj.getBoolean(ActionDomain.BO_FIELD_QUERY_COUNT))) {
            dict.set("CNT", dao.qryLawsCount(dict));
        } else {
            List<DynamicDict> laws = dao.qryLaws(dict);
            if (laws != null && laws.size() > 0) {
                AttachmentService serv = new AttachmentService();
                for (int i = 0; i < laws.size(); i++) {
                    DynamicDict d = laws.get(i);
                    List<Attachment> attachList = new ArrayList<Attachment>();
                    String file = d.getString("FILE");
                    if (StringUtil.isNotEmpty(file)) {
                        String[] fileIdList = file.split(",");
                        for (int j = 0; j < fileIdList.length; j++) {
                            if (StringUtil.isEmpty(fileIdList[j])) {
                                continue;
                            }
                            int id = Integer.parseInt(fileIdList[j]);
                            DynamicDict param = new DynamicDict();
                            param.set("ID", id);
                            Attachment a = serv.qrySingleAttachment(param);
                            attachList.add(a);
                        }
                        laws.get(i).set("FILE_LIST", BoHelper.listDtoToListBO(attachList));
                    }

                }
            }
            dict.set("z_d_r", laws);
        }

        return null;
    }

    /**
     * 新增一个法律法规库or 编辑修改一个法律法规库
     * 判断条件 参数是否有主键ID。
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int addOrUpdateLaws(DynamicDict dict) throws BaseAppException {

        Laws item = (Laws) BoHelper.boToDto(dict, Laws.class);
        //state
        item.setState("A");
        List<String> fileIdList = dict.getList("FILE_ID");
        String file = "";
        if (fileIdList != null && fileIdList.size() > 0) {
            file = StringUtils.join(fileIdList, ",");
        }
        item.setFile(file);
        if (item.getId() != null && item.getId() > 0) {
            GenericJdbcDAO.getInstance().genericUpdate(item);
        } else {
            GenericJdbcDAO.getInstance().genericInsert(item);
        }
        return 0;
    }


    /**
     * delete single laws
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int deleteLaws(DynamicDict dict) throws BaseAppException {

        Laws item = (Laws) BoHelper.boToDto(dict, Laws.class);
        //查询原有对象
        Laws newItem = GenericJdbcDAO.getInstance().genericLoad(Laws.class, item.getId());
        //change state
        newItem.setState("X");
        //excuteUpdate
        GenericJdbcDAO.getInstance().genericUpdate(newItem);
        return 0;

    }

}

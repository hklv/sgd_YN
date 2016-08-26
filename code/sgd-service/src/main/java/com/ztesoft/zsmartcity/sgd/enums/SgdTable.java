package com.ztesoft.zsmartcity.sgd.enums;

import com.ztesoft.sgd.base.enums.DbTable;

/**
 * 表的描述.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-17 13:56
 */
public enum SgdTable implements DbTable {
    DICT("t_dict", "id", "s_dict"),
    LAWS_TABLE("SGD_LAWS", "id", "LAWS_SEQ"),
    ITEMS_TABLE("sgd_items", "id", "ITEM_SEQ"),
    ITEM_MATERIALS_TABLE("SGD_ITEM_MATERIALS", "ID", "SGD_ITEM_MATERIALS_SEQ"),
    DICT_TABLE("bfm_dictionary", "id", "DICT_SEQ"),
    DICT_DATA_TABLE("bfm_dictionary_data", "id", "DICT_DATA_SEQ"),
    BFM_ATTACHMENT_TABLE("bfm_attachment","id","BFM_ATTACHMENT_SEQ"),
    APPLY_TABLE("SGD_APPLY","id","APPLY_SEQ"),
    ORG_TABLE("BFM_ORG","ORG_ID","ORG_SEQ"),
    AGENT("SGD_AGENT","ID","AGENT_SEQ"),
    SYSTEM_TABLE("SGD_SYSTEM","ID","SYSTEM_SEQ"),
    APPLY_MATERIALS_TABLE("SGD_APPLY_MATERIALS","ID","APPLY_MATERIALS_SEQ");
    private String tableName;
    private String idColumnName;
    private String sequenceName;

    SgdTable(String tableName, String idColumnName, String sequenceName) {
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.sequenceName = sequenceName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getIdColumnName() {
        return idColumnName;
    }

    @Override
    public String getSequenceName() {
        return sequenceName;
    }
}

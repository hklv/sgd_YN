package com.ztesoft.uboss.form.model;

/**
 * 环节表单的Dto
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/7/14
 */
public class TaskFormTestDto {

    private long id;
    private int isMatchNationalStandard;
    private int isApproval;
    private String holderNo;

    public String getHolderNo() {
        return holderNo;
    }

    public void setHolderNo(String holderNo) {
        this.holderNo = holderNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsMatchNationalStandard() {
        return isMatchNationalStandard;
    }

    public void setIsMatchNationalStandard(int isMatchNationalStandard) {
        this.isMatchNationalStandard = isMatchNationalStandard;
    }

    public int getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(int isApproval) {
        this.isApproval = isApproval;
    }
}

package com.ztesoft.uboss.admin.serviceconfig.model.dto;

import com.ztesoft.zsmart.core.jdbc.AbstractDto;
import com.ztesoft.zsmart.core.utils.EqualsUtil;
import com.ztesoft.zsmart.core.utils.HashCodeUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;

import java.sql.Date;


/**
 * @author ZEN 2012-4-26
 */
@SuppressWarnings("serial")
public class TfmServiceScopeDefineDto extends AbstractDto {
    private Long id;
    private String serviceName;
    private String name;
    private String state;
    private Date stateDate;
    private Date createDate;

    public TfmServiceScopeDefineDto() {
    }

    public TfmServiceScopeDefineDto(Long id, String serviceType, String name,
                                    String state, Date stateDate, Date crateDate) {
        this.id = id;
        this.serviceName = serviceType;
        this.name = name;
        this.state = state;
        this.stateDate = stateDate;
        this.createDate = crateDate;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + HashCodeUtil.hashCode(id);
        result = 37 * result + HashCodeUtil.hashCode(serviceName);
        result = 37 * result + HashCodeUtil.hashCode(name);
        result = 37 * result + HashCodeUtil.hashCode(state);
        result = 37 * result + HashCodeUtil.hashCode(stateDate);
        result = 37 * result + HashCodeUtil.hashCode(createDate);
        return result;
    }

    public boolean equals(Object other) {
        int retVal = EqualsUtil.preEquals(this, other);
        if (retVal != 2) {
            return (retVal == 1);
        }
        TfmServiceScopeDefineDto that = (TfmServiceScopeDefineDto) other;
        if (!(EqualsUtil.equals(that.id, this.id))) {
            return false;
        }
        if (!(EqualsUtil.equals(that.serviceName, this.serviceName))) {
            return false;
        }
        if (!(EqualsUtil.equals(that.name, this.name))) {
            return false;
        }
        if (!(EqualsUtil.equals(that.state, this.state))) {
            return false;
        }
        if (!(EqualsUtil.equals(that.stateDate, this.stateDate))) {
            return false;
        }
        if (!(EqualsUtil.equals(that.createDate, this.createDate))) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer returnString = new StringBuffer();
        returnString.append("TfmServiceScopeDefineDto Value List :");
        returnString.append("id: [").append(StringUtil.toString(id))
                .append("]");
        returnString.append("serviceType: [")
                .append(StringUtil.toString(serviceName)).append("]");
        returnString.append("name: [").append(StringUtil.toString(name))
                .append("]");
        returnString.append("state: [").append(StringUtil.toString(state))
                .append("]");
        returnString.append("stateDate: [")
                .append(StringUtil.toString(stateDate)).append("]");
        returnString.append("crateDate: [")
                .append(StringUtil.toString(createDate)).append("]");
        return returnString.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceType) {
        this.serviceName = serviceType;
    }

    public String getScopeName() {
        return name;
    }

    public void setScopeName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date crateDate) {
        this.createDate = crateDate;
    }
}

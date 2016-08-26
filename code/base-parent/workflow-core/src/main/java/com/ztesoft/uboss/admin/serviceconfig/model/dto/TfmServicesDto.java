package com.ztesoft.uboss.admin.serviceconfig.model.dto;

import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import org.dom4j.Element;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Administrator
 */
public class TfmServicesDto {

    protected String serviceName;// 服务名称

    protected String envId;// 环境编码

    protected String envDesc;// 环境说明

    protected String envName;// 使用的server配置名


    protected String envType;// 环境类型

    protected String version;// 版本号


    protected String serviceDesc;// 服务说明

    protected String serviceType;// 服务类型

    protected String definition;// 服务定义

    protected String state;// 状态


    protected String cacheFlag;// 缓存标志

    protected String modifier;// 最后修改人

    protected Date modifyDate;// 最后修改人

    protected String moduleName;// 模块名称

    protected String methodDef;//方法定义


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEnvId() {
        return envId;
    }

    public void setEnvId(String envId) {
        this.envId = envId;
    }

    public String getEnvDesc() {
        return envDesc;
    }

    public void setEnvDesc(String envDesc) {
        this.envDesc = envDesc;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCacheFlag() {
        return cacheFlag;
    }

    public void setCacheFlag(String cacheFlag) {
        this.cacheFlag = cacheFlag;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMethodDef() {
        return methodDef;
    }

    public void setMethodDef(String methodDef) {
        this.methodDef = methodDef;
    }

    /**
     * @param parent 父节点
     * @return 生成的删除服务节点;
     */
    public Element toServDelElement(Element parent) {
        AssertUtil.isNotEmpty(serviceName, "service_name is empty");
        return XMLDom4jUtils.appendChild(parent, "del_service", serviceName);
    }

    public void toXml(Element e) {
        if (cacheFlag != null)
            XMLDom4jUtils.appendChild(e, "cache_flag", cacheFlag);
        if (definition != null)
            XMLDom4jUtils.appendChild(e, "definition", definition);
        if (envDesc != null)
            XMLDom4jUtils.appendChild(e, "env_desc", envDesc);
        if (envId != null)
            XMLDom4jUtils.appendChild(e, "env_id", envId);
        if (envName != null)
            XMLDom4jUtils.appendChild(e, "env_name", envName);
        if (envType != null)
            XMLDom4jUtils.appendChild(e, "env_type", envType);
        if (serviceDesc != null)
            XMLDom4jUtils.appendChild(e, "service_desc", serviceDesc);
        if (serviceName != null)
            XMLDom4jUtils.appendChild(e, "service_name", serviceName);
        if (serviceType != null)
            XMLDom4jUtils.appendChild(e, "service_type", serviceType);
        if (moduleName != null)
            XMLDom4jUtils.appendChild(e, "module_name", moduleName);
        if (state != null)
            XMLDom4jUtils.appendChild(e, "state", state);
        if (version != null)
            XMLDom4jUtils.appendChild(e, "version", version);
        if (modifier != null)
            XMLDom4jUtils.appendChild(e, "modifier", modifier);
        if (modifyDate != null)
            XMLDom4jUtils.appendChild(e, "modify_date", DateUtil.date2String(
                    modifyDate, DateUtil.DEFAULT_TIME_FORMAT));
    }

    public static TfmServicesDto createTfmServicesDto(Element e) {
        TfmServicesDto dto = new TfmServicesDto();
        List elst = e.elements();
        for (Iterator it = elst.iterator(); it.hasNext(); ) {
            Element e1 = (Element) it.next();
            System.out.println(e1.getTextTrim());
            if ("cache_flag".equals(e1.getName()))
                dto.setCacheFlag(e1.getTextTrim());
            else if ("definition".equals(e1.getName()))
                dto.setDefinition(e1.getTextTrim());
            else if ("env_desc".equals(e1.getName()))
                dto.setEnvDesc(e1.getTextTrim());
            else if ("env_id".equals(e1.getName()))
                dto.setEnvId(e1.getTextTrim());
            else if ("env_name".equals(e1.getName()))
                dto.setEnvName(e1.getTextTrim());
            else if ("env_type".equals(e1.getName()))
                dto.setEnvType(e1.getTextTrim());
            else if ("service_desc".equals(e1.getName()))
                dto.setServiceDesc(e1.getTextTrim());
            else if ("service_name".equals(e1.getName()))
                dto.setServiceName(e1.getTextTrim());
            else if ("service_type".equals(e1.getName()))
                dto.setServiceType(e1.getTextTrim());
            else if ("state".equals(e1.getName()))
                dto.setState(e1.getTextTrim());
            else if ("version".equals(e1.getName()))
                dto.setVersion(e1.getTextTrim());
            else if ("modifier".equals(e1.getName()))
                dto.setModifier(e1.getTextTrim());
            else if ("modify_date".equals(e1.getName()))
                dto.setModifyDate(DateUtil.string2SQLDate(e1.getTextTrim(),
                        DateUtil.DEFAULT_TIME_FORMAT));
            else if ("module_name".equals(e1.getName()))
                dto.setModuleName(e1.getTextTrim());
        }
        return dto;
    }
}

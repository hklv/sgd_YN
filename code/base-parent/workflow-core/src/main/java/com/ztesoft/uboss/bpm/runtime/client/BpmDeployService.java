package com.ztesoft.uboss.bpm.runtime.client;

import org.activiti.engine.ActivitiException;

import java.io.InputStream;

/**
 * 流程模板存储服务
 *
 * @author chen.gang71
 */
public interface BpmDeployService {
    /**
     * 向流程引擎发布模板
     *
     * @param resourceName
     * @param inputStream
     * @return 返回deploymentId
     */
    String deploy(String resourceName, InputStream inputStream) throws ActivitiException;

    /**
     * 向流程引擎发布模板
     *
     * @param resource classPath下的资源名称
     * @return
     * @throws ActivitiException
     */
    String deploy(String resource) throws ActivitiException;

    String deploy(String resourceName, String text) throws ActivitiException;


    void undeploy(String deploymentId) throws ActivitiException;

    boolean hasProcessDef(String processDefinitionKey);
}

package com.ztesoft.uboss.bpm.runtime.service;

import com.ztesoft.uboss.bpm.runtime.client.BpmDeployService;
import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import java.io.InputStream;

/**
 * 流程部署
 *
 * @author LiybC
 */
public class BpmDeployServiceImpl implements BpmDeployService {
    private ZSmartLogger logger = ZSmartLogger.getLogger(BpmDeployServiceImpl.class);
    private ServiceProvider serviceProvider;

    public BpmDeployServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String deploy(String resourceName, InputStream inputStream) throws ActivitiException {
        DeploymentBuilder deploymentBuilder = serviceProvider.getRepositoryService().createDeployment();
        deploymentBuilder.addInputStream(resourceName, inputStream);
        deploymentBuilder.name(resourceName);
        Deployment deployment = deploymentBuilder.deploy();
        if (logger.isDebugEnabled()) {
            logger.debug("Deployed by InputStream,id={},name={}", deployment.getId(), deployment.getName());
        }
        return deployment.getId();
    }

    public String deploy(String resource) throws ActivitiException {
        DeploymentBuilder deploymentBuilder = serviceProvider.getRepositoryService().createDeployment();
        deploymentBuilder.addClasspathResource(resource);

        int index = resource.lastIndexOf('/');
        String resourceName = null;
        if (index != -1) {
            resourceName = resource.substring(index + 1, resource.length());
        } else {
            resourceName = resource;
        }
        deploymentBuilder.name(resourceName);
        Deployment deployment = deploymentBuilder.deploy();
        if (logger.isDebugEnabled()) {
            logger.debug("Deployed by classpath resource,id={},name={}", deployment.getId(), deployment.getName());
        }
        return deployment.getId();
    }


    public String deploy(String resourceName, String text) throws ActivitiException {
        DeploymentBuilder deploymentBuilder = serviceProvider.getRepositoryService().createDeployment();
        deploymentBuilder.addString(resourceName, text);
        deploymentBuilder.name(resourceName);
        Deployment deployment = deploymentBuilder.deploy();
        if (logger.isDebugEnabled()) {
            logger.debug("Deployed by String,id={},name={}", deployment.getId(), deployment.getName());
        }
        return deployment.getId();
    }


    public void undeploy(String deploymentId) throws ActivitiException {
        try {
            serviceProvider.getRepositoryService().deleteDeployment(deploymentId);
        } catch (ActivitiException ae) {
            throw new ActivitiException("BPM engine error", ae);
        } catch (RuntimeException re) {
            throw new ActivitiException(
                    "There are still runtime or history process instances or jobs", re);
        }
    }

    public boolean hasProcessDef(String processDefinitionKey) {
        ProcessDefinitionQuery procDefQuery =
                serviceProvider.getRepositoryService().createProcessDefinitionQuery();
        ProcessDefinition procDef = procDefQuery.processDefinitionKey(
                processDefinitionKey).latestVersion().singleResult();
        return (procDef != null);
    }
}

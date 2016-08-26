package com.ztesoft.uboss.bpm.runtime.client;

import com.ztesoft.uboss.bpm.runtime.service.BpmDeployServiceImpl;
import com.ztesoft.uboss.bpm.runtime.service.BpmRuntimeServiceImpl;

public final class ProcessServiceManager {
    private BpmRuntimeService runtimeService;
    private BpmDeployServiceImpl deployService;
    private ServiceProvider serviceProvider;
    
    private static Object lock = new Object();
    
    private static ProcessServiceManager instance;
    
    private ProcessServiceManager(){
        serviceProvider = new ServiceProvider();
        serviceProvider.init();
        runtimeService = new BpmRuntimeServiceImpl(serviceProvider);
        deployService = new BpmDeployServiceImpl(serviceProvider);
    }
    
    public static ProcessServiceManager getManager(){
        if(instance == null){
            synchronized(lock){
                if(instance == null){
                    instance = new ProcessServiceManager();
                }
            }
        }
        return instance;
    }
    
    public BpmRuntimeService getRuntimeService(){
        return runtimeService;
    }
    
    public BpmDeployService getDeployService(){
        return deployService;
    }
    
    public ServiceProvider getServiceProvider(){
        return serviceProvider;
    }
}

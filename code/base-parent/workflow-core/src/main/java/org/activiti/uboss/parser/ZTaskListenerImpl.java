package org.activiti.uboss.parser;

import java.util.List;

import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.ext.ZServiceDefinition;
import org.activiti.uboss.model.BpmTaskEventDto;


public class ZTaskListenerImpl implements TaskListener {

	private BpmTaskEventDto bpmTaskEventDto;
	
	private ZServiceDefinition serviceDefinition;
	
	public ZTaskListenerImpl(BpmTaskEventDto bpmTaskEventDto){
		
		this.bpmTaskEventDto = bpmTaskEventDto;
	}
	
	public ZServiceDefinition getServiceDefinition() {
		return serviceDefinition;
	}

	public void setServiceDefinition(ZServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		
		DynamicDict dict = new DynamicDict();
		try{
			String serviceName = bpmTaskEventDto.getServiceName();
			
			if(StringUtil.isNotEmpty(serviceName) && serviceName.indexOf(".") != -1){
				
				PvmActivity active = ((ActivityExecution)delegateTask.getExecution()).getActivity();
				
				//获取模板参数
				List<ServiceParamDto> serviceParamList = ZExtendsionUtil
						.qryTaskTemplateParam(active.getProcessDefinition().getDeploymentId(),
								(String) active.getProperty("taskTemplateId"), active.getId());
				
				for(ServiceParamDto param: serviceParamList){
					
					String varType = param.getVarType();
					String defaultValue = param.getVarValue();
					if(StringUtil.isEmpty(defaultValue)){
						
						continue;
					}
					
					if ("String".equals(varType)) {
						dict.set(param.getVarCode(), defaultValue);
					} else if ("Long".equals(varType)) {
						dict.set(param.getVarCode(),Long.valueOf(defaultValue));
					} else if ("Date".equals(varType)) {
						String[] dateValue = defaultValue.split(" ");
						if (dateValue != null && dateValue.length == 1) {
							defaultValue += " 00:00:00";
						}
						dict.set(param.getVarCode(), DateUtil.string2SQLDate(defaultValue, DateUtil.DATETIME_FORMAT_1));
					}
				}
			
				String[] services = serviceName.split("\\.");
				
				dict.serviceName = services[0];
				dict.set("method", services[1]);
			
				ServiceFlow.callService(dict, true);
			
				if("END".equals(bpmTaskEventDto.getEventType())){
				
					if(dict.get("bpm_user_id") != null){
						delegateTask.getExecution().setVariable("bpm_user_id",
								dict.get("bpm_user_id"));
					}else if(dict.get("bpm_role_id") != null){
						delegateTask.getExecution().setVariable("bpm_role_id",
								dict.get("bpm_role_id"));
					}else if(dict.get("bpm_job_id") != null){
						delegateTask.getExecution().setVariable("bpm_job_id",
								dict.get("bpm_job_id"));
					}else if(dict.get("bpm_org_id") != null){
						delegateTask.getExecution().setVariable("bpm_org_id",
								dict.get("bpm_org_id"));
					}else if(dict.get("bpm_user_list") != null){
						delegateTask.getExecution().setVariable("bpm_user_list",
								dict.get("bpm_user_list"));
					}
				}
				
				//设置流程变量
				for(ServiceParamDto param: serviceParamList){
					
					if("1".equals(param.getScope())){
						
						Object obj = dict.get(param.getVarCode());
						
						if(obj != null){
							
							delegateTask.getExecution().setVariable(param.getVarCode(), obj);
						}
					}
				}
			}
			
		}catch(BaseAppException e){
			throw new ActivitiException("notify task event error, templateId:"+bpmTaskEventDto.getTaskTempLateId(), e);
		}
	}

}

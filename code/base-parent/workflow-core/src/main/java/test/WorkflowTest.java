package test;

import com.ztesoft.uboss.bpm.test.StaticFormService;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;

/**
 * 工作流测试
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/5/27
 */
public class WorkflowTest {

    public static void main(String[] args) throws BaseAppException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
        //startProcess();
        //completeTask();
        //deployProcess();
        //testCreateForm();
        //qryStaticForm();
        //qryProcessTrack();Str
        //ArrayList ss =  ArrayList.class.newInstance();
        //qryAbnormalTaskList();
        //qryAbnormalTaskListByQueryService();
        // qryTaskRepoService();
        //addTaskTemp();
//        [Ljava.lang.String
        // String[].class.newInstance();
        // Array.newInstance(String.class,2);
        //bindSgdApplyIdToFlowVer();
        //getSysAllServices();
        //getMethodsOfService();
        //qryLinkFormDetail();
        //addTaskForm();
        qryTaskForm();
    }

    private static void qryTaskForm() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("TaskFormServiceTest");
        dict.set("method", "qryTaskForm");
        dict.set("holder_no", "132456");
        ServiceFlow.callService(dict);
    }

    private static void addTaskForm() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("TaskFormServiceTest");
        dict.set("method", "addTaskForm");
        dict.set("is_match_national_standard", 0);
        dict.set("is_approval", 1);
        dict.set("holder_no", "132456");
        ServiceFlow.callService(dict);
    }

    public static void completeTask() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        //模拟界面session
        DynamicDict uboss_session = new DynamicDict();
        uboss_session.set("user-id", 1);
        dict.set("zsmart_session", uboss_session);
        //设置服务名BPM_TASK_HOLDER_ID
        dict.serviceName = "WorkItemService";
        //完成的任务ID
        dict.set("TASK_LIST_ID", "adc1331c-66db-4017-8a6f-1b0892c2654f");
        //本地变量，只有当前环节可以访问
        DynamicDict lvar = new DynamicDict();
        lvar.set("TASK_RESULT", "同意");
        //全局变量，整个流程都可以访问，并且需要在流程定义时的流程全局变量里面定义
        DynamicDict gvar = new DynamicDict();
        gvar.set("HOLDER_ID", "3610affa-536a-4bd0-be6c-078566aef278");
        dict.set("LVAR", lvar);
        dict.set("GVAR", gvar);
        //设置服务定义方法
        dict.set("method", "completeTask");
        //调用服务
        ServiceFlow.callService(dict, true);
    }

    //
    public static void startProcess() throws BaseAppException {
        ServiceFlow serviceFlow = new ServiceFlow();
        DynamicDict dict = new DynamicDict();
        DynamicDict userSession = new DynamicDict();
        userSession.set("user-id", 1);
        dict.setServiceName("BpmClientService");
        //dict.set("PROC_DEF_ID", "C7159BEE6970000154CA1A4E1D807170");
        dict.set("PROC_DEF_ID", "C7241BEF37100001D83A11081AD78080");
        dict.set("method", "startProcess");
        dict.set("zsmart_session", userSession);
        serviceFlow.callService(dict, true);
        String processHolderNo = dict.getString("PROCESS_HOLDER_NO");
        System.out.println(processHolderNo);
    }

    public static void deployProcess() throws BaseAppException {
        ServiceFlow serviceFlow = new ServiceFlow();
        DynamicDict dict = new DynamicDict();
        dict.set("user-id", 1);
        dict.setServiceName("ProcessDefineService");
        dict.set("method", "deployProcessDef");
        dict.set("BPMN_CONTENT", "&amp;lt;?xml version=&amp;quot;1.0&amp;quot;?&amp;gt;&amp;lt;definitions xmlns=&amp;quot;http://www.omg.org/spec/BPMN/20100524/MODEL&amp;quot; xmlns:xsi=&amp;quot;http://www.w3.org/2001/XMLSchema-instance&amp;quot; xmlns:uboss=&amp;quot;http://www.ztesoft.com/uboss/bpmn&amp;quot; xmlns:activiti=&amp;quot;http://activiti.org/bpmn&amp;quot; xmlns:bpmndi=&amp;quot;http://www.omg.org/spec/BPMN/20100524/DI&amp;quot; xmlns:omgdc=&amp;quot;http://www.omg.org/spec/DD/20100524/DC&amp;quot; xmlns:omgdi=&amp;quot;http://www.omg.org/spec/DD/20100524/DI&amp;quot; typeLanguage=&amp;quot;http://www.w3.org/2001/XMLSchema&amp;quot; expressionLanguage=&amp;quot;http://www.w3.org/1999/XPath&amp;quot; targetNamespace=&amp;quot;http://www.ztesoft.com/uboss/bpmn&amp;quot;&amp;gt;&amp;lt;process id=&amp;quot;C71697457C3000011C699E061915C840&amp;quot; name=&amp;quot;C71697457C3000011C699E061915C840&amp;quot;&amp;gt;&amp;lt;endEvent id=&amp;quot;pro18&amp;quot; name=&amp;quot;&amp;quot;/&amp;gt;&amp;lt;serviceTask activiti:taskTemplateId=&amp;quot;80&amp;quot; taskTemplateName=&amp;quot;签报局长通知&amp;quot; blockFlag=&amp;quot;N&amp;quot; imgName=&amp;quot;&amp;quot; id=&amp;quot;pro16&amp;quot; name=&amp;quot;邮件&amp;quot;&amp;gt;&amp;lt;extensionElements/&amp;gt;&amp;lt;/serviceTask&amp;gt;&amp;lt;userTask activiti:taskTemplateId=&amp;quot;98&amp;quot; taskTemplateName=&amp;quot;办公室主任2&amp;quot; imgName=&amp;quot;&amp;quot; id=&amp;quot;pro14&amp;quot; name=&amp;quot;领导审核&amp;quot;&amp;gt;&amp;lt;extensionElements/&amp;gt;&amp;lt;/userTask&amp;gt;&amp;lt;startEvent id=&amp;quot;pro12&amp;quot; name=&amp;quot;&amp;quot;/&amp;gt;&amp;lt;sequenceFlow id=&amp;quot;line16&amp;quot; name=&amp;quot;&amp;quot; sourceRef=&amp;quot;pro16&amp;quot; targetRef=&amp;quot;pro18&amp;quot;/&amp;gt;&amp;lt;sequenceFlow id=&amp;quot;line14&amp;quot; name=&amp;quot;&amp;quot; sourceRef=&amp;quot;pro14&amp;quot; targetRef=&amp;quot;pro16&amp;quot;/&amp;gt;&amp;lt;sequenceFlow id=&amp;quot;line12&amp;quot; name=&amp;quot;&amp;quot; sourceRef=&amp;quot;pro12&amp;quot; targetRef=&amp;quot;pro14&amp;quot;/&amp;gt;&amp;lt;/process&amp;gt;&amp;lt;bpmndi:BPMNDiagram id=&amp;quot;BPMNDiagram_C71697457C3000011C699E061915C840&amp;quot;&amp;gt;&amp;lt;bpmndi:BPMNPlane id=&amp;quot;BPMNPlane_C71697457C3000011C699E061915C840&amp;quot; bpmnElement=&amp;quot;C71697457C3000011C699E061915C840&amp;quot;&amp;gt;&amp;lt;bpmndi:BPMNShape id=&amp;quot;BPMNShape_18&amp;quot; bpmnElement=&amp;quot;pro18&amp;quot;&amp;gt;&amp;lt;omgdc:Bounds y=&amp;quot;191&amp;quot; x=&amp;quot;573&amp;quot; width=&amp;quot;50&amp;quot; height=&amp;quot;50&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNShape&amp;gt;&amp;lt;bpmndi:BPMNShape id=&amp;quot;BPMNShape_16&amp;quot; bpmnElement=&amp;quot;pro16&amp;quot;&amp;gt;&amp;lt;omgdc:Bounds y=&amp;quot;192&amp;quot; x=&amp;quot;395&amp;quot; width=&amp;quot;100&amp;quot; height=&amp;quot;50&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNShape&amp;gt;&amp;lt;bpmndi:BPMNShape id=&amp;quot;BPMNShape_14&amp;quot; bpmnElement=&amp;quot;pro14&amp;quot;&amp;gt;&amp;lt;omgdc:Bounds y=&amp;quot;194&amp;quot; x=&amp;quot;197&amp;quot; width=&amp;quot;100&amp;quot; height=&amp;quot;50&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNShape&amp;gt;&amp;lt;bpmndi:BPMNShape id=&amp;quot;BPMNShape_12&amp;quot; bpmnElement=&amp;quot;pro12&amp;quot;&amp;gt;&amp;lt;omgdc:Bounds y=&amp;quot;198&amp;quot; x=&amp;quot;67&amp;quot; width=&amp;quot;50&amp;quot; height=&amp;quot;50&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNShape&amp;gt;&amp;lt;bpmndi:BPMNEdge bpmnElement=&amp;quot;line16&amp;quot; id=&amp;quot;BPMNEdge_line16&amp;quot; xIndex=&amp;quot;5&amp;quot; wIndex=&amp;quot;4&amp;quot;&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;497&amp;quot; y=&amp;quot;218&amp;quot;/&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;573&amp;quot; y=&amp;quot;217&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNEdge&amp;gt;&amp;lt;bpmndi:BPMNEdge bpmnElement=&amp;quot;line14&amp;quot; id=&amp;quot;BPMNEdge_line14&amp;quot; xIndex=&amp;quot;5&amp;quot; wIndex=&amp;quot;4&amp;quot;&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;299&amp;quot; y=&amp;quot;220&amp;quot;/&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;395&amp;quot; y=&amp;quot;218&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNEdge&amp;gt;&amp;lt;bpmndi:BPMNEdge bpmnElement=&amp;quot;line12&amp;quot; id=&amp;quot;BPMNEdge_line12&amp;quot; xIndex=&amp;quot;5&amp;quot; wIndex=&amp;quot;4&amp;quot;&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;119&amp;quot; y=&amp;quot;224&amp;quot;/&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;119&amp;quot; y=&amp;quot;223&amp;quot;/&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;197&amp;quot; y=&amp;quot;223&amp;quot;/&amp;gt;&amp;lt;omgdi:waypoint x=&amp;quot;197&amp;quot; y=&amp;quot;220&amp;quot;/&amp;gt;&amp;lt;/bpmndi:BPMNEdge&amp;gt;&amp;lt;/bpmndi:BPMNPlane&amp;gt;&amp;lt;/bpmndi:BPMNDiagram&amp;gt;&amp;lt;/definitions&amp;gt;");
        serviceFlow.callService(dict, true);

    }

    public static void testCreateForm() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.set("PERSON", "hklv1");
        dict.set("MONEY", 50001);
        dict.set("MONEY_FOR", "买房子1");
        dict.set("FLOW_ID", "C7159BEE6970000154CA1A4E1D807170");
        StaticFormService staticFormService = new StaticFormService();
        staticFormService.createForm(dict);
    }

    public static void qryStaticForm() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.set("FLOWID", "C7159BEE6970000154CA1A4E1D807170");
        StaticFormService staticFormService = new StaticFormService();
        staticFormService.qryForm(dict);
    }

    public static void qryProcessTrack() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("BpmClientService");
        dict.set("method", "qryProcessTrack");
        dict.set("PROCESS_INSTANCE_ID", "150088");
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void qryAbnormalTaskList() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("WorkItemService");
        dict.set("method", "qryabnoramlTaskList");
        ServiceFlow serviceFlow = new ServiceFlow();
        DynamicDict row = new DynamicDict();
        row.set("PAGE_SIZE", 20);
        row.set("PAGE_INDEX", 0);
        dict.set("ROW_SET_FORMATTER", row);
        serviceFlow.callService(dict, true);
    }

    public static void qryAbnormalTaskListByQueryService() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("QueryService");
        dict.set("method", "queryList");
        dict.set("QUERY_COUNT_METHOD", "qryAbnoramlTaskListCount");
        dict.set("QUERY_METHOD", "qryAbnoramlTaskList");
        dict.set("DOMAIN", "WorkItemManager");
        dict.set("MODULE_NAME", "bpm.taskcenter");
        DynamicDict row = new DynamicDict();
        DynamicDict selCondition = new DynamicDict();
        row.set("PAGE_INDEX", 0);
        row.set("PAGE_SIZE", 20);
        selCondition.set("ROW_SET_FORMATTER", row);
        dict.set("SEL_CONDITION", selCondition);
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void qryTaskRepoService() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("TaskRepoService");
        dict.set("method", "qryTaskTempList");
        DynamicDict row = new DynamicDict();
        dict.set("PROC_DEF_ID", 22);
        row.set("PAGE_INDEX", 0);
        row.set("PAGE_SIZE", 20);
        dict.set("ROW_SET_FORMATTER", row);
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void addTaskTemp() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("TaskRepoService");
        dict.set("method", "addTaskTemp");
        dict.set("TEMPLATE_ID", 1);
        dict.set("TEMPLATE_NAME", "拟稿");
        dict.set("ADD_OR_EDIT", "edit");
        dict.set("TASK_TYPE", "U");
        dict.set("IS_MULTI_SIGIN", "A");
        dict.set("CODE", "NGCSZXFZR");
        dict.set("TEMPLATE_TYPE", "A");
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void bindSgdApplyIdToFlowVer() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("ProcessDefineService");
        dict.set("method", "bindSgdApplyToFlowVer");
        dict.set("PROCESS_VER_ID", "15");
        dict.set("SGD_APPLY_ID", 1);
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void getSysAllServices() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("ServiceConfigService");
        dict.set("method", "getSysAllServices");
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void getMethodsOfService() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("ServiceConfigService");
        dict.set("method", "getMethodsOfService");
        dict.set("SERVICE_NAME", "service_AddArea");
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict, true);
    }

    public static void qryLinkFormDetail() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.setServiceName("TaskFormService");
        dict.set("method", "qryTaskLinkForm");
        dict.set("FORM_ID", 54L);
        ServiceFlow serviceFlow = new ServiceFlow();
        serviceFlow.callService(dict);
    }
}

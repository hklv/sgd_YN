<![if !IE]> <!--判断是否是IE浏览器，如果不是在加入<!DOCTYPE html>声明 ，用来解决非IE浏览器下任务库弹出框无法关闭的问题 -->
<!DOCTYPE html>
<![endif]>
<meta http-equiv="X-UA-Compatible" content="IE7"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.ztesoft.zsmart.core.configuation.ConfigurationMgr" %>

<%!
    String WebRoot = "";

    public String getWebRoot(HttpServletRequest request) {
        String strWebRoot;
        String ips = ConfigurationMgr.instance().getString("https.ip", null);
        String requestfrom = request.getRemoteAddr();
        if (requestfrom != null) {
            if (ips != null && ips.indexOf(requestfrom) > -1) {
                strWebRoot = "https";
            } else {
                strWebRoot = request.getScheme();
            }
        } else {
            strWebRoot = request.getScheme();
        }
        strWebRoot += "://";
        strWebRoot += request.getServerName();

        int port = request.getServerPort();
        if (port != 80) {
            strWebRoot += ":" + port;
        }
        strWebRoot += request.getContextPath() + "/";
        return strWebRoot;
    }

%>

<%
    WebRoot = getWebRoot(request);
%>


<script type="text/javascript">
    var g_GlobalInfo = new Object();
    g_GlobalInfo.WebRoot = "<%=WebRoot%>";
</script>
<link href="<%=WebRoot%>skins/default/zh_CN/css/pagecss/bpmn/flowtemplat.css" rel="stylesheet" type="text/css">
<script src="<%=WebRoot%>public/script/comm/ZTEsoft.js" type="text/javascript"></script>
<script src="<%=WebRoot%>public/script/comm/RemoteHandler.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/popup/windows.js"></script>
<script type="text/javascript" src="js/sideWindow.js"></script>
<script type="text/javascript" src="<%=WebRoot%>frm/fish-desktop/js/fish-desktop-all.js?v=8.1.5"></script>

<!-- <html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"> -->
<html xmlns:v>
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>ZSMART流程设计器
    </title>

    <link rel="stylesheet" type="text/css" href="../themes/default/easyui.css">
    <link href="css/flowPath.css" type="text/css" rel="stylesheet"/>
    <!--  <?import namespace="v" implementation="#default#VML" ?>
    <style>
        v\:line,v\:oval,v\:roundrect,v\:rect,v\:arc,v\:polyline,v\:curve { display:inline-block }
    </style> -->

    <STYLE>
        v\: * {
            behavior: url(#default#VML);
            display: inline-block
        }
    </STYLE>
    <style>

        .w {
            position: absolute;
            width: 100%;
            height: 100%;
            background: url("images/toum.png");
            0 0 repeat;
        }

        .logo {
            position: relative;
            left: 40%;
            top: 50%;
        }

    </style>
</head>

<body class="easyui-layout bodySelectNone" id="body" onselectstart="return false">

<div id="title" region="north" split="true" border="false" title="工具栏" style="overflow: hidden;" class="titleTool">
    <div id="message" class="message"></div>
    <!--<img alt="预览"	title="预览" src="images/zoom.png" onclick="graphUtils.printView()" class="buttonStyle"/>	-->
    <img alt="撤销" title="撤销" src="images/back.png" onclick="graphUtils.undo();" class="buttonStyle"/>
    <img alt="重做" title="重做" src="images/next.png" onclick="graphUtils.redo();" class="buttonStyle"/>
    <img id="saveImg" alt="保存" title="保存"  src="images/save.png" onclick="graphUtils.saveXml();" class="buttonStyle"/>
    <!--  <img alt="加载"	title="加载" src="images/download_page.png" onclick="graphUtils.loadTextXml();" class="buttonStyle"/>-->
    <img alt="清空" title="清空" src="images/trash.png" onclick="graphUtils.clearHtml();" class="buttonStyle"/>
    <img alt="删除" title="删除" src="images/delete.png" onclick="graphUtils.removeNode();" class="buttonStyle"/>
    <img alt="流程变量" title="流程变量" id="flowvariable" src="images/add.png"  class="buttonStyle"/>
    <img alt="流程校验" title="流程校验" src="images/check.png" onclick="graphUtils.checkXml('check');" class="buttonStyle"/>
    <img alt="异常设置" title="异常设置" id="exceptionsetting" src="images/exception_set.png"  class="buttonStyle"/>
</div>

<div id="leftContent" region="west" split="true" title="图元区" class="leftContent">


    <div id="tt" class="tabs-container" fit="true" style="height: 300px;width:150px;">
        <div title="基本">
            <div title="" style="position:relative;background-color:#c7dbfc;min-height:550px;">
                <!--  <div style="width: 170px;height: 100px;"> -->
                <div id="baseLine1" style="float:left;margin-top:10px;margin-right:10px;width: 100%;">

                    <img id="backGroundImg1" src="images/line1.png" class="nodeStyle" style="height: 20px;width: 20px;"/>
                    <div style="display: inline;line-height: 30px;width: 30px;">转换</div>
                </div>

                <div id="baseLine3" style="position:absolute;left:50px !important;">
                    <div class="contentTitle">&nbsp;</div>
                </div>
                <div id="baseLine2" style="position:absolute;left:90px !important;">
                    <div class="contentTitle">&nbsp;</div>
                </div>

                <div id="baseMode36" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg2" src="images/startEvent.png"
                         style="cursor:pointer;height: 30px;width: 30px;float: left;"/>
                    <div taskType="startEvent" style="display: inline;line-height: 30px;width: 30px;">开始</div>
                </div>

                <div id="baseMode37" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg3" src="images/endEvent.png"
                         style="cursor:pointer;height: 30px;width: 30px;float: left;"/>
                    <div taskType="endEvent" style="display: inline;line-height: 30px;width: 30px;">结束</div>
                </div>


                <div id="userTask" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg4" src="images/userTask.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="userTask" style="display: inline;line-height: 30px;width: 50px">人工任务</div>
                </div>

                <div id="serviceTask" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">

                    <img id="backGroundImg5" src="images/serviceTask.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="serviceTask" style="display: inline;line-height: 30px;width: 50px;">服务任务</div>
                </div>

                <div id="scriptTask" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg6" src="images/scriptTask.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="scriptTask" style="display: inline;line-height: 30px;width: 50px;">脚本任务</div>
                </div>

                <div id="esbTask" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg7" src="images/esbTask.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="esbTask" style="display: inline;line-height: 30px;width: 50px;">总线任务</div>
                </div>

                <div id="businessRuleTask" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg8" src="images/businessRuleTask.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="businessRuleTask" style="display: inline;line-height: 30px;width: 50px;">规则任务</div>
                </div>

                <div id="sendTask" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg9" src="images/sendTask.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="sendTask" style="display: inline;line-height: 30px;width: 50px;">消息任务</div>
                </div>

                <div id="subProcess" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg10" src="images/subProcess.png"
                         style="cursor:pointer;height: 30px;width: 50px;float: left;"/>
                    <div taskType="subProcess" style="display: inline;line-height: 30px;width: 50px;">子任务</div>
                </div>

                <div id="inclusiveGateway" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg11" src="images/inclusiveGateway.png"
                         style="cursor:pointer;height: 30px;width: 30px;float: left;"/>
                    <div taskType="inclusiveGateway" style="display: inline;line-height: 30px;width: 50px;">多路网关</div>
                </div>

                <div id="parallelGateway" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg12" src="images/parallelGateway.png"
                         style="cursor:pointer;height: 30px;width: 30px;float: left;"/>
                    <div taskType="parallelGateway" style="display: inline;line-height: 30px;width: 50px;">并行网关</div>
                </div>

                <div id="exclusiveGateway" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">
                    <img id="backGroundImg13" src="images/exclusiveGateway.png"
                         style="cursor:pointer;height: 30px;width: 30px;float: left;"/>
                    <div taskType="exclusiveGateway" style="display: inline;line-height: 30px;width: 50px;">独立网关</div>
                </div>


            </div>
        </div>
        <!-- </div> -->
        <div title="高级" id="Tab2" style="overflow: hidden;">

            <div id="accordion" class="easyui-accordion" fit="true" border="false">

            </div>

        </div>
    </div>


</div>
<div id="test" style="display: none;">我是测试页面test</div> 


<div region="center" title="绘图区" id="contextBody" class="mapContext">

    <!-- Line右键菜单 -->
    <div id="lineRightMenu" class="modeRight">
        <div class="modeRightTop"></div>
        <div class="modeRightDel" onmousemove="this.style.backgroundColor='#c5e7f6'" onclick="graphUtils.removeNode();"
             onmouseout="this.style.backgroundColor=''"><span
                class="menuSpan">删除</span></div>
        <div class="modeRightButtom"></div>
    </div>

    <!-- Mode右键菜单 -->
    <div id="rightMenu" class="modeRight">
        <div class="modeRightTop"></div>
        <div class="modeRightDel" onmousemove="this.style.backgroundColor='#c5e7f6'" onclick="graphUtils.removeNode();"
             onmouseout="this.style.backgroundColor=''"><span
                class="menuSpan">删除</span></div>
        <div class="modeRightPro" onmousemove="this.style.backgroundColor='#c5e7f6'" onclick="graphUtils.showModePro();"
             onmouseout="this.style.backgroundColor=''"><span
                class="menuSpan">参数</span></div>
        <div class="modeRightButtom"></div>
    </div>


    <div id="topCross"></div>
    <div id="leftCross"></div>

</div>


<div region="south" split="true" title="辅助区" class="auxiliaryArea" style="overflow-y: hidden; ">
    <!-- 小地图 -->
    <div id="smallMap"></div>

    <div id="mainControl">


        <div id="tab" class="control">


            <div id="tab1" class="htabup" style="overflow-y:auto;">

                <table id="modelAttr" cellpadding="0" cellspacing="0">
                    <tr>
                        <td name="inputTitle" style="text-align: center; width: 80px; height: 25px;">
                            任务名称
                        </td>
                        <td name="inputTitle"><input type="text" id="inputTitle" class="inputComm"
                                                     style="width: 150px;" onkeyup="submitModeTitle();"/>
                            <input type="button" id="subProcessButton" value="..."
                                   style="width: 25px; height: 20px; padding: 1px 4px; margin-left: -5px;display: none;"
                                   onclick="subProcessClick();"/></td>
                        <td name="taskTemplate" id="taskText"
                            style="text-align: center; height: 25px; width: 80px; padding-left: 30px; display: none;">
                            任务模板
                        </td>
                        <td name="taskTemplate" style="display: none;"><input
                                type="text" id="taskTemplateName" class="inputComm"
                                style="width: 150px;" readonly="readonly"/> <input type="text"
                                                                                   id="taskTemplateId" class="inputComm"
                                                                                   style="display: none;"/>
                            <input type="text" id="index" value="" style="display: none;"/>
                            <input type="button" value="..." id="tasktemplate" 
                                   style="width: 25px; height: 20px; padding: 1px 4px; margin-left: -5px;"
                                  /></td>

                        <td name="esbInput"
                            style="text-align: center; width: 80px; height: 25px;padding-left: 30px; display: none;">
                            URL
                        </td>
                        <td><input name="esbInput" type="text" id="endpointUrl" class="inputComm"
                                   style="width: 150px;display: none;" onkeyup="submitEndpointUrl();"/></td>
                    </tr>
                    <tr id="Variable" style="display: none;" data-role="none">
                        <td style="text-align: center; height: 25px; width: 80px;">
                            结果变量
                        </td>
                        <td><input type="text" id="resultVariable" class="inputComm"
                                   style="width: 150px;" onkeyup="submitResultVariable();"/></td>
                    </tr>
                    <tr id="countersign" style="display: none;" data-role="none">
                        <td style="text-align: center; height: 25px; width: 80px;">
                            是否会签
                        </td>
                        <td><input type="radio" name="countersign" value="true"/>是
                            <input type="radio" name="countersign" value="false"
                                   checked="checked"/>否
                        </td>
                    </tr>
                    <tr id="isSequential" style="display: none;">
                        <td style="text-align: center; height: 25px; width: 80px;">
                            签入方式
                        </td>
                        <td><input type="radio" name="sequential" value="false"/>并行签入
                            <input type="radio" name="sequential" value="true"
                                   checked="checked"/>串行签入
                        </td>
                    </tr>
                    <tr id="script" style="display: none;">
                        <td style="text-align: center; height: 25px; width: 80px; ">
                            脚本任务
                        </td>
                        <td><textarea rows="3" id="scriptEdit" class="contextArea" readonly="readonly"
                                      onkeyup="submitScript();"></textarea><input type="button" value="..."
                                                                                  style="position: absolute;width: 25px; height: 20px; padding: 1px 4px; margin-left: 2px;margin-bottom: 22px;"
                                                                                  onclick="scriptClick();"/></td>
                    </tr>
                    <tr id="blockFlag" style="display: none;" data-role="none">
                        <td style="text-align: center; height: 25px; width: 80px;">堵塞标识
                        </td>

                        <td><input type="radio" name="blockFlag" value="Y"
                                   onclick="blockFlagClick();"/>是
                            <input type="radio" name="blockFlag" value="N" onclick="blockFlagClick();"
                                   checked="checked"/>否
                        </td>
                    </tr>
                    <tr style="display: none;">
                        <td style="width: 80px; text-align: center;">内容</td>
                        <td><textarea rows="3" id="modeContent" class="contextArea"></textarea>
                        </td>
                    </tr>

                    <tr name="mail" style="display: none;">
                        <td style="text-align: center; height: 25px; width: 80px; ">
                            收件人
                        </td>
                        <td><input type="text" id="inputMailTo" class="inputComm"
                                   style="width: 150px;" onkeyup="submitMailTo();"/></td>
                    </tr>
                    <tr name="mail" style="display: none;">
                        <td style="text-align: center; height: 25px; width: 80px; ">
                            主题
                        </td>
                        <td><input type="text" id="inputMailTheme" class="inputComm"
                                   style="width: 150px;" onkeyup="submitMailTheme();"/></td>
                    </tr>
                    <tr name="mail" style="display: none;">
                        <td style="text-align: center; height: 25px; width: 80px; ">内容
                        </td>
                        <td><textarea rows="3" id="mailBodyVal" class="contextArea"
                                      onkeyup="submitMailBody();"></textarea></td>
                    </tr>
                    <!-- <tr name="esbInput" style="display: none;">
                        <td style="text-align: center; width: 80px; height: 25px;">
                            URL</td>
                        <td><input type="text" id="endpointUrl" class="inputComm"
                            style="width: 150px;" onkeyup="submitEndpointUrl();"/></td>
                    </tr> -->
                    <tr name="esbInput" style="display: none;">
                        <td style="text-align: center; width: 80px; height: 25px;">
                            队列名
                        </td>
                        <td><input type="text" id="queueName" class="inputComm"
                                   style="width: 150px;" onkeyup="submitQqueueName();"/></td>

                        <td style="text-align: center; width: 80px; height: 25px;padding-left: 30px;">
                            Payload
                        </td>
                        <td><input type="text" id="payloadExpression" class="inputComm"
                                   style="width: 150px;" onkeyup="submitPayloadExpression();"/></td>
                    </tr>
                </table>


                <table id="lineAttr" style="display: none;" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="text-align: center; width: 80px; height: 25px;">
                            转移名称
                        </td>
                        <td><input type="text" id="lineTitle" class="inputComm"
                                   style="width: 200px;" onkeyup="submitTitle();"/>
                            <input type="text" id="lineId"
                                   style="display: none;"/></td>
                    </tr>
                    <tr id="lineType" data-role="none">
                        <td style="text-align: center; height: 25px; width: 80px;">默认流程
                        </td>

                        <td><input type="radio" name="lineType" value="normal" checked="checked"
                                   onclick="lineTypeClick();"/>否
                            <input type="radio" name="lineType" value="default" onclick="lineTypeClick();"
                            />是
                        </td>
                    </tr>
                    <tr id="conditionExpression">

                        <td style="text-align: center; height: 25px; width: 80px; ">
                            表达式
                        </td>
                        <td><textarea rows="3" id="lineCondition" class="contextArea" style="height: 30px;"
                                      onkeyup="submitCondition();"></textarea><input type="button" value="..."
                                                                                     style="position: absolute;width: 25px; height: 20px; padding: 1px 4px; margin-left: 2px;margin-bottom: 22px;"
                                                                                     onclick="conditionClick();"/></td>
                    </tr>

                    <tr id="serviceExpression" style="display: none;">
                        <td style="text-align: center; width: 80px; height: 25px;">
                            服务名
                        </td>
                        <td><input type="text" id="serviceName" class="inputComm"
                                   style="width: 200px;" readonly="readonly"/>
                            <input type="button" value="..."
                                   style="width: 25px; height: 20px; padding: 1px 4px; margin-left: 2px;"
                                   onclick="conditionClick();"/></td>

                    </tr>
                </table>

            </div>


        </div>

    </div>


</div>

<!-- 移动时的图象 -->
<div id="moveBaseMode" class="moveBaseMode">
    <img id="moveBaseModeImg" src="images/Favourite.png" class="nodeStyle"/>
</div>

<div id="prop" style="visibility: hidden;">
    Dialog Content.
</div>


<div class="w" id="process" style="display: none;">
    <div class="logo">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td align="left" style="text-align: center; height: 25px;">
                    流程名称:
                </td>
                <td><input type="text" id="processId" value=""
                           class="inputComm" style="width: 200px;"/></td>

            </tr>


        </table>
    </div>

</div>
</body>


<script src="js/jquery-1.6.1.js" type="text/javascript"></script>
<script src="js/jquery.easyui.min.js" type="text/javascript"></script>
<script src="js/uuid.js" type="text/javascript"></script>
<script src="js/BaseTool.js" type="text/javascript" language="javascript"></script>
<script src="js/LineTool.js" type="text/javascript" language="javascript"></script>
<script src="js/ModeTool.js" type="text/javascript" language="javascript"></script>
<script src="js/Map.js" type="text/javascript" language="javascript"></script>
<script src="js/Entity.js" type="text/javascript" language="javascript"></script>
<script src="js/KeyEvent.js" type="text/javascript" language="javascript"></script>
<script src="js/SmallMapTool.js" type="text/javascript" language="javascript"></script>
<script src="js/ControlTool.js" type="text/javascript" language="javascript"></script>
<script src="js/Global.js" type="text/javascript" language="javascript"></script>
<script src="js/BeanXml.js" type="text/javascript" language="javascript"></script>
<script src="js/Utils.js" type="text/javascript" language="javascript"></script>
<script src="js/objectJS.js" type="text/javascript" language="javascript"></script>

<link rel="stylesheet" type="text/css" href="../../../../skins/default/zh_CN/css/ztesoft/grid/grid.css"/>
<script type="text/javascript" src="../../../../public/script/ui/ztesoft/grid/js/grid.js"></script>
<%
    String procVersionId = request.getParameter("procVersionId");
    String deployId = request.getParameter("deployId");
    if ("".equals(deployId)) {
        deployId = null;
    }
    String procTypeId = request.getParameter("procTypeId");
%>

<script type="text/javascript" language="javascript">

    var flowguide = '<%=request.getParameter("flowguide")%>';
    var WebRoot = '<%=WebRoot%>';


    if (flowguide == "y") {
        $("#saveImg").hide();
    }

    function blockFlagClick() {
        var index = $("#index").val();
        $("#blockFlag" + index).text($("input[name='blockFlag'][type='radio']:checked").val());
    }

    var mainControl = $id("mainControl");
    mainControl.style.width = '83%';
    var bgImg = "url(images/bg.gif)";
    var backColor = "#e0ecff";


    /*打开子流程 设计界面 */
    function subProcessClick() {
        var input = new Object();
        var index = $("#index").val();
        input.index = index;
        input.procTypeId =<%=procTypeId%>;
        input.subXml = varData.subProcess[index];
        input.data = varData.data;
        input.procVersionId = varData.procVersionId;
        input.allActTache = varData.allActTache;
        showModalWindow('子流程设计器', 'modules/bpmn/flowdesigner/draw/subFlowdesigner.jsp', 900, 500
                , input
                , function (obj) {
                    if (obj != null) {
                        varData.subProcess[index] = obj;
                    }
                });
    }
    /* 打开设置任务模板 */
    function taskTemplateClick() {
        var index = $("#index").val();
        var imgSrc = $("#backImg" + index).attr("src");
        var task = imgSrc.substring((imgSrc.lastIndexOf("/") + 1), imgSrc.lastIndexOf("."));
        var taskType = $("#backImg" + index).attr("taskType");
        var page = "taskTemplate";
        if (taskType == "businessRuleTask") {
            page = "flowRule";
        }
        var procTypeId = <%=procTypeId%>;
        var inParam = {};
        inParam.procTypeId = <%=procTypeId%>;
        inParam.taskTemplateId = $("#taskTemplateId" + index).text();
        inParam.taskType = taskType;
        showModalWindow('任务库', '<%=WebRoot%>modules/bpmn/flowdesigner/draw/' + page + '.jsp', 800, 400
                , inParam
                , function (obj) {
                    if (obj != null) {
                        $("#taskTemplateId" + index).text(obj.taskTemplateId);
                        $("#taskTemplateName" + index).text(obj.taskTemplateName);
                        $("#taskTemplateId").val(obj.taskTemplateId);
                        $("#taskTemplateName").val(obj.taskTemplateName);
                    }
                });

    }
    /*打开转移表达式设置界面  */
    function conditionClick() {
        var lineId = $("#lineId").val();
        var conditionValue = $("#lineCondition").val();
        var serviceName = $("#serviceName").val();
        var input = new Object();
        input.list = varData.data;
        input.conditionValue = conditionValue;
        input.serviceName = serviceName;
        showModalWindow('表达式', '<%=WebRoot%>modules/bpmn/flowdesigner/draw/lineProperty.jsp', 550, 500
                , input
                , function (obj) {
                    if (obj != null) {
                        var lineCondition = obj.conditionValue;
                        var data_type = obj.type;
                        if (data_type == "conditionExpression") {
                            $("#serviceExpression").hide();
                            $("#serviceName").val("");
                            $("#conditionExpression").show();
                            $("#lineCondition").val(lineCondition);
                            $("#" + lineId + "Condition").attr("data_type", "conditionExpression");
                        } else {
                            $("#conditionExpression").hide();
                            $("#lineCondition").val("");
                            $("#serviceExpression").show();
                            $("#serviceName").val(lineCondition);
                            $("#" + lineId + "Condition").attr("data_type", "serviceExpression");
                        }
                        $("#" + lineId + "Condition").text(lineCondition);
                    }
                });

    }
    /* 定义流程变量LIST */
    var varData = {
        data: null,
        subProcess: {},
        subProcessStr: "",
        subBpminStr: "",
        procVersionId: null,
        deployId: null,
        processType: "process",
        allActTache: {},
        actTache: {},
        exceptionFallBackObj: {},
        exceptionFallBackList: []
    };
    /*打开流程变量添加编辑界面  */
    function flowVariable() {
        var input = new Object();
        input.list = varData.data;

        showModalWindow('流程变量', '<%=WebRoot%>modules/bpmn/flowdesigner/draw/flowVariable.jsp', 800, 400
                , input
                , function (obj) {
                    if (obj != null) {
                        varData.data = obj.list;

                    }
                });
    }
    /*设置规则任务结果变量  */
    function submitResultVariable() {
        var index = $("#index").val();
        var resultVariable = $("#resultVariable").val();
        $("#resultVariable" + index).text(resultVariable);
    }
    /*设置esbURL  */
    function submitEndpointUrl() {
        var index = $("#index").val();
        var endpointUrl = $("#endpointUrl").val();
        $("#endpointUrl" + index).text(endpointUrl);
    }
    /*设置esb payload名称  */
    function submitPayloadExpression() {
        var index = $("#index").val();
        var payloadExpression = $("#payloadExpression").val();
        $("#payloadExpression" + index).text(payloadExpression);
    }
    /*设置esb队列名称  */
    function submitQqueueName() {
        var index = $("#index").val();
        var queueName = $("#queueName").val();
        $("#queueName" + index).text(queueName);
    }
    /*设置任务名称  */
    function submitModeTitle() {
        var index = $("#index").val();
        var title = $("#inputTitle").val();
        $("#title" + index).text(title);
        $("#title" + index).attr("title", title);
    }
    /*设置线的转移名称  */
    function submitTitle() {
        var lineId = $("#lineId").val();
        var lineTitle = $("#lineTitle").val();
        $("#" + lineId + "Title").text(lineTitle);
        if (BaseTool.prototype.isSVG()) {
            //$("#"+lineId+"ShowTitle").text(lineTitle);
            LineTool.prototype.create_tspan(lineId);
        }
    }
    /*设置线的转移表达式  */
    function submitCondition() {
        var lineId = $("#lineId").val();
        var lineCondition = $("#lineCondition").val();
        $("#serviceName").val("");
        $("#" + lineId + "Condition").text(lineCondition);
        $("#" + lineId + "Condition").attr("data_type", "conditionExpression");

    }
    /*设置脚本VALUE*/
    function submitScript() {
        var index = $("#index").val();
        var scriptEdit = $("#scriptEdit").val();
        console.log("111111", scriptEdit);
        $("#scriptVal" + index).text(scriptEdit);

    }
    /*打开脚本设置界面  */
    function scriptClick() {
        var index = $("#index").val();
        var scriptEdit = $("#scriptEdit").val();
        var input = new Object();
        input.scriptValue = scriptEdit;
        showModalWindow('脚本', '<%=WebRoot%>modules/bpmn/flowdesigner/draw/scriptEdit.jsp', 800, 450
                , input
                , function (obj) {
                    if (obj != null) {
                        var scriptValue = obj.scriptValue;
                        $("#scriptEdit").val(scriptValue);
                        $("#scriptVal" + index).text(scriptValue);
                    }
                });

    }


    /*设置收件人*/
    function submitMailTo() {
        var index = $("#index").val();
        var sendTo = $("#inputMailTo").val();
        $("#sendMail" + index).attr("sendto", sendTo);
    }
    /*设置邮件主题  */
    function submitMailTheme() {
        var index = $("#index").val();
        var inputMailThemeVal = $("#inputMailTheme").val();
        $("#sendMail" + index).attr("sendtheme", inputMailThemeVal);
    }
    /*设置邮件内容*/
    function submitMailBody() {
        var index = $("#index").val();
        var mailBodyVal = $("#mailBodyVal").val();
        $("#sendMail" + index).text(mailBodyVal);
    }

    /* 设置default流程线 */
    function lineTypeClick() {
        var lineId = $("#lineId").val();
        var lineTypeText = $("input[name='lineType'][type='radio']:checked").val();
        $("#" + lineId + "LineType").text(lineTypeText);
        if (lineTypeText == "default") {
            $("#lineCondition").val("");
            $("#serviceName").val("");
            $("#conditionExpression").hide();
            $("#serviceExpression").hide();
        } else {
            $("#conditionExpression").show();
        }
        LineTool.prototype.lineTypeSet(lineId);
    }

    var procVersionId =<%=procVersionId%>;
    var deployId =<%=deployId%>;


    jQuery(document).ready(function () {
        $('#tt').tabs({});


        var showModeinParam = {};
        showModeinParam.method = "qryAllCustomTemplate";
        var showModeresult = callRemoteFunction("TaskRepoService", showModeinParam);
        var template_type_list = showModeresult.TEMPLATE_TYPE_LIST;
        var taskList = showModeresult.TASK_TEMPLATE_LIST;
        if (template_type_list === undefined) {

        } else {

            for (var k = 0; k < template_type_list.length; k++) {
                var content = "";
                var webRoot = "<%=WebRoot%>";

                if (taskList === undefined) {

                } else {
                    for (var i = 0; i < taskList.length; i++) {
                        if (taskList[i].TEMPLATE_TYPE == template_type_list[k].TEMPLATE_TYPE) {
                            if (taskList[i].PHOTO_PATH == "" || taskList[i].PHOTO_PATH === undefined) {

                            } else {
                                var taskType = "";
                                if (taskList[i].TASK_TYPE == "U") {
                                    taskType = "userTask";
                                }
                                if (taskList[i].TASK_TYPE == "S") {
                                    taskType = "serviceTask";
                                }
                                var imgName = taskList[i].PHOTO_PATH.substring(taskList[i].PHOTO_PATH.lastIndexOf("/") + 1);
                                var div = '<div id="baseMode' + taskList[i].TEMPLATE_ID + '" divType="mode" style="float:left;margin-top:10px;margin-right:10px;">'
                                        + '<img id="backGroundImg" src="' + webRoot + taskList[i].PHOTO_PATH + '"  style="cursor:pointer;height: 30px;width: 50px;float: left;"/>'
                                        + '<div id="' + imgName + '" taskType="' + taskType + '" taskMode="AD" taskName="' + taskList[i].TEMPLATE_NAME + '" taskId="' + taskList[i].TEMPLATE_ID + '" style="display: inline;line-height: 30px;width: 50px">' + taskList[i].TEMPLATE_NAME + '</div>'
                                        + '</div>';
                                content += div;

                            }

                        }
                    }
                }
                if (content !== "") {
                    $('#accordion').accordion('add', {
                        title: template_type_list[k].TYPE_NAME,
                        content: '<div id="' + template_type_list[k].TEMPLATE_TYPE + '" style="position:relative;white-space:nowrap;background-color:#c7dbfc;height: 100%;width: 100%;overflow: auto;">' + content + '</div>'
                    });
                    if (k != 0) {
                        $("#" + template_type_list[k].TEMPLATE_TYPE).parent("div").hide();
                    }
                }

            }
        }


        varData.procVersionId = procVersionId;
        $("#processId").val("");
        var global = com.xjwgraph.Global;
        graphUtils = com.xjwgraph.Utils.create({

            contextBody: 'contextBody',
            width: document.body.offsetWidth,
            height: document.body.offsetHeight,
            smallMap: 'smallMap',
            mainControl: 'mainControl',
            historyMessage: 'historyMessage',
            prop: 'prop'

        });
        graphUtils.nodeDrag($id("baseLine1"), true, 1);
        graphUtils.nodeDrag($id("baseLine2"), true, 2);
        graphUtils.nodeDrag($id("baseLine3"), true, 3);

        var modes = jQuery("[divType='mode']");
        var modeLength = modes.length;

        for (var i = 0; i < modeLength; i++) {
            graphUtils.nodeDrag(modes[i]);
        }

        document.body.onclick = function () {
            if (!stopEvent) {
                global.modeTool.clear();
            }

        }
        document.onkeydown = KeyDown;

        varData.allActTache = new FastMap();
        varData.actTache = new FastMap();

        if (deployId != null) {
            varData.deployId = deployId;
            /*如果有deployId则说明数据库中存在流程图，查询数据库流程xml开始  */
            var inParam = {};
            inParam.method = "qryBpmContent";
            inParam.DEPLOY_ID = deployId;
            try {
                var result = callRemoteFunction("ProcessDefineService", inParam);
                if (result.BPM_XML != undefined) {
                    graphUtils.loadTextXml(result.BPM_XML);
                }
            }
            catch (e) {
                showError("操作失败!", e);
            }
            /*如果有deployId则说明数据库中存在流程图，查询数据库流程xml结束  */
            /*查询是否有流程变量，有的话将结果返回给 varData.data 开始*/
            var inVarParam = {};
            inVarParam.method = "queryPorcessVarDef";
            inVarParam.DEPLOY_ID = deployId;    //测试用"4210"
            try {
                var varresult = callRemoteFunction("ProcessDefineService", inVarParam);
                var varlist = varresult.PROC_DEF_VAR_LIST;
                varData.data = varlist;
            } catch (e) {
                showError("Error_INITPAGE", e);
            }
            /*查询是否有流程变量，有的话将结果返回给 varData.data 开始*/


            var inTacheVarParam = {};
            inTacheVarParam.method = "queryProcessTacheVar";
            inTacheVarParam.DEPLOY_ID = deployId;    //测试用"4210"
            try {
                var resultParam = callRemoteFunction("ProcessDefineService", inTacheVarParam);
                var tache_list = resultParam.PROC_TEMPLATE_ID_LIST;
                var service_var_list = resultParam.PROC_TACHE_VAR_LIST;
                if (tache_list != undefined && service_var_list != undefined) {
                    for (var i = 0; i < tache_list.length; i++) {
                        var tache_id = tache_list[i].TACHE_ID;
                        var list = [];
                        for (var k = 0; k < service_var_list.length; k++) {
                            if (service_var_list[k].TACHE_ID == tache_id) {
                                list.push(service_var_list[k]);
                            }
                        }
                        varData.allActTache.add(tache_id, list);
                    }
                }
            } catch (e) {
                showError("Error_INITPAGE", e);
            }


            /*查询回退设置开始*/
            var inBackParam = {};
            inBackParam.method = "queryPorcessBackCfgDef";
            inBackParam.DEPLOY_ID = deployId;    //测试用"4210"
            try {
                var backresult = callRemoteFunction("ProcessDefineService", inBackParam);
                var src_tache_list = backresult.BACK_SRC_TACHE_ID_LIST;
                var back_list = backresult.PROC_DEF_BACK_CFG_LIST;
                if (src_tache_list != undefined && back_list != undefined) {
                    for (var i = 0; i < src_tache_list.length; i++) {
                        var src_tache_id = src_tache_list[i].SRC_TACHE_ID;
                        var list = [];
                        for (var k = 0; k < back_list.length; k++) {
                            if (back_list[k].SRC_ACT_ID == src_tache_id) {
                                list.push(back_list[k]);
                            }
                        }
                        varData.exceptionFallBackObj[src_tache_id] = list;
                    }
                }


            } catch (e) {
                showError("Error_INITPAGE", e);
            }
            /*查询回退设置结束始*/


        }
        setTimeout(function () {

            $('body').layout('collapse', 'south');
            $('body').layout('collapse', 'west');

        }, 0);
    });

</script>

</html>
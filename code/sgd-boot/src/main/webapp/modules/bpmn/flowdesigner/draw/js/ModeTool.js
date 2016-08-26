//模元工具. 模元工具文件
var ModeTool = com.xjwgraph.ModeTool = function(a) {
	var b = this;
	b.moveable = false;
	b.optionMode;
	b.baseModeIdIndex = PathGlobal.modeDefIndex;
	b.stepIndex = PathGlobal.modeDefStep;
	b.pathBody = a;
	b.tempMode
};
ModeTool.prototype = {
		initScaling : function(a) {
			var b = this, c = com.xjwgraph.Global.smallTool;
			
			b
					.forEach(function(l) {
						var g = $id(l), f = g.style, h = b.getModeIndex(g), i = $id("content"
								+ h), j = $id("backImg" + h), m = i.style, k = j.style, e = parseInt(parseInt(g.offsetWidth)
								/ a)
								+ "px", d = parseInt(parseInt(g.offsetHeight) / a)
								+ "px";
						f.top = parseInt(parseInt(f.top) / a) + "px";
						f.left = parseInt(parseInt(f.left) / a) + "px";
						m.width = e;
						m.height = d;
						k.width = e;
						k.height = d;
						f.width = e;
						f.height = d;
						b.showPointer(g);
						c.drawMode(g);
					})
					
					
		},
		showMenu : function(a, f) {
			PathGlobal.rightMenu = true;
			var j = this;
			j.tempMode = f.parentNode;
			a = a || window.event;
			if (!a.pageX) {
				a.pageX = a.clientX
			}
			if (!a.pageY) {
				a.pageY = a.clientY
			}
			var e = a.pageX, d = a.pageY, c = com.xjwgraph.Global, b = c.lineTool.pathBody, h = c.baseTool
					.sumLeftTop(b);
			e = e - parseInt(h[0]) + parseInt(b.scrollLeft);
			d = d - parseInt(h[1]) + parseInt(b.scrollTop);
			var g = $id("rightMenu"), i = g.style;
			i.top = d + "px";
			i.left = e + "px";
			i.visibility = "visible";
			i.zIndex = j.getNextIndex()
		},
		showProperty : function(e) {
			var d = com.xjwgraph.Global, c = d.modeMap, b = c.get(this.tempMode.id), h = b.prop, g = document, a = $id("prop"), f = d.clientTool;
			a.style.visibility = "";
			a.innerHTML = "";
//			a.appendChild(f.addProItem(h));
//			f.showDialog(e, PathGlobal.modeProTitle, b)

			var mode=b.id;
			var taskTemplateId=$("#"+b.id).find(" .taskTemplateId").text();

			var taskType=$("#"+b.id).find(" .taskType").text();
			var tache_id=b.id.replace("module","pro");
			if(taskType!="serviceTask"){
				return;
				}
			var listActTache=[];
//			if(taskTemplateId==""){
//					alert("先选择模板");
//					return;
//				}
//				
//			var deployId=varData.deployId;
//			console.log("varData.actTache",varData.actTache.get(taskTemplateId));
//			if(deployId!=null){							//如果存在部署的流程ID则从实例表中查询出结果
//				if(varData.actTache.get(taskTemplateId)==undefined){    //判断存放实例数据的任务模板中有没有数据，没有根据deployId和模板ID从数据库中查询				
//					var inActTacheParam = {};
//		  			inActTacheParam.method= "qryActTacheVar";
//		  			inActTacheParam.DEPLOY_ID = deployId;
//		  			inActTacheParam.TEMPLATE_ID = taskTemplateId;
//		  			
//		  			//try{
//		  			var resultActTache = callRemoteFunction("ProcessDefineService", inActTacheParam); 
//		  			listActTache=resultActTache.list;
//		  		}else{
//		  				listActTache=varData.actTache.get(taskTemplateId);
//		  		}
//			}else{
//				if(varData.actTache.get(taskTemplateId)==undefined){
//						  listActTache=varData.allActTache.get(taskTemplateId);
//					}
//					else{
//		  				listActTache=varData.actTache.get(taskTemplateId);
//		  		}
//			}
			
			/*	if(varData.allActTache.get(tache_id)==undefined){
					var inActTacheParam = {};
		  	 inActTacheParam.method= "qryActTacheVar";		 		
		  	 inActTacheParam.TEMPLATE_ID = taskTemplateId;			  			
		  	 var resultActTache = callRemoteFunction("ProcessDefineService", inActTacheParam);
		  	 var list=resultActTache.SERVICE_VAR_DATA;		  	 
		  		varData.allActTache.add(tache_id,list);
				}*/
				
			
			var url=WebRoot+"modules/bpmn/flowdesigner/draw/serviceParams.jsp";
			if(varData.processType=="subProcess"){
				tache_id=varData.subIndex+"_"+tache_id;
				url=WebRoot+"modules/bpmn/flowdesigner/draw/serviceParams.jsp";
				if(varData.allActTache.get(tache_id)!=undefined){
					listActTache=	varData.allActTache.get(tache_id);		
				}else{					
					listActTache=	varData.subActTache.get(tache_id);			
				}
			}else{
				listActTache=	varData.allActTache.get(tache_id);			
			}
			var input = new Object();
  			//input.list = varData.data;  			
  			input.list =listActTache;
  			input.taskTemplateId =taskTemplateId;
  			input.tache_id =tache_id;
  			var windowTitle=qryWebRes("PARAME_TITLE")
  			showModalWindow(windowTitle,url,800,400  					
  				,input
  				,function(obj){
  				if(obj != null){  	
  					if(varData.processType=="subProcess"){  						
  						varData.subActTache.add(tache_id,obj.list);
  					}
  					else{
  					varData.allActTache.add(tache_id,obj.list);
  					}
  					
  				}
  			});
		},
		removeAll : function() {
			var a = this;
			a.forEach(function(b) {
				a.removeNode(b)
			})
		},
		removeNode : function(d) {
			var c = $id(d);
			if (c) {
				var b = com.xjwgraph.Global, a = b.lineTool.pathBody;
				this.hiddPointer(c);
				b.modeMap.remove(c.id);
				b.smallTool.removeMode(c.id);
				a.removeChild(c)
			}
		},
		cutter : function() {
			var c = this, b = c.tempMode.id, e = com.xjwgraph.Global, g = e.modeMap
					.get(b), f = $id(b), a = e.lineTool.pathBody;
			c.removeNode(b);
			var d = new com.xjwgraph.UndoRedoEvent(function() {
				e.modeMap.put(b, g);
				a.appendChild(f);
				c.showPointer(f);
				e.smallTool.drawMode(f)
			}, PathGlobal.modeCutter);
			d.setRedo(function() {
				c.removeNode(b)
			})
		},
		duplicate : function() {
			var e = com.xjwgraph.Global, c = e.lineTool.pathBody, b = this, a = e.modeTool
					.copy(b.tempMode), f = e.modeMap.get(a.id);
			e.modeTool.hiddPointer(b.tempMode);
			e.smallTool.drawMode(a);
			var d = new com.xjwgraph.UndoRedoEvent(function() {
				b.removeNode(a.id)
			}, PathGlobal.modeDuplicate);
			d.setRedo(function() {
				e.modeMap.put(a.id, f);
				c.appendChild(a);
				b.showPointer(a);
				e.smallTool.drawMode(a)
			})
		},
		del : function() {
			var a = this;
			a.cutter();
			a.tempMode = null
		},
		getNextIndex : function() {
			var a = this;
			a.baseModeIdIndex += a.stepIndex;
			return a.baseModeIdIndex
		},
		setClass : function(b, a) {
			if (b) {
				b.setAttribute("class", a);
				b.setAttribute("className", a)
			}
		},
		//创建拖动的图标
		createBaseMode : function(o, f, a, i, e, l,baseMode) {
    var baseModeTaskTemplateName="";
    var baseModeTaskType="";
    var baseModeTaskTemplateId="";
    var baseImg="";
    var taskMode="";
    if(baseMode===undefined){
    	}
    	else{
     baseModeTaskTemplateName=$(baseMode).find('div').attr('taskName');
     baseModeTaskType=$(baseMode).find('div').attr('taskType');
     baseModeTaskTemplateId=$(baseMode).find('div').attr('taskId');
     taskMode =$(baseMode).find('div').attr('taskMode');
     if(taskMode=="AD"){
    	
     var imgurl=$(baseMode).find('img').attr('src');
     baseImg=imgurl.substring(imgurl.substring(0,imgurl.lastIndexOf("/")).lastIndexOf("/"));
     
     if(varData.processType=="subProcess"){
    	 if(varData.subActTache.get(varData.subIndex+"_pro"+i)==undefined){
        	 var inActTacheParam = {};
        	 inActTacheParam.method= "qryActTacheVar";		 		
        	 inActTacheParam.TEMPLATE_ID = baseModeTaskTemplateId;			  			
        	 var resultActTache = callRemoteFunction("ProcessDefineService", inActTacheParam);
        	 var list=resultActTache.SERVICE_VAR_DATA;		
        	 var allActTacheList=[];
        	 if(list!=undefined){
        	 for (var k=0;k<list.length;k++){
        		 list[k].TACHE_ID=varData.subIndex+"_pro"+i;
        		 allActTacheList.push(list[k]);
        	 }
        	 varData.subActTache.add(varData.subIndex+"_pro"+i,list);
    		}
         }
    	 
     }else{     
     if(varData.allActTache.get("pro"+i)==undefined){
    	 var inActTacheParam = {};
    	 inActTacheParam.method= "qryActTacheVar";		 		
    	 inActTacheParam.TEMPLATE_ID = baseModeTaskTemplateId;			  			
    	 var resultActTache = callRemoteFunction("ProcessDefineService", inActTacheParam);
    	 var list=resultActTache.SERVICE_VAR_DATA;		
    	 var allActTacheList=[];
    	 if(list!=undefined){
    	 for (var k=0;k<list.length;k++){
    		 list[k].TACHE_ID="pro"+i;
    		 allActTacheList.push(list[k]);
    	 		}
    	 varData.allActTache.add("pro"+i,list);
    	 	}
     	}
     }
             
  	}
   }
		var p = this, u = document, j = u.createElement("div"), t = u
					.createElement("div"), q = u.createElement("div"), s = u
					.createElement("img");
		//创建taskTemplateIdDiv,countersignDiv,sequentialDiv
		var taskTemplateIdDiv = u.createElement("div");
			p.setClass(taskTemplateIdDiv, "taskTemplateId");
		var taskTemplateNameDiv = u.createElement("div");
			p.setClass(taskTemplateNameDiv, "taskTemplateName");	
		var countersignDiv = u.createElement("div");
			p.setClass(countersignDiv, "countersign");	
		var sequentialDiv = u.createElement("div");
			p.setClass(sequentialDiv, "sequential");	
		var resultVariableDiv = u.createElement("div");
			p.setClass(resultVariableDiv, "resultVariable");
		var scriptValDiv = u.createElement("div");
			p.setClass(scriptValDiv, "scriptVal");	
		var endpointUrlDiv = u.createElement("div");
			p.setClass(endpointUrlDiv, "endpointUrl");
		var payloadExpressionDiv = u.createElement("div");
			p.setClass(payloadExpressionDiv, "payloadExpression");
		var blockFlagDiv = u.createElement("div");
			p.setClass(blockFlagDiv, "blockFlag");		
		var queueNameDiv = u.createElement("div");
			p.setClass(queueNameDiv, "queueName");	
		var taskTypeDiv = u.createElement("div");
			p.setClass(taskTypeDiv, "taskType");
		var taskImgDiv = u.createElement("div");
			p.setClass(taskImgDiv, "taskImg");		
		var sendMailDiv = u.createElement("div");
			p.setClass(sendMailDiv, "sendMail");			
			j.appendChild(t);
			j.appendChild(taskTemplateIdDiv);
			j.appendChild(taskTemplateNameDiv);
			j.appendChild(resultVariableDiv);
			j.appendChild(countersignDiv);
			j.appendChild(sequentialDiv);
			j.appendChild(scriptValDiv);
			j.appendChild(endpointUrlDiv);
			j.appendChild(payloadExpressionDiv);
			j.appendChild(blockFlagDiv);
			j.appendChild(queueNameDiv);
			j.appendChild(taskTypeDiv);
			j.appendChild(taskImgDiv);
			j.appendChild(sendMailDiv);
			j.appendChild(q);
			q.appendChild(s);
			var n = j.style;
			n.top = o + "px";
			n.left = f + "px";
			n.zIndex = i;
			p.setClass(j, "module");
			 if(taskMode=="AD"){
				 p.setClass(t, "customTitle");
			 }else{
				 p.setClass(t, "title");
			 }
			p.setClass(q, "content");
			j.id = "module" + i;
			t.id = "title" + i;
			q.id = "content" + i;
			taskTemplateIdDiv.id = "taskTemplateId" + i;
			taskTemplateIdDiv.style.display="none";
			taskTemplateNameDiv.id = "taskTemplateName" + i;
			taskTemplateNameDiv.style.display="none";
			resultVariableDiv.id = "resultVariable" + i;
			resultVariableDiv.style.display="none";
			countersignDiv.id = "countersign" + i;
			countersignDiv.style.display="none";
			sequentialDiv.id = "sequential" + i;
			sequentialDiv.style.display="none";
			blockFlagDiv.id = "blockFlag" + i;
			blockFlagDiv.style.display="none";
			scriptValDiv.id = "scriptVal" + i;
			scriptValDiv.style.display="none";
			endpointUrlDiv.id = "endpointUrl" + i;
			endpointUrlDiv.style.display="none";
			payloadExpressionDiv.id = "payloadExpression" + i;
			payloadExpressionDiv.style.display="none";
			queueNameDiv.id = "queueName" + i;
			queueNameDiv.style.display="none";
			taskTypeDiv.id = "taskType" + i;
			taskTypeDiv.style.display="none";
			taskImgDiv.id = "taskImg" + i;
			taskImgDiv.style.display="none";
			sendMailDiv.id = "sendMail" + i;
			sendMailDiv.style.display="none";
			$(sendMailDiv).attr("sendto","");                      //初始化收件人
			$(sendMailDiv).attr("sendtheme","");	               //初始化主题
			/*对于自定义任务图标初始化加载任务名称，任务ID，任务类型到界面元素中开始*/
			$(taskTemplateIdDiv).text(baseModeTaskTemplateId);
			$(taskTemplateNameDiv).text(baseModeTaskTemplateName);
			$(taskTypeDiv).text(baseModeTaskType);		
			$(taskImgDiv).text(baseImg);
			$(t).text(baseModeTaskTemplateName);
			/*对于自定义任务图标初始化加载任务名称，任务ID，任务类型到界面元素中结束*/
			$(blockFlagDiv).text("N");//初始化阻塞标志为N，为serviceTask使用
			s.id = "backImg" + i;		
			//当创建的图标类型为TASK的时候，图标宽度设置为100px			
			//var imgName=a.substring((a.lastIndexOf("/") + 1), a.lastIndexOf("."));
			var moduleType=baseModeTaskType;
			if(moduleType.substring(moduleType.length-4)=="Task"||moduleType=="subProcess")
			{
				e="100px";
			}
			s.style.width = e;
			s.style.height = l;
			s.src = a;
			$(s).attr("taskType",""+baseModeTaskType);
			var k = u.createElement("div"), d = u.createElement("div"), m = u
					.createElement("div"), c = u.createElement("div"), h = u
					.createElement("div"), b = u.createElement("div"), g = u
					.createElement("div"), r = u.createElement("div");
			p.setClass(k, "top_left");
			p.setClass(d, "top_middle");
			p.setClass(m, "top_right");
			p.setClass(c, "middle_left");
			p.setClass(h, "middle_right");
			p.setClass(b, "bottom_left");
			p.setClass(g, "bottom_middle");
			p.setClass(r, "bottom_right");
			k.id = "top_left" + i;
			d.id = "top_middle" + i;
			m.id = "top_right" + i;
			c.id = "middle_left" + i;
			h.id = "middle_right" + i;
			b.id = "bottom_left" + i;
			g.id = "bottom_middle" + i;
			r.id = "bottom_right" + i;
			j.appendChild(k);
			j.appendChild(d);
			j.appendChild(m);
			j.appendChild(c);
			j.appendChild(h);
			j.appendChild(b);
			j.appendChild(g);
			j.appendChild(r);
			return j;
		},
		//将图元区选中的图标创建到绘图区
		create : function(f, c, j,baseMode) {
			
			if(flowguide != "y" && varData.procVersionId==null){
				//showMessage(qryWebRes("PROCESS_VERSION_CHECK"));
				console.log(12);
				fish.error("fdfd");
				return;
			}
			var taskType=$(baseMode).find('div').attr('taskType');
			/*创建mode图标的同时将mode所用到的input显示，同时隐藏line的input框开始*/
			$("#modelAttr").show();
			$("#lineAttr").hide();
			var taskText=$("#taskText");
			/*创建mode图标的同时将mode所用到的input显示，同时隐藏line的input框结束*/
			//判断图标所选类型，如果是任务类型则在辅助区中增加任务模板ID
			BaseTool.prototype.showtaskTemplate(taskType);
//			var img=j.substring((j.lastIndexOf("/") + 1), j.lastIndexOf("."));
			
//			if(img=="serviceTask"){
//				$("#taskText").text("任务模板");
//				$("[name='taskTemplate']").show();
//				$("[name='inputTitle']").show();
//				$("#countersign").hide();
//				$("#Variable").hide();
//				$("#subProcessButton").hide();
//				$("#script").hide();
//			}
//			else if(img=="userTask"){
//				$("#taskText").text("任务模板");
//				$("[name='taskTemplate']").show();
//				$("[name='inputTitle']").show();
//				$("#countersign").show();
//				$("#Variable").hide();
//				$("#subProcessButton").hide();
//				$("#script").hide();
//			}
//			else if(img=="businessRuleTask"){
//				$("#taskText").text("规则名称");
//				$("[name='taskTemplate']").show();
//				$("[name='inputTitle']").show();
//				$("#Variable").show();
//				$("#countersign").hide();
//				$("#subProcessButton").hide();
//				$("#script").hide();
//			}
//			else if(img=="scriptTask"){
//				$("[name='taskTemplate']").hide();
//				$("[name='inputTitle']").show();
//				$("#Variable").hide();
//				$("#countersign").hide();
//				$('#isSequential').hide();
//				$("#subProcessButton").hide();
//				$("#script").show();
//			}
//			else if(img=="esbTask"){
//				$("[name='taskTemplate']").hide();
//				$("[name='inputTitle']").show();
//				$("#Variable").hide();
//				$("#countersign").hide();
//				$('#isSequential').hide();
//				$("#subProcessButton").hide();
//				$("#script").hide();
//			}
//			else if(img=="subProcess"){
//				$("[name='taskTemplate']").hide();
//				$("[name='inputTitle']").show();
//				$("#countersign").hide();
//				$("#Variable").hide();
//				$("#subProcessButton").show();
//				$("#script").hide();
//			}
//			else{
//				$("[name='taskTemplate']").hide();
//				$("[name='inputTitle']").hide();
//				$("#countersign").hide();
//				$("#Variable").hide();
//				$("#subProcessButton").hide();
//				$("#script").hide();
//			}
			var k = this, i = k.getNextIndex(), g = document, h = k.createBaseMode(
					f, c, j, i, "50px", "50px",baseMode);
			k.pathBody.appendChild(h);
			var d = new BaseMode();
			d.id = h.id;			
			var b = com.xjwgraph.Global;
			b.modeMap.put(d.id, d);
			this.initEvent(i);
			b.smallTool.drawMode(h);
			var e = b.modeTool;
			e.flip(i);
			var a = new com.xjwgraph.UndoRedoEvent(function() {
				if ($id(h.id)) {
					b.smallTool.removeMode(h.id);
					e.pathBody.removeChild(h);
					b.modeMap.remove(h.id)
				}
			}, PathGlobal.modeCreate);
			a.setRedo(function() {
				b.modeMap.put(d.id, d);
				e.pathBody.appendChild(h);
				e.showPointer(h);
				e.changeBaseModeAndLine(h, true);
				b.smallTool.drawMode(h)
			})
		},
		initEvent : function(a) {
			var b = com.xjwgraph.Global.modeTool;
			b.drag($id("content" + a));
			b.dragPoint($id("top_left" + a));
			b.dragPoint($id("top_middle" + a));
			b.dragPoint($id("top_right" + a));
			b.dragPoint($id("middle_left" + a));
			b.dragPoint($id("middle_right" + a));
			b.dragPoint($id("bottom_left" + a));
			b.dragPoint($id("bottom_middle" + a));
			b.dragPoint($id("bottom_right" + a));
			$id("content" + a).onclick = function() {
				b.showPointer($id("module" + a))
			}
		},
		clear : function() {
			
			var b = com.xjwgraph.Global, a = b.modeTool;
			this.forEach(function(c) {
				a.hiddPointer($id(c))
			});
			b.smallTool.clear()
		},
		toTop : function() {
			var a = com.xjwgraph.Global.modeTool;
			this.forEach(function(d) {
				var c = $id(d), b = c.style;
				if (b.visibility == "visible") {
					b.zIndex = a.getNextIndex()
				} else {
					if (b.zIndex < 1) {
						b.zIndex = 0
					} else {
						b.zIndex = b.zIndex - 1
					}
				}
			})
		},
		toBottom : function() {
			this.forEach(function(c) {
				var b = $id(c), a = b.style;
				if (a.visibility == "visible") {
					a.zIndex = 0;
				} else {
					if (a.zIndex == 0) {
						a.zIndex = 1;
					}
				}
			});
			stopEvent = true
		},
		forEach : function(d) {
			var a = com.xjwgraph.Global.modeMap.getKeys(), b = a.length;
			for ( var c = b; c--;) {
				if (d) {
					d(a[c]);
				}
			}
			stopEvent = true;
		},
		hiddPointer : function(e) {
			var d = this.getModeIndex(e);
			$id("module" + d).style.visibility = "hidden";
			$id("top_left" + d).style.visibility = "hidden";
			$id("top_middle" + d).style.visibility = "hidden";
			$id("top_right" + d).style.visibility = "hidden";
			$id("middle_left" + d).style.visibility = "hidden";
			$id("middle_right" + d).style.visibility = "hidden";
			$id("bottom_left" + d).style.visibility = "hidden";
			$id("bottom_middle" + d).style.visibility = "hidden";
			$id("bottom_right" + d).style.visibility = "hidden";
			var c = $id("rightMenu");
			c.style.visibility = "hidden";
			PathGlobal.rightMenu = false;
			var a = $id("topCross"), b = $id("leftCross");
			a.style.visibility = "hidden";
			b.style.visibility = "hidden";
			com.xjwgraph.Global.smallTool.clearMode($id("small" + e.id))
		},
		getModeIndex : function(b) {
			var a;
			
			if (b.className == "module") {
				a = 6
			} else {
				if (b.className == "content") {
					a = 7
				}
			}
			return b.id.substr(a)
		},
		showPointer : function(a) {
			this.showPointerId(this.getModeIndex(a))
		},
		showPointerId : function(i) {			
			var d = $id("smallmodule" + i), p = com.xjwgraph.Global;
			if (d) {
				var a = d.style;
				a.borderWidth = "1px";
				a.borderColor = p.smallTool.checkColor;
				a.borderStyle = "solid";
			}
			var e = $id("module" + i);
			e.style.visibility = "visible";
			var r = $id("top_left" + i), b = r.style, n = r.offsetHeight, f = r.offsetWidth, k = e.offsetHeight, c = e.offsetWidth;
			/*注释掉绘图区图元拉伸事件
			$id("title" + i).style.width = c + "px";
			b.top = -n / 2 + "px";
			b.left = -f / 2 + "px";
			b.visibility = "visible";
			var g = $id("top_middle" + i).style;
			g.top = -n / 2 + "px";
			g.left = c / 2 - f / 2 + "px";
			g.visibility = "visible";
			var q = $id("top_right" + i).style;
			q.top = -n / 2 + "px";
			q.left = c - f / 2 + "px";
			q.visibility = "visible";
			var l = $id("middle_left" + i).style;
			l.top = k / 2 - n / 2 + "px";
			l.left = -f / 2 + "px";
			l.visibility = "visible";
			var h = $id("middle_right" + i).style;
			h.top = k / 2 - n / 2 + "px";
			h.left = c - f / 2 + "px";
			h.visibility = "visible";
			var o = $id("bottom_left" + i).style;
			o.top = k - n / 2 + "px";
			o.left = -f / 2 + "px";
			o.visibility = "visible";
			var s = $id("bottom_middle" + i).style;
			s.top = k - n / 2 + "px";
			s.left = c / 2 - f / 2 + "px";
			s.visibility = "visible";
			var m = $id("bottom_right" + i).style;
			m.top = k - n / 2 + "px";
			m.left = c - f / 2 + "px";
			m.visibility = "visible";
			var j = $id("backImg" + i).style;
			j.width = (c - 2) + "px";
			j.height = (k - 2) + "px";
			j.top = "0px";
			j.left = "0px";*/
			p.controlTool.print(i);
		},
		drag : function(f) {
			var b = f.parentNode, e = b.style, c = com.xjwgraph.Global, a = c.modeTool, d = c.lineTool;
			f.ondragstart = function() {
				return false
			};
			f.onclick = function() {
				d.clear();
				a.clear();
				a.showPointer(b)
			};
			f.ondblclick = function() {
				a.hiddPointer(b);
				a.flip(c.modeTool.getModeIndex(b))
			};
			f.onmousemove = function() {
				if (d.moveable) {
					a.showPointerId(c.modeTool.getModeIndex(b));
				}
			};
			f.onmouseout = function() {
				if (d.moveable) {
					a.hiddPointer(b);
				}
			};
			f.oncontextmenu = function(g) {
				a.showMenu(g, f);
				return false
			};
			//选中绘图区图标事件 first
			f.onmousedown = function(j) {
				/*创建mode图标的同时将mode所用到的input显示，同时隐藏line的input框开始*/
				$("#modelAttr").show();
				$("#lineAttr").hide();
				/*创建mode图标的同时将mode所用到的input显示，同时隐藏line的input框结束*/
				d.clear();
				a.clear();
				a.isModeCross(b);
				a.moveable = true;
				j = j || window.event;
				a.showPointer(b);
				if (b.setCapture) {
					b.setCapture();
				} else {
					if (window.captureEvents) {
						window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
					}
				}
				//h表示鼠标点击时的坐标x，l表示y坐标
				var h = j.layerX && j.layerX >= 0 ? j.layerX : j.offsetX, l = j.layerX
						&& j.layerY >= 0 ? j.layerY : j.offsetY;
				stopEvent = true;
				var k = document, g = parseInt(b.offsetLeft), i = parseInt(b.offsetTop);
				k.onmousemove = function(p) {
					p = p || window.event;
					if (a.moveable) {
						if (!p.pageX) {
							p.pageX = p.clientX
						}
						if (!p.pageY) {
							p.pageY = p.clientY
						}
						var n = p.pageX - h, m = p.pageY - l, o = c.lineTool.pathBody, q = c.baseTool
								.sumLeftTop(o);
						n = n - parseInt(q[0]) + parseInt(o.scrollLeft);
						m = m - parseInt(q[1]) + parseInt(o.scrollTop);
						e.left = n + "px";
						e.top = m + "px";
						a.isModeCross(b);
						a.changeBaseModeAndLine(b, true);
						a.showPointer(b);
						c.smallTool.drawMode(b);
					}
				};
				k.onmouseup = function(n) {
					n = n || window.event;
					a.moveable = false;
					if (b.releaseCapture) {
						b.releaseCapture()
					} else {
						if (window.releaseEvents) {
							window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
						}
					}
					k.onmousemove = null;
					k.onmouseup = null;
					var p = parseInt(b.offsetLeft), o = parseInt(b.offsetTop);
					if (g != p || i != o) {
						var m = new com.xjwgraph.UndoRedoEvent(function() {
							b.style.left = g + "px";
							b.style.top = i + "px";
							a.showPointer(b);
							a.changeBaseModeAndLine(b, true);
							c.smallTool.drawMode(b)
						}, PathGlobal.modeMove);
						m.setRedo(function() {
							e.left = p + "px";
							e.top = o + "px";
							a.showPointer(b);							
							a.changeBaseModeAndLine(b, true);
							c.smallTool.drawMode(b)
						})
					}
					
				}
			}
		},
		findModeLine : function(g, b) {
			var a = com.xjwgraph.Global.modeMap.get(g.id), f = a.lineMap, h = f
					.getKeys(), d = h.length;
			for ( var c = d; c--;) {
				var e = f.get(h[c]);
				if (b.id == e.id) {
					return e
				}
			}
			return null
		},
		changeLineType : function(k, f, b) {
			var i = this, j = com.xjwgraph.Global.lineMap.get(k.id), a = j.xBaseMode, h = j.wBaseMode, d, e;
			if (a) {
				d = $id(a.id)
			}
			if (h) {
				e = $id(h.id)
			}
			if (a && a.id == f.id) {
				if (e && d) {
					var c = i.findModeLine(e, k);
					if (e.offsetTop - (d.offsetTop + d.offsetHeight) > 0) {
						b.index = PathGlobal.pointTypeDown;
						c.index = PathGlobal.pointTypeUp
					} else {
						if (d.offsetTop - (e.offsetTop + e.offsetHeight) > 0) {
							b.index = PathGlobal.pointTypeUp;
							c.index = PathGlobal.pointTypeDown
						} else {
							if (d.offsetLeft - (e.offsetLeft + e.offsetWidth) > 0) {
								b.index = PathGlobal.pointTypeLeft;
								c.index = PathGlobal.pointTypeRight
							} else {
								if (e.offsetLeft - (d.offsetLeft + d.offsetWidth) > 0) {
									b.index = PathGlobal.pointTypeRight;
									c.index = PathGlobal.pointTypeLeft
								}
							}
						}
					}
					this.changeBaseModeAndLine(e, false)
				}
			}
			if (h && h.id == f.id) {
				if (e && d) {
					var g = i.findModeLine(d, k);
					if (d.offsetTop - (e.offsetTop + e.offsetHeight) > 0) {
						b.index = PathGlobal.pointTypeDown;
						g.index = PathGlobal.pointTypeUp
					} else {
						if (e.offsetTop - (d.offsetTop + d.offsetHeight) > 0) {
							b.index = PathGlobal.pointTypeUp;
							g.index = PathGlobal.pointTypeDown
						} else {
							if (e.offsetLeft - (d.offsetLeft + d.offsetWidth) > 0) {
								b.index = PathGlobal.pointTypeLeft;
								g.index = PathGlobal.pointTypeRight
							} else {
								if (d.offsetLeft - (e.offsetLeft + e.offsetWidth) > 0) {
									b.index = PathGlobal.pointTypeRight;
									g.index = PathGlobal.pointTypeLeft
								}
							}
						}
					}
					i.changeBaseModeAndLine(d, false)
				}
			}
			return b
		},
		changeBaseModeAndLine : function(j, l) {
			var q = this, o = 0, m = 0, b = com.xjwgraph.Global, g = b.modeMap
					.get(j.id), n = g.lineMap, d = n.getKeys(), a = d.length;
			for ( var e = a; e--;) {
				var c = n.get(d[e]), r = $id(c.id);
				if (r) {
					if (l && PathGlobal.isAutoLineType) {
						q.changeLineType(r, j, c);
					}
					var p = j.offsetWidth, f = j.offsetHeight;					
					if (c.index == PathGlobal.pointTypeUp) {
						o = 0;
						m = p / 2
					} 
					else if (c.index == PathGlobal.pointTypeLeft) {
							o = f / 2;
							m = 0
						} 
					else if (c.index == PathGlobal.pointTypeDown) {
							o = f;
							m = p / 2
						} 
					else if (c.index == PathGlobal.pointTypeRight) {
							o = f / 2;
							m = p
						}
							
					o += parseInt(j.offsetTop);
					m += parseInt(j.offsetLeft);
					var k = b.lineTool;
					k.pathLine(m, o, r, c.type);
					k.setDragPoint(r)
				}
			}
		},
		dragPoint : function(a) {
			var b = com.xjwgraph.Global;
			a.onmousedown = function(q) {
				var f = a.parentNode, g = f.style, l = b.modeTool;
				l.isModeCross(f);
				var k = b.modeTool.getModeIndex(f), m = $id("backImg" + k), d = m.style, o = $id("content"
						+ k), p = o.style, h = a.className, e = f.offsetTop, t = f.offsetLeft, n = f.offsetWidth, c = f.offsetHeight, r = new com.xjwgraph.UndoRedoEvent(
						function() {
							g.left = t + "px";
							g.top = e + "px";
							g.width = n + "px";
							g.height = c + "px";
							p.left = "0px";
							p.top = "0px";
							p.width = n + "px";
							p.height = c + "px";
							d.left = "0px";
							d.top = "0px";
							d.width = n + "px";
							d.height = c + "px";
							l.showPointer(f);
							l.changeBaseModeAndLine(f, true);							
							b.smallTool.drawMode(f)
						}, PathGlobal.modeDragPoint);
				b.modeTool.moveable = true;
				q = q || window.event;
				if (f.setCapture) {
					f.setCapture()
				} else {
					if (window.captureEvents) {
						window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
					}
				}
				var j = q.layerX && q.layerX >= 0 ? q.layerX : q.offsetX, i = q.layerX
						&& q.layerY >= 0 ? q.layerY : q.offsetY;
				stopEvent = true;
				var s = document;
				s.onmousemove = function(v) {
					v = v || window.event;
					if (b.modeTool.moveable) {
						if (!v.pageX) {
							v.pageX = v.clientX
						}
						if (!v.pageY) {
							v.pageY = v.clientY
						}
						var A = v.pageX - j, y = v.pageY - i, x = b.lineTool.pathBody, C = b.baseTool
								.sumLeftTop(x);
						A = A - parseInt(C[0]) + parseInt(x.scrollLeft);
						y = y - parseInt(C[1]) + parseInt(x.scrollTop);
						var w = PathGlobal.minWidth, D = PathGlobal.minHeight;
						if ("bottom_right" == h) {
							if ((parseInt(A) - parseInt(f.offsetLeft)) < w) {
								d.width = w + "px";
								g.width = w + "px";
								p.width = w + "px"
							} else {
								d.width = parseInt(A) - parseInt(f.offsetLeft)
										+ "px";
								g.width = parseInt(A) - parseInt(f.offsetLeft)
										+ "px";
								p.width = parseInt(A) - parseInt(f.offsetLeft)
										+ "px"
							}
							if ((parseInt(y) - parseInt(f.offsetTop)) < D) {
								d.height = D + "px";
								g.height = D + "px";
								p.height = D + "px"
							} else {
								d.height = parseInt(y) - parseInt(f.offsetTop)
										+ "px";
								g.height = parseInt(y) - parseInt(f.offsetTop)
										+ "px";
								p.height = parseInt(y) - parseInt(f.offsetTop)
										+ "px"
							}
						} else {
							if ("bottom_middle" == h) {
								if ((parseInt(y) - parseInt(f.offsetTop)) < D) {
									d.height = D + "px";
									g.height = D + "px";
									p.height = D + "px"
								} else {
									d.height = parseInt(y) - parseInt(f.offsetTop)
											+ "px";
									g.height = parseInt(y) - parseInt(f.offsetTop)
											+ "px";
									p.height = parseInt(y) - parseInt(f.offsetTop)
											+ "px"
								}
							} else {
								if ("bottom_left" == h) {
									if (parseInt(y) - parseInt(f.offsetTop) < D) {
										d.height = D + "px";
										g.height = D + "px";
										p.height = D + "px"
									} else {
										d.height = parseInt(y)
												- parseInt(f.offsetTop) + "px";
										g.height = parseInt(y)
												- parseInt(f.offsetTop) + "px";
										p.height = parseInt(y)
												- parseInt(f.offsetTop) + "px"
									}
									var z = 0;
									if (parseInt(A) > parseInt(t)) {
										z = parseInt(A) - parseInt(t);
										z = parseInt(n) - z;
										if (z <= w) {
											z = w;
											A = parseInt(n) - w + parseInt(t)
										}
									} else {
										z = parseInt(t) - parseInt(A);
										z = parseInt(n) + z;
										if (z <= w) {
											z = w;
											A = w - parseInt(n) + parseInt(t)
										}
									}
									d.width = z + "px";
									g.width = z + "px";
									p.width = z + "px";
									g.left = parseInt(A) + "px"
								} else {
									if ("middle_right" == h) {
										if (parseInt(A) - parseInt(f.offsetLeft) < w) {
											d.width = w + "px";
											g.width = w + "px";
											p.width = w + "px"
										} else {
											d.width = parseInt(A)
													- parseInt(f.offsetLeft) + "px";
											g.width = parseInt(A)
													- parseInt(f.offsetLeft) + "px";
											p.width = parseInt(A)
													- parseInt(f.offsetLeft) + "px"
										}
									} else {
										if ("middle_left" == h) {
											var z = 0;
											if (parseInt(A) > parseInt(t)) {
												z = parseInt(A) - parseInt(t);
												z = parseInt(n) - z;
												if (z <= w) {
													z = w;
													A = parseInt(n) - w
															+ parseInt(t)
												}
											} else {
												z = parseInt(t) - parseInt(A);
												z = parseInt(n) + z;
												if (z <= w) {
													z = w;
													A = w - parseInt(n)
															+ parseInt(t)
												}
											}
											d.width = z + "px";
											g.width = z + "px";
											p.width = z + "px";
											g.left = parseInt(A) + "px"
										} else {
											if ("top_right" == h) {
												var z = 0;
												if (parseInt(A) > (parseInt(t) + parseInt(n))) {
													z = parseInt(A)
															- (parseInt(t) + parseInt(n));
													z = parseInt(n) + z;
													if (z <= w) {
														z = w
													}
												} else {
													z = (parseInt(t) + parseInt(n))
															- parseInt(A);
													z = parseInt(n) - z;
													if (z <= w) {
														z = w
													}
												}
												d.width = z + "px";
												g.width = z + "px";
												p.width = z + "px";
												var u = 0;
												if (parseInt(y) > parseInt(e)) {
													u = parseInt(y) - parseInt(e);
													u = parseInt(c) - parseInt(u);
													if (u <= D) {
														u = D;
														y = parseInt(c) - D
																+ parseInt(e)
													}
												} else {
													u = parseInt(e) - parseInt(y);
													u = parseInt(c) + parseInt(u);
													if (u <= D) {
														u = D;
														y = D - parseInt(c)
																+ parseInt(e)
													}
												}
												d.height = u + "px";
												g.height = u + "px";
												p.height = u + "px";
												g.top = parseInt(y) + "px"
											} else {
												if ("top_middle" == h) {
													var u = 0;
													if (parseInt(y) > parseInt(e)) {
														u = parseInt(y)
																- parseInt(e);
														u = parseInt(c)
																- parseInt(u);
														if (u <= D) {
															u = D;
															y = parseInt(c)
																	+ parseInt(e)
																	- D
														}
													} else {
														u = parseInt(e)
																- parseInt(y);
														u = parseInt(c)
																+ parseInt(u);
														if (u <= D) {
															u = D;
															y = D - parseInt(c)
																	+ parseInt(e)
														}
													}
													d.height = u + "px";
													p.height = u + "px";
													g.height = u + "px";
													g.top = parseInt(y) + "px"
												} else {
													if ("top_left" == h) {
														var z = 0;
														if (parseInt(A) > parseInt(t)) {
															z = parseInt(A)
																	- parseInt(t);
															z = parseInt(n) - z;
															if (z <= w) {
																z = w;
																A = parseInt(n)
																		- w
																		+ parseInt(t)
															}
														} else {
															z = parseInt(t)
																	- parseInt(A);
															z = parseInt(n) + z;
															if (z <= w) {
																z = w;
																A = w
																		- parseInt(n)
																		+ parseInt(t)
															}
														}
														d.width = z + "px";
														g.width = z + "px";
														p.width = z + "px";
														var u = 0;
														if (parseInt(y) > parseInt(e)) {
															u = parseInt(y)
																	- parseInt(e);
															u = parseInt(c)
																	- parseInt(u);
															if (u <= D) {
																u = D;
																y = parseInt(c)
																		+ parseInt(e)
																		- D
															}
														} else {
															u = parseInt(e)
																	- parseInt(y);
															u = parseInt(c)
																	+ parseInt(u);
															if (u <= D) {
																u = D;
																y = D
																		- parseInt(c)
																		+ parseInt(e)
															}
														}
														d.height = u + "px";
														g.height = u + "px";
														p.height = u + "px";
														g.top = parseInt(y) + "px";
														g.left = parseInt(A) + "px"
													}
												}
											}
										}
									}
								}
							}
						}
						var B = b.modeTool;
						B.isModeCross(f);
						B.showPointer(f);
						B.changeBaseModeAndLine(f, true);
						b.smallTool.drawMode(f)
					}
				};
				s.onmouseup = function(v) {
					v = v || window.event;
					b.modeTool.moveable = false;
					if (f.releaseCapture) {
						f.releaseCapture()
					} else {
						if (window.releaseEvents) {
							window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
						}
					}
					var x = f.offsetTop, y = f.offsetLeft, w = f.offsetWidth, u = f.offsetHeight;
					r.setRedo(function() {
						g.left = y + "px";
						g.top = x + "px";
						g.width = w + "px";
						g.height = u + "px";
						d.left = "0px";
						d.top = "0px";
						d.width = w + "px";
						d.height = u + "px";
						p.top = "0px";
						p.left = "0px";
						p.width = w + "px";
						p.height = u + "px";
						l.showPointer(f);
						l.changeBaseModeAndLine(f, true);
						b.smallTool.drawMode(f)
					});
					s.onmousemove = null;
					s.onmouseup = null
				}
			}
		},
		isActiveMode : function(a) {
			return a.style.visibility == "visible"
		},
		getActiveMode : function() {
			var b, a = com.xjwgraph.Global.modeTool;
			
			this.forEach(function(d) {
				var c = $id(d);
				if (a.isActiveMode(c)) {
					b = c
				}
			});
			return b
		},
		getSonNode : function(e, b) {
			for ( var c = e.firstChild; c != null; c = c.nextSibling) {
				if (c.nodeType == 1) {
					var d = c.className;
					if (d == b) {
						return c
					}
					if (d == "content" && b == "backImg") {
						for ( var a = c.firstChild; a != null; a = a.nextSibling) {
							if (a.nodeType == 1) {
								return a
							}
						}
					}
				}
			}
		},
		setIndex : function(e, d) {
			e.id = "module" + d;
			for ( var b = e.firstChild; b != null; b = b.nextSibling) {
				if (b.nodeType == 1) {
					var c = b.className;
					b.id = c + d;
					if (c == "content") {
						for ( var a = b.firstChild; a != null; a = a.nextSibling) {
							if (a.nodeType == 1) {
								a.id = "backImg" + d;
								break
							}
						}
					}
				}
			}
			return e
		},
		copy : function(h) {
			var c = this, b = h.cloneNode(true), g = c.getNextIndex();
			c.setIndex(b, g);
			var e = b.style;
			e.left = parseInt(e.left) + PathGlobal.copyModeDec + "px";
			e.top = parseInt(e.top) + PathGlobal.copyModeDec + "px";
			var a = new BaseMode();
			a.id = b.id;
			var d = com.xjwgraph.Global;
			d.modeMap.put(a.id, a);
			var f = d.lineTool;
			f.pathBody.appendChild(b);
			this.initEvent(g);
			return b
		},
		//图片滑动翻转动画效果
		flip : function(g, a,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal) {
			var c = com.xjwgraph.Global.modeMap.get("module" + g);
			if (c.isFilp) {
				return
			}
			
			c.isFilp = true;
			var f = $id("backImg" + g), e = f.height, d = $id("content" + g), i = d.style;
			c.modeHeigh = e;
			i.width = f.width + "px";
			i.fontSize = (e - parseInt(e * 0.15)) + "px";
			i.lineHeight = e + "px";
			i.height = e + "px";
			var b = $id(c.id), h = b.style;
			h.width = f.width + "px";
			h.height = e + "px";
			c.inc = c.modeHeigh / 10;			
			this.doFlip(g, a,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal);
		},
		doFlip : function(h, d,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal) {

			var b = $id("backImg" + h);
			if (!b) {
				return
			}
			var e = com.xjwgraph.Global, c = b.height, g = e.modeMap.get("module"
					+ h);
			c = c - g.inc;
			if (c < 1) {
				c = 1
			}
			if (c <= 1) {
				g.inc = -g.inc
			} else {
				if (c >= g.modeHeigh) {
					$id("backImg" + h).style.height = g.modeHeigh + "px";
					g.modeHeigh = 0;
					g.isFilp = false;
					g.inc = -g.inc;
					var a = $id("content" + h).style;
					a.width = 0 + "px";
					a.height = 0 + "px";
					a.lineHeight = 0 + "px";
					a.fontSize = 0 + "px";
					var f = $id("title" + h);
					var taskTemplateIdDiv=$id("taskTemplateId"+h);	
					var taskTemplateNameDiv=$id("taskTemplateName"+h);	
					var resultVariableDiv=$id("resultVariable"+h);	
					var countersignDiv=$id("countersign"+h);
					var sequentialDiv=$id("sequential"+h);
					if (d == "undefined" || !d) {
					} else {
						f.innerHTML = d;
					}
					
					
					//判断TaskTemplate值是否存在，
					if (TaskTemplateIdHTML == 'undefined' || !TaskTemplateIdHTML) {
					} else {
						taskTemplateIdDiv.innerHTML = TaskTemplateIdHTML;
					}
					if (TaskTemplateNameHTML == 'undefined' || !TaskTemplateNameHTML) {
					} else {
						taskTemplateNameDiv.innerHTML = TaskTemplateNameHTML;
					}
					if (resultVariableHTML == 'undefined' || !resultVariableHTML) {
					} else {
						resultVariableDiv.innerHTML = resultVariableHTML;
					}
					if (countersignVal == 'undefined' || !countersignVal) {
					} else {
						countersignDiv.innerHTML = countersignVal;
					}
					if (sequentialVal == 'undefined' || !sequentialVal) {
					} else {
						sequentialDiv.innerHTML = sequentialVal;
					}
					this.showPointerId(h);
					return
				} else {
					$id("backImg" + h).style.height = c + "px";
				}
			}
			setTimeout(function() {
				e.modeTool.doFlip(h, d,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal)
			}, PathGlobal.pauseTime)
		},
		isModeCross : function(k) {
			var e = parseInt(k.offsetLeft), c = k.offsetWidth + e, a = parseInt(k.offsetWidth / 2)
					+ e, h = parseInt(k.offsetTop), l = k.offsetHeight + h, g = parseInt(k.offsetHeight / 2)
					+ h, n = $id("leftCross"), w = $id("topCross"), u = n.style, x = w.style, d = com.xjwgraph.Global.modeMap
					.getKeys(), s = false, j = false, m = d.length;
			for ( var t = m; t--;) {
				var o = $id(d[t]);
				if (k.id == o.id) {
					continue
				}
				var f = parseInt(o.offsetLeft), q = o.offsetWidth + f, b = parseInt(o.offsetWidth / 2)
						+ f, p = parseInt(o.offsetTop), r = o.offsetHeight + p, v = parseInt(o.offsetHeight / 2)
						+ p;
				if (e == f || e == q) {
					n.style.left = e;
					j = true;
					n.style.visibility = "visible"
				}
				if (c == f || c == q) {
					u.left = c;
					j = true;
					u.visibility = "visible"
				}
				if (a == f || a == b) {
					u.left = a;
					j = true;
					u.visibility = "visible"
				}
				if (a == q) {
					u.left = a;
					j = true;
					u.visibility = "visible"
				}
				if (e == b || c == b) {
					u.left = b;
					j = true;
					u.visibility = "visible"
				}
				if (h == p || h == r) {
					x.top = h;
					s = true;
					x.visibility = "visible"
				}
				if (h == v || v == g || v == l) {
					x.top = v;
					s = true;
					x.visibility = "visible"
				}
				if (g == p || g == r) {
					x.top = g;
					s = true;
					x.visibility = "visible"
				}
				if (v == g) {
					x.top = v;
					s = true;
					x.visibility = "visible"
				}
				if (l == p || l == r) {
					x.top = l;
					s = true;
					x.visibility = "visible"
				}
			}
			if (!s) {
				x.visibility = "hidden"
			}
			if (!j) {
				u.visibility = "hidden"
			}
		},
		showServiceTaskParams : function(e) {
		var d = com.xjwgraph.Global, c = d.modeMap, b = c.get(this.tempMode.id), h = b.prop, g = document, a = $id("prop"), f = d.clientTool;
			a.style.visibility = "";
			a.innerHTML = "";
			a.appendChild(f.addProItem(h));
			f.showDialog(e, PathGlobal.modeProTitle, b)
		}
	};
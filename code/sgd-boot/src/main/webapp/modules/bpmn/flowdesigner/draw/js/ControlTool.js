var ControlTool = com.xjwgraph.ControlTool = function(mainBody) {
	var self = this, doc = document;
	self.mainBody = mainBody;
	self.indexId;
	var inputFunction = function(inputText, isNoStyle,radioName) {
		
		inputText.onkeydown = function(event) {
			event = event || window.event;
			
			if (event.keyCode == 13) {
				self.submit();
			}
		};
		if(inputText.length>0){
			//userTask中的radio在change事件后的属性
			for(var i=0;i<inputText.length;i++){
			inputText[i].onclick = function() {		
				if(radioName=="countersign"){
				var countersign = $(":radio[name='countersign']:checked").val();
				if (countersign == "true") {					
					$('#isSequential').show();						
					$(":radio[name='countersign'][value='true']").attr("checked", true);
					$(":radio[name='countersign'][value='false']").attr("checked", false);
					$(":radio[name='sequential'][value='true']").attr("checked", true);
					$(":radio[name='sequential'][value='false']").attr("checked", false);
					
				} else {	
					$('#isSequential').hide();
					$(":radio[name='countersign'][value='true']").attr("checked", false);
					$(":radio[name='countersign'][value='false']").attr("checked", true);
					$(":radio[name='sequential'][value='true']").attr("checked", true);
					$(":radio[name='sequential'][value='false']").attr("checked", false);
				}
				}
				else if(radioName=="sequential"){
				var sequential = $(":radio[name='sequential']:checked").val();
				if (sequential == "true") {
					$(":radio[name='sequential'][value='true']").attr("checked", true);
					$(":radio[name='sequential'][value='false']").attr("checked", false);

				} else {
					$(":radio[name='sequential'][value='true']").attr("checked", false);
					$(":radio[name='sequential'][value='false']").attr("checked", true);
				}
				}
				self.submit();				
				};
			}
		}
		if (isNoStyle) {
			return
		}
		inputText.setAttribute("class", "inputComm");
		inputText.setAttribute("className", "inputComm");
		inputText.onclick = function() {
			this.setAttribute("class", "inputClick");
			this.setAttribute("className", "inputClick");
		};
//		inputText.onblur = function(event) {
//			event = event || window.event;			
//			this.setAttribute("class", "inputComm");
//			this.setAttribute("className", "inputComm");			
//			self.submit();			
//		};
		
	}
	self.inputTitle = $id("inputTitle");
	inputFunction(self.inputTitle);
	
	self.modeContent = $id("modeContent");
	inputFunction(self.modeContent, true);
	
	self.inputTaskTemplateId = $id("taskTemplateId");
	inputFunction(self.inputTaskTemplateId);
	
	self.inputTaskTemplateName = $id("taskTemplateName");
	inputFunction(self.inputTaskTemplateName);
	
	self.inputResultVariable = $id("resultVariable");
	inputFunction(self.inputResultVariable);
	
	self.inputCountersign = $("[name=countersign]");
	inputFunction(self.inputCountersign,true,"countersign");
	
	self.inputSequential = $("[name=sequential]");
	inputFunction(self.inputSequential,true,"sequential");
};
ControlTool.prototype = {
	submit : function() {
		var a = com.xjwgraph.Global, N = a.controlTool;
		if (!$id("module" + N.indexId)) {
			return
		}
		var l = $id("module" + N.indexId), e = l.style, k = $id("backImg"
				+ N.indexId), m = k.style, n = $id("content" + N.indexId), F = n.style, B = $id("title"
				+ N.indexId), L = B.style, p = parseInt(l.offsetWidth), A = parseInt(l.offsetHeight), z = parseInt(e.top), H = parseInt(e.left), u = parseInt(k.offsetWidth), b = parseInt(k.offsetHeight), y = parseInt(m.top), s = parseInt(m.left), J = k.src, M = parseInt(n.offsetWidth), I = parseInt(n.offsetHeight), o = parseInt(F.top), x = parseInt(F.left), r = l
				.getAttribute("modeContent"), E = B.innerHTML, w = a.modeTool;
	
		var taskTemplateName = $id("taskTemplateName" + N.indexId),			
		taskTemplateNameInnerHTML = taskTemplateName.innerHTML;
		var taskTemplateId = $id("taskTemplateId" + N.indexId),			
		taskTemplateIdInnerHTML = taskTemplateId.innerHTML;
		var resultVariable = $id("resultVariable" + N.indexId),			
		resultVariableInnerHTML = resultVariable.innerHTML;
		var countersign = $id("countersign" + N.indexId),			
		countersignInnerHTML = countersign.innerHTML;
		var sequential = $id("sequential" + N.indexId),			
		sequentialInnerHTML = sequential.innerHTML;
		var f = new com.xjwgraph.UndoRedoEvent(function() {
			if (p) {
				e.width = p + "px";
			}
			if (A) {
				e.height = A + "px";
			}
			if (z) {
				e.top = z + "px";
			}
			if (H) {
				e.left = H + "px";
			}
			if (u) {
				m.width = u + "px";
			}
			if (b) {
				m.height = b + "px";
			}
			if (y) {
				m.top = y + "px";
			}
			if (s) {
				m.left = s + "px";
			}
			if (J) {
				k.src = J;
			}
			if (M) {
				F.width = M + "px";
			}
			if (I) {
				F.height = I + "px";
			}
			if (o) {
				F.top = o + "px";
			}
			if (x) {
				F.left = x + "px";
			}
			if (r) {
				l.setAttribute("modeContent", r);
			}
			w.showPointer(l);
			w.changeBaseModeAndLine(l, true);
			B.innerHTML = E;
			taskTemplateId.innerHTML = taskTemplateIdInnerHTML;
			taskTemplateName.innerHTML = taskTemplateNameInnerHTML;
			resultVariable.innerHTML = resultVariableInnerHTML;
			countersign.innerHTML = countersignInnerHTML;
			sequential.innerHTML = sequentialInnerHTML;
			a.smallTool.drawMode(l);
		}, PathGlobal.updateMode);

				
		l.setAttribute("modeContent", N.modeContent.value);
		w.isModeCross(l);
		w.changeBaseModeAndLine(l, true);
		B.innerHTML = "";
		taskTemplateId.innerHTML = "";
		taskTemplateName.innerHTML = "";
		resultVariable.innerHTML = "";
		countersign.innerHTML = "";
		sequential.innerHTML = "";
		var countersignVal="";			//获取会签值
		var brower=navigator.appName.indexOf("Microsoft") != -1 ? 'IE' : navigator.appName;		
		for(var i=0;i<N.inputCountersign.length;i++){
			if(brower=="IE"){
				if(N.inputCountersign[i].getAttribute("checked")){					
					countersignVal=N.inputCountersign[i].value;						
				}
			}else{
			if(N.inputCountersign[i].getAttribute("checked")=="checked"){
				
				countersignVal=N.inputCountersign[i].value;
				
				}
			}
		}
		var sequentialVal="";	//获取会签类型，并行签入还是串行签入
		for(var i=0;i<N.inputSequential.length;i++){	
			if(brower=="IE"){
				if(N.inputSequential[i].getAttribute("checked")){					
					sequentialVal=N.inputSequential[i].value;						
				}
			}else{
			if(N.inputSequential[i].getAttribute("checked")=="checked"){
				
				sequentialVal=N.inputSequential[i].value;
				
				}
			}
		}
		//图片转动动画效果
		w.flip(w.getModeIndex(l), N.inputTitle.value,N.inputTaskTemplateId.value,N.inputTaskTemplateName.value,N.inputResultVariable.value,countersignVal,sequentialVal);
		a.smallTool.drawMode(l);
		
		var i = parseInt(l.offsetWidth), h = parseInt(l.offsetHeight), q = parseInt(e.top), v = parseInt(e.left), c = parseInt(k.offsetWidth), P = parseInt(k.offsetHeight), g = parseInt(m.top), d = parseInt(m.left), t = k.src, D = parseInt(n.offsetWidth), C = parseInt(n.offsetHeight), K = parseInt(F.top), O = parseInt(F.left), G = N.modeContent.value, j = N.inputTitle.value;
		var afterTaskTemplateIdInnerHTML = N.inputTaskTemplateId.value;
		var afterTaskTemplateNameInnerHTML = N.inputTaskTemplateName.value;
		var afterResultVariableInnerHTML = N.inputResultVariable.value;		
		var afterCountersignInnerHTML = countersignVal;
		var afterSequentialInnerHTML = sequentialVal;
		f.setRedo(function() {
			e.width = i + "px";
			e.height = h + "px";
			e.top = q + "px";
			e.left = v + "px";
			m.width = c + "px";
			m.height = P + "px";
			m.top = g + "px";
			m.left = d + "px";
			k.src = t;
			F.width = D + "px";
			F.height = C + "px";
			F.top = K + "px";
			F.left = O + "px";
			l.setAttribute("modeContent", G);
			w.showPointer(l);
			w.changeBaseModeAndLine(l, true);
			B.innerHTML = j;
			taskTemplateId.innerHTML = afterTaskTemplateIdInnerHTML;
			taskTemplateName.innerHTML = afterTaskTemplateNameInnerHTML;
			resultVariable.innerHTML = afterResultVariableInnerHTML;
			countersign.innerHTML = afterCountersignInnerHTML;
			sequential.innerHTML = afterSequentialInnerHTML;
			a.smallTool.drawMode(l);
		});
	},
	reBack : function() {
	},
	//模元信息映射到输入框
	print : function(d) {		
		var f = $id("module" + d), c = f.style, b = $id("backImg" + d), e = $id("title"
				+ d),taskTemplateName = $id("taskTemplateName" + d),taskTemplateId = $id("taskTemplateId" + d),resultVariable = $id("resultVariable" + d), a = this;
		var countersign=$id("countersign" + d).innerHTML;
		var sequential=$id("sequential" + d).innerHTML;
		a.indexId = d;		
		//在点击图标的时候将Title值填入到标题中、将taskTemplate的text值填到辅助区任务模板ID中去、将当前图标的Id填入到隐藏的Index中
		a.inputTitle.value = e.innerHTML;
		a.inputTaskTemplateId.value = taskTemplateId.innerHTML;
		a.inputTaskTemplateName.value = taskTemplateName.innerHTML;
		a.inputResultVariable.value=resultVariable.innerHTML;
		var scriptVal=$("#scriptVal"+d).text();
		$("#scriptEdit").val(scriptVal);
		var endpointUrl=$("#endpointUrl"+d).text();
		$("#endpointUrl").val(endpointUrl);
		var payloadExpression=$("#payloadExpression"+d).text();
		$("#payloadExpression").val(payloadExpression);
		var queueName=$("#queueName"+d).text();
		$("#queueName").val(queueName);
		var blockFlag=$("#blockFlag"+d).text();
		$("#blockFlag").val(blockFlag);
		
		if(blockFlag== "" ||blockFlag=="N"){			
			$(":radio[name='blockFlag'][value='Y']").attr("checked", false);
			$(":radio[name='blockFlag'][value='N']").attr("checked", true);
		}else{
			$(":radio[name='blockFlag'][value='Y']").attr("checked", true);
			$(":radio[name='blockFlag'][value='N']").attr("checked", false);
		}
		
		if(countersign== "" ||countersign=="false"){
			$('#isSequential').hide();
			$(":radio[name='countersign'][value='true']").attr("checked", false);
			$(":radio[name='countersign'][value='false']").attr("checked", true);
		}else{
			$('#isSequential').show();						
			$(":radio[name='countersign'][value='true']").attr("checked", true);
			$(":radio[name='countersign'][value='false']").attr("checked", false);
		}
		if(sequential== "" ||sequential=="false"){
			$(":radio[name='sequential'][value='true']").attr("checked", false);
			$(":radio[name='sequential'][value='false']").attr("checked", true);
		}else{
			$(":radio[name='sequential'][value='true']").attr("checked", true);
			$(":radio[name='sequential'][value='false']").attr("checked", false);
		}
		$("#index").val("");
		$("#index").val(d);
		
		a.modeContent.value = f.getAttribute("modeContent") || "";
	}
};
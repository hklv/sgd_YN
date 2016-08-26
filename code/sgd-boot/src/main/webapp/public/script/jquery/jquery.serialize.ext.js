jQuery.fn.serializeObject = function(splitFlag) {
	var o = {};
	var a = this.serializeArray();

	$.each(a, function() {
				if (o[this.name] !== undefined) {

					if (splitFlag) {
						o[this.name] = o[this.name] + splitFlag + this.value;
					} else {
						if (!o[this.name].push) {
							o[this.name] = [o[this.name]];
						}
						o[this.name].push(this.value || '');
					}

				} else {
					o[this.name] = this.value || '';
				}
			});
	return o;
};

/**
 * minLen 最少字符长度； 值为-1，表示不考虑最少长度
 * maxLen 最大字符长度；值为-1，表示不考虑最大长度
 * regValue 校验字符串类型，用正则表达式，如 “/\d/g” 表示只能输入数字； 如果值为空，则不校验
 * alertText 错误提示文本
 */
jQuery.fn.deserializeObject = function(obj, splitFlag) {
	var btn = /^button|submit|reset$/i;
	var rselectTextarea = /^(?:select|textarea)/i;
	this.find(":input").each(function() {
				if (rselectTextarea.test(this.nodeName)) {
					this.value = obj[this.name] || "";
				} else if (!btn.test(this.type)) {
					switch (this.type) {
						case "checkbox" :
						case "radio" :
							var vals = obj[this.name];
							if (vals && splitFlag) {
								vals = vals.split(splitFlag)
							}

							if ($.inArray(this.value, vals) != -1) {
								console.log("loop", this);

								this.checked = true;
							} else {
								this.checked = "";
							}
							break;
						default :
							this.value = obj[this.name] || "";
							break;
					}
				} else {
				}
			});
};
jQuery.fn.getFormData = function(splitFlag,removeChart,isNotNull) {
	var o = {};
	var btn = /^button|submit|reset$/i;
	var rselectTextarea = /^(?:select|textarea)/i;
	var _form = $(this);
	this.find(":input").each(function() {
		        if(o==null)return null;
		        var na = this.name||this.id;
		        if(na!=null && na.length>0){
		        if(removeChart){
		        	var reg = "/"+removeChart+"$/";
		        	na = na.replace(eval(reg),"");
		        }
				if (rselectTextarea.test(this.nodeName)||!btn.test(this.type)) {
					
					//if(isNotNull && isNotNull ==true &&this.value+"".trim().length<=0 )return;
					if((this.type=="radio" ||this.type=="checkbox") ){//radio,checkbox
						if($(this).is(':checked')){
							if ( o[na] !== undefined) {
								if (splitFlag) {
									o[na] = o[na] + splitFlag + this.value;
								}else {
									if (!o[na].push) {
										o[na] = [o[na]];
									}
									o[na].push(this.value || '');
								}
							}
							else{
								o[na] = this.value || '';
								}
						}else{
							if($(this).is(":visible") && $(":input[name='"+(this.name||this.id)+"']:"+this.type+":checked",_form).length<=0){
								
								if($(this).attr("minLen") && parseInt($(this).attr("minLen"))>0){
									showWarning(($(this).attr("alertText")||qryWebRes("REQUIRED_FIELDS"))+","+qryWebRes("IS_NULL"));
									this.focus();
									o =null;
									return null;
								}else{
									o[na]='';
								}
								
							}
						}	
					}else{// text,hidden,area
						o[na] = this.value || '';
						if(this.nodeName=="SELECT"){
							if($(this)[0].selectedIndex>=0 && $(this)[0].options[$(this)[0].selectedIndex])
							 o[na+"_NAME"]=$(this)[0].options[$(this)[0].selectedIndex].text;	
						}
						if($(this).is(":visible")){
							if($(this).attr("minLen") && parseInt($(this).attr("minLen"))>0 && this.value.trim().length<parseInt($(this).attr("minLen"))){
								if(parseInt($(this).attr("minLen"))==1){
									showWarning(($(this).attr("alertText")||qryWebRes("REQUIRED_FIELDS"))+","+qryWebRes("IS_NULL"));
									this.focus();
									o =null;
									return null;
								}else{
									showWarning(($(this).attr("alertText")||qryWebRes("REQUIRED_FIELDS"))+","+qryWebRes("LESS_MIN_LENGTH").replace("{n}",$(this).attr("minLen")));
									this.focus();
									o =null;
									return null;
								}
								
							}
							if($(this).attr("maxLen") && parseInt($(this).attr("maxLen"))>0 && this.value.trim().length>=parseInt($(this).attr("maxLen"))){
								showWarning(($(this).attr("alertText")||qryWebRes("FIELDS"))+","+qryWebRes("OVER_MAX_LENGTH").replace("{n}",$(this).attr("maxLen")));
								this.focus();
								o =null;
								return null;
							}
							if($(this).attr("regValue") && $(this).attr("regValue").length>0){
								if(this.value.replace(eval($(this).attr("regValue")),"").trim().length>0){
									showWarning(($(this).attr("alertText")||qryWebRes("FIELDS"))+","+qryWebRes("BAD_DATA_FORMAT").replace("[{n}]",""));
									this.focus();
									o =null;
									return null;
								}
							}
							
						}
					}
					
					
				} 
			}
	});
	if(o!=null && isNotNull && isNotNull ==true){
		for (v in o){
			if(o[v]==null || o[v].trim().length<=0)delete o[v];
		}
	}else{
		for (v in o){
			if(o[v]==null || o[v].trim().length<=0) o[v]=null;
		}
	}
	return o;
};
jQuery.fn.setFormData = function(o, splitFlag) {

	var btn = /^button|submit|reset$/i;
	var rselectTextarea = /^(?:select|textarea)/i;
	var isInit = new Object();
	if(o!=null)
    var _form=$(this);
	this.find(":input").each(function() {
		        var na = this.id||this.name;
		        if(na!=null && na.length>0){
		        
				if (rselectTextarea.test(this.nodeName)||!btn.test(this.type)) {
					
					if(o[na]==null)return;
					if((this.type=="radio" ||this.type=="checkbox") ){//radio,checkbox
						
						if(isInit && isInit[na] && isInit[na]==true)return;
						isInit[na]=true;
						var vals =new Array();
						
						if (splitFlag) {
							vals = o[na].split(splitFlag);
							
						}else{
							vals.push(o[na]);
						}
						$("input:"+this.type+"[name='"+this.name+"']",_form).each(function(){
								 for(i=0;i<vals.length;i++) {
								  if($(this).val()==vals[i])$(this).attr('checked','true');
								}
							});
					}else{// text,hidden,area
						
						$(this).val(o[na]);
					}
				} 
			}
	});
}
String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g, "");}
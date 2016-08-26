define([
	"text!modules/SynthAccept/SynthAcceptMgr/templates/AcceptTemplate.html",
	"i18n!modules/SynthAccept/SynthAcceptMgr/i18n/SynthAccept",
	"modules/SynthAccept/SynthAcceptMgr/views/ItemFileRowView",
	"modules/SynthAccept/SynthAcceptMgr/actions/SynthAcceptAction"
], function(template, i18nData, FileView, acceptAction){
	return portal.BaseView.extend({
		className:"container_right",
		template: fish.compile(template),
	
		events: {
			"change :checkbox[name='isProxyApply']": "showProxyApply",
			"click .js-submit": "submit",
			"click #specialHref": "toSpecial"
		},
		
		initialize: function(itemRowData, eventModel){
			this.itemRowData = itemRowData;
			this.eventModel = eventModel;
			this.initData = {
				ID_NO:"320107199912121212",
				NAME:"李好",
				BIRTH_DATE:"1999-12-12",
				MOBILE_PHONE:"13813813888",
				FIXED_PHONE:"025-88888888",
				PERMANENT_ADD:"一号",
				RESIDENCE_ADD:"二号",
				MAILING_ADD:"三号"
			};
		},
		
		render: function(){
			this.$el.html(this.template(this.itemRowData));
		},
		
		afterRender: function(){
		   this.$applyForm = this.$(".js-form").form();
		   this.$proxyApplyForm = this.$(".js-form-proxy").form();
		   
		   acceptAction.queryApplyerType().then(function(result){
				var typeList = result.z_d_r;
				this.$("[name='APPLY_TYPE_ID']").combobox({
					dataTextField: 'VALUE_NAME',
					dataValueField: 'ID',
					editable: false,
					dataSource: typeList
				}).combobox("value",typeList[0].ID);
		   }.bind(this));
		   
		   acceptAction.queryIDType().then(function(result){
				var typeList = result.z_d_r;
				this.$("[name='ID_TYPE_ID']").combobox({
					dataTextField: 'VALUE_NAME',
					dataValueField: 'ID',
					editable: false,
					dataSource: typeList
				}).combobox("value",typeList[0].ID);
		   }.bind(this));
		   this.$("[name='GENDER']").combobox();
		   this.$("[name='BIRTH_DATE']").datetimepicker({viewType:"date",format:"yyyy-mm-dd"});
		   
		   this.$applyForm.form("value",this.initData);
		   this.$proxyApplyForm.form("value",this.initData);
		   
		   this.queryItemFile();
		},
		
		subResize :function(delta){
			var hadHeight = this.$(".panel-default").height() + this.$(".text-right").height() + 20; 
			this.$("#scrollPart").height(delta - hadHeight + "px");
			$("#scrollPart").niceScroll({
		        cursorcolor: '#1d5987',
		        cursorwidth: "10px",
		        cursoropacitymax:"0.4"
		    });
		},
		
		queryItemFile: function(){
			var getFormatNameById = function(formatId){
				switch(formatId){
					case 1:return "原件";break;
					case 2:return "复印件";break;
					case 3:return "电子件";break;
					default: return "其它";
				}
			};
			
			acceptAction.queryMaterialList(this.itemRowData.ORG_ID.split("-")[1])
			.then(function(result){
				var materialList = result.z_d_r;
				$.each(materialList, function(index, material) {
					material.FORMAT_NAME = getFormatNameById(material.FORMAT_ID);
				});
				return materialList;
			})
			.then(function(materialList){
				$.each(materialList, function(index, file) {
					$(".FILE_LIST_TABLE tr:last").after(new FileView(file).el);	
				});
			});
		},
		
		//是否显示代理人信息
		showProxyApply: function(){
			var isProxyApply = this.$applyForm.form("value").isProxyApply;
			if(isProxyApply === "on"){
				this.$(".js-proxy-panel").show();
			}
			else{
				this.$(".js-proxy-panel").hide();
			}
		},
		
		//获取要件材料和上传文件的对应关系
		getMaterialFile: function(){
			var materialIds = [];
			var fileIds = [];
			var materialFileMapping = [];
			$.each(this.$("[name='MATERIAL_ID']"),function(index, item){
				materialIds.push(item.value);
			});
			$.each(this.$("[name='FILE_ID']"),function(index, item){
				fileIds.push(item.value);
			});
			for(var i=0;i<materialIds.length;i++){
				materialFileMapping.push({MATERIALS_ID:materialIds[i],ATTACH_ID:fileIds[i]});
			}
			//return [{MATERIALS_ID:1,ATTACH_ID:2}];
			if(materialFileMapping.length === 0){
				return null;
			}
			if(_.reject(materialFileMapping,function(obj){
				return obj.MATERIALS_ID !== "" && obj.ATTACH_ID !== "";
			}).length > 0){
				return null;
			}
			return materialFileMapping;
		},
		
		startWorkFlow: function(){
			
		},
		
		submit: function(){
			if (!this.$applyForm.isValid()) {
				return;
			};
			if(this.$applyForm.form("value").isProxyApply){
				if (!this.$proxyApplyForm.isValid()) {
					return;
				};
			}
			var materialList = this.getMaterialFile();
			if(!materialList){
				fish.warn("请上传所需的证明材料");
				return;
			}
			var data = {};
			
			//申请人信息
			data.APPLY = this.$applyForm.form("value");
			//事项ID
			data.APPLY.ITEM_ID = this.itemRowData.ORG_ID.split("-")[1];
			//流程定义ID
			data.APPLY.PROC_DEF_ID = this.itemRowData.PROC_DEF_ID;
			//代理人信息
			if(data.APPLY.isProxyApply){
				data.AGENT = this.$proxyApplyForm.form("value");
			}
			//要求材料附件
			data.APPLY_MATERIALS = materialList;
			data.APPLY.isProxyApply && delete data.APPLY.isProxyApply;

			//提交受理单据并启动流程
			acceptAction.submitAccept(data).then(function(result){
				fish.info("受理成功",function(){
					this.eventModel.trigger("newAcceptView",this.itemRowData);
				}.bind(this));
			}.bind(this));
		},
		
		toSpecial: function(){
			window.open("private.html",'newwindow',"fullscreen=yes,channelmode=yes");
		}
	});
});
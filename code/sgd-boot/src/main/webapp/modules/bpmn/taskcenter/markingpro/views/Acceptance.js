define([
	'text!modules/bpmn/taskcenter/markingpro/templates/AcceptanceMgrTemplate.html',
	"i18n!modules/bpmn/taskcenter/markingpro/i18n/markingMgr",
	'modules/bpmn/taskcenter/markingpro/actions/acceptanceAction',
],function(template,i18nData,acceptanceAction){
	return portal.BaseView.extend({
		template: fish.compile(template),
		initialize:function(data){
			this.holderNo = data.HOLDER_NO;
		},
		render:function(){
			this.$el.html(this.template());
			return this;
		},
		afterRender:function(){
			$(".js-form-proposerInfo").form('disable');
			$(".js-form-proxy").form('disable');
			this.loadData();

		},
		loadData: function() {
			acceptanceAction.qryApplication(this.holderNo,function(result){
				this.applyQry(result.APPLY);	
				this.agentQry(result.AGENT);	
				this.materialsHandle(result.ITEM_MATERIALS);					
			}.bind(this));	
		},

		agentQry:function(agent){
			this.$proxy = $(".js-form-proxy").form();
			this.$proxy.form("value",agent);
			if (!agent) {
				$(".proxyDiv").hide();
				return;
			}
			var idTypeId = agent.ID_TYPE_ID;
				var genderCode = agent.GENDER;
				acceptanceAction.queryApplyerTypeById(idTypeId,function(result){
					var idType  = result.DICTIONARY_DATA.valueName;
					$(".js-form-proxy input[name='ID_TYPE_ID']").val(idType);
				});
			$(".js-form-proxy input[name='BIRTH_DATE']").val((agent.BIRTH_DATE).substring(0,10));
			$(".js-form-proxy input[name='GENDER']").val(this.genderChange(genderCode));	
		},

		applyQry:function(apply){				
			this.$apply = $(".js-form-proposerInfo").form();
			this.$apply.form("value",apply);
			if(!apply){
				return;
			}
			var genderCode = apply.GENDER;
			acceptanceAction.queryApplyerTypeById(apply.APPLY_TYPE_ID,function(result){
				var applyType  = result.DICTIONARY_DATA.valueName;
				$(".js-form-proposerInfo input[name='APPLY_TYPE_ID']").val(applyType);
			});	
			acceptanceAction.queryApplyerTypeById(apply.ID_TYPE_ID,function(result){
				var idType  = result.DICTIONARY_DATA.valueName;
				$(".js-form-proposerInfo input[name='ID_TYPE_ID']").val(idType);
			})
			$(".js-form-proposerInfo input[name='BIRTH_DATE']").val((apply.BIRTH_DATE).substring(0,10));
			$(".js-form-proposerInfo input[name='GENDER']").val(this.genderChange(genderCode));
		},

		materialsHandle:function(materials){
			if(!materials){
				return;
			}
			$.each(materials, function(key, val) { 
				$(".materialsContainer").append('<a style="display:block;height:30px;width:100%;" href="download4Sgd?fileName='+materials[key].ALIAS_NAME+'">'+materials[key].NAME+'</a>');			          
			}); 	
								
		},

			//性别判断
		genderChange:function(genderCode){
			switch(genderCode){
				case 'M':
					return "男";
					break;
				case 'F':
					return "女";
					break;
				default:
					return "未知";
			}
		}
	});
});


define([
	'text!modules/bpmn/popwin/flowerpicture/templates/flowpic.html',
    'i18n!modules/bpmn/popwin/flowerpicture/i18n/flowpic'
	],function(template,i18n){
		return portal.BaseView.extend({
			template: fish.compile(template),
			initialize: function(option) {
				console.log("option:",option);
				this.option = option;
            this.srcUrl = 'http://10.45.8.13:8080/portal/modules/bpmn/flowdesigner/draw/holderDetail.jsp';
        	},
			render: function () {
            this.setElement(this.template(i18n));
            return this;
        	},
        	afterRender:function(){
        		var path = this.srcUrl + '?PROC_INST_ID="' + this.option.PROC_INST_ID
        					+'"&HOLDER_ID="'+this.option.HOLDER_ID+'"&HOLDER_NO="'+this.option.HOLDER_NO
							+'"&PROCESS_NAME="'+this.option.PROCESS_NAME+'"&HOLDER_STATE="'+this.option.HOLDER_STATE
							+'"';
	            this.$("#flow-pic").attr('src', path);
	        	}
		})
	})
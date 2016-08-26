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
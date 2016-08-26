/**
 * linkbutton - jQuery EasyUI
 * 
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2010 stworthy [ stworthy@gmail.com ] 
 */
(function($) {
	function createButton(target) {
		var opts = $.data(target, 'linkbutton').options;
		$(target).empty();
		
		$(target).addClass('l-btn');
		if (opts.id) {
			$(target).attr('id', opts.id);
		} else {
			$.fn.removeProp ? $(target).removeProp('id') : $(target)
					.removeAttr('id');
		}
		if (opts.plain) {
			$(target).addClass('l-btn-plain');
		} else {
			$(target).removeClass('l-btn-plain');
		}
		if (opts.text) {
			
			$(target).html(opts.text).wrapInner('<span class="l-btn-left">'
					+ '<span class="l-btn-text">' + '</span>' + '</span>');
			if (opts.iconCls) {
				
				$(target).find('.l-btn-text').addClass(opts.iconCls).css(
						'padding-left', '20px');
			}
		} else {
			$(target).html('&nbsp;').wrapInner('<span class="l-btn-left">'
					+ '<span class="l-btn-text">'
					+ '<span class="l-btn-empty"></span>' + '</span>'
					+ '</span>');
			if (opts.iconCls) {
				$(target).find('.l-btn-empty').addClass(opts.iconCls);
			}
		}
		setDisabled(target, opts.disabled);
	};
	function setDisabled(target, disabled) {
		var state = $.data(target, 'linkbutton');
		if (disabled) {
			state.options.disabled = true;
			var href = $(target).attr('href');
			if (href) {
				state.href = href;
				$(target).attr('href', 'javascript:void(0)');
			}
			if(target.onclick){
				state.onclick = target.onclick;
				target.onclick = null;
			}
			$(target).addClass('l-btn-disabled');
		} else {
			state.options.disabled = false;
			if (state.href) {
				$(target).attr('href', state.href);
			}
			if (state.onclick) {
				target.onclick = state.onclick;
			}
			$(target).removeClass('l-btn-disabled');
		}
	};
	$.fn.linkbutton = function(options, param) {
		
		if (typeof options == 'string') {
			return $.fn.linkbutton.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
					var state = $.data(this, 'linkbutton');
					if (state) {
						$.extend(state.options, options);
					} else {
						$.data(this, 'linkbutton', {
									options : $.extend({},
											$.fn.linkbutton.defaults,
											$.fn.linkbutton.parseOptions(this),
											options)
								});
						$(this).removeAttr('disabled');
					}
					createButton(this);
				});
	};
	$.fn.linkbutton.methods = {
		options : function(jq) {
			return $.data(jq[0], 'linkbutton').options;
		},
		enable : function(jq) {
			return jq.each(function() {
						setDisabled(this, false);
					});
		},
		disable : function(jq) {
			return jq.each(function() {
						setDisabled(this, true);
					});
		}
	};
	$.fn.linkbutton.parseOptions = function(target) {
	
		var t = $(target);
	
		return {
			id : t.attr('id'),
			disabled : (t.attr('disabled') ? true : undefined),
			plain : (t.attr('plain') ? t.attr('plain') == 'true' : undefined),
			text : $.trim(t.html()),
			iconCls : (t.attr('icon') || t.attr('iconCls'))
		};
		
		
	};
	$.fn.linkbutton.defaults = {
		id : null,
		disabled : false,
		plain : false,
		text : '',
		iconCls : null
	};
	
})(jQuery);

/**
 * parser - jQuery EasyUI
 * 
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2010 stworthy [ stworthy@gmail.com ] 
 * 
 */
(function($){
	$.parser = {
		auto : true,
		onComplete : function(context) {
		},
		plugins : ['linkbutton'],
		parse : function(context) {
			var aa = [];
			for (var i = 0; i < $.parser.plugins.length; i++) {
				var name = $.parser.plugins[i];
				var r = $('.easyui-' + name, context);
				if (r.length) {
					if (r[name]) {
						r[name]();
					} else {
						aa.push({
									name : name,
									jq : r
								});
					}
				}
			}
			if (aa.length && window.easyloader) {
				var names = [];
				for (var i = 0; i < aa.length; i++) {
					names.push(aa[i].name);
				}
				easyloader.load(names, function() {
							for (var i = 0; i < aa.length; i++) {
								var name = aa[i].name;
								var jq = aa[i].jq;
								jq[name]();
							}
							$.parser.onComplete.call($.parser, context);
						});
			} else {
				$.parser.onComplete.call($.parser, context);
			}
		},
	parseBtn : function(context) {
			$("button").load(function(){
				   if(!$(this).hasCss("button")){
					     $(this).addClass("button");
					   }
			   });
			   $("button").live("mouseover",function(){
					$(this).addClass("button_over");
				});
			   $("button").live("mouseout",function(){
					$(this).removeClass("button_over");
				});
	},

	parseInput : function(context) {
		   $("input[type='button']").load(function(){
			   if(!$(this).hasCss("button")){
				     $(this).addClass("button");
				   }
		   });
		   $("input[type='button']").live("mouseover",function(){
				$(this).addClass("button_over");
			});
		   $("input[type='button']").live("mouseout",function(){
				$(this).removeClass("button_over");
			});
			  
     }
	};
	$(function() {
				if (!window.easyloader && $.parser.auto) {
					$.parser.parseBtn();
					$.parser.parseInput();
					$.parser.parse();
					
				}
			});
})(jQuery);

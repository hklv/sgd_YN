/**
前台统一校验模块开发完成



校验失败动作可以设置：


1. 浮动提示
2. 抛出异常
3. 只返回结果



当设置成为浮动提示的时候，界面元素失去焦点将触发校验，并且显示提示

可用来校验的界面元素有：
1. 文本框


2. 多行文本域


3. 下拉选择框


4. Popedit，此种元素构造校验配置项的时候，是用实例名，而前三种用$("id")

基本校验类型：


1. 是否为空
2. 最大长度


3. 最小长度



特殊校验类型：


1. 整数
2. 浮点数


3. Email
4. 日期
5. IPV4

不参与校验的几种界面元素的考虑：


1. checkbox 这个类型一般无需校验，如果是判断必选，可以循环
2. radion 这个类型如果强制必选，可以设置默认值


3. DateTimePicker 这个类型是通过选择出来的日期，不会有格式不正确的问题


4. MultiSelect 这个类型取值可以直接通过选中项取得，一般也没有格式相关的校验


**/

var ValidateType = {
	"Empty": "empty",
	"Integer": "integer",
	"Float": "float",
	"Email": "email",
	"IPV4": "ipv4",
	"MaxLength": "maxLength",
	"MinLength": "minLength",
	"RegExp": "regexp"
};

var ValidateFailType = {
	"Tip": "tip",
	"Exception": "exception",
	"None": "none"
};

/**
 * 表单校验器


 */
function FormValidator()
{
	this.configs = [];
	this.activeConfig = null;

	this.validateFailType = ValidateFailType.Tip;

	this.tip = null;
}

/**
 * 加载校验配置
 * @param {Object} data 校验配置
 */
FormValidator.prototype.loadByData = function(data)
{
	this.clear();

	this.validateFailType = data.validateFailType != null ? data.validateFailType : ValidateFailType.Tip;

	for (var i=0; i<data.configs.length; i++)
	{
		this.addConfig(data.configs[i]);
	}
}

/**
 * 清除校验配置
 */
FormValidator.prototype.clear = function()
{
	this.hideTip();
	this.configs = [];
	this.activeConfig = null;
}

/**
 * 添加校验配置
 * @param {Config} data 校验配置
 */
FormValidator.prototype.addConfig = function(data)
{
	var config = new Config(data);
	config.parent = this;

	config.getElement().attachEvent("onblur", new Delegate(config, config.onBlur));

	this.configs.push(config);
}

/**
 * 校验
 */
FormValidator.prototype.validate = function()
{
	for (var i=0; i<this.configs.length; i++)
	{
		this.activeConfig = this.configs[i];

		if (!this.configs[i].validate())
		{
			switch (this.validateFailType)
			{
				case ValidateFailType.Tip:
				{
					this.showTip(this.activeConfig);
					this.configs[i].focus();
					break;
				}
				case ValidateFailType.Exception:
				{
					throw new Error(this.activeConfig.activeRule.warning);
					break;
				}
				case ValidateFailType.None:
				{
					this.configs[i].focus();
					break;
				}
			}
			return false;
		}
	}
	return true;
}

/**
 * 显示提示框


 * @param {Config} config 配置项


 */
FormValidator.prototype.showTip = function(config)
{
	var position = config.getPosition();
	var element = config.getElement();

	if (this.tip == null)
	{
		var tip = document.createElement("div");
		tip.className = "Tip";

		this.tip = tip;

		document.body.appendChild(tip);
	}

	this.tip.style.left = position.x + 10;
	this.tip.style.top = position.y + element.offsetHeight - 5;

	var text = config.activeRule.warning;
	this.tip.innerHTML = text;

	this.tip.style.display = "";
}

/**
 * 显示提示框


 */
FormValidator.prototype.hideTip = function()
{
	if (this.tip != null)
	{
		this.tip.style.display = "none";
	}
}

/**
 * 校验配置项


 * @param {Object} data 配置数据
 */
function Config(data)
{
	this.element = data.element;
	this.rules = data.rules;

	this.parent = null;
	this.activeRule = null;
}

/**
 * 失去焦点的时候


 */
Config.prototype.onBlur = function()
{
	if (this.parent.validateFailType == ValidateFailType.Tip)
	{
		if (this.validate())
		{
			this.parent.hideTip();
		}
		else
		{
			this.parent.showTip(this);
		}
	}
}

/**
 * 校验本配置项
 */
Config.prototype.validate = function()
{
	for (var i=0; i<this.rules.length; i++)
	{
		this.activeRule = this.rules[i];

		var result = false;

		switch (this.activeRule.type)
		{
			case ValidateType.Empty:
			{
				result = this.validateEmpty();
				break;
			}
			case ValidateType.Integer:
			{
				result = this.validateInteger();
				break;
			}
			case ValidateType.Float:
			{
				result = this.validateDouble();
				break;
			}
			case ValidateType.Email:
			{
				result = this.validateEmail();
				break;
			}
			case ValidateType.IPV4:
			{
				result = this.validateIPV4();
				break;
			}
			case ValidateType.MaxLength:
			{
				result = this.validateMaxLength();
				break;
			}
			case ValidateType.MinLength:
			{
				result = this.validateMinLength();
				break;
			}
			case ValidateType.RegExp:
			{
				result = this.validateRegExp();
				break;
			}
		}

		if (!result)
		{
			return false;
		}
	}

	return true;
}

/**
 * 添加校验规则
 * @param {Rule} rule 校验规则，格式：{type:"empty", warning:"姓名不准为空"}，value和regexp选项是可选的
 */
Config.prototype.addRule = function(rule)
{
	this.rules.push(rule);
}

/**
 * 本校验配置的界面元素聚焦
 */
Config.prototype.focus = function()
{
	this.getElement().focus();
}

/**
 * 取得本校验配置的界面元素
 */
Config.prototype.getElement = function()
{
	var element = null;
	switch (this.element.tagName)
	{
		case "POPEDIT":
		{
			element = this.element.textBox;
			break;
		}
		default:
		{
			element = this.element;
			break;
		}
	}

	return element;
}

/**
 * 取得本校验配置界面元素的坐标
 */
Config.prototype.getPosition = function()
{
	var pointer = this.getElement();
	var left = 0;
	var top = 0;

	while ((pointer != null) && (pointer != document.body))
	{
		left += pointer.offsetLeft;
		top += pointer.offsetTop - pointer.scrollTop;

		pointer = pointer.offsetParent;
	}

	return {x:left, y:top};
}

/**
 * 取得本校验配置界面元素的值


 */
Config.prototype.getValue = function()
{
	var value = "";
	switch (this.element.tagName)
	{
		case "POPEDIT":
		{
			value = this.element.textBox.value;
			break;
		}
		default:
		{
			value = this.element.value;
			break;
		}
	}

	return value.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 计算字符串长度


 * @param {String} str 要计算长度的字符串


 */
Config.prototype.getSize = function(str)
{
	var strNew = "";
	strNew = str.replace(/[\u0080-\u07FF]/g, "**");
	strNew = strNew.replace(/[\u0800-\uFFFF]/g, "***");
	strNew = strNew.replace(/[^\u0000-\uFFFF]/g, "****");
	return strNew.length;
}

/**
 * 校验空字符串
 */
Config.prototype.validateEmpty = function()
{
	return /.+/.test(this.getValue());
}

/**
 * 校验电子邮件
 */
Config.prototype.validateEmail = function()
{
	return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(this.getValue());
}

/**
 * 校验英文字符
 */
Config.prototype.validateEnglish = function()
{
	return /^[A-Za-z]+$/.test(this.getValue());
}

/**
 * 校验数字
 */
Config.prototype.validateNumber = function()
{
	return /^\d+$/.test(this.getValue());
}

/**
 * 校验整数
 */
Config.prototype.validateInteger = function()
{
	return /^[-\+]?\d+$/.test(this.getValue());
}

/**
 * 校验浮点数


 */
Config.prototype.validateDouble = function()
{
	return /^[-\+]?\d+(\.\d+)?$/.test(this.getValue());
}

/**
 * 校验最大长度


 */
Config.prototype.validateMaxLength = function()
{
	return this.getSize(this.getValue()) > this.activeRule.value ? false : true;
}

/**
 * 校验最小长度


 */
Config.prototype.validateMinLength = function()
{
	var size = this.getSize(this.getValue());
	if ((size < this.activeRule.value) && (size != 0))
	{
		return false;
	}
	return true;
}

/**
 * 校验IPV4地址
 */
Config.prototype.validateIPV4 = function()
{
	return /^(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5]).(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5]).(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5]).(0|[1-9]\d?|[0-1]\d{2}|2[0-4]\d|25[0-5])$/.test(this.getValue());
}

/**
 * 使用自定义正则表达式校验
 */
Config.prototype.validateRegExp = function()
{
	return this.activeRule.regexp.test(this.getValue());
}

/**
 * 一个模仿.net中delegate的东西，主要是替换了事件主体
 * @param {Object} context 事件上下文，通常用以取代默认的发生事件的元素
 * @param {Function} fun 事件处理函数
 */
function Delegate(context, fun)
{
	var args = Array.prototype.slice.call(arguments).slice(2);
	return function()
	{
		return fun.apply(context, Array.prototype.slice.call(arguments).concat(args));
	};
}
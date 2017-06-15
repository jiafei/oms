// 去除字符串前后空格
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

// 计算字符串中包含目标字符串的数量
String.prototype.countStr = function(lookingStr) {
	var index = this.indexOf(lookingStr);
	if (index == -1) {
		return 0;
	} else {
		return 1 + this.substr(index+1).countStr(lookingStr);
	}
};

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

/**
 * 根据long返回一个固定格式的时间字符串。
 * 
 * @param val
 * @returns
 */
function timeFormat(val) {
	var timestamp = new Date(val);
	return timestamp.format("yyyy-MM-dd hh:mm:ss");
}
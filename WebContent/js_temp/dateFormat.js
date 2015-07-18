function formatDate(value, rec, index) {
	if (value == undefined) {
		return "";
	}
	var t = new Date(value);
	var tf = function(i) {
		return (i < 10 ? '0' : '') + i
	};
	return tf(t.getFullYear()) + '年' + tf(t.getMonth() + 1) + '月'
			+ tf(t.getDate()) + '日 ' + tf(t.getHours()) + ':'
			+ tf(t.getMinutes());
}
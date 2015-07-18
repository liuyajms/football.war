var url = "../../rest/contact";
$(document).ready(function() {

	select();

	$("#submit").click(function() {
		var button = $(this);
		button.button('loading');
		restUpdate(url, null, $("#form"), function(data) {
			button.button('reset');
			alert("保存成功！");
			select();
		});
	});
});

function select() {
	restSelect(url, null, function(data) {
		$("#name").val(data.name);
		$("#phone").val(data.phone);
		$("#email").val(data.email);
		$("#address").val(data.address);
		$("#picture").val("");
		$("#picture_show").attr("src", "../../files/" + data.id + "/logo.png?" + data.updateTime);
	});
}
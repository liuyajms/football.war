var url = "../rest/news";

$(document).ready(function() {
	restSelect(url, {type: 1, page: 0, rows: 10}, function(data) {
		var list = $("#list");
		$.each(data.rows, function(i, d) {
			var img = $("<img></img>");
			img.attr("src", "../files/" + d.schoolId + "/news/" + d.id + ".png");
			img.width(80);
			img.height(80);
			var h2 = $("<h2></h2>");
			h2.text(d.title);
			var p = $("<p></p>");
			p.text(d.descriptionSummary);

			var a = $("<a></a>");
			a.attr("href", "#detail");
			a.attr("data-transition", "slide");
			a.append(img);
			a.append(h2);
			a.append(p);
			
			var li = $("<li></li>");			
			li.append(a);
			
			list.append(li);
		});
		list.listview("refresh");
	});
});




//设置多个表格可编辑  
function EditTables() {
	
	for (var i = 0; i < arguments.length; i++) {
		SetTableCanEdit(arguments[i]);
	}
}

//设置表格是可编辑的  
function SetTableCanEdit(table) {
	for (var i = 1; i < table.rows.length; i++) {
		SetRowCanEdit(table.rows[i]);
	}
}

function SetRowCanEdit(row) {
	for (var j = 0; j < row.cells.length; j++) {

		//如果当前单元格指定了编辑类型，则表示允许编辑  
		var editType = row.cells[j].getAttribute("EditType");
		if (!editType) {
			//如果当前单元格没有指定，则查看当前列是否指定  
			editType = row.parentNode.rows[0].cells[j].getAttribute("EditType");
		}
		if (editType) {
			row.cells[j].onclick = function() {
				EditCell(this);
			}
		}
	}

}

//设置指定单元格可编辑  
function EditCell(element, editType) {

	var editType = element.getAttribute("EditType");
	if (!editType) {
		//如果当前单元格没有指定，则查看当前列是否指定  
		editType = element.parentNode.parentNode.rows[0].cells[element.cellIndex]
				.getAttribute("EditType");
	}

	switch (editType) {
	case "TextBox":
		CreateTextBox(element, element.innerHTML);
		break;
	/*case "DropDownList":
		CreateDropDownList(element);
		break;*/
	default:
		break;
	}
}

//为单元格创建可编辑输入框  
function CreateTextBox(element, value) {
	//检查编辑状态，如果已经是编辑状态，跳过  
	var editState = element.getAttribute("EditState");
	if (editState != "true") {
		//创建文本框  
		var textBox = document.createElement("INPUT");
		textBox.style.width = "70px";
		textBox.type = "text";
		textBox.className = "EditCell_TextBox";
		
		//设置文本框当前值  
		if (!value) {
			value = element.getAttribute("Value");
		}
		textBox.value = value;

		//设置文本框的失去焦点事件  
		textBox.onblur = function() {
			CancelEditCell(this.parentNode, this.value);
		}
		//向当前单元格添加文本框  
		ClearChild(element);
		element.appendChild(textBox);
		textBox.focus();
		textBox.select();

		//改变状态变量  
		element.setAttribute("EditState", "true");
		element.parentNode.parentNode.setAttribute("CurrentRow",
				element.parentNode.rowIndex);
	}

}

//取消单元格编辑状态  
function CancelEditCell(element, value, text) {
	element.setAttribute("Value", value);
	if (text) {
		element.innerHTML = text;
	} else {
		element.innerHTML = value;
	}
	element.setAttribute("EditState", "false");

}

//清空指定对象的所有字节点  
function ClearChild(element) {
	element.innerHTML = "";
}

//添加行  
function AddRow(table, index, location) {
	var lastRow = table.rows[table.rows.length - 1];
	var length = table.rows.length;
	var newRow = lastRow.cloneNode(true);

	for (var i = 0; i < $(newRow)[0].cells.length; i++) {
		$(newRow)[0].cells[i].innerText = "";
	}
	if (location == null) {
		$('#tabAdd tbody tr:eq(' + (index) + ')').after(newRow);
	} else if (location == "before" && index != 0) {
		$('#tabAdd tbody tr:eq(' + (index) + ')').before(newRow);
	} else {
		$('#tabAdd tbody tr:eq(' + (index) + ')').after(newRow);
	}

	flushTable(table);

	SetRowCanEdit(newRow);
	$('#tabAdd tr td').bind(
		'mousemove',
		function() {
			var trSeq = $(this).parent().parent().find("tr").index($(this).parent()[0]);
			$('#trId').val(trSeq);
		}
	);
	
	
	//绑定右键菜单
	$('#tabAdd tr').contextMenu('myMenu1', {
		bindings: {
	    	'addOneUp': function(t) {
	    		AddRow($('#tabAdd').get(0), parseInt($('#trId').val()), "before");
	        },
	        'addOneDown': function(t) {
	        	AddRow($('#tabAdd').get(0),parseInt($('#trId').val()), "after");
	        },
	        'delete': function(t) {
	        	DeleteRow($('#tabAdd').get(0),parseInt($('#trId').val()));
	        }
		}
				

	});
	return newRow;

}

//删除行  
function DeleteRow(table, index) {
	if(index != 0){
		table.deleteRow(index);
		flushTable(table);
	}
	
}

//前面的序号刷新
function flushTable(table){
	for(var i = 1; i < table.rows.length; i ++){
		$('#tabAdd tbody tr:eq(' + (i) + ') td:eq(0)').html(i);
	}
}

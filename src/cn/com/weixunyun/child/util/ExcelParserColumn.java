package cn.com.weixunyun.child.util;

import java.util.ArrayList;
import java.util.List;

import cn.com.weixunyun.child.util.excel.*;

public class ExcelParserColumn {

	public List<ColumnProperties> getStudentParserList(Long schoolId) {
		List<ColumnProperties> list = new ArrayList<ColumnProperties>();
		list.add(new ColumnProperties("name", false));
		list.add(new ColumnProperties("gender", true, new DictionaryParser(schoolId, "student", "gender")));
		list.add(new ColumnProperties("birthday", true, new DateParser()));
		list.add(new ColumnProperties("code"));
		list.add(new ColumnProperties("card"));
		list.add(new ColumnProperties("address"));
		list.add(new ColumnProperties("description"));
		list.add(new ColumnProperties("parentsName"));
		list.add(new ColumnProperties("parentsMobile", true, new PhoneParser()));
		list.add(new ColumnProperties("parentsUsername"));
		list.add(new ColumnProperties("parentsType", true, new DictionaryParser(schoolId, "parents", "type")));
		list.add(new ColumnProperties("parentsPta", true, new BoolParser()));
		return list;
	}

	public List<ColumnProperties> getTeacherParserList(Long schoolId) {
		List<ColumnProperties> list = new ArrayList<ColumnProperties>();
		list.add(new ColumnProperties("name", false));
		list.add(new ColumnProperties("gender", true, new DictionaryParser(schoolId, "student", "gender")));
		list.add(new ColumnProperties("mobile", false, new PhoneParser()));
		list.add(new ColumnProperties("username", false));
		list.add(new ColumnProperties("code"));
		list.add(new ColumnProperties("card"));
		list.add(new ColumnProperties("title", true, new DictionaryParser(schoolId, "teacher", "title")));
		list.add(new ColumnProperties("email"));
		list.add(new ColumnProperties("remark"));
		list.add(new ColumnProperties("description"));
		list.add(new ColumnProperties("type", true, new BoolParser()));
		return list;
	}

}

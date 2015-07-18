package cn.com.weixunyun.child.util.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.ServiceFactory;
import cn.com.weixunyun.child.model.service.TeacherService;

public class TeacherParser extends AbstractParser<Long> {

	private Map<String, Long> map = new HashMap<String, Long>();
	
	public String exception = "";

	public TeacherParser(Long schoolId) {
		List<Teacher> list = ServiceFactory.getService(TeacherService.class).selectTeacher(schoolId);
		for (Teacher teacher : list) {
			map.put(teacher.getName(), teacher.getId());
		}
	}

	@Override
	public Long parse(String s, Long schoolId) throws ParserException {
		if (s != null && !"".equals(s)) {
			return map.get(s);
		} else {
			return null;
		}

	}

}

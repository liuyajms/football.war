package cn.com.weixunyun.child.util.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.pojo.Course;
import cn.com.weixunyun.child.model.service.CourseService;
import cn.com.weixunyun.child.model.service.ServiceFactory;

public class CourseParser extends AbstractParser<Long> {

	private Map<String, Long> map = new HashMap<String, Long>();
	
	public String exception = "";

	public CourseParser(Long schoolId) {
		List<Course> list = ServiceFactory.getService(CourseService.class).selectAll(schoolId, 0L, 100L, null);
		for (Course course : list) {
			map.put(course.getName(), course.getId());
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

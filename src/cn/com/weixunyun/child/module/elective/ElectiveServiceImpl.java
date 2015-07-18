package cn.com.weixunyun.child.module.elective;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class ElectiveServiceImpl extends AbstractService implements ElectiveService {

	@Override
	public void insert(Elective course) {
		super.getMapper(ElectiveMapper.class).insert(course);
	}

	@Override
	public void update(Elective course) {
		super.getMapper(ElectiveMapper.class).update(course);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(ElectiveMapper.class).delete(id);
	}

	@Override
	public Elective select(Long id) {
		return getMapper(ElectiveMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId, String term) {
		return getMapper(ElectiveMapper.class).selectAllCount(schoolId, term);
	}

	@Override
	public List<TeacherElective> selectAll(Long schoolId, String term, Long offset, Long rows) {
		return getMapper(ElectiveMapper.class).selectAll(schoolId, term, offset, rows);
	}

	@Override
	public List<TeacherElective> getStudentList(Long schoolId, Long studentId,
			Integer grade, String term, Long offset, Long rows) {
		return getMapper(ElectiveMapper.class).getStudentList(schoolId, studentId, grade, term, offset, rows);
	}

	@Override
	public void deleteElectives(Long schoolId, String term) {
		getMapper(ElectiveMapper.class).deleteElectives(schoolId, term);
	}

	@Override
	public int insertElectives(Long schoolId, String term, Long userId, int del, List<Map<String, Object>> list) {
		ElectiveMapper mapper = super.getMapper(ElectiveMapper.class);
		if (del == 1) {
			mapper.deleteElectives(schoolId, term);
		}
		
		TeacherElective elective = new TeacherElective();
		for (Map<String, Object> m : list) {
			elective.setId(super.getMapper(SequenceMapper.class).sequence());
			elective.setSchoolId(schoolId);
			elective.setTerm(term);
			elective.setCreateTeacherId(userId);
			elective.setCreateTime(new Timestamp(System.currentTimeMillis()));
			elective.setName(m.get("name").toString());
			elective.setDate(m.get("date")==null?null:m.get("date").toString());
			elective.setGrade(m.get("grade")==null?null:gradeParser(m.get("grade").toString()));
			elective.setNum(m.get("num")==null?null:Long.parseLong(m.get("num").toString()));
			elective.setTeacherName(m.get("teacherName").toString());
			mapper.insertMulti(elective);
		}
		return list.size();
	}
	
	public int gradeParser(String str) {
		str = str.substring(0, str.length()-2);
		String attr[] = str.split("、");
		int attr2[] = {0, 0, 0, 0, 0, 0, 0, 0};
		for (int i = 0; i < attr.length; i++) {
			attr2[Integer.parseInt(attr[i])-1] = 1;
		}
		int grade = 0;
		for (int i = 0; i < 8; i++) {
			if (attr2[i] == 1) {
				grade += (1 << i);
			}
		}
		return grade;
	}

	@Override
	public void insertMulti(TeacherElective s) {
		getMapper(ElectiveMapper.class).insertMulti(s);
	}

}

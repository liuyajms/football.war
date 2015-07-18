package cn.com.weixunyun.child.module.elective;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.AbstractService;

public class ElectiveStudentServiceImpl extends AbstractService implements ElectiveStudentService {

	@Override
	public CourseElectiveStudent select(Long id) {
		return super.getMapper(ElectiveStudentMapper.class).select(id);
	}

	@Override
	public void insert(ElectiveStudent elective) {
		super.getMapper(ElectiveStudentMapper.class).insert(elective);
	}

	@Override
	public void update(ElectiveStudent elective) {
		super.getMapper(ElectiveStudentMapper.class).update(elective);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(ElectiveStudentMapper.class).delete(id);
	}

	@Override
	public List<StudentElectiveStudent> selectStudent(Long offset, Long rows, Long studentId, String term) {
		return super.getMapper(ElectiveStudentMapper.class).selectStudent(offset, rows, studentId, term);
	}

	@Override
	public void inserElectiveStudent(MultivaluedMap<String, String> formData, School school, String term) {
		List<String> electiveListStr = formData.get("electiveId");
		System.out.println(electiveListStr);

		ElectiveStudentMapper mapper = super.getMapper(ElectiveStudentMapper.class);

		Long studentId = Long.parseLong(formData.getFirst("studentId"));
		mapper.deleteStudent(school.getId(), studentId, term);

		if (electiveListStr != null && electiveListStr.size() > 0) {
			for (String electiveIdStr : electiveListStr) {
				mapper = super.getMapper(ElectiveStudentMapper.class);
				Long electiveId = Long.parseLong(electiveIdStr);

				System.out.println(electiveId);
				System.out.println(mapper.selectElectiveNumCount(electiveId));
				System.out.println();

				if (mapper.selectElectiveNumCount(electiveId) > 0) {
					// 查询是否满员！！
					ElectiveStudent elective = new ElectiveStudent();
					elective.setId(super.getMapper(SequenceMapper.class).sequence());
					elective.setTerm(term);
					elective.setElectiveId(electiveId);
					elective.setStudentId(studentId);
					elective.setSchoolId(school.getId());
					elective.setTime(new Timestamp(System.currentTimeMillis()));
					mapper.insert(elective);
				} else {
					throw new RuntimeException();
				}

			}
		}

	}

	@Override
	public int selectElectiveNumCount(Long electiveId) {
		return super.getMapper(ElectiveStudentMapper.class).selectElectiveNumCount(electiveId);
	}

	@Override
	public int selectAllCount(Long schoolId, String keyword, String term) {
		return super.getMapper(ElectiveStudentMapper.class).selectAllCount(schoolId, keyword, term);
	}

	@Override
	public List<ElectiveStudent> selectAll(Long offset, Long rows, String keyword, Long schoolId, String term) {
		return super.getMapper(ElectiveStudentMapper.class).selectAll(offset, rows, keyword, schoolId, term);
	}

	@Override
	public List<CourseElectiveStudent> selectElective(Long offset, Long rows, Long schoolId, Long electiveId,
			String term, String keyword) {
		return super.getMapper(ElectiveStudentMapper.class).selectElective(offset, rows, schoolId, electiveId, term, keyword);
	}

	@Override
	public int selectElectiveCount(Long schoolId, Long electiveId, String term, String keyword) {
		return super.getMapper(ElectiveStudentMapper.class).selectElectiveCount(schoolId, electiveId, term, keyword);
	}

	@Override
	public void updateScore(ElectiveStudent student) {
		super.getMapper(ElectiveStudentMapper.class).updateScore(student);
	}

	@Override
	public void deleteStudent(Long schoolId, Long studentId, String term) {
		super.getMapper(ElectiveStudentMapper.class).deleteStudent(schoolId, studentId, term);
	}

	@Override
	public int updateElectiveStudents(Long schoolId, String term, Long electiveId, Long userId, List<Map<String, Object>> list) {
		ElectiveStudentMapper mapper = super.getMapper(ElectiveStudentMapper.class);
		
		CourseElectiveStudent es = new CourseElectiveStudent();
		for (Map<String, Object> m : list) {
			es.setSchoolId(schoolId);
			es.setTerm(term);
			es.setElectiveId(electiveId);
			es.setStudentName(m.get("studentName").toString());
			es.setScore(m.get("score")==null?null:m.get("score").toString());
			es.setScoreDescription(m.get("scoreDescription")==null?null:m.get("scoreDescription").toString());
			es.setScoreTime(new Timestamp(System.currentTimeMillis()));
			mapper.updateMulti(es);
		}
		return list.size();
	}

	@Override
	public void updateMulti(CourseElectiveStudent es) {
		super.getMapper(ElectiveStudentMapper.class).updateMulti(es);
	}

}

package cn.com.weixunyun.child.module.homework.check;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class HomeworkCheckServiceImpl extends AbstractService implements HomeworkCheckService {

	@Override
	public HomeworkCheck get(Long id) {
		return super.getMapper(HomeworkCheckMapper.class).get(id);
	}

	@Override
	public int getListCount(Long schoolId, String keyword, String term, Long homeworkId) {
		return super.getMapper(HomeworkCheckMapper.class).getListCount(schoolId, keyword, term, homeworkId);
	}

	@Override
	public List<HomeworkCheck> getList(Long offset, Long rows, String keyword,
			Long schoolId, String term, Long homeworkId) {
		return super.getMapper(HomeworkCheckMapper.class).getList(offset, rows, keyword, schoolId, term, homeworkId);
	}

	@Override
	public void insert(HomeworkCheck check) {
		super.getMapper(HomeworkCheckMapper.class).insert(check);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(HomeworkCheckMapper.class).delete(id);
	}

	@Override
	public void deleteHomeworkChecks(Long studentId, String date) {
		super.getMapper(HomeworkCheckMapper.class).deleteHomeworkChecks(studentId, date);
	}

	@Override
	public void insertChecks(MultivaluedMap<String, String> form, Long schoolId, String term, Long userId) {
		HomeworkCheckMapper mapper = super.getMapper(HomeworkCheckMapper.class);
		//HomeworkEvaluationMapper mapperE = super.getMapper(HomeworkEvaluationMapper.class);
		//classesId、studentId、date、description

		List<String> homeworkListStr = form.get("homeworkId");
		List<String> courseListStr = form.get("courseId");
		List<String> descList = form.get("description");
		Long studentId = Long.parseLong(form.getFirst("studentId"));
		Long classesId = Long.parseLong(form.getFirst("classesId"));
		
		
		int total = courseListStr.size();
		
		/*//添加作业评价
		HomeworkEvaluation evaluation = new HomeworkEvaluation();
		String des = "";
		try {
			des = URLDecoder.decode(form.getFirst("description"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!"".equals(des)) {
			evaluation.setId(super.getMapper(SequenceMapper.class).sequence());
			evaluation.setDescription(des);
			evaluation.setClassesId(classesId);
			evaluation.setStudentId(studentId);
			evaluation.setSchoolId(schoolId);
			evaluation.setTerm(term);
			evaluation.setCreateUserId(userId);
			evaluation.setCreateTime(new Timestamp(System.currentTimeMillis()));
			evaluation.setUpdateUserId(userId);
			evaluation.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			if (form.getFirst("date") != null && !"".equals(form.getFirst("date").toString())) {
				try {
					java.util.Date date = new SimpleDateFormat("yyyyMMdd").parse(form.getFirst("date").toString());
					evaluation.setDate(new Date(date.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			mapperE.insert(evaluation);
		}*/
		
		if (courseListStr != null && courseListStr.size() > 0) {
			//删除某个学生、某一天的检查数据
		//	mapper.deleteHomeworkChecks(studentId, form.getFirst("date").toString());
			
			for (int i = 0; i < total; i++) {
				HomeworkCheck hc = new HomeworkCheck();
				
				if (homeworkListStr.get(i) != null && !"".equals(homeworkListStr.get(i))) {
					hc.setHomeworkId(Long.parseLong(homeworkListStr.get(i)));
				}
				
				hc.setSchoolId(schoolId);
				hc.setTerm(term);
				hc.setClassesId(classesId);
				hc.setStudentId(studentId);
				hc.setDescription(descList.get(i));
				hc.setCourseId(Long.parseLong(courseListStr.get(i)));
				hc.setCreateUserId(userId);
				hc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				hc.setUpdateUserId(userId);
				hc.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				hc.setChecked(true);
				
				if(mapper.update(hc) == 0){//未进行评论,则新增该评论
					hc.setId(super.getMapper(SequenceMapper.class).sequence());
					mapper.insert(hc);
				}
			}
		}
	}

	@Override
	public int getStudentCheckListCount(Long schoolId, String term,
			Long classesId, Long homeworkId, String date) {
		return super.getMapper(HomeworkCheckMapper.class)
				.getStudentCheckListCount(schoolId, term, classesId,
						homeworkId, date);
	}

	@Override
	public List<StudentHomeworkCheck> getStudentCheckList(Long offset,
			Long rows, Long schoolId, String term, Long classesId,
			Long homeworkId, String date) {
		return super.getMapper(HomeworkCheckMapper.class).getStudentCheckList(
				offset, rows, schoolId, term, classesId, homeworkId, date);
	}

	@Override
	public List<StudentHomeworkCheck> getClassesCheckList(Long offset,
			Long rows, Long schoolId, String term, Long classesId,
			String dateBegin, String dateEnd) {
		return super.getMapper(HomeworkCheckMapper.class).getClassesCheckList(
				offset, rows, schoolId, term, classesId, dateBegin, dateEnd);
	}

	@Override
	public int getClassesCheckListCount(Long schoolId, String term,
			Long classesId, String dateBegin, String dateEnd) {
		return super.getMapper(HomeworkCheckMapper.class)
				.getClassesCheckListCount(schoolId, term, classesId, dateBegin,
						dateEnd);
	}

	@Override
	public int update(HomeworkCheck check) {
		return super.getMapper(HomeworkCheckMapper.class).update(check);
	}

}

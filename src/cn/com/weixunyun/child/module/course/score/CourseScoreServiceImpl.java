package cn.com.weixunyun.child.module.course.score;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

import org.apache.ibatis.annotations.Param;

public class CourseScoreServiceImpl extends AbstractService implements
		CourseScoreService {

	@Override
	public CourseScore select(Long id) {
		return super.getMapper(CourseScoreMapper.class).select(id);
	}

	@Override
	public int getListCount(Long schoolId, Long courseId, Long classesId,
			String term, String queryDate, String keyword) {
		return super.getMapper(CourseScoreMapper.class).getListCount(schoolId,
				courseId, classesId, term, queryDate, keyword);
	}

	@Override
	public List<CourseScore> getList(int offset, int rows, Long schoolId,
			Long courseId, Long classesId, String term, String queryDate,
			String keyword) {
		return super.getMapper(CourseScoreMapper.class).getList(offset, rows,
				schoolId, courseId, classesId, term, queryDate, keyword);
	}

	@Override
	public void insert(CourseScore courseScore) {
		super.getMapper(CourseScoreMapper.class).insert(courseScore);
	}

	@Override
	public void update(CourseScore courseScore) {
		super.getMapper(CourseScoreMapper.class).update(courseScore);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(CourseScoreMapper.class).delete(id);
	}

    @Override
    public void insertMulti(CourseScoreMulti s) {
        super.getMapper(CourseScoreMapper.class).insertMulti(s);
    }

    @Override
    public CourseScore getCurrentInfo(String term, Long classesId, Long courseId) {
        return super.getMapper(CourseScoreMapper.class).getCurrentInfo(term,classesId,courseId);
    }

    @Override
    public int hasData(@Param(value = "map") Map<String, Object> map) {
        return super.getMapper(CourseScoreMapper.class).hasData(map);
    }

    @Override
    public void deleteCurrentDay() {
        super.getMapper(CourseScoreMapper.class).deleteCurrentDay();
    }

    @Override
    public int insertMulti(Long schoolId, Long courseId, Long teacherId, String term, List<Map<String, Object>> list) {
        int count = list.size();
        CourseScoreMapper mapper = super.getMapper(CourseScoreMapper.class);

        for (Map<String, Object> map : list){
            CourseScoreMulti s = new CourseScoreMulti();
            if(map.get("studentName") != null){
                s.setStudentName(map.get("studentName").toString());
            }
            if(map.get("studentNo") !=null && !"".equals(map.get("studentNo"))){
                s.setStudentNo(map.get("studentNo").toString());
            } else {
                s.setStudentNo("unkown");
            }
            Object dateStr = map.get("date");
            if(dateStr !=null){
                java.util.Date date = null;
                try {
                    if( dateStr.toString().contains("/")){
                         date = new SimpleDateFormat("yyyy/MM/dd").parse(dateStr.toString());
                    } else if(dateStr.toString().contains("-")){
                         date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr.toString());
                    }
                    s.setDate(new Date(date.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(map.get("type") !=null){
                s.setType(map.get("type").toString());
            }
            if(map.get("score")!=null && !"".equals(map.get("score"))){
               s.setScore(Double.parseDouble(map.get("score").toString()));
            } else { //如果分数为空默认不插入
                count--;
                continue;
            }
            s.setSchoolId(schoolId);
            s.setId(super.getMapper(SequenceMapper.class).sequence());
            s.setCreateTeacherId(teacherId);
            s.setUpdateTeacherId(teacherId);
            s.setTerm(term);
            s.setCreateTime(new java.sql.Timestamp(System
                    .currentTimeMillis()));
            s.setUpdateTime(new java.sql.Timestamp(System
                    .currentTimeMillis()));

            s.setCourseId(courseId);
            mapper.insertMulti(s);
        }
        return count;
    }


	@Override
	public List<CourseScore> getStudentCourseList(Long schoolId, String term,Long studentId, Long courseId) {
		return super.getMapper(CourseScoreMapper.class).getStudentCourseList(schoolId, term, studentId, courseId);
	}

	@Override
	public List<CourseScore> getTeacherCourseList(Long schoolId, String term,
			Long classesId,
			Long courseId,
			Long teacherId) {
		return super.getMapper(CourseScoreMapper.class).getTeacherCourseList(schoolId, term, classesId,courseId,teacherId);
	}
}

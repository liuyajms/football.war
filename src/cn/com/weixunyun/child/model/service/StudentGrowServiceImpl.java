package cn.com.weixunyun.child.model.service;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.dao.StudentGrowMapper;
import cn.com.weixunyun.child.model.pojo.StudentGrow;
import cn.com.weixunyun.child.model.pojo.StudentGrowMulti;

public class StudentGrowServiceImpl extends AbstractService implements
		StudentGrowService {

	@Override
	public StudentGrow select(Long id) {
		return super.getMapper(StudentGrowMapper.class).select(id);
	}

	@Override
	public int getListCount(Long schoolId, Long studentId, String term,
			String queryDate, String keyword) {
		return super.getMapper(StudentGrowMapper.class).getListCount(schoolId,
				studentId, term, queryDate, keyword);
	}

	@Override
	public List<StudentGrow> getList(int offset, int rows, Long schoolId,
			Long studentId, String term, String queryDate, String keyword) {
		return super.getMapper(StudentGrowMapper.class).getList(offset, rows,
				schoolId, studentId, term, queryDate, keyword);
	}

	@Override
	public void insert(StudentGrow StudentGrow) {
		super.getMapper(StudentGrowMapper.class).insert(StudentGrow);
	}

	@Override
	public void update(StudentGrow StudentGrow) {
		super.getMapper(StudentGrowMapper.class).update(StudentGrow);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(StudentGrowMapper.class).delete(id);
	}

    @Override
    public void insertMulti(StudentGrowMulti s) {
        super.getMapper(StudentGrowMapper.class).insertMulti(s);
    }

    @Override
    public void deleteAll(Long classesId) {
        super.getMapper(StudentGrowMapper.class).deleteAll(classesId);
    }

    @Override
    public int hasData(Map<String, Object> map) {
        return super.getMapper(StudentGrowMapper.class).hasData(map);
    }

    @Override
    public void deleteCurrentDay() {
        super.getMapper(StudentGrowMapper.class).deleteCurrentDay();
    }


    @Override
    public int insertMulti(Long schoolId, Long classesId, Long teacherId, String term, List<Map<String, Object>> list, String del_grow) {
        int count = list.size();
        StudentGrowMapper mapper = super.getMapper(StudentGrowMapper.class);
        if( "1".equals(del_grow) ){
            mapper.deleteAll(classesId);
        }

        for (Map<String, Object> map : list){
            StudentGrowMulti s = new StudentGrowMulti();
            if(map.get("studentName") != null){
                s.setStudentName(map.get("studentName").toString());
            }
            if(map.get("studentNo") !=null && !"".equals(map.get("studentNo"))){
                s.setStudentNo(map.get("studentNo").toString());
            } else {
                s.setStudentNo("unkown");
            }
            if(map.get("name") !=null){
                s.setName(map.get("name").toString());
            }
            if(map.get("type") !=null){
                s.setType(map.get("type").toString());
            }
            if(map.get("description")!=null){
                s.setDescription(map.get("description").toString());
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

            mapper.insertMulti(s);
        }
        return count;
    }
}

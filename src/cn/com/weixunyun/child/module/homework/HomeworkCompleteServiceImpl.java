package cn.com.weixunyun.child.module.homework;

import java.util.List;

import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.service.AbstractService;

public class HomeworkCompleteServiceImpl extends AbstractService implements HomeworkCompleteService {

	@Override
	public int selectAllCount() {
		
		return super.getMapper(HomeworkCompleteMapper.class).selectAllCount();
	}

	@Override
	public List<HomeworkComplete> selectAll() {
		
		return super.getMapper(HomeworkCompleteMapper.class).selectAll();
	}

	@Override
	public void insert(HomeworkComplete hwc) {
		
		super.getMapper(HomeworkCompleteMapper.class).insert(hwc);
		
	}

	@Override
	public int selectAllStudentCount(Long teacherId) {
		return super.getMapper(HomeworkCompleteMapper.class).selectAllStudentCount(teacherId);
	}

	@Override
	public List<ClassesStudent> selectAllStudent(int offset, int rows,
			Long teacherId) {
		return super.getMapper(HomeworkCompleteMapper.class).selectAllStudent(offset, rows, teacherId);
	}

	@Override
	public int selectStudentHomeworkCount(Long teacherId, Long studentId) {
		return super.getMapper(HomeworkCompleteMapper.class).selectStudentHomeworkCount(teacherId, studentId);
	}

	@Override
	public List<StudentHomeworkComplete> selectStudentHomework(Long teacherId, Long studentId) {
		return super.getMapper(HomeworkCompleteMapper.class).selectStudentHomework(teacherId, studentId);
	}

	@Override
	public void update(HomeworkComplete hwc) {
		
		super.getMapper(HomeworkCompleteMapper.class).update(hwc);
	}

}

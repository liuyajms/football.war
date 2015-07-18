package cn.com.weixunyun.child.module.point;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.bean.User;
import cn.com.weixunyun.child.model.dao.ParentsMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.dao.TeacherMapper;
import cn.com.weixunyun.child.model.dao.UserMapper;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.AbstractService;

public class PointServiceImpl extends AbstractService implements PointService {

	@Override
	public Point get(Long id) {
		return super.getMapper(PointMapper.class).get(id);
	}

	@Override
	public int getListCount(Long schoolId) {
		return super.getMapper(PointMapper.class).getListCount(schoolId);
	}

	@Override
	public List<Point> getList(Long offset, Long rows, Long schoolId) {
		return super.getMapper(PointMapper.class).getList(offset, rows, schoolId);
	}

	@Override
	public void insert(Point point) {
		super.getMapper(PointMapper.class).insert(point);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(PointMapper.class).delete(id);
	}

	@Override
	public void deletePoints(Long schoolId) {
		super.getMapper(PointMapper.class).deletePoints(schoolId);
	}

	@Override
	public void insertPoint(Long schoolId, Long userId, String module, Boolean plus) {
		//添加广播，相应的积分表中添加一条记录，相应的人也要更新相应的分数
		PointMapper mapper = super.getMapper(PointMapper.class);
		TeacherMapper mapperT = getMapper(TeacherMapper.class);
		ParentsMapper mapperP = getMapper(ParentsMapper.class); 
		int p = plus == true? 1: -1;
		
		Point point = new Point();
		point.setId(super.getMapper(SequenceMapper.class).sequence());
		point.setSchoolId(schoolId);
		point.setTime(new Timestamp(System.currentTimeMillis()));
		point.setUserId(userId);
		point.setModule(module);
		point.setPoint(p);
		mapper.insert(point);
		
		User user = getMapper(UserMapper.class).get(userId);
		if (0 == user.getType()) { //是教师
			Teacher teacher = mapperT.select(userId);
			mapperT.updatePoint(userId, teacher.getPoint() + p);
		} else {
			StudentParents parents = mapperP.select(userId);
			mapperP.updatePoint(userId, parents.getPoint() + p);
		}
	}

	@Override
	public void exchange(Long teacherId, MultivaluedMap<String, String> form) {
		//积分兑换
		TeacherMapper mapperT = super.getMapper(TeacherMapper.class);
		Teacher teacher = mapperT.select(teacherId);
		mapperT.updatePoint(teacherId, teacher.getPoint() - Integer.parseInt(form.getFirst("score")));
		
		PointMapper mapperP = super.getMapper(PointMapper.class);
		Point point = new Point();
		point.setId(super.getMapper(SequenceMapper.class).sequence());
		point.setPoint(- Integer.parseInt(form.getFirst("score")));
		point.setSchoolId(teacher.getSchoolId());
		point.setUserId(teacher.getId());
		point.setTime(new Timestamp(System.currentTimeMillis()));
		point.setModule("exchange");
		try {
			point.setDescription(form.getFirst("desc")==null?null:URLDecoder.decode(form.getFirst("desc").toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		mapperP.insert(point);
	}

}

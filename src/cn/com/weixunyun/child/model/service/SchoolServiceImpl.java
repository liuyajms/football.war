package cn.com.weixunyun.child.model.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import cn.com.weixunyun.child.model.dao.ClassesMapper;
import cn.com.weixunyun.child.model.dao.ClassesTeacherMapper;
import cn.com.weixunyun.child.model.dao.CourseMapper;
import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.dao.ParentsMapper;
import cn.com.weixunyun.child.model.dao.SchoolMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.dao.StudentMapper;
import cn.com.weixunyun.child.model.dao.StudentParentsMapper;
import cn.com.weixunyun.child.model.dao.TeacherMapper;
import cn.com.weixunyun.child.model.dao.TemplateMapper;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.ClassesTeacher;
import cn.com.weixunyun.child.model.pojo.Course;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.pojo.Parents;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Student;
import cn.com.weixunyun.child.model.pojo.StudentParents;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.pojo.Template;
import cn.com.weixunyun.child.module.broadcast.Broadcast;
import cn.com.weixunyun.child.module.broadcast.BroadcastMapper;
import cn.com.weixunyun.child.module.notice.Notice;
import cn.com.weixunyun.child.module.notice.NoticeMapper;
import cn.com.weixunyun.child.module.weibo.Weibo;
import cn.com.weixunyun.child.module.weibo.WeiboMapper;

public class SchoolServiceImpl extends AbstractService implements SchoolService {

	@Override
	public School select(long id) {
		return super.getMapper(SchoolMapper.class).select(id);
	}

	@Override
	public int selectAllCount(String keyword) {
		return super.getMapper(SchoolMapper.class).selectAllCount(keyword);
	}

	@Override
	public List<School> selectAll(Long offset, Long rows, String keyword) {
		return super.getMapper(SchoolMapper.class).selectAll(offset, rows, keyword);
	}

	@Override
	public void insert(School school) {
		super.getMapper(SchoolMapper.class).insert(school);
	}

	@Override
	public void update(School school) {
		super.getMapper(SchoolMapper.class).update(school);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(SchoolMapper.class).delete(id);
	}

	@Override
	public List<School> getAllSchools(String countyCode) {
		return super.getMapper(SchoolMapper.class).getAllSchools(countyCode);
	}

	@Override
	public void insertSchool(School s) {
		
		SchoolMapper mapperSchool = super.getMapper(SchoolMapper.class);
		mapperSchool.insert(s);
		
		Date date = new Date(System.currentTimeMillis());
		int year =  date.getYear() + 1900;
		int month = date.getMonth() + 1;
		String term = "";
		
		//全局设置，复制schoolId=1176的数据
		GlobalMapper mapperGlobal = super.getMapper(GlobalMapper.class);
		List<Global> gList = mapperGlobal.selectAll(1176L);
		for (Global global : gList) {
			if ("elective".equals(global.getCodeParent()) && ("begin".equals(global.getCode()) || "end".equals(global.getCode()))) {
				global.setValue(year + "" + global.getValue().substring(4));
			} else if ("term".equals(global.getCodeParent()) && "begin".equals(global.getCode())) {
				global.setValue(year + "" + global.getValue().substring(4));
			} else if ("term".equals(global.getCodeParent()) && "default".equals(global.getCode())) {
				if (month > 7) {
					global.setValue(year + "" + 0);
					term =year + "" + 0;
				} else {
					global.setValue((year - 1) + "" + 1);
					term = (year - 1) + "" + 1;
				}
			}
		
			global.setSchoolId(s.getId());
			mapperGlobal.insert(global);
		}
		
		//数据字典，复制schoolId=0的数据
		DictionaryValueMapper mapperValue = super.getMapper(DictionaryValueMapper.class);
		List<DictionaryValue> vList = mapperValue.getList(0L);
		for (DictionaryValue dv : vList) {
			dv.setSchoolId(s.getId());
			mapperValue.insert(dv);
		}
		
		//课程，复制schoolId=0的数据
		Long courseId = null;
		CourseMapper mapperCourse = super.getMapper(CourseMapper.class);
		List<Course> cList = mapperCourse.selectAll(0L, 0L, 1000L, null);
		for (Course c : cList) {
			c.setId(super.getMapper(SequenceMapper.class).sequence());
			c.setSchoolId(s.getId());
			mapperCourse.insert(c);
			
			courseId = c.getId();
		}
		
		//模板，复制schoolId=0的数据
		TemplateMapper mapperTemplate = super.getMapper(TemplateMapper.class);
		Template template = mapperTemplate.select("cook", 0L);
		template.setSchoolId(s.getId());
		mapperTemplate.insert(template);
		
		//添加一个教师：小张，一个班级，并在这个班级里面添加两个学生：小李、小王
		TeacherMapper mapperTeacher = super.getMapper(TeacherMapper.class);
		Teacher teacher = new Teacher();
		teacher.setId(super.getMapper(SequenceMapper.class).sequence());
		teacher.setName("张小");
		teacher.setMobile("1370000" + (Math.random()*9000+1000+"").toString().substring(0, 4));
		teacher.setUsername(teacher.getMobile());
		teacher.setPassword(DigestUtils.md5Hex("1234"));
		teacher.setAvailable(true);
		teacher.setCreateTime(new Timestamp(System.currentTimeMillis()));
		teacher.setSchoolId(s.getId());
		teacher.setType(6L);
		mapperTeacher.insert(teacher);
		
		ClassesMapper mapperClasses = super.getMapper(ClassesMapper.class);
		Classes classes = new Classes();
		classes.setId(super.getMapper(SequenceMapper.class).sequence());
		classes.setYear((long) year);
		classes.setNum("1");
		classes.setName(year + "级1班");
		classes.setTeacherId(teacher.getId());
		classes.setSchoolId(s.getId());
		mapperClasses.insert(classes);
		
		ClassesTeacherMapper mapperClassesTeacher = super.getMapper(ClassesTeacherMapper.class);
		ClassesTeacher ct = new ClassesTeacher();
		ct.setSchoolId(s.getId());
		ct.setClassesId(classes.getId());
		ct.setCourseId(courseId);
		ct.setTeacherId(teacher.getId());
		mapperClassesTeacher.insert(ct);
		
		StudentMapper mapperStudent = super.getMapper(StudentMapper.class);
		Student student = new Student();
		student.setId(super.getMapper(SequenceMapper.class).sequence());
		student.setClassesId(classes.getId());
		student.setSchoolId(s.getId());
		student.setName("李小");
		student.setCreateTime(new Timestamp(System.currentTimeMillis()));
		student.setSchoolId(s.getId());
		mapperStudent.insert(student);
		
		//家长
		ParentsMapper mapperParents = super.getMapper(ParentsMapper.class);
		Parents parents = new Parents();
		parents.setId(super.getMapper(SequenceMapper.class).sequence());
		parents.setName("李大");
		parents.setMobile("1390000" + (Math.random()*9000+1000+"").toString().substring(0, 4));
		parents.setAvailable(true);
		parents.setPassword(DigestUtils.md5Hex("1234"));
		parents.setPoint(0);
		parents.setPta(false);
		parents.setCreateTime(new Timestamp(System.currentTimeMillis()));
		mapperParents.insert(parents);
		
		StudentParentsMapper mapperSP = super.getMapper(StudentParentsMapper.class);
		StudentParents sp = new StudentParents();
		sp.setParentsId(parents.getId());
		sp.setSchoolId(s.getId());
		sp.setStudentId(student.getId());
		sp.setType("1");
		sp.setUsername(parents.getMobile());
		mapperSP.insert(sp);
		
		student = new Student();
		student.setId(super.getMapper(SequenceMapper.class).sequence());
		student.setClassesId(classes.getId());
		student.setSchoolId(s.getId());
		student.setName("王小");
		student.setCreateTime(new Timestamp(System.currentTimeMillis()));
		student.setSchoolId(s.getId());
		mapperStudent.insert(student);
		
		//家长
		parents = new Parents();
		parents.setId(super.getMapper(SequenceMapper.class).sequence());
		parents.setName("王大");
		parents.setMobile("1390000" + (Math.random()*9000+1000+"").toString().substring(0, 4));
		parents.setAvailable(true);
		parents.setPassword(DigestUtils.md5Hex("1234"));
		parents.setPoint(0);
		parents.setPta(false);
		parents.setCreateTime(new Timestamp(System.currentTimeMillis()));
		mapperParents.insert(parents);
		
		sp = new StudentParents();
		sp.setParentsId(parents.getId());
		sp.setSchoolId(s.getId());
		sp.setStudentId(student.getId());
		sp.setType("1");
		sp.setUsername(parents.getMobile());
		mapperSP.insert(sp);
		
		//学校公告、班级广播、班级微博
		NoticeMapper mapperNotice = super.getMapper(NoticeMapper.class);
		Notice notice = new Notice();
		notice.setId(super.getMapper(SequenceMapper.class).sequence());
		notice.setClassesId(classes.getId());
		notice.setSchoolId(s.getId());
		notice.setCreateUserId(teacher.getId());
		notice.setCreateTime(new Timestamp(System.currentTimeMillis()));
		notice.setDescription("欢迎使用微讯云通！使用本产品前，请认真阅读使用手册，并妥善保存，必备查阅。");
		mapperNotice.insert(notice);
		
		BroadcastMapper mapperBroadcast = super.getMapper(BroadcastMapper.class);
		Broadcast broadcast = new Broadcast();
		broadcast.setId(super.getMapper(SequenceMapper.class).sequence());
		broadcast.setSchoolId(s.getId());
		broadcast.setClassesId(classes.getId());
		broadcast.setCreateTeacherId(teacher.getId());
		broadcast.setCreateTime(new Timestamp(System.currentTimeMillis()));
		broadcast.setDescription("非常感谢您使用微讯云通。使用本产品前，请认真阅读使用手册，并妥善保存，必备查阅。");
		broadcast.setTitle("欢迎使用微讯云通");
		broadcast.setTerm(term);
		broadcast.setComment(true);
		mapperBroadcast.insert(broadcast);
		
		WeiboMapper mapperWeibo = super.getMapper(WeiboMapper.class);
		Weibo weibo = new Weibo();
		weibo.setId(super.getMapper(SequenceMapper.class).sequence());
		weibo.setClassesId(classes.getId());
		weibo.setSchoolId(s.getId());
		weibo.setCreateUserId(teacher.getId());
		weibo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		weibo.setDescription("非常感谢您使用微讯云通。使用本产品前，请认真阅读使用手册，并妥善保存，必备查阅。");
		mapperWeibo.insert(weibo);
	}

}

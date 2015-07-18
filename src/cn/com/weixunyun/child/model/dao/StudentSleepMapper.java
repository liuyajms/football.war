package cn.com.weixunyun.child.model.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import cn.com.weixunyun.child.model.pojo.StudentSleep;

public interface StudentSleepMapper {

	@Insert("insert into student_sleep (student_id, sleep_time,date) values (#{studentId}, #{sleepTime},#{date})")
	public void insert(StudentSleep studentSleep);
	
	@Delete("delete from student_sleep where student_id = #{studentId} and date = #{date}")
	public void delete(StudentSleep studentSleep);

}

package cn.com.weixunyun.child.module.elective;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface ElectiveStudentMapper {

	@Select("select es.*, e.name as electiveName, e.date as electiveDate, s.name as studentName from elective_student es join elective e on e.id = es.elective_id join student s on s.id = es.student_id where es.id = #{id} limit 1")
	public CourseElectiveStudent select(Long id);

	@Select("select (c.num - count(e.id)) as count from elective c left join  elective_student e on c.id = e.elective_id where c.id = #{electiveId} group by c.num")
	public int selectElectiveNumCount(@Param(value = "electiveId") Long electiveId);

	@SelectProvider(type = ElectiveStudentMapperProvider.class, method = "selectElective")
	public List<CourseElectiveStudent> selectElective(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "schoolId") Long schoolId,
			@Param(value = "electiveId") Long electiveId, @Param(value = "term") String term, @Param(value = "keyword") String keyword);

	@SelectProvider(type = ElectiveStudentMapperProvider.class, method = "selectElectiveCount")
	public int selectElectiveCount(@Param(value = "schoolId") Long schoolId,
			@Param(value = "electiveId") Long electiveId, @Param(value = "term") String term, @Param(value = "keyword") String keyword);

	@SelectProvider(type = ElectiveStudentMapperProvider.class, method = "selectStudent")
	public List<StudentElectiveStudent> selectStudent(@Param(value = "offset") Long offset,
			@Param(value = "rows") Long rows, @Param(value = "studentId") Long studentId,
			@Param(value = "term") String term);

	@SelectProvider(type = ElectiveStudentMapperProvider.class, method = "selectCount")
	public int selectAllCount(@Param(value = "schoolId") Long schoolId, @Param(value = "keyword") String keyword,
			@Param(value = "term") String term);

	@SelectProvider(type = ElectiveStudentMapperProvider.class, method = "select")
	public List<ElectiveStudent> selectAll(@Param(value = "offset") Long offset, @Param(value = "rows") Long rows,
			@Param(value = "keyword") String keyword, @Param(value = "schoolId") Long schoolId,
			@Param(value = "term") String term);

	@Insert("insert into elective_student (id, term, student_id, elective_id, time, score, score_time, score_description, school_id) values (#{id}, #{term}, #{studentId}, #{electiveId}, now(), #{score}, #{scoreTime}, #{scoreDescription}, #{schoolId})")
	public void insert(ElectiveStudent es);

	@Update("update elective_student set term=#{term}, student_id=#{studentId}, elective_id=#{electiveId}, time=#{time}, score=#{score}, score_time=#{score_time}, score_description=#{score_description} where id=#{id}")
	public void update(ElectiveStudent es);

	@Update("update elective_student set score=#{score}, score_time=now(), score_description=#{scoreDescription} where id=#{id}")
	public void updateScore(ElectiveStudent es);
	
	@Update("update elective_student set score=#{score}, score_time=now(), score_description=#{scoreDescription} where elective_id=#{electiveId} and school_id=#{schoolId} and term=#{term} and student_id = (select id from student where name=#{studentName} and school_id = #{schoolId} limit 1) ")
	public void updateMulti(CourseElectiveStudent es);

	@Delete("delete from elective_student where school_id = #{schoolId} and student_id = #{studentId} and term = #{term}")
	public void deleteStudent(@Param(value = "schoolId") Long schoolId, @Param(value = "studentId") Long studentId,
			@Param(value = "term") String term);

	@Delete("delete from elective_student where id=#{id}")
	public void delete(Long id);

}

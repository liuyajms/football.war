package cn.com.weixunyun.child.model.dao;

import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.pojo.StudentGrowMulti;
import org.apache.ibatis.annotations.*;

import cn.com.weixunyun.child.model.pojo.StudentGrow;

public interface StudentGrowMapper {

    @Select("select * from student_Grow where id = #{id}")
    public StudentGrow select(Long id);


    @SelectProvider(type = StudentGrowMapperProvider.class, method = "getListCount")
    public int getListCount(
            @Param(value = "schoolId") Long schoolId,
            @Param(value = "studentId") Long studentId,
            @Param(value = "term") String term,
            @Param(value = "queryDate") String queryDate,
            @Param(value = "keyword") String keyword);


    @SelectProvider(type = StudentGrowMapperProvider.class, method = "getList")
    public List<StudentGrow> getList(@Param(value = "offset") int offset,
                                     @Param(value = "rows") int rows,
                                     @Param(value = "schoolId") Long schoolId,
                                     @Param(value = "studentId") Long studentId,
                                     @Param(value = "term") String term,
                                     @Param(value = "queryDate") String queryDate,
                                     @Param(value = "keyword") String keyword);


    @Insert("insert into Student_Grow (id, student_id, type, name, description, create_teacher_id, create_time, update_teacher_id, update_time, school_id, term) values (#{id}, #{studentId}, #{type}, #{name}, #{description}, #{createTeacherId}, now(),#{updateTeacherId}, now(), #{schoolId}, #{term})")
    public void insert(StudentGrow StudentGrow);

    @Update("update Student_Grow set  name=#{name}, description=#{description}, update_teacher_id=#{updateTeacherId}, update_time=now() where id=#{id}")
    public void update(StudentGrow StudentGrow);

    @Delete("delete from Student_Grow where id=#{id}")
    public void delete(Long id);

    @Insert("insert into Student_Grow (id, student_id, type, name, description, create_teacher_id, create_time, update_teacher_id, update_time, school_id, term) values (#{id}, (select id from student where code=#{studentNo} or (name=#{studentName} and #{studentNo}='unkown') limit 1), #{type}, #{name}, #{description}, #{createTeacherId}, now(),#{updateTeacherId}, now(), #{schoolId}, #{term})")
    void insertMulti(StudentGrowMulti s);

    @Delete("delete from Student_Grow where student_id in (select id from student where classes_id=#{classesId})")
    void deleteAll(Long classesId);

    @Select("select count(*) from Student_Grow where #{map.type}=type and #{map.name}=name and to_char(create_time,'yyyy-MM-dd') = to_char(current_date,'yyyy-MM-dd')")
    int hasData(Map<String, Object> map);

    @Delete("delete from Student_Grow where to_char(create_time,'yyyy-MM-dd') = to_char(current_date,'yyyy-MM-dd')")
    void deleteCurrentDay();
}

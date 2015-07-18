package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;




import cn.com.weixunyun.child.model.bean.Sensitive;


public interface SensitiveMapper {

	@Insert({
		"insert into sensitive (id, available, name,name0,name1,name2,name3,name4,name5,name6,name7)",
		"values (#{id}, #{available},#{name}, #{name0},#{name1},#{name2},#{name3},#{name4},#{name5},#{name6},#{name7}) ",
})
int insert(Sensitive record);



@Update({ "update sensitive set name=#{name},available=#{available}, name0=#{name0},name1= #{name1},name2= #{name2},name3= #{name3},name4= #{name4},name5= #{name5},name6= #{name6},name7= #{name7} where id = #{id}" })
void update(Sensitive record);

//@Select("select a.*, b.name moduleName from website_Link a left join website_Link b on a.id_parent=b.id order by a.id")
//		+ "where  a.id_parent=#{id} order by a.id")

@SelectProvider(type = SensitiveMapperProvider.class, method = "getList")
public List<Sensitive> getList(@org.apache.ibatis.annotations.Param(value = "offset") int offset,
        @org.apache.ibatis.annotations.Param(value = "rows") int rows,
        @org.apache.ibatis.annotations.Param(value = "keyword") String keyword);// 查询模块下所有接口


@SelectProvider(type =  SensitiveMapperProvider.class, method = "getListCount")
public int getListCount( @Param(value = "keyword") String keyword);

@Select("select a.* from sensitive a where  a.id=#{id}")
public Sensitive select(Long id);// 查询接口

@Delete("delete from sensitive where id=#{id}")
public void delete(Long id);



//use for templateParser
@SelectProvider(type =  SensitiveMapperProvider.class, method = "getSQL")
public List<Sensitive> getSqlList(String sql);



@Delete("delete from sensitive")
public void deleteSensitives();
}

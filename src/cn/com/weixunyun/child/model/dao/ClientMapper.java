package cn.com.weixunyun.child.model.dao;

import cn.com.weixunyun.child.model.pojo.Client;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ClientMapper {

    @Select("select * from client where code=#{code}")
    public Client select(String code);

    @Select("select count(*) from client")
    public int count(Map<String, Object> params);

    @Select("select * from client ")
    public List<Client> list(Map<String, Object> params);

    @Select("select * from client where available=true")
    public List<Client> listAvailable();

    @Insert("insert into client(code,platform,description,available,web) " +
            "values(#{code},#{platform},#{description},#{available},#{web})")
    public void insert(Client record);

    @Update("update client set platform=#{platform}, description=#{description}, available=#{available}, web=#{web} where code=#{code}")
    public void update(Client record);

    @Delete("delete from client where code=#{code}")
    public void delete(String code);
}

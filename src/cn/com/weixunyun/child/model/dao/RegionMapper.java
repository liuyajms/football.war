package cn.com.weixunyun.child.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import cn.com.weixunyun.child.model.pojo.Region;

public interface RegionMapper {
	@Select("select * from region where level = '1'")
	public List<Region> getAllPrivinces();
	
	@Select("select * from region where level = '2' and code like #{provinceCode}||'%'")
	public List<Region> getAllCities(String provinceCode);
	
	@Select("select * from region where level = '3' and code like #{cityCode}||'%'")
	public List<Region> getAllCounties(String cityCode);

}

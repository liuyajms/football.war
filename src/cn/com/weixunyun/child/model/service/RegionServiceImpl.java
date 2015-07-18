package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.RegionMapper;
import cn.com.weixunyun.child.model.pojo.Region;

public class RegionServiceImpl extends AbstractService implements RegionService{

	@Override
	public List<Region> getAllPrivinces() {
		return super.getMapper(RegionMapper.class).getAllPrivinces();
	}

	@Override
	public List<Region> getAllCities(String provinceCode) {
		return super.getMapper(RegionMapper.class).getAllCities(provinceCode);
	}

	@Override
	public List<Region> getAllCounties(String cityCode) {
		return super.getMapper(RegionMapper.class).getAllCounties(cityCode);
	}

}

package cn.com.weixunyun.child.model.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.annotations.Param;

import cn.com.weixunyun.child.model.bean.Sensitive;
import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.dao.SensitiveMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.dao.TeacherMapper;
import cn.com.weixunyun.child.model.pojo.Teacher;



import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-9-16.
 */
public class SensitiveServiceImpl extends AbstractService implements
SensitiveService {

	@Override
	public int insert(Sensitive record) {
		return super.getMapper(SensitiveMapper.class).insert(record);
	}



	@Override
	public Sensitive select(Long id) {
		return super.getMapper(SensitiveMapper.class).select(id);
	}

	@Override
	public void delete(Long id) {

		super.getMapper(SensitiveMapper.class).delete(id);

	}


	@Override
	public void update(Sensitive record) {
		super.getMapper(SensitiveMapper.class).update(record);
	}



	@Override
	public List<Sensitive> getList(int offset, int rows, String keyword) {
		return super.getMapper(SensitiveMapper.class).getList(offset, rows, keyword);
	}

	@Override
	public int getListCount(String keyword) {
		return super.getMapper(SensitiveMapper.class).getListCount(keyword);
	}



	@Override
	public List<Sensitive> getSqlList(String sql) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int insertSensitives(Long schoolId, int del,
			List<Map<String, Object>> list) {
		SensitiveMapper mapper = super.getMapper(SensitiveMapper.class);
		
		if (del == 1) {
			mapper.deleteSensitives();
		}
		
		Sensitive sensitive = new Sensitive();
		for (Map<String, Object> m : list) {
			sensitive.setId(super.getMapper(SequenceMapper.class).sequence());
			sensitive.setName(m.get("name").toString());
			sensitive.setName0(m.get("name0") == null?null:m.get("name0").toString());
			sensitive.setName1(m.get("name1") == null?null:m.get("name1").toString());
			sensitive.setName2(m.get("name2") == null?null:m.get("name2").toString());
			sensitive.setName3(m.get("name3") == null?null:m.get("name3").toString());
			sensitive.setName4(m.get("name4") == null?null:m.get("name4").toString());
			sensitive.setName5(m.get("name5") == null?null:m.get("name5").toString());
			sensitive.setName6(m.get("name6") == null?null:m.get("name6").toString());
			sensitive.setName7(m.get("name7") == null?null:m.get("name7").toString());
			
			if (m.get("available") != null && m.get("available").toString().equals("是")) {
				sensitive.setAvailable(true);
			} else {
				sensitive.setAvailable(false);
			}

			
			mapper.insert(sensitive);
		}
		
		return list.size();
	}



	@Override
	public void deleteSensitives() {
		super.getMapper(SensitiveMapper.class).deleteSensitives();	
	}



	

}

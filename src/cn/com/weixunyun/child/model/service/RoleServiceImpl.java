package cn.com.weixunyun.child.model.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.weixunyun.child.model.bean.RolePopedom;
import cn.com.weixunyun.child.model.dao.RoleMapper;
import cn.com.weixunyun.child.model.pojo.Role;

public class RoleServiceImpl extends AbstractService implements RoleService {

	@Override
	public Role select(Long id) {
		return super.getMapper(RoleMapper.class).select(id);
	}

	@Override
	public int selectAllCount(Long schoolId) {
		return super.getMapper(RoleMapper.class).selectAllCount(schoolId);
	}

	@Override
	public List<Role> selectAll(Long offset, Long rows, Long schoolId) {
		return super.getMapper(RoleMapper.class).selectAll(offset, rows, schoolId);
	}

	@Override
	public void insert(Role role) {
		super.getMapper(RoleMapper.class).insert(role);
	}

	@Override
	public void update(Role role) {
		super.getMapper(RoleMapper.class).update(role);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(RoleMapper.class).delete(id);
	}

	@Override
	public List<RolePopedom> getPopedomList(Long id) {
		return super.getMapper(RoleMapper.class).getPopedomList(id);
	}

	@Override
	public void updatePopedom(Long roleId, List<String> popedomIdList, List<String> actionList) {
		RoleMapper mapper = super.getMapper(RoleMapper.class);
		mapper.clearPopedom(roleId);
		int i = 0;
		for (String popedomId : popedomIdList) {
			mapper.insertPopedom(roleId, Long.parseLong(popedomId), Long.parseLong(actionList.get(i)));
			i++;
		}
	}

	@Override
	public void clearPopedom(Long id) {
		super.getMapper(RoleMapper.class).clearPopedom(id);
	}

	@Override
	public void insertPopedom(@Param("roleId") Long roleId, @Param("popedomId") Long popedomId,
			@Param("action") Long action) {
		super.getMapper(RoleMapper.class).insertPopedom(roleId, popedomId, action);
	}

	@Override
	public Long getAdmin() {
		return super.getMapper(RoleMapper.class).getAdmin();
	}

}

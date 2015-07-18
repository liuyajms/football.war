package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.RoleMapper;

public interface RoleService extends RoleMapper {

	public void updatePopedom(Long roleId, List<String> popedomIdList, List<String> actionList);
}

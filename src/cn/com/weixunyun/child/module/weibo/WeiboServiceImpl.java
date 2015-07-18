package cn.com.weixunyun.child.module.weibo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

public class WeiboServiceImpl extends AbstractService implements WeiboService {

	@Override
	public void insert(Weibo weibo) {
		super.getMapper(WeiboMapper.class).insert(weibo);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(WeiboMapper.class).delete(id);
	}

	@Override
	public void update(Weibo weibo) {
		super.getMapper(WeiboMapper.class).update(weibo);
	}

	@Override
	public UserWeibo get(@Param("id") Long id, @Param("userId") Long userId) {
		return super.getMapper(WeiboMapper.class).get(id, userId);
	}

	@Override
	@SelectProvider(type = WeiboMapperProvider.class, method = "getSchoolWeiboListCount")
	public int getSchoolWeiboListCount(@Param("schoolId") Long schoolId,
			@Param(value = "userId") Long userId,
			@Param(value = "keyword") String keyword) {
		return super.getMapper(WeiboMapper.class).getSchoolWeiboListCount(
				schoolId, userId, keyword);
	}

	@Override
	@SelectProvider(type = WeiboMapperProvider.class, method = "getSchoolWeiboList")
	public List<UserWeibo> getSchoolWeiboList(@Param("offset") Long offset,
			@Param("rows") Long rows, @Param("schoolId") Long schoolId,
			@Param(value = "userId") Long userId,
			@Param(value = "keyword") String keyword) {
		return super.getMapper(WeiboMapper.class).getSchoolWeiboList(offset,
				rows, schoolId, userId, keyword);
	}

	@Override
	public int getClassesWeiboListCount(Long schoolId, Long keyword) {
		return super.getMapper(WeiboMapper.class).getClassesWeiboListCount(schoolId, keyword);
	}

    @Override
    public void insertZan(WeiboZan zan) {
        super.getMapper(WeiboMapper.class).insertZan(zan);
    }

    @Override
    public void deleteZan(Long weiboId, Long userId) {
        super.getMapper(WeiboMapper.class).deleteZan(weiboId,userId);
    }


    @Override
	@SelectProvider(type = WeiboMapperProvider.class, method = "getListCount")
	public int getListCount(@Param("schoolId") Long schoolId,
			@Param("userId") Long userId, @Param("classesId") Long classesId,
			@Param("studentId") Long studentId,
			@Param("teacherId") Long teacherId) {
		return super.getMapper(WeiboMapper.class).getListCount(schoolId,
				userId, classesId, studentId, teacherId);
	}

	@Override
	@SelectProvider(type = WeiboMapperProvider.class, method = "getList")
	public List<UserWeibo> getList(@Param("offset") Long offset, @Param("rows") Long rows, @Param("schoolId") Long schoolId,
			@Param("userId") Long userId, @Param("classesId") Long classesId, @Param("studentId") Long studentId,
			@Param("teacherId") Long teacherId) {
        return super.getMapper(WeiboMapper.class).getList(offset, rows, schoolId, userId, classesId, studentId, teacherId);
	}


    @Override
    public List<UserWeibo> getClassesWeiboList(@Param(value = "offset") Long offset,
                                               @Param(value = "rows") Long rows,
                                               @Param(value = "schoolId") Long schoolId,
                                               @Param(value = "classesId") Long classesId) {
        WeiboMapper mapper = super.getMapper(WeiboMapper.class);
        return mapper.getClassesWeiboList(offset,rows,schoolId,classesId);
    }

    @Override
	public void updateImage(Long pic, Long id) {
		super.getMapper(WeiboMapper.class).updateImage(pic, id);
	}

	@Override
	public int insertWeibos(Long schoolId, Long classesId, Long userId, String term, int del, List<Map<String, Object>> list) {
		WeiboMapper mapper = super.getMapper(WeiboMapper.class);
		if (del == 1) {
			if (classesId == null) {
				//mapper.deleteWeiboSchool(schoolId);
			} else {
				mapper.deleteWeiboClasses(classesId);
			}
		}
		
		Weibo weibo = new Weibo();
		for (Map<String, Object> m : list) {
			weibo.setId(super.getMapper(SequenceMapper.class).sequence());
			weibo.setSchoolId(schoolId);
			weibo.setClassesId(classesId);
			weibo.setCreateUserId(userId);
			weibo.setCreateTime(new Timestamp(System.currentTimeMillis()));
			weibo.setUpdateUserId(userId);
			weibo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			weibo.setPic(0L);
//			weibo.setDescription(m.get("description")==null?null:m.get("description").toString());
			if(m.get("description")!=null){
				weibo.setDescription(SensitivewordFilter.replaceSensitiveWord(m.get("description").toString()));
			}
			mapper.insert(weibo);
		}
		return list.size();
	}

	@Override
	public void deleteWeiboClasses(Long classesId) {
		super.getMapper(WeiboMapper.class).deleteWeiboClasses(classesId);
	}
}

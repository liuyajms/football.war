package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.StudentGrowMapper;

import java.util.List;
import java.util.Map;

public interface StudentGrowService extends StudentGrowMapper {

    int insertMulti(Long schoolId, Long classesId, Long teacherId, String term, List<Map<String, Object>> list, String del_grow);
}

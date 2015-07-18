package cn.com.weixunyun.child.module.download;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.service.AbstractService;

public class DownloadServiceImpl extends AbstractService implements DownloadService {

	@Override
	public Download select(Long id) {
		return super.getMapper(DownloadMapper.class).select(id);
	}

	@Override
	public void insert(Download menu) {
		super.getMapper(DownloadMapper.class).insert(menu);
	}
	
	@Override
    public void update(Download record) {
        super.getMapper(DownloadMapper.class).update(record);
    }

	@Override
	public void delete(Long id) {
		super.getMapper(DownloadMapper.class).delete(id);
	}

	@Override
	public int selectCount(Long schoolId, Long classesId, Long flag) {
		return super.getMapper(DownloadMapper.class).selectCount(schoolId, classesId, flag);
	}

	@Override
	public List<ClassesDownload> selectAll(int offset, int rows, Long classesId, Long schoolId, Long flag) {
		return super.getMapper(DownloadMapper.class).selectAll(offset, rows, classesId, schoolId, flag);
	}

	@Override
	public int insertDownloads(Long schoolId, Long classesId, Long userId, int del, List<Map<String, Object>> list) {
		DownloadMapper mapper = super.getMapper(DownloadMapper.class);
		if (del == 1) {
			mapper.deleteDownloads(schoolId, classesId);
		}
		
		Download download = new Download();
		for (Map<String, Object> m : list) {
			download.setId(super.getMapper(SequenceMapper.class).sequence());
			download.setClassesId(classesId);
			download.setSchoolId(schoolId);
			download.setCreateTeacherId(userId);
			download.setCreateTime(new Timestamp(System.currentTimeMillis()));
			download.setName(m.get("name").toString());
			download.setSize(0L);
			if(m.get("topDays") != null){
				download.setTopDays(Long.parseLong(m.get("topDays").toString()));
			}
			mapper.insert(download);
		}
		return list.size();
	}

	@Override
	public void deleteDownloads(Long schoolId, Long classesId) {
		super.getMapper(DownloadMapper.class).deleteDownloads(schoolId, classesId);
	}

}

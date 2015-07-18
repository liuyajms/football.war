package cn.com.weixunyun.child.module.personal.journal;

import cn.com.weixunyun.child.model.service.AbstractService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class JournalServiceImpl extends AbstractService implements JournalService {

    @Override
    public UserJournal select(Long id) {
        return super.getMapper(JournalMapper.class).select(id);
    }

    @Override
    public int getListCount(@Param(value = "schoolId") Long schoolId, 
    		@Param(value = "userId") Long userId,
    		@Param(value = "keyword") String keyword) {
        return super.getMapper(JournalMapper.class).getListCount(schoolId,userId,keyword);
    }

    @Override
    public List<UserJournal> getList(@Param(value = "offset") int offset, @Param(value = "rows") int rows, @Param(value = "schoolId") Long schoolId,
    		@Param(value = "userId") Long userId,
    		@Param(value = "keyword") String keyword) {
        return super.getMapper(JournalMapper.class).getList(offset,rows,schoolId,userId, keyword);
    }

    @Override
    public void insert(Journal journal) {
        super.getMapper(JournalMapper.class).insert(journal);
    }

    @Override
    public void update(Journal journal) {
        super.getMapper(JournalMapper.class).update(journal);
    }

    @Override
    public void delete(Long id) {
        super.getMapper(JournalMapper.class).delete(id);
    }

    @Override
    public void updateImage(@Param(value = "id") Long id, @Param(value = "userId") Long userId, @Param(value = "pic") int pic) {
        super.getMapper(JournalMapper.class).updateImage(id,userId,pic);
    }
}

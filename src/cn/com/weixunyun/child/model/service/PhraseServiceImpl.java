package cn.com.weixunyun.child.model.service;

import java.util.List;

import cn.com.weixunyun.child.model.dao.PhraseMapper;
import cn.com.weixunyun.child.model.pojo.Phrase;

public class PhraseServiceImpl extends AbstractService implements PhraseService {

	@Override
	public Phrase select(long id) {
		return super.getMapper(PhraseMapper.class).select(id);
	}

	@Override
	public int selectAllCount() {
		return super.getMapper(PhraseMapper.class).selectAllCount();
	}

	@Override
	public List<Phrase> selectAll(int offset, int rows) {
		return super.getMapper(PhraseMapper.class).selectAll(offset * rows, rows);
	}

	@Override
	public void insert(Phrase Phrase) {
		super.getMapper(PhraseMapper.class).insert(Phrase);
	}

	@Override
	public void update(Phrase Phrase) {
		super.getMapper(PhraseMapper.class).update(Phrase);
	}

	@Override
	public void delete(long id) {
		super.getMapper(PhraseMapper.class).delete(id);
	}

}

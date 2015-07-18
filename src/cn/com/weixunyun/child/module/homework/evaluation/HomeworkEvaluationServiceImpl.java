package cn.com.weixunyun.child.module.homework.evaluation;

import java.util.List;

import cn.com.weixunyun.child.model.service.AbstractService;

public class HomeworkEvaluationServiceImpl extends AbstractService implements
		HomeworkEvaluationService {

	@Override
	public HomeworkEvaluation get(Long id) {
		return super.getMapper(HomeworkEvaluationMapper.class).get(id);
	}

	@Override
	public void insert(HomeworkEvaluation evaluation) {
		super.getMapper(HomeworkEvaluationMapper.class).insert(evaluation);
	}

	@Override
	public void update(HomeworkEvaluation evaluation) {
		super.getMapper(HomeworkEvaluationMapper.class).update(evaluation);
	}

	@Override
	public void delete(Long id) {
		super.getMapper(HomeworkEvaluationMapper.class).delete(id);
	}

	@Override
	public int getListCount(Long schoolId, String term, Long classesId,
			Long studentId, String date, String keyword) {
		return super.getMapper(HomeworkEvaluationMapper.class).getListCount(
				schoolId, term, classesId, studentId, date, keyword);
	}

	@Override
	public List<HomeworkEvaluation> getList(Long offset, Long rows,
			Long schoolId, String term, Long classesId, Long studentId,
			String date, String keyword) {
		return super.getMapper(HomeworkEvaluationMapper.class).getList(offset,
				rows, schoolId, term, classesId, studentId, date, keyword);
	}

}

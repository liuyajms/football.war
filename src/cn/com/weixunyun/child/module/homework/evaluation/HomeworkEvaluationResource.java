package cn.com.weixunyun.child.module.homework.evaluation;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;

@Path("/homeworkEvaluation")
@Produces(MediaType.APPLICATION_JSON)
@Description("作业评价")
public class HomeworkEvaluationResource extends AbstractResource {
	
	@GET
	@Description("列表")
	public List<HomeworkEvaluation> getList(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("keyword") String keyword, @QueryParam("classesId") Long classesId,
			@QueryParam("studentId") Long studentId, @QueryParam("date") String date) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkEvaluationService service = super.getService(HomeworkEvaluationService.class);
		return service.getList(page * rows, rows, schoolId, term, classesId, studentId, date, keyword);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
			@QueryParam("homeworkId") Long homeworkId, @QueryParam("classesId") Long classesId,
			@QueryParam("studentId") Long studentId, @QueryParam("date") String date) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkEvaluationService service = super.getService(HomeworkEvaluationService.class);
		return service.getListCount(schoolId, term, classesId, studentId, date, keyword);
	}
	
	@GET
	@Path("{id}")
	@Description("详情")
	public HomeworkEvaluation get(@PathParam("id") Long id) {
		return super.getService(HomeworkEvaluationService.class).get(id);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> form)
			throws Exception {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long userId = super.getAuthedId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		
		System.out.println("--------------form-----------");
		System.out.println(form);
		System.out.println();
		
		HomeworkEvaluation evaluation = super.buildBean(HomeworkEvaluation.class, form, null);
		if (form.getFirst("date") != null && !"".equals(form.getFirst("date").toString())) {
			try {
				java.util.Date date = new SimpleDateFormat("yyyyMMdd").parse(form.getFirst("date").toString());
				evaluation.setDate(new Date(date.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		evaluation.setSchoolId(schoolId);
		evaluation.setTerm(term);
		evaluation.setCreateUserId(userId);
		evaluation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		evaluation.setUpdateUserId(userId);
		evaluation.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		HomeworkEvaluationService service = super.getService(HomeworkEvaluationService.class);
		service.insert(evaluation);
	}
	
	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(HomeworkEvaluationService.class).delete(id);
	}
}

package cn.com.weixunyun.child.module.homework.check;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;

@Path("/homeworkCheck")
@Produces(MediaType.APPLICATION_JSON)
@Description("作业检查")
public class HomeworkCheckResource extends AbstractResource {
	
	@GET
	@Path("teacher/export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") final Long classesId,
			@QueryParam("homeworkId") final Long homeworkId,
			@QueryParam("date") final String date) {
		final Long schoolId = super.getAuthedSchool(rsessionid).getId();
		final String term = super.getCurrentTerm(schoolId);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {

				HomeworkCheckService service = getService(HomeworkCheckService.class);
				List<StudentHomeworkCheck> list = service.getStudentCheckList(0L, 1000L, schoolId, term, classesId, homeworkId, date);

				List<Column<StudentHomeworkCheck>> columnList = new ArrayList<ExcelUtils.Column<StudentHomeworkCheck>>();
				columnList.add(new Column<StudentHomeworkCheck>() {
					@Override
					public String getTitle() {
						return "学生";
					}

					@Override
					public String getValue(StudentHomeworkCheck t) {
						return t.getStudentName();
					}
				});
				columnList.add(new Column<StudentHomeworkCheck>() {
					@Override
					public String getTitle() {
						return "检查";
					}

					@Override
					public String getValue(StudentHomeworkCheck t) {
						return t.getChecked()==null?"": t.getChecked()==true?"是":"";
					}

				});

				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=homework_check.xlsx").build();
	}
	
	@GET
	@Path("classes/export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出班级学生作业检查情况")
	public Response exportClassesChecks(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") final Long classesId,
			@QueryParam("dateBegin") final String dateBegin,
			@QueryParam("dateEnd") final String dateEnd) {
		final Long schoolId = super.getAuthedSchool(rsessionid).getId();
		final String term = super.getCurrentTerm(schoolId);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {

				HomeworkCheckService service = getService(HomeworkCheckService.class);
				List<StudentHomeworkCheck> list = service.getClassesCheckList(0L, 1000L, schoolId, term, classesId, dateBegin, dateEnd);


				List<Column<StudentHomeworkCheck>> columnList = new ArrayList<ExcelUtils.Column<StudentHomeworkCheck>>();
				columnList.add(new Column<StudentHomeworkCheck>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(StudentHomeworkCheck t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<StudentHomeworkCheck>() {
					@Override
					public String getTitle() {
						return "学生";
					}

					@Override
					public String getValue(StudentHomeworkCheck t) {
						return t.getStudentName();
					}
				});
				columnList.add(new Column<StudentHomeworkCheck>() {
					@Override
					public String getTitle() {
						return "检查";
					}

					@Override
					public String getValue(StudentHomeworkCheck t) {
						return t.getChecked()==null?"": t.getChecked()==true?"是":"";
					}

				});
				columnList.add(new Column<StudentHomeworkCheck>() {
					@Override
					public String getTitle() {
						return "评价";
					}

					@Override
					public String getValue(StudentHomeworkCheck t) {
						return t.getDescription();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=homework_check_classes.xlsx").build();
	}
	
	@GET
	@Description("列表")
	public List<HomeworkCheck> getList(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("keyword") String keyword, @QueryParam("homeworkId") Long homeworkId) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		return service.getList(page * rows, rows, keyword, schoolId, term, homeworkId);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
			@QueryParam("homeworkId") Long homeworkId) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		return service.getListCount(schoolId, keyword, term, homeworkId);
	}
	
	@GET
	@Path("teacher")
	@Description("列表")
	public List<StudentHomeworkCheck> getStudentCheckList(@CookieParam("rsessionid") String rsessionid, 
			@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("homeworkId") Long homeworkId, @QueryParam("classesId") Long classesId,
			@QueryParam("time") Long time) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		return service.getStudentCheckList(page * rows, rows, schoolId, term, classesId, homeworkId, new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
	}
	
	@GET
	@Path("teacher/count")
	@Description("列表")
	public int getStudentCheckListCount(@CookieParam("rsessionid") String rsessionid, 
			@QueryParam("homeworkId") Long homeworkId, @QueryParam("classesId") Long classesId,
			@QueryParam("time") Long time) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		return service.getStudentCheckListCount(schoolId, term, classesId, homeworkId, new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
	}
	
	@GET
	@Path("classes")
	@Description("列表")
	public List<StudentHomeworkCheck> getClassesCheckList(@CookieParam("rsessionid") String rsessionid, 
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("classesId") Long classesId,
			@QueryParam("dateBegin") String dateBegin, @QueryParam("dateEnd") String dateEnd) {
		if (dateBegin.contains("-")) {
			dateBegin = dateBegin.replace("-", "");
		}
		if (dateEnd.contains("-")) {
			dateEnd = dateEnd.replace("-", "");
		}
		
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		
		return service.getClassesCheckList(page * rows, rows, schoolId, term, classesId, dateBegin, dateEnd);
	}
	
	@GET
	@Path("classes/count")
	@Description("列表")
	public int getClassesCheckListCount(@CookieParam("rsessionid") String rsessionid, 
			@QueryParam("courseId") Long courseId, @QueryParam("classesId") Long classesId,
			@QueryParam("dateBegin") String dateBegin, @QueryParam("dateEnd") String dateEnd) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		return service.getClassesCheckListCount(schoolId, term, classesId, dateBegin, dateEnd);
	}
	
	@GET
	@Path("{id}")
	@Description("详情")
	public HomeworkCheck get(@PathParam("id") Long id) {
		return super.getService(HomeworkCheckService.class).get(id);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("家长检查作业及评价-提交")
	public void insert(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> form)
			throws Exception {
		System.out.println("###########formData:"+form);
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long userId = super.getAuthedId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		HomeworkCheckService service = super.getService(HomeworkCheckService.class);
		service.insertChecks(form, schoolId, term, userId);
	}
	
	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(HomeworkCheckService.class).delete(id);
	}
}

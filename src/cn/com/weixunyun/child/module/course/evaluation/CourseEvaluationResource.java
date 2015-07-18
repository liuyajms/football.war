package cn.com.weixunyun.child.module.course.evaluation;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/evaluation")
@Produces(MediaType.APPLICATION_JSON)
@Description("课堂评价")
public class CourseEvaluationResource extends AbstractResource {
	
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") final Long classesId,
			@QueryParam("courseId") final Long courseId) {
		final School school = super.getAuthedSchool(rsessionid);
		final String term = super.getCurrentTerm(school.getId());
		ClassesService cService = getService(ClassesService.class);
		Classes classes = cService.select(classesId);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				CourseEvaluationService service = getService(CourseEvaluationService.class);
				List<ClassesCourseEvaluation> list = service.selectAll(0L, 10000L, school.getId(), classesId, courseId, term);

				List<Column<ClassesCourseEvaluation>> columnList = new ArrayList<ExcelUtils.Column<ClassesCourseEvaluation>>();
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "课程";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getCourseName();
					}
				});
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "学生";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getStudentName();
					}
				});
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "今日表扬数";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getPraise() + "";
					}
				});
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "答题正确率";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getAccuracy() + "";
					}
				});
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "评价内容";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getDescription();
					}
				});
				/*columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "录入人";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						return t.getCreateTeacherName();
					}
				});
				columnList.add(new Column<ClassesCourseEvaluation>() {
					@Override
					public String getTitle() {
						return "录入时间";
					}

					@Override
					public String getValue(ClassesCourseEvaluation t) {
						if (t.getCreateTime() != null) {
							return new SimpleDateFormat("yyyy-MM-dd hh:mm").format(t.getCreateTime());
						} else {
							return "";
						}
					}
				});*/
				
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=" + classes.getYear() + "_" + classes.getNum() + "_evaluation.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesCourseEvaluation> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("courseId") Long courseId) {
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectAll(page * rows, rows, school.getId(), classesId, courseId, term);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId) {
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectAllCount(school.getId(), classesId, courseId, term);
	}
	
	@GET
	@Path("student")
	@Description("某个学生当天的课程评价")
	public List<ClassesCourseEvaluation> selectOneStudent(@CookieParam("rsessionid") String rsessionid, @QueryParam("studentId") Long studentId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("courseId") Long courseId) {
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectTodayStudent(page * rows, rows, school.getId(), studentId, courseId, term);
	}

	@GET
	@Path("student/count")
	@Description("某个学生当天的课程评价-总数")
	public int selectOneStudentCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("studentId") Long studentId,
			@QueryParam("courseId") Long courseId) {
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectTodayStudentCount(school.getId(), studentId, courseId, term);
	}
	
	@GET
	@Path("teacher")
	@Description("某个班全部学生当天的课程评价")
	public List<ClassesCourseEvaluation> selectAllStudent(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("courseId") Long courseId) {
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectTodayTeacher(page * rows, rows, school.getId(), classesId, courseId, term);
	}

	@GET
	@Path("teacher/count")
	@Description("某个班全部学生当天的课程评价-总数")
	public int selectAllStudentCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId) {
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectTodayTeacherCount(school.getId(), classesId, courseId, term);
	}
	
	@GET
    @Path("rule")
    @Description("course_evaluation表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "course_evaluation");
    }
	
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		System.out.println("---------------insert-------------");
		System.out.println(formData);

		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		CourseEvaluationService service = super.getService(CourseEvaluationService.class);

		CourseEvaluation evaluation = super.buildBean(CourseEvaluation.class, formData, null);
		evaluation.setSchoolId(school.getId());
		evaluation.setTerm(term);
		evaluation.setCreateTeacherId(super.getAuthedId(rsessionid));
		evaluation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		service.insert(evaluation);
	}
	
	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertCourseEvaluations(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);
			
			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long classesId = Long.parseLong(map.get("classesId").getValue());
			Long courseId = Long.parseLong(map.get("courseId").getValue());
			String term = super.getCurrentTerm(school.getId());
			Long userId = super.getAuthedId(rsessionid);
			
			System.out.println(map);
			System.out.println();
			
			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("courseEvaluation", file, "1", school.getId());
			System.out.println(list);
			
			CourseEvaluationService service = super.getService(CourseEvaluationService.class);
			int n = service.insertCourseEvaluations(school.getId(), classesId, courseId, term, userId, del, list);
			return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") Long id, 
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		System.out.println("---------------update------------");
		System.out.println(formData);
		System.out.println();
		CourseEvaluation evaluation = super.buildBean(CourseEvaluation.class, formData, id);
		evaluation.setUpdateTeacherId(super.getAuthedId(rsessionid));
		evaluation.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		super.getService(CourseEvaluationService.class).update(evaluation);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(CourseEvaluationService.class).delete(id);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public CourseEvaluation select(@PathParam("id") Long id) {
		return super.getService(CourseEvaluationService.class).select(id);
	}
}

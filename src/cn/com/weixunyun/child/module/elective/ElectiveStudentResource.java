package cn.com.weixunyun.child.module.elective;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
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
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.server.utils.LinkBuilders;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/elective/student")
@Produces(MediaType.APPLICATION_JSON)
@Description("学生选修记录")
public class ElectiveStudentResource extends AbstractResource {

	@GET
	@Path("choosed")
	@Description("某个学生的已选课程列表")
	public List<StudentElectiveStudent> selectStudents(@QueryParam("studentId") Long studentId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @CookieParam("rsessionid") String rsessionid) {
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		ElectiveStudentService service = super.getService(ElectiveStudentService.class);
		return service.selectStudent(page * rows, rows, studentId, term);
	}

	@GET
	@Description("已选某个课程的学生列表")
	public List<CourseElectiveStudent> selectCourse(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword, 
			@QueryParam("electiveId") Long electiveId, @QueryParam("page") Long page, @QueryParam("rows") Long rows) {
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		ElectiveStudentService service = super.getService(ElectiveStudentService.class);
		return service.selectElective(page * rows, rows, school.getId(), electiveId, term, keyword);
	}

	@GET
	@Path("/count")
	@Description("总数")
	public int selectAllCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("electiveId") Long electiveId, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		ElectiveStudentService service = super.getService(ElectiveStudentService.class);
		return service.selectElectiveCount(school.getId(), electiveId, term, keyword);
	}
	
	@GET
	@Path("{id}")
	@Description("详情")
	public CourseElectiveStudent select(@PathParam("id") Long id) {
		return super.getService(ElectiveStudentService.class).select(id);
	}
	
	@GET
    @Path("rule")
    @Description("electiveStudent表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "electiveStudent");
    }

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("选课")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid,
			@Context UriInfo uriInfo, @Context LinkBuilders linksBuilders) throws Exception {

		School school = super.getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		ElectiveStudentService service = super.getService(ElectiveStudentService.class);

		// id, term, schoolId, studentId, electiveId, time->term, studentId,
		// courseId
		// 添加的时候可能是几条数据，对应几个elective

		service.inserElectiveStudent(formData, school, term);

		/*
		 * List<String> electiveListStr = formData.get("electiveId");
		 * System.out.println(electiveListStr); String term =
		 * formData.getFirst("term"); Long studentId =
		 * Long.parseLong(formData.getFirst("studentId"));
		 * service.deleteStudent(studentId);
		 * 
		 * Long electiveId;
		 * 
		 * for (String electiveIdStr : electiveListStr) { //查询是否满员！！ electiveId
		 * = Long.parseLong(electiveIdStr);
		 * 
		 * if
		 * (super.getService(ElectiveStudentService.class).selectElectiveCount
		 * (electiveId) > 0) { ElectiveStudent elective = new ElectiveStudent();
		 * elective.setId(getService(SequenceService.class).sequence());
		 * elective.setTerm(term); elective.setElectiveId(electiveId);
		 * elective.setStudentId(studentId);
		 * elective.setSchoolId(school.getId()); elective.setTime(new
		 * Timestamp(System.currentTimeMillis()));
		 * super.getService(ElectiveStudentService.class).insert(elective); } }
		 */

	}
	
	@POST
	@Path("imported")
	@Description("导入")
	public DMLResponse insertMulti(@CookieParam("rsessionid") String rsessionid, @Context HttpServletRequest request)
			throws Exception {
		try {
			School school = super.getAuthedSchool(rsessionid);
			
			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			Long userId = super.getAuthedId(rsessionid);
			String term = super.getCurrentTerm(school.getId());
			Long electiveId = Long.parseLong(map.get("electiveId").getValue());
			
			System.out.println(map);
			System.out.println();
			
			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("electiveStudent", file, "1", school.getId());
			
			ElectiveStudentService service = super.getService(ElectiveStudentService.class);
			int n = service.updateElectiveStudents(school.getId(), term, electiveId, userId, list);
		return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@PUT
	@Path("update")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void updateElective(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		// 若是修改，则先删除关于该学生的选课记录，再添加一次
		Long studentId = Long.parseLong(formData.getFirst("studentId"));
		List<String> electiveListStr = formData.get("electiveId");
		String term = formData.getFirst("term");

		School school = super.getAuthedSchool(rsessionid);

		for (String electiveIdStr : electiveListStr) {
			ElectiveStudent elective = new ElectiveStudent();
			elective.setId(getService(SequenceService.class).sequence());
			elective.setTerm(term);
			elective.setElectiveId(Long.parseLong(electiveIdStr));
			elective.setStudentId(studentId);
			elective.setSchoolId(school.getId());
			elective.setTime(new Timestamp(System.currentTimeMillis()));
			// service.insert(elective);
		}

		// Elective elective = super.buildBean(Elective.class, formData, id);
		// elective.setScoreDescription(URLDecoder.decode(formData.getFirst("remark"),
		// "UTF-8"));
		// super.getService(ElectiveService.class).update(elective);

	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws Exception {
		ElectiveStudent elective = new ElectiveStudent();
		elective.setId(id);
		elective.setScore(URLDecoder.decode(formData.getFirst("score"), "UTF-8"));
		elective.setScoreDescription(URLDecoder.decode(formData.getFirst("scoreDescription"), "UTF-8"));
		elective.setScoreTime(new Timestamp(System.currentTimeMillis()));

		super.getService(ElectiveStudentService.class).updateScore(elective);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(ElectiveStudentService.class).delete(id);
	}

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("electiveId") Long electiveId) {
		final School school = super.getAuthedSchool(rsessionid);
		final String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				ElectiveStudentService service = getService(ElectiveStudentService.class);

				List<CourseElectiveStudent> list = service.selectElective(0L, 200L, school.getId(), electiveId, term, "");

				List<Column<CourseElectiveStudent>> columnList = new ArrayList<ExcelUtils.Column<CourseElectiveStudent>>();
				/*columnList.add(new Column<CourseElectiveStudent>() {
					@Override
					public String getTitle() {
						return "学期";
					}

					@Override
					public String getValue(CourseElectiveStudent t) {
						return t.getTerm().substring(0, 4)  + "学年" + ("1".equals(t.getTerm().substring(4, 5))==true?"下学期":"上学期");
					}
				});*/
				columnList.add(new Column<CourseElectiveStudent>() {
					@Override
					public String getTitle() {
						return "课程名称";
					}

					@Override
					public String getValue(CourseElectiveStudent t) {
						return t.getElectiveName();
					}
				});
				columnList.add(new Column<CourseElectiveStudent>() {
					@Override
					public String getTitle() {
						return "学生姓名";
					}

					@Override
					public String getValue(CourseElectiveStudent t) {
						return t.getStudentName();
					}
				});
				columnList.add(new Column<CourseElectiveStudent>() {
					@Override
					public String getTitle() {
						return "成绩";
					}

					@Override
					public String getValue(CourseElectiveStudent t) {
						return t.getScore();
					}
				});
				columnList.add(new Column<CourseElectiveStudent>() {
					@Override
					public String getTitle() {
						return "成绩评价";
					}

					@Override
					public String getValue(CourseElectiveStudent t) {
						return t.getScoreDescription();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=elective_student_" + term + ".xlsx").build();
	}
}

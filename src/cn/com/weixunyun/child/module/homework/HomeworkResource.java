package cn.com.weixunyun.child.module.homework;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/homework")
@Produces(MediaType.APPLICATION_JSON)
@Description("家庭作业")
public class HomeworkResource extends AbstractResource {

	/**
	 * 教师登陆
	 * @param rsessionid
	 * @param classesId
	 * @param keyword
	 * @param queryDate 查询时间参数，默认为空，查询所有数据； 查询当天数据："day"； 查询昨日数据"lastday"
	 * @return
	 */
	@GET
	@Path("t/count")
	@Description("教师登陆，总数")
	public int getCount(
			@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId,
			@QueryParam("keyword") String keyword,
			@QueryParam("queryDate") String queryDate) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long teacherId = super.getAuthedId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		
		return service.getCount(schoolId, teacherId, classesId, courseId, term,
				keyword, queryDate);
	}

	@GET
	@Path("t")
	@Description("教师登陆，查询分页数据")
	public List<Homework> getList(
			@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId,
			@QueryParam("keyword") String keyword,
			@QueryParam("queryDate") String queryDate) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long teacherId = super.getAuthedTeacher(rsessionid).getId();
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		
		return service.getList(page * rows, rows, schoolId, classesId, courseId,
				teacherId, term, keyword, queryDate);
	}

	/*@GET
	@Description("其他登陆，查询分页数据")
	@Path("/other")
	public List<Homework> getOtherList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId,
			@QueryParam("keyword") String keyword,
			@QueryParam("queryDate") String queryDate) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		return service.getOtherList(page * rows, rows, schoolId, classesId, courseId, term,
				keyword, queryDate);
	}

	@GET
	@Path("/other/count")
	@Description("其他登陆，总数")
	public int getOtherCount(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId,
			@QueryParam("keyword") String keyword,
			@QueryParam("queryDate") String queryDate) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		return service.getOtherCount(schoolId, classesId, courseId, term, keyword, queryDate);
	}*/
	
	@GET
	@Description("查询分页数据：家长登陆，需传studentId; 教师登陆：不传studentId。")
	public List<StudentHomework> getStudentHomeworkList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("classesId") Long classesId, @QueryParam("studentId") Long studentId, 
			@QueryParam("time") Long time) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		return service.getStudentHomeworkList(page*rows, rows, schoolId, classesId, studentId, term, time == null ? null : new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
	}
	
	@GET
	@Path("teacher")
	@Description("教师，布置作业列表页")
	public List<StudentHomework> getHomeworkList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("classesId") Long classesId, @QueryParam("time") Long time) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Long teacherId = super.getAuthedId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		return service.getHomeworkList(page * rows, rows, schoolId, teacherId, term, time == null ? null : new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
	}
	
	@GET
	@Path("count")
	@Description("家长登陆，总数")
	public int getStudentHomeworkListCount(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") Long classesId, @QueryParam("studentId") Long studentId,
			@QueryParam("time") Long time) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);
		HomeworkService service = super.getService(HomeworkService.class);
		return service.getStudentHomeworkListCount(schoolId, classesId, studentId, term, time == null ? null : new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("教师添加作业")
	public void insert(MultivaluedMap<String, String> formData,
			@CookieParam("rsessionid") String rsessionid) throws IOException {
		String des = URLDecoder.decode(formData.getFirst("description"), "UTF-8");
		/*
		 * 去掉最后一个换行符
		 */
		String[] str = des.split("\n");
		String descip = "";
		for (int i = 0; i < str.length - 1; i++) {
			descip += str[i] + "<br/>";
		}
		descip += str[str.length - 1];

		Teacher teacher = super.getAuthedTeacher(rsessionid);

		Homework homework = super.buildBean(Homework.class, formData, null);
		homework.setDescription(descip);// 家庭作业内容
		homework.setClassesId(Long.valueOf(formData.getFirst("classesId")));
		homework.setSchoolId(teacher.getSchoolId());
		homework.setCourseId(Long.valueOf(formData.getFirst("courseId")));
		homework.setCreateTeacherId(teacher.getId());
		homework.setUpdateTeacherId(teacher.getId());
		homework.setTerm(super.getCurrentTerm(super.getAuthedSchoolId(rsessionid)));
		homework.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
		homework.setUpdateTime((new java.sql.Timestamp(System.currentTimeMillis())));
		
		HomeworkService service = super.getService(HomeworkService.class);
		service.insert(homework);
	}
	
	@POST
	@Path("multi")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("教师对所教课程布置作业")
	public void insertHomeworks(MultivaluedMap<String, String> formData,
			@CookieParam("rsessionid") String rsessionid) throws IOException {
		HomeworkService service = super.getService(HomeworkService.class);
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		
		service.insertHomeworks(formData, schoolId, super.getCurrentTerm(schoolId), super.getAuthedId(rsessionid));
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("教师修改作业")
	public void update(MultivaluedMap<String, String> formData,
			@CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws IOException {

		Homework homework = super.buildBean(Homework.class, formData, id);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		// homework.setClassesId(Long.valueOf(formData.getFirst("classesIdName")));
		// 班级ID

		String des = URLDecoder.decode(formData.getFirst("description"),
				"UTF-8");
		String[] str = des.split("\n");
		String descip = "";
		for (int i = 0; i < str.length - 1; i++) {
			descip += str[i] + "<br/>";
		}
		descip += str[str.length - 1];
		homework.setDescription(descip);
		homework.setUpdateTeacherId(teacher.getId());
		homework.setUpdateTime((new java.sql.Timestamp(System.currentTimeMillis())));

		super.getService(HomeworkService.class).update(homework);
	}

	@GET
	@Path("{id}")
	@Description("教师查看某一作业")
	public Homework select(@PathParam("id") Long id) {
		return super.getService(HomeworkService.class).select(id);
	}

	@DELETE
	@Path("{id}")
	@Description("教师删除某一作业")
	public void delete(@PathParam("id") Long id) {
		super.getService(HomeworkService.class).delete(id);
	}

	/**
	 * 导出excel数据
	 * 
	 * @param rsessionid
	 * @param isTeacher
	 *            是否导出为教师数据，true为是；false为否
	 * @param classesId
	 * @param keyword
	 * @return
	 */
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("isTeacher") final boolean isTeacher,
			@QueryParam("classesId") final Long classesId,
			@QueryParam("courseId") final Long courseId,
			@QueryParam("keyword") final String keyword,
			@QueryParam("queryDate") final String queryDate) {
		final Long schoolId = super.getAuthedSchool(rsessionid).getId();

		final Long teacherId = super.getAuthedTeacher(rsessionid).getId();

		final String term = super.getCurrentTerm(schoolId);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {

				HomeworkService service = getService(HomeworkService.class);

				List<Homework> list;
				if (isTeacher) {
					list = service.getList(0L, 9999L, schoolId,
							classesId, courseId, teacherId, term, keyword, queryDate);
				} else {
					list = service.getOtherList(0L, 9999L, schoolId, classesId, courseId, term,
							keyword, queryDate);
				}

				List<Column<Homework>> columnList = new ArrayList<ExcelUtils.Column<Homework>>();
				columnList.add(new Column<Homework>() {
					@Override
					public String getTitle() {
						return "课程名称";
					}

					@Override
					public String getValue(Homework t) {
						return t.getCourseName();
					}
				});
				columnList.add(new Column<Homework>() {
					@Override
					public String getTitle() {
						return "作业内容";
					}

					@Override
					public String getValue(Homework t) {
						return t.getDescription();
					}

				});
				columnList.add(new Column<Homework>() {
					@Override
					public String getTitle() {
						return "修改时间";
					}

					@Override
					public String getValue(Homework t) {
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
						// Timestamp now = new
						// Timestamp(System.currentTimeMillis());//获取系统当前时间
						String str = df.format(t.getUpdateTime());
						return str;
					}

				});

				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response
				.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition",
						"attachment; filename=homework.xlsx").build();
	}
}

package cn.com.weixunyun.child.module.elective;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
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

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.StudentService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/elective")
@Produces(MediaType.APPLICATION_JSON)
@Description("课程")
public class ElectiveResource extends AbstractResource {

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid) {
		final School school = super.getAuthedSchool(rsessionid);
		final String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				ElectiveService service = getService(ElectiveService.class);

				List<TeacherElective> list = service.selectAll(school.getId(), term, 0L, 200L);

				List<Column<TeacherElective>> columnList = new ArrayList<ExcelUtils.Column<TeacherElective>>();
				/*columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "学期";
					}

					@Override
					public String getValue(TeacherElective t) {
						return t.getTerm().substring(0, 4) + "学年"
								+ ("1".equals(t.getTerm().substring(4, 5)) == true ? "下学期" : "上学期");
					}
				});*/
				columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "课程名称";
					}

					@Override
					public String getValue(TeacherElective t) {
						return t.getName();
					}
				});
				columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "教师姓名";
					}

					@Override
					public String getValue(TeacherElective t) {
						return t.getTeacherName();
					}
				});
				columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "上课时间";
					}

					@Override
					public String getValue(TeacherElective t) {
						return t.getDate();
					}
				});
				columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "允许选课人数";
					}

					@Override
					public String getValue(TeacherElective t) {
						return t.getNum().toString();
					}
				});
				columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "允许选课年级";
					}

					@Override
					public String getValue(TeacherElective t) {
						StringBuilder sb = new StringBuilder();
						if (t.getGrade() != 0) {
							int grade = t.getGrade();
							for (int i = 0; i < 8; i++) {
								if ((grade & (1 << i)) > 0) {
									if (sb.length() > 0) {
										sb.append("、");
									}
									sb.append(i + 1);
								}
							}
							return sb.toString() + "年级";
						} else {
							return "";
						}
					}
				});
				columnList.add(new Column<TeacherElective>() {
					@Override
					public String getTitle() {
						return "已选人数";
					}

					@Override
					public String getValue(TeacherElective t) {
						return t.getChoosedCount().toString();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=elective.xlsx").build();
	}

	@GET
	@Description("选修课列表")
	public List<TeacherElective> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("studentId") Long studentId) {
		School school = super.getAuthedSchool(rsessionid);

		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(school.getId(), "term", "default").getValue();

		StudentService studentService = super.getService(StudentService.class);

		ClassesStudent student = studentService.select(studentId);

		int grade = super.getGrade(school.getId(), student.getClassesYear());

		ElectiveService service = super.getService(ElectiveService.class);
		return service.getStudentList(school.getId(), studentId, grade, term, page * rows, rows);
	}

	@GET
	@Path("/list")
	@Description("选修课列表")
	public List<TeacherElective> selectList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows) {
		School school = super.getAuthedSchool(rsessionid);

		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(school.getId(), "term", "default").getValue();

		ElectiveService service = super.getService(ElectiveService.class);
		return service.selectAll(school.getId(), term, page * rows, rows);
	}

	@GET
	@Path("/list/count")
	@Description("总数")
	public int selectCount(@CookieParam("rsessionid") String rsessionid) {
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		ElectiveService service = super.getService(ElectiveService.class);
		return service.selectAllCount(school.getId(), term);
	}
	
	@GET
    @Path("rule")
    @Description("elective表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "elective");
    }
	
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		School school = super.getAuthedSchool(rsessionid);

		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(school.getId(), "term", "default").getValue();

		ElectiveService service = super.getService(ElectiveService.class);

		Elective elective = super.buildBean(Elective.class, formData, null);
		elective.setSchoolId(school.getId());
		elective.setTerm(term);
		elective.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));
		elective.setCreateTeacherId(super.getAuthedId(rsessionid));
		elective.setCreateTime(new Timestamp(System.currentTimeMillis()));
		service.insert(elective);
	}
	
	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertElectives(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);
			
			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long userId = super.getAuthedId(rsessionid);
			String term = super.getCurrentTerm(school.getId());
			
			System.out.println(map);
			System.out.println();
			
			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("elective", file, "1", school.getId());
			System.out.println(list);
			
			ElectiveService service = super.getService(ElectiveService.class);
			int n = service.insertElectives(school.getId(), term, userId, del, list);
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
		Elective elective = super.buildBean(Elective.class, formData, id);
		elective.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));
		elective.setUpdateTeacherId(super.getAuthedId(rsessionid));
		elective.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		super.getService(ElectiveService.class).update(elective);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(ElectiveService.class).delete(id);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Elective select(@PathParam("id") Long id) {
		return super.getService(ElectiveService.class).select(id);
	}
}

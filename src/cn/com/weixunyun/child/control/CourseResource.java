package cn.com.weixunyun.child.control;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
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
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Course;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.CourseService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Description("课程")
public class CourseResource extends AbstractResource {
	
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				CourseService service = getService(CourseService.class);
				List<Course> list = service.selectAll(school.getId(), 0L, 1000L, "");

				List<Column<Course>> columnList = new ArrayList<ExcelUtils.Column<Course>>();
				columnList.add(new Column<Course>() {
					@Override
					public String getTitle() {
						return "课程名称";
					}

					@Override
					public String getValue(Course t) {
						return t.getName();
					}
				});
				/*columnList.add(new Column<Course>() {
					@Override
					public String getTitle() {
						return "课程介绍";
					}

					@Override
					public String getValue(Course t) {
						return t.getDescription();
					}
				});*/
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=course.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<Course> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows) {
		CourseService service = super.getService(CourseService.class);
		School school = super.getAuthedSchool(rsessionid);
        return service.selectAll(school.getId(), page *rows, rows, keyword);
	}
	
	@GET
    @Path("rule")
    @Description("course表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "course");
    }

	@GET
	@Path("count")
	@Description("总数")
	public int selectCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		CourseService service = super.getService(CourseService.class);
		return service.selectAllCount(super.getAuthedSchool(rsessionid).getId(), keyword);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		School school = super.getAuthedSchool(rsessionid);
		CourseService service = super.getService(CourseService.class);

		Course course = super.buildBean(Course.class, formData, null);
		course.setSchoolId(school.getId());
		course.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));
		service.insert(course);
	}
	
	@POST
	@Path("imported")
	@Description("添加")
	public DMLResponse insertClasses(@CookieParam("rsessionid") String rsessionid, @Context HttpServletRequest request)
			throws Exception {
		try {
			School school = super.getAuthedSchool(rsessionid);
			
			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			
			System.out.println(map);
			System.out.println();
			
			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("course", file, "1", school.getId());
			
			CourseService service = super.getService(CourseService.class);
			int n = service.insertCourses(school.getId(), del, list);
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
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") Long id) throws Exception {
		System.out.println("---------------update------------");
		System.out.println(formData);
		System.out.println();
		Course course = super.buildBean(Course.class, formData, id);
		course.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));
		super.getService(CourseService.class).update(course);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(CourseService.class).delete(id);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Course select(@PathParam("id") Long id) {
		return super.getService(CourseService.class).select(id);
	}
}

package cn.com.weixunyun.child.module.course.knowledge;

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
@Path("/knowledge")
@Produces(MediaType.APPLICATION_JSON)
@Description("知识树")
public class CourseKnowledgeResource extends AbstractResource {
	
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
				CourseKnowledgeService service = getService(CourseKnowledgeService.class);
				List<ClassesCourseKnowledge> list = service.selectExportList(school.getId(), classesId, courseId, term);

				List<Column<ClassesCourseKnowledge>> columnList = new ArrayList<ExcelUtils.Column<ClassesCourseKnowledge>>();
				columnList.add(new Column<ClassesCourseKnowledge>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesCourseKnowledge t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<ClassesCourseKnowledge>() {
					@Override
					public String getTitle() {
						return "课程";
					}

					@Override
					public String getValue(ClassesCourseKnowledge t) {
						return t.getCourseName();
					}
				});
				columnList.add(new Column<ClassesCourseKnowledge>() {
					@Override
					public String getTitle() {
						return "知识点";
					}

					@Override
					public String getValue(ClassesCourseKnowledge t) {
						return t.getName();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=" + classes.getYear() + "_" + classes.getNum() + "_knowledge.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesCourseKnowledge> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("courseId") Long courseId) {
		CourseKnowledgeService service = super.getService(CourseKnowledgeService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectAll(page * rows, rows, school.getId(), classesId, courseId, term);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("courseId") Long courseId) {
		CourseKnowledgeService service = super.getService(CourseKnowledgeService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		return service.selectAllCount(school.getId(), classesId, courseId, term);
	}
	
	@GET
    @Path("rule")
    @Description("course_knowledge表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "course_knowledge");
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
		CourseKnowledgeService service = super.getService(CourseKnowledgeService.class);

		CourseKnowledge knowledge = super.buildBean(CourseKnowledge.class, formData, null);
		knowledge.setSchoolId(school.getId());
		knowledge.setTerm(term);
		knowledge.setCreateTeacherId(super.getAuthedId(rsessionid));
		knowledge.setCreateTime(new Timestamp(System.currentTimeMillis()));
		service.insert(knowledge);
	}
	
	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertCourseKnowledge(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
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
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("courseKnowledge", file, "1", school.getId());
			System.out.println(list);
			
			CourseKnowledgeService service = super.getService(CourseKnowledgeService.class);
			int n = service.insertCourseKnowledges(school.getId(), classesId, courseId, term, userId, del, list);
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
		CourseKnowledge knowledge = super.buildBean(CourseKnowledge.class, formData, id);
		knowledge.setUpdateTeacherId(super.getAuthedId(rsessionid));
		knowledge.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		super.getService(CourseKnowledgeService.class).update(knowledge);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(CourseKnowledgeService.class).delete(id);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public CourseKnowledge select(@PathParam("id") Long id) {
		return super.getService(CourseKnowledgeService.class).select(id);
	}
}

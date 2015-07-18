package cn.com.weixunyun.child.module.course.classroom;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/classroom")
@Produces(MediaType.APPLICATION_JSON)
@Description("下载")
public class CourseClassroomResource extends AbstractResource {
	
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
				CourseClassroomService service = getService(CourseClassroomService.class);
				List<ClassesCourseClassroom> list = service.selectAll(0L, 1000L, school.getId(), classesId, courseId, term);

				List<Column<ClassesCourseClassroom>> columnList = new ArrayList<ExcelUtils.Column<ClassesCourseClassroom>>();
				columnList.add(new Column<ClassesCourseClassroom>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesCourseClassroom t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<ClassesCourseClassroom>() {
					@Override
					public String getTitle() {
						return "课程";
					}

					@Override
					public String getValue(ClassesCourseClassroom t) {
						return t.getCourseName();
					}
				});
				columnList.add(new Column<ClassesCourseClassroom>() {
					@Override
					public String getTitle() {
						return "文件名称";
					}

					@Override
					public String getValue(ClassesCourseClassroom t) {
						return t.getName();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=" + classes.getYear() + "_" + classes.getNum() + "_classroom.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesCourseClassroom> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("classesId") Long classesId, @QueryParam("courseId") Long courseId) {
		School school = super.getAuthedSchool(rsessionid);
		CourseClassroomService service = super.getService(CourseClassroomService.class);
		String term = super.getCurrentTerm(school.getId());
		return service.selectAll(page * rows, rows, school.getId(), classesId, courseId, term);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAllCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId, 
			@QueryParam("courseId") Long courseId) {
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getCurrentTerm(school.getId());
		CourseClassroomService service = super.getService(CourseClassroomService.class);
		return service.selectCount(school.getId(), classesId, courseId, term);
	}
	
	@GET
    @Path("rule")
    @Description("course_classroom表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "course_classroom");
    }
	
	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws IOException {
		try {
			Map<String, PartField> map = super.partMulti(request);
			Long id = (long) super.getService(SequenceService.class).sequence();
			Teacher teacher = super.getAuthedTeacher(rsessionid);
			String term = super.getCurrentTerm(super.getAuthedSchoolId(rsessionid));
			
			CourseClassroom d = new CourseClassroom();
			
			PartFieldFile file = map.get("fileDiv").getFile();
			String fileName = map.get("fileDiv").getFile().getOriName();
			if (file != null) {
				FileUtils.copyFile(file, new File(super.getFilePath(), teacher.getSchoolId() + "/course/classroom/" + id + "/"
						+ fileName));
			}
			d.setName(fileName);
			if (!"".equals(map.get("remark").getValue())) {
				d.setDescription(map.get("remark").getValue());
			}
			d.setSize(file.getSize());
			d.setSchoolId(teacher.getSchoolId());
			d.setTerm(term);
			d.setCourseId(Long.valueOf(map.get("courseId").getValue()));
			d.setCreateTeacherId(teacher.getId());
			d.setClassesId(Long.valueOf(map.get("classesId").getValue()));
			d.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
			d.setId(id);
			d.setType(file.getContentType());
			CourseClassroomService service = super.getService(CourseClassroomService.class);
			service.insert(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("courseClassroom", file, "1", school.getId());
			System.out.println(list);
			
			CourseClassroomService service = super.getService(CourseClassroomService.class);
			int n = service.insertCourseClassrooms(school.getId(), classesId, courseId, term, userId, del, list);
			return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {
		try {
			Teacher teacher = super.getAuthedTeacher(rsessionid);
			super.getService(CourseClassroomService.class).delete(id);
			FileUtils.deleteDirectory(new File(super.getFilePath() + teacher.getSchoolId() + "/course/classroom/" + id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public CourseClassroom select(@PathParam("id") Long id) {
		return super.getService(CourseClassroomService.class).select(id);
	}

}

package cn.com.weixunyun.child.control;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.ClassesTeacher;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.model.service.ClassesTeacherService;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.TeacherService;
import cn.com.weixunyun.child.third.simiyun.SimiyunHelper;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/classesTeacher")
@Produces(MediaType.APPLICATION_JSON)
@Description("班级教师")
public class ClassesTeacherResource extends AbstractResource {
	
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") final Long classesId) {
		final Long schoolId = super.getAuthedSchoolId(rsessionid);
		
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				ClassesTeacherService service = getService(ClassesTeacherService.class);
				List<ClassesTeacher> list = service.selectAll(classesId, 0L, 100L, null, schoolId);

				List<Column<ClassesTeacher>> columnList = new ArrayList<ExcelUtils.Column<ClassesTeacher>>();
				columnList.add(new Column<ClassesTeacher>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesTeacher t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<ClassesTeacher>() {
					@Override
					public String getTitle() {
						return "课程";
					}

					@Override
					public String getValue(ClassesTeacher t) {
						return t.getCourseName();
					}
				});
				columnList.add(new Column<ClassesTeacher>() {
					@Override
					public String getTitle() {
						return "任课教师";
					}

					@Override
					public String getValue(ClassesTeacher t) {
						return t.getTeacherName();
					}
				});

				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=classes_teacher.xlsx").build();
	}
	
	@GET
	@Path("rule")
	@Description("classes_teacher表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "classesTeacher");
	}

	@GET
	@Description("列表，传classesId：某个班的任课列表，否则：查询本学校的任课列表")
	public List<ClassesTeacher> selectAll(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("keyword") String keyword) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);
		return service.selectAll(classesId, page * rows, rows, keyword, schoolId);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAllCount(@QueryParam("classesId") Long classesId, @QueryParam("keyword") String keyword,
			@CookieParam("rsessionid") String rsessionid) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);
		return service.selectAllCount(classesId, keyword, schoolId);
	}
	
	@GET
	@Path("teacher")
	@Description("列表")
	public List<ClassesTeacher> selectTeacherClasses(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);
		return service.selectTeacherClasses(classesId, page * rows, rows, super.getAuthedId(rsessionid), schoolId);
	}

	@GET
	@Path("teacher/count")
	@Description("总数")
	public int selectTeacherClassesCount(@QueryParam("classesId") Long classesId, @CookieParam("rsessionid") String rsessionid) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);
		return service.selectTeacherClassesCount(classesId, super.getAuthedId(rsessionid), schoolId);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid) throws IOException {

		ClassesTeacher classesTeacher = new ClassesTeacher();
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);
		Long teacherId = Long.valueOf(formData.getFirst("teacherId"));
		Long classesId = Long.valueOf(formData.getFirst("classesId"));

		classesTeacher.setClassesId(Long.valueOf(formData.getFirst("classesId")));
		classesTeacher.setCourseId(Long.valueOf(formData.getFirst("courseId")));
		classesTeacher.setTeacherId(teacherId);
		classesTeacher.setSchoolId(super.getAuthedSchool(rsessionid).getId());

		service.insert(classesTeacher);
		
		Classes cls = getService(ClassesService.class).select(classesId);
		Long ownId = cls.getCreateTeacherId() == null ? -1 : cls.getCreateTeacherId().longValue();
		
		Global simiyunAvailableGlobal = getService(GlobalService.class).select(super.getAuthedSchoolId(rsessionid), "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
		
		//添加的教师不是群主拥有者，则添加；否则，不添加（若添加，群主拥有者身份会被覆盖）
		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable) && ownId != teacherId.longValue()) {
			SimiyunHelper.addGroupUsers(teacherId + "", super.getAuthedId(rsessionid), Long.valueOf(formData.getFirst("classesId")), true);
			long uid = SimiyunHelper.addGroupManager(teacherId, super.getAuthedId(rsessionid), Long.valueOf(formData.getFirst("classesId")));
			getService(TeacherService.class).idDisk(teacherId, uid);
		}
	}
	
	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertTeachers(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {
			System.out.println("------------imported-----------");
			Long schoolId = super.getAuthedSchoolId(rsessionid);

			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long classesId = Long.parseLong(map.get("classesId").getValue());

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("classesTeacher", file, "1", schoolId);
			
			Global sG = getService(GlobalService.class).select(schoolId, "simiyun", "available");
			String sPG = sG == null ? "" :sG.getValue();
			String sCg = PropertiesListener.getProperty("simiyun.available", null);
			
			Boolean flag = false;
			if ("1".equals(sPG) && "1".equals(sCg)) {
				flag = true;
			}
			
			ClassesTeacherService service = super.getService(ClassesTeacherService.class);
			int n = service.insertClassesTeachers(schoolId, classesId, del, list, flag, super.getAuthedId(rsessionid));
			
			return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") String id, @CookieParam("rsessionid") String rsessionid) throws IOException {
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);

		String[] ids = id.split(",");
		Long classesId = Long.valueOf(ids[0]);
		Long teacherId = service.select(Long.valueOf(ids[0]), Long.valueOf(ids[1])).getTeacherId();
		Classes cls = getService(ClassesService.class).select(classesId);
		Long ownId = cls.getCreateTeacherId() == null ? -1 : cls.getCreateTeacherId().longValue();
		Long tId = cls.getTeacherId() == null ? -1 : cls.getTeacherId().longValue();//班主任
		
		service.delete(Long.valueOf(ids[0]), Long.valueOf(ids[1]));
		int count = service.selectTeacherCount(classesId, teacherId);
		
		Global simiyunAvailableGlobal = getService(GlobalService.class).select(super.getAuthedSchoolId(rsessionid), "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
		
		//删除的时候，不删除群组拥有者；并且，如果该教师，在该班，教授课程在两门及以上，则不允许删除；不能删除班主任
		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable) && ownId != teacherId.longValue() && count == 0 && tId != teacherId.longValue()) {
			SimiyunHelper.delGroupUsers(teacherId, super.getAuthedId(rsessionid), classesId);
		}
	}

	@GET
	@Path("residue")
	@Description("列表")
	public List<Map<String, ?>> selectResidue(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") Long classesId) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		ClassesTeacherService service = super.getService(ClassesTeacherService.class);
		return service.selectResidue(schoolId, classesId);
	}

}

package cn.com.weixunyun.child.control;

import java.io.IOException;
import java.io.OutputStream;
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
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.bean.TeacherClasses;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
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
@Path("/classes")
@Produces(MediaType.APPLICATION_JSON)
@Description("班级")
public class ClassesResource extends AbstractResource {
	
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid, @QueryParam("classesName") final String classesName) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				ClassesService service = getService(ClassesService.class);
				List<TeacherClasses> list =  service.selectAllClass(0, 1000, classesName, school.getId());

				List<Column<TeacherClasses>> columnList = new ArrayList<ExcelUtils.Column<TeacherClasses>>();
				columnList.add(new Column<TeacherClasses>() {
					@Override
					public String getTitle() {
						return "班级名称";
					}

					@Override
					public String getValue(TeacherClasses t) {
						return t.getName();
					}
				});
				columnList.add(new Column<TeacherClasses>() {
					@Override
					public String getTitle() {
						return "入学年份";
					}

					@Override
					public String getValue(TeacherClasses t) {
						return t.getYear() + "";
					}
				});
				columnList.add(new Column<TeacherClasses>() {
					@Override
					public String getTitle() {
						return "班级号数";
					}

					@Override
					public String getValue(TeacherClasses t) {
						return t.getNum();
					}
				});
				columnList.add(new Column<TeacherClasses>() {
					@Override
					public String getTitle() {
						return "班主任";
					}

					@Override
					public String getValue(TeacherClasses t) {
						return t.getTeacherName();
					}
				});
				columnList.add(new Column<TeacherClasses>() {
					@Override
					public String getTitle() {
						return "班级介绍";
					}

					@Override
					public String getValue(TeacherClasses t) {
						return t.getDescription();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=classes.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<TeacherClasses> selectAll(@QueryParam("page") int page, @QueryParam("rows") int rows,
			@QueryParam("classesName") String classesName, @CookieParam("rsessionid") String rsessionid) {
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		ClassesService service = super.getService(ClassesService.class);
		return service.selectAllClass(page * rows, rows, classesName, teacher.getSchoolId());
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		ClassesService service = super.getService(ClassesService.class);
		return service.selectAllClassCount(keyword, teacher.getSchoolId());
	}

	@GET
	@Path("classesList")
	@Description("班级列表")
	public List<Classes> getAllPrivinces(@CookieParam("rsessionid") String rsessionid) {
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		try {
			ClassesService service = super.getService(ClassesService.class);
			return service.selectAllClasses(teacher.getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
    @Path("rule")
    @Description("classes表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "classes");
    }

	@GET
	@Path("{id}")
	@Description("详情")
	public Classes select(@PathParam("id") Long id) {
		return super.getService(ClassesService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws IOException {

		Classes classes = super.buildBean(Classes.class, formData, null);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		classes.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));
		classes.setSchoolId(teacher.getSchoolId());
		classes.setCreateTeacherId(super.getAuthedId(rsessionid));

		ClassesService service = super.getService(ClassesService.class);
		service.insert(classes);
		
		Global simiyunAvailableGlobal = getService(GlobalService.class).select(teacher.getSchoolId(), "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
		
		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable)) {
			String uid = SimiyunHelper.addGroup(classes.getId(), super.getAuthedId(rsessionid), classes.getName());
			service.idDisk(classes.getId(), uid);

			if (formData.getFirst("teacherId") != null && !"".equals(formData.getFirst("teacherId")) 
					&& super.getAuthedId(rsessionid).longValue() != Long.parseLong(formData.getFirst("teacherId"))) {
				Long teacherId = Long.parseLong(formData.getFirst("teacherId"));
				Long uidManager = SimiyunHelper.addGroupManager(teacherId, super.getAuthedId(rsessionid), classes.getId());
				super.getService(TeacherService.class).idDisk(teacherId, uidManager);
			}
			
		}
		 
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
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("classes", file, "1", school.getId());
			
			Global simiyunAvailableGlobal = getService(GlobalService.class).select(school.getId(), "simiyun", "available");
			String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
			String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
			
			Boolean flag = false;
			
			ClassesService service = super.getService(ClassesService.class);
			int n = service.insertClasses(school.getId(), del, list, flag, super.getAuthedId(rsessionid));
			
			if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable)) {
				for (Map<String, Object> m : list) {
					Long cId = Long.parseLong(m.get("cId").toString());
					String uid = SimiyunHelper.addGroup(cId, super.getAuthedId(rsessionid), m.get("cName").toString());
					
					/*if (m.get("cTeacherId") != null 
							&& super.getAuthedId(rsessionid) != Long.parseLong(m.get("cTeacherId").toString())) {
						long uidT = SimiyunHelper.addGroupManager(Long.parseLong(m.get("cTeacherId").toString()), 
								super.getAuthedId(rsessionid), cId);
						System.out.println("---------------cTeacherId--------------");
						System.out.println();
						System.out.println("---uidT: " + uidT);
						System.out.println();
						getService(TeacherService.class).idDisk(Long.parseLong(m.get("cTeacherId").toString()), uidT);
					}*/
					
					service.idDisk(cId, uid);
					
				}
			}
			
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
			@CookieParam("rsessionid") String rsessionid) throws IOException {
		ClassesService service = super.getService(ClassesService.class);
		Classes cls = service.select(id);

		Classes classes = super.buildBean(Classes.class, formData, id);
		classes.setDescription(URLDecoder.decode(formData.getFirst("remark"), "UTF-8"));
		classes.setUpdateTeacherId(super.getAuthedId(rsessionid));
		
		Long t_old = cls.getTeacherId();//旧班主任
		Long t_new = classes.getTeacherId();
		long t_id = classes.getCreateTeacherId() == null ? -1 : classes.getCreateTeacherId().longValue();//群组

		service.update(classes);
		
		Global simiyunAvailableGlobal = getService(GlobalService.class).select(super.getAuthedSchoolId(rsessionid), "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
		
		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable)) {
			
			//班主任信息变更
			if (t_new != null && t_old == null) {
				long uid = SimiyunHelper.addGroupManager(t_new, super.getAuthedId(rsessionid), id);
				super.getService(TeacherService.class).idDisk(t_new, uid);
			} else if (t_new == null && t_old != null && t_old.longValue() != t_id) {
				SimiyunHelper.delGroupUsers(t_old, super.getAuthedId(rsessionid), id);
			} else if (t_new != null && t_old != null && t_new.longValue() != t_old.longValue() && t_old.longValue() != t_id) {
				int count = super.getService(ClassesTeacherService.class).selectTeacherCount(id, t_old);
				if (count == 0) {
					//删除旧的
					SimiyunHelper.delGroupUsers(t_old, super.getAuthedId(rsessionid), id);
				}
				
				//添加新的
				long uidNew = SimiyunHelper.addGroupManager(t_new, super.getAuthedId(rsessionid), id);
				super.getService(TeacherService.class).idDisk(t_new, uidNew);
			}
		
		}
		
	}
	
	@PUT
	@Path("{id}/disk/{flag}/{teacherId}")
	@Description("开通云盘")
	public void updateIdDisk(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid,
			@PathParam("flag") int flag, @PathParam("teacherId") Long teacherId) throws Exception {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		ClassesService service = super.getService(ClassesService.class);
		Classes classes = service.select(id);
		
		Global simiyunAvailableGlobal = getService(GlobalService.class).select(schoolId, "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
		
		System.out.println("-----------------flag:" + flag);
		
		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable) && flag == 1) {
			
			//将create_teacher_id更新为当前登陆用户，作为群组拥有者存在
			service.updateCreateTeacher(id, super.getAuthedId(rsessionid));
			
			String uid = SimiyunHelper.addGroup(id, super.getAuthedId(rsessionid), classes.getName());
			service.idDisk(id, uid);
			
			//当前登陆用户已经为群组拥有者，若班主任为其他人员，则添加为管理员（该处create_teacher_id作为群组拥有者存在）
			if (teacherId != null && super.getAuthedId(rsessionid).longValue() != teacherId.longValue()) {
				Long uidManager = SimiyunHelper.addGroupManager(teacherId, super.getAuthedId(rsessionid), id);
				super.getService(TeacherService.class).idDisk(teacherId, uidManager);
			}
			
		} else if ("1".equals(properSimiyunAvailable) && "1".equals(globalSimiyunAvailable) && flag == 0) {
			SimiyunHelper.delGroup(id, super.getAuthedId(rsessionid));
			service.idDisk(id, null);
		}
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {
		ClassesService service = super.getService(ClassesService.class);
		service.delete(id);
		
		Global simiyunAvailableGlobal = getService(GlobalService.class).select(super.getAuthedSchoolId(rsessionid), "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
		
		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable)) {
			SimiyunHelper.delGroup(id, super.getAuthedId(rsessionid));
		}

	}
}

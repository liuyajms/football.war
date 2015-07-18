package cn.com.weixunyun.child.control;

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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.model.bean.CourseClasses;
import cn.com.weixunyun.child.model.bean.DictionaryTeacher;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.ClassesTeacher;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.model.service.ClassesTeacherService;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.RoleService;
import cn.com.weixunyun.child.model.service.TeacherService;
import cn.com.weixunyun.child.third.simiyun.SimiyunHelper;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/teacher")
@Produces(MediaType.APPLICATION_JSON)
@Description("教师")
public class TeacherResource extends AbstractResource {

	private static final int IMAGE_W = 200;
	private static final int IMAGE_H = 200;

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") final String keyword, @QueryParam("classesId") final Long classesId) {
		final School school = super.getAuthedSchool(rsessionid);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				TeacherService service = getService(TeacherService.class);
				List<DictionaryTeacher> list = service.selectAll(0L, 1000L, school.getId(), keyword);

				List<Column<DictionaryTeacher>> columnList = new ArrayList<ExcelUtils.Column<DictionaryTeacher>>();
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "姓名";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getName();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "性别";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getGenderName() == null ? "" : t.getGenderName();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "手机号码";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getMobile();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "用户名";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getUsername();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "编号";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getCode();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "卡号";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getCard();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "职称";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getTitleName() == null ? "" : t.getTitleName();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "邮箱";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getEmail();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "教师介绍";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getRemark();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "备注";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getDescription();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "是否管理员";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getType() == 6L ? "是" : "否";
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=teacher.xlsx").build();
	}

	@GET
	@Path("export/point")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response exportPoint(final @CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") final String keyword, @QueryParam("schoolId") final Long schoolId) {

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				TeacherService service = getService(TeacherService.class);
				List<DictionaryTeacher> list = service.selectAll(0L, 1000L, schoolId, keyword);

				List<Column<DictionaryTeacher>> columnList = new ArrayList<ExcelUtils.Column<DictionaryTeacher>>();
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "姓名";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getName();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "手机号码";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getMobile();
					}
				});
				columnList.add(new Column<DictionaryTeacher>() {
					@Override
					public String getTitle() {
						return "积分";
					}

					@Override
					public String getValue(DictionaryTeacher t) {
						return t.getPoint() + "";
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=teacher_point.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<DictionaryTeacher> selectAll(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") String keyword, @QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@QueryParam("schoolId") Long schoolId, @QueryParam("classesId") Long classesId) {
		TeacherService service = super.getService(TeacherService.class);
		List<DictionaryTeacher> teacherList = new ArrayList<DictionaryTeacher>();

		if (classesId != null) {
			// 获取某个班的班主任，以及该班的任课教师
			Classes classes = super.getService(ClassesService.class).select(classesId);
			Long teacherId = classes.getTeacherId();
			if (teacherId != null) {
				DictionaryTeacher dt = service.selectDictionaryTeacher(teacherId);
				dt.setCourseName("班主任");
				teacherList.add(dt);
			}
			Long sId = super.getAuthedSchoolId(rsessionid);
			ClassesTeacherService ctService = super.getService(ClassesTeacherService.class);
			List<ClassesTeacher> list = ctService.selectAll(classesId, page * rows, rows, null, sId);
			for (ClassesTeacher ct : list) {
				DictionaryTeacher teacher = new DictionaryTeacher();
				teacher.setId(ct.getTeacherId());
				teacher.setName(ct.getTeacherName());
				teacher.setCourseId(ct.getCourseId());
				teacher.setCourseName(ct.getCourseName());
				teacherList.add(teacher);
			}
		} else {
			if (schoolId != null) {
				teacherList = service.getAdminList(page * rows, rows, schoolId, keyword);
			} else {
				School school = getAuthedSchool(rsessionid);
				teacherList = service.selectAll(page * rows, rows, school.getId(), keyword);
			}
		}
		return teacherList;
	}

	@GET
	@Path("notAdmin")
	@Description("不是管理员（type=0）的列表")
	public List<DictionaryTeacher> selectNotAdminList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("schoolId") Long schoolId) {
		TeacherService service = super.getService(TeacherService.class);
		return service.getNotAdminList(schoolId);
	}

	@GET
	@Path("all")
	@Description("获取某个学校全部教师")
	public List<DictionaryTeacher> selectAllTeacher(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("schoolId") Long schoolId,
			@QueryParam("keyword") String keyword) {
		TeacherService service = super.getService(TeacherService.class);
		return service.selectAll(page * rows, rows, schoolId, keyword);
	}

	@GET
	@Path("all/count")
	@Description("总数")
	public int selectAllTeacherCount(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") String keyword, @QueryParam("schoolId") Long schoolId) {
		TeacherService service = super.getService(TeacherService.class);
		return service.selectAllCount(schoolId, keyword);
	}

	@GET
	@Path("classes")
	@Description("列表")
	public List<Teacher> selectTeacher(@CookieParam("rsessionid") String rsessionid) {
		School school = getAuthedSchool(rsessionid);
		TeacherService service = super.getService(TeacherService.class);

		return service.selectTeacher(school.getId());
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAllCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
			@QueryParam("schoolId") Long schoolId) {
		int total;
		TeacherService service = super.getService(TeacherService.class);
		if (schoolId != null) {
			total = service.getAdminListCount(schoolId, keyword);
		} else {
			School school = getAuthedSchool(rsessionid);
			total = service.selectAllCount(school.getId(), keyword);
		}
		return total;
	}

	@GET
	@Path("inclasses")
	@Description("教师所在班级列表")
	public List<CourseClasses> selectClassesTeacher(@CookieParam("rsessionid") String rsessionid) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		TeacherService service = super.getService(TeacherService.class);
		return service.getClassesList(schoolId, super.getAuthedId(rsessionid));
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Teacher select(@PathParam("id") long id) {
		return super.getService(TeacherService.class).select(id);
	}

	@GET
	@Path("rule")
	@Description("teacher表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "teacher");
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Map<String, PartField> map = super.partMulti(request);

		Teacher teacher = super.buildBean(Teacher.class, map, null);
		School school = super.getAuthedSchool(rsessionid);

		if (map.get("username") == null || "".equals(map.get("username").getValue())) {
			teacher.setUsername(map.get("mobile").getValue());
		}

		PartField imageField = map.get("image");
		if (imageField != null) {
			File file = imageField.getFile();
			if (file != null) {
				FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/teacher/" + teacher.getId()
						+ "@l.png"));
				ImageUtils.zoom(file, new File(super.getFilePath(), school.getId() + "/teacher/" + teacher.getId()
						+ ".png"), IMAGE_W, IMAGE_H);
			}
		}

		if (map.get("schoolId") != null && !"".equals(map.get("schoolId").toString())) {
			teacher.setSchoolId(Long.valueOf(map.get("schoolId").getValue()));
			teacher.setType(6L);
		} else {
			teacher.setSchoolId(school.getId());
			if (map.get("type") == null) {
				teacher.setType(0L);
			} else {
				teacher.setType(6L);
			}
		}

		if (map.get("roldId") != null) {
			teacher.setRoleId(Long.valueOf(map.get("roldId").getValue()));
		} else {
			RoleService roleService = super.getService(RoleService.class);
			teacher.setRoleId(roleService.getAdmin());
		}

		teacher.setAvailable(true);
		teacher.setPassword(DigestUtils.md5Hex(map.get("password").getValue()));
		teacher.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
		teacher.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		TeacherService service = super.getService(TeacherService.class);
		service.insert(teacher);

		// 需要先判断配置文件中的simiyun.available是否为true，并且查询global中，这个学校的simiyun.available是否为true，同时满足才能执行操作

		Global simiyunAvailableGlobal = getService(GlobalService.class).select(teacher.getSchoolId(), "simiyun",
				"available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);

		if ("1".equals(properSimiyunAvailable) && "1".equals(globalSimiyunAvailable)) {
			long uid = SimiyunHelper.addUser(super.getAuthedId(rsessionid), teacher.getId(), teacher.getName());
			service.idDisk(teacher.getId(), uid);
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
			School school = super.getAuthedSchool(rsessionid);

			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());

			System.out.println(map);
			System.out.println();

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("teacher", file, "1", school.getId());
			System.out.println(list);

			Global simiyunAvailableGlobal = getService(GlobalService.class).select(school.getId(), "simiyun", "available");
			String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
			String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);

			Boolean flag = false;

			TeacherService service = super.getService(TeacherService.class);
			int n = service.insertTeachers(school.getId(), del, list, flag);
			
			if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable)) {
				for (Map<String, Object> m : list) {
					Long tId = Long.parseLong(m.get("tId").toString());
					long uid = SimiyunHelper.addUser(super.getAuthedId(rsessionid), tId, m.get("tName").toString());
					service.idDisk(tId, uid);
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
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改")
	public void update(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		Map<String, PartField> map = super.partMulti(request);

		School school = super.getAuthedSchool(rsessionid);
		Teacher teacher = super.buildBean(Teacher.class, map, id);

		if (map.get("type") == null) {
			teacher.setType(0L);
		} else {
			teacher.setType(6L);
		}

		PartField imageField = map.get("image");
		if (imageField != null) {
			File file = imageField.getFile();
			if (file != null) {
				FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/teacher/" + teacher.getId()
						+ "@l.png"));
				ImageUtils.zoom(file, new File(super.getFilePath(), school.getId() + "/teacher/" + teacher.getId()
						+ ".png"), IMAGE_W, IMAGE_H);
			}
		}

		if (map.get("roldId") != null) {
			teacher.setRoleId(Long.valueOf(map.get("roldId").getValue()));
		} else {
			RoleService roleService = super.getService(RoleService.class);
			teacher.setRoleId(roleService.getAdmin());
		}

		teacher.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		super.getService(TeacherService.class).update(teacher);

	}

	@PUT
	@Path("{id}/disk/{flag}")
	@Description("开通云盘")
	public void updateIdDisk(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid,
			@PathParam("flag") int flag) throws Exception {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		TeacherService service = super.getService(TeacherService.class);
		Teacher teacher = service.select(id);

		Global simiyunAvailableGlobal = getService(GlobalService.class).select(schoolId, "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailabal = PropertiesListener.getProperty("simiyun.available", null);

		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailabal) && flag == 1) {
			
			Long uid = SimiyunHelper.addUser(super.getAuthedId(rsessionid), id, teacher.getName());
			service.idDisk(id, uid);

		} else if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailabal) && flag == 0) {
			SimiyunHelper.delUser(id);
			service.idDisk(id, null);
		}
	}

	@PUT
	@Path("{id}/card")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改卡的可用性")
	public void updateCardAvailable(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		Map<String, PartField> map = super.partMulti(request);
		if (map.get("image") != null && !"".equals(map.get("image").toString())) {
			String cardAvailable = map.get("cardAvailable").getValue();
			super.getService(TeacherService.class).cardAvailable(id, "1".equals(cardAvailable) ? true : false);
		}
	}

	@PUT
	@Path("{id}/image")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改头像")
	public void updateImage(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		Map<String, PartField> map = super.partMulti(request);
		PartField field = map.get("image");
		if (field != null) {
			PartFieldFile file = field.getFile();
			if (file != null) {
				TeacherService teacherService = super.getService(TeacherService.class);
				teacherService.updated(id);

				File targetFile = new File(super.getFilePath(), super.getAuthedSchool(rsessionid).getId() + "/teacher/"
						+ id + "@l.png");
				FileUtils.copyFile(file, targetFile);
				ImageUtils.zoom(file, new File(super.getFilePath(), super.getAuthedSchool(rsessionid).getId()
						+ "/teacher/" + id + ".png"), IMAGE_W, IMAGE_H);
			}
		}
	}

	@PUT
	@Path("{id}/background")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改背景")
	public void updateBackground(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) {
		Map<String, PartField> map = super.partMulti(request);
		PartField field = map.get("image");
		if (field != null) {
			PartFieldFile file = field.getFile();
			if (file != null) {
				TeacherService teacherService = super.getService(TeacherService.class);
				teacherService.updated(id);

				File targetFile = new File(super.getFilePath(), "/" + super.getAuthedSchool(rsessionid).getId()
						+ "/teacher/" + id + "_background.png");
				if (targetFile.getParentFile().exists() == false) {
					targetFile.getParentFile().mkdirs();
				}
				try {
					FileUtils.copyFile(file, targetFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@PUT
	@Path("{id}/password")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改密码")
	public Boolean password(MultivaluedMap<String, String> form, @PathParam("id") Long id) {
		Map<String, PartField> map = super.part(form);
		String passwordOld = map.get("password_old").getValue();
		String passwordNew = map.get("password_new").getValue();

		TeacherService teacherService = super.getService(TeacherService.class);
		Teacher teacher = teacherService.select(id);

		if (teacher != null && teacher.getPassword().equals(DigestUtils.md5Hex(passwordOld))) {
			teacherService.password(id, DigestUtils.md5Hex(passwordNew));
			return true;
		} else {
			return false;
		}

	}

	@PUT
	@Path("{id}/username")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改帐号")
	public Boolean username(MultivaluedMap<String, String> form, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) {
		Map<String, PartField> map = super.part(form);
		String username = map.get("username").getValue();

		TeacherService teacherService = super.getService(TeacherService.class);
		School school = super.getAuthedSchool(rsessionid);
		Teacher teacher = teacherService.selectTeacherMobile(school.getId(), username);

		if (teacher == null) {
			teacherService.username(id, username);
			return true;
		} else {
			return false;
		}
	}

	@PUT
	@Path("{id}/resetPwd")
	@Description("重置密码")
	public void resetPassword(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {
		School school = getAuthedSchool(rsessionid);
		System.out.println("-----------reset----------");
		String pwd = super.getService(GlobalService.class).select(school.getId(), "parents", "password").getValue();
		super.getService(TeacherService.class).password(id, DigestUtils.md5Hex(pwd));
	}

	@PUT
	@Path("admin")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("设置/取消为管理员")
	public void admin(MultivaluedMap<String, String> form, @CookieParam("rsessionid") String rsessionid) {
		if (form.getFirst("teacherId") != null && !"".equals(form.getFirst("teacherId"))) {
			// 设置为管理员
			super.getService(TeacherService.class).admin(Long.parseLong(form.getFirst("teacherId")), 6L);
		} else {
			// 取消管理员
			super.getService(TeacherService.class).admin(Long.parseLong(form.getFirst("id")), 0L);
		}
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") long id, @CookieParam("rsessionid") String rsessionid) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);

		new PicResource().delete(schoolId + "/teacher/" + id + ".png");
		super.getService(TeacherService.class).delete(id);

		Global simiyunAvailableGlobal = getService(GlobalService.class).select(schoolId, "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailabal = PropertiesListener.getProperty("simiyun.available", null);

		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailabal)) {
			SimiyunHelper.delUser(id);
		}
	}

	@DELETE
	@Path("delete/image/{id}")
	@Description("删除头像")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		School school = super.getAuthedSchool(rsessionid);
		new PicResource().delete(school.getId() + "/teacher/" + id + ".png");
		// super.getService(TeacherService.class).updateImage(id, false);
	}

}

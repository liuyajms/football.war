package cn.com.weixunyun.child.control;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
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

import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.bean.StudentClasses;
import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Student;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.ParentsService;
import cn.com.weixunyun.child.model.service.StudentService;
import cn.com.weixunyun.child.third.simiyun.SimiyunHelper;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Description("学生")
public class StudentResource extends AbstractResource {

	private static final int IMAGE_W = 200;
	private static final int IMAGE_H = 200;

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") final String keyword, @QueryParam("classesId") final Long classesId) {
		final School school = super.getAuthedSchool(rsessionid);

		ClassesService service = getService(ClassesService.class);
		Classes classes = service.select(classesId);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				StudentService service = getService(StudentService.class);
				List<StudentClasses> list = service.selectExportList(school.getId(), classesId, keyword);

				List<Column<StudentClasses>> columnList = new ArrayList<ExcelUtils.Column<StudentClasses>>();
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "学生";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getName();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "性别";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getGenderName();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "生日";
					}

					@Override
					public String getValue(StudentClasses t) {
						if (t.getBirthday() != null) {
							return new SimpleDateFormat("yyyy-MM-dd").format(t.getBirthday());
						} else {
							return "";
						}

					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "学籍号";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getCode();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "卡号";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getCard();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "联系地址";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getAddress();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "备注";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getDescription();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "家长姓名";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getParentsName();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "家长手机号";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getParentsMobile();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "家长用户名";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getParentsUsername();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "与学生关系";
					}

					@Override
					public String getValue(StudentClasses t) {
						return t.getParentsType();
					}
				});
				columnList.add(new Column<StudentClasses>() {
					@Override
					public String getTitle() {
						return "是否家委会成员";
					}

					@Override
					public String getValue(StudentClasses t) {
						if (t.getParentsPta() != null) {
							return t.getParentsPta() == true ? "是" : "否";
						} else {
							return "";
						}
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response
				.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition",
						"attachment; filename=student_" + classes.getYear() + "_" + classes.getNum() + ".xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesStudent> getList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") String keyword, @QueryParam("classesId") Long classesId,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows) {
		School school = super.getAuthedSchool(rsessionid);
		StudentService service = super.getService(StudentService.class);
		return service.getList(school.getId(), page * rows, rows, classesId, keyword);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
			@QueryParam("classesId") Long classesId) {
		School school = super.getAuthedSchool(rsessionid);
		StudentService service = super.getService(StudentService.class);
		return service.getListCount(school.getId(), classesId, keyword);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Student select(@PathParam("id") Long id) {
		return super.getService(StudentService.class).selectGet(id);
	}

	@GET
	@Path("rule")
	@Description("student表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "student");
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		School school = super.getAuthedSchool(rsessionid);

		Student student = super.buildBean(Student.class, formData, null);
		student.setSchoolId(school.getId());

		StudentService service = super.getService(StudentService.class);

		if (!"".equals(formData.get("birthday"))) {
			try {
				java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(formData.getFirst("birthday"));
				student.setBirthday(new Date(date.getTime()));
			} catch (Exception e) {
			}

		}
		service.insert(student);
	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertSome(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);
			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();

			Long classesId = Long.parseLong(map.get("classesId").getValue());
			int del = Integer.parseInt(map.get("del").getValue());

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("student", file, "1", school.getId());
			
			Global simiyunAvailableGlobal = getService(GlobalService.class).select(school.getId(), "simiyun", "available");
			String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
			String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);
			
			Boolean flag = false;

			StudentService service = super.getService(StudentService.class);
			int n = service.insertStudents(school.getId(), classesId, del, list, flag, super.getAuthedId(rsessionid));
			
			if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable)) {
				for (Map<String, Object> m : list) {
					Long pId = Long.parseLong(m.get("pId").toString());
					long uid = SimiyunHelper.addUser(super.getAuthedId(rsessionid), pId, m.get("pName").toString());
					super.getService(ParentsService.class).idDisk(pId, uid);
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
	public void update(MultivaluedMap<String, String> formData, @PathParam("id") long id) throws Exception {
		Student student = super.buildBean(Student.class, formData, id);
		if (formData.getFirst("birthday") != null && !"".equals(formData.getFirst("birthday"))) {
			try {
				java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(formData.getFirst("birthday"));
				student.setBirthday(new Date(date.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.getService(StudentService.class).update(student);
	}

	@PUT
	@Path("{id}/card")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改卡的可用性")
	public void updateCardAvailable(MultivaluedMap<String, String> formData, @PathParam("id") long id) throws Exception {
		if (formData.getFirst("cardAvailable") != null && !"".equals(formData.getFirst("cardAvailable"))) {
			String cardAvailable = formData.getFirst("cardAvailable");
			super.getService(StudentService.class).updatedCardAvailable(id, "1".equals(cardAvailable) ? true : false);
		}
	}

	@PUT
	@Path("{id}/image")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改头像")
	public void updateImage(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Map<String, PartField> map = super.partMulti(request);
		PartField field = map.get("image");
		if (field != null) {
			PartFieldFile file = field.getFile();
			if (file != null) {
				StudentService studentService = super.getService(StudentService.class);
				studentService.updated(id);

				File targetLargeFile = new File(super.getFilePath(), "/" + schoolId + "/student/" + id + "@l.png");
				FileUtils.copyFile(file, targetLargeFile);

				File targetFile = new File(super.getFilePath(), "/" + schoolId + "/student/" + id + ".png");
				ImageUtils.zoom(file, targetFile, IMAGE_W, IMAGE_H);

				ParentsService parentsService = super.getService(ParentsService.class);
				List<StudentParents> parentsList = parentsService.selectAll(0L, 100L, schoolId, id, null);
				for (StudentParents parents : parentsList) {
					parentsService.updated(parents.getId());
					File targetParentsFile = new File(super.getFilePath(), "/" + schoolId + "/parents/"
							+ parents.getId() + ".png");
					if (targetParentsFile.getParentFile().exists() == false) {
						targetParentsFile.getParentFile().mkdirs();
					}
					FileUtils.copyFile(targetFile, targetParentsFile);
					FileUtils.copyFile(targetLargeFile, new File(super.getFilePath(), "/" + schoolId + "/parents/"
							+ parents.getId() + "@l.png"));
				}
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
				StudentService studentService = super.getService(StudentService.class);
				studentService.updated(id);

				File targetFile = new File(super.getFilePath(), "/" + super.getAuthedSchool(rsessionid).getId()
						+ "/student/" + id + "_background.png");
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

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(StudentService.class).delete(id);
	}
}

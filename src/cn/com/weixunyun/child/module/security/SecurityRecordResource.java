package cn.com.weixunyun.child.module.security;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.bean.User;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.ParentsService;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.model.service.StudentService;
import cn.com.weixunyun.child.model.service.UserService;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.PushProducer;
import cn.com.weixunyun.child.util.ExcelUtils.Column;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/security/record")
@Produces(MediaType.APPLICATION_JSON)
@Description("进出校门记录")
public class SecurityRecordResource extends AbstractResource {

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid, @QueryParam("userId") final Long userId) {
		final School school = super.getAuthedSchool(rsessionid);
		final String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				SecurityRecordService service = getService(SecurityRecordService.class);

				List<DeviceSecurityRecord> list = service.selectAll(0L, 10000L, school.getId(), term, userId);

				List<Column<DeviceSecurityRecord>> columnList = new ArrayList<ExcelUtils.Column<DeviceSecurityRecord>>();
				columnList.add(new Column<DeviceSecurityRecord>() {
					@Override
					public String getTitle() {
						return "学期";
					}

					@Override
					public String getValue(DeviceSecurityRecord t) {
						return t.getTerm().substring(0, 4) + "学年"
								+ ("1".equals(t.getTerm().substring(4, 5)) == true ? "下学期" : "上学期");
					}
				});
				columnList.add(new Column<DeviceSecurityRecord>() {
					@Override
					public String getTitle() {
						return "角色";
					}

					@Override
					public String getValue(DeviceSecurityRecord t) {
						return "2".equals(t.getUserType()) == true ? "学生" : "教师";
					}
				});
				columnList.add(new Column<DeviceSecurityRecord>() {
					@Override
					public String getTitle() {
						return "姓名";
					}

					@Override
					public String getValue(DeviceSecurityRecord t) {
						return t.getUserName();
					}
				});
				columnList.add(new Column<DeviceSecurityRecord>() {
					@Override
					public String getTitle() {
						return "时间";
					}

					@Override
					public String getValue(DeviceSecurityRecord t) {
						return new SimpleDateFormat("yyyy-MM-dd hh:mm").format(t.getDeviceTime());
					}
				});
				columnList.add(new Column<DeviceSecurityRecord>() {
					@Override
					public String getTitle() {
						return "校门";
					}

					@Override
					public String getValue(DeviceSecurityRecord t) {
						return t.getDeviceName();
					}
				});
				columnList.add(new Column<DeviceSecurityRecord>() {
					@Override
					public String getTitle() {
						return "到达/离开";
					}

					@Override
					public String getValue(DeviceSecurityRecord t) {
						return t.getReach() == true ? "到达" : "离开";
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=security_record.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<DeviceSecurityRecord> selectAll(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("userId") Long userId) {
		School school = super.getAuthedSchool(rsessionid);
		SecurityRecordService service = super.getService(SecurityRecordService.class);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		return service.selectAll(page, rows, school.getId(), term, userId);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAllCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("userId") Long userId) {
		School school = super.getAuthedSchool(rsessionid);
		SecurityRecordService service = super.getService(SecurityRecordService.class);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		return service.selectAllCount(school.getId(), term, userId);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public SecurityRecord select(@PathParam("id") Long id) {
		return super.getService(SecurityRecordService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Path("/alarm/card")
	@Description("卡片警告")
	public void alarmCard(MultivaluedMap<String, String> form) throws Exception {
		Long schoolId = Long.parseLong(form.getFirst("schoolId"));

		StudentService studentService = super.getService(StudentService.class);
		ClassesStudent student = studentService.getCardStudent(schoolId, form.getFirst("card"));

		if (student != null && student.getClassesTeacherId() != null) {
			PushProducer.sendPersonalNotification(student.getSchoolId(), student.getClassesTeacherId(),
					student.getName() + "的卡片电量不足", "请为她办理换卡", "record", null);
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> form) throws Exception {
		Long schoolId = Long.parseLong(form.getFirst("schoolId"));

		GlobalService globalService = super.getService(GlobalService.class);
		String securityPassword = globalService.select(schoolId, "security", "password").getValue();
		if (securityPassword.equals(form.getFirst("secret"))) {
			String term = globalService.select(schoolId, "term", "default").getValue();

			SequenceService sequenceService = getService(SequenceService.class);

			SecurityRecord record = new SecurityRecord();
			record.setSchoolId(schoolId);
			record.setTerm(term);
			record.setDevice(form.getFirst("device"));
			record.setTime(new java.sql.Timestamp(System.currentTimeMillis()));

			List<String> cardList = form.get("card");
			List<String> deviceTimeList = form.get("deviceTime");
			List<String> reachList = form.get("reach");
			int i = 0;
			for (String card : cardList) {
				System.out.println(i + " - " + card);

				StudentService studentService = getService(StudentService.class);
				ClassesStudent student = studentService.getCardStudent(schoolId, card);

				if (student != null) {
					System.out.println(student.getName());
					System.out.println();
					record.setId(sequenceService.sequence());
					record.setCard(card);
					record.setUserId(student.getId());
					record.setDeviceTime(new java.sql.Timestamp(Long.parseLong(deviceTimeList.get(i))));
					record.setReach("1".equals(reachList.get(i)));

					super.getService(SecurityRecordService.class).insert(record);

					ParentsService parentsService = super.getService(ParentsService.class);
					List<StudentParents> parentsList = parentsService.selectAll(0L, 100L, schoolId, student.getId(),
							null);
					for (StudentParents parents : parentsList) {
						PushProducer.sendPersonalNotification(student.getSchoolId(), parents.getId(), student.getName()
								+ (record.getReach() ? "已进入学校" : "已离开学校"),
								new SimpleDateFormat("HH:mm").format(record.getDeviceTime())
										+ (record.getReach() ? "已进入学校" : "已离开学校"), "record", null);
					}

					SecurityService securityService = super.getService(SecurityService.class);
					if (record.getReach()) {
						securityService.updateReach(schoolId, term, student.getId(), record.getDeviceTime());
					} else {
						securityService.updateLeave(schoolId, term, student.getId(), record.getDeviceTime());
					}
				} else {
					UserService userService = super.getService(UserService.class);
					User user = userService.getCardUser(schoolId, card);

					record.setId(sequenceService.sequence());
					record.setCard(card);
					record.setUserId(user.getId());
					record.setDeviceTime(new java.sql.Timestamp(Long.parseLong(deviceTimeList.get(i))));
					record.setReach("1".equals(reachList.get(i)));

					SecurityRecordService service = super.getService(SecurityRecordService.class);
					service.insert(record);
				}

				i++;
			}
		}
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws Exception {

		SecurityRecord record = super.buildBean(SecurityRecord.class, formData, id);

		super.getService(SecurityRecordService.class).update(record);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(SecurityRecordService.class).delete(id);
	}
}

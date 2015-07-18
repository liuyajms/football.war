package cn.com.weixunyun.child.module.notice;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.control.AbstractResource.PartField;
import cn.com.weixunyun.child.control.AbstractResource.PartFieldFile;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.PushProducer;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/notice")
@Produces(MediaType.APPLICATION_JSON)
@Description("学校公告")
public class NoticeResource extends AbstractResource {

	private static final int IMAGE_W = 200;
	private static final int IMAGE_H = 200;

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("keyword") String keyword, @QueryParam("classesName") final String classesName) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				List<TeacherNotice> list = null;

				NoticeService service = getService(NoticeService.class);
				Teacher teacher = getAuthedTeacher(rsessionid);
				if (teacher != null && teacher.getType() >= 6) {
					list = service.getList(0L, 1000L, school.getId(), true, true, keyword);
				} else {
					list = service.getList(0L, 1000L, school.getId(), getAuthedTeacher(rsessionid) != null,
							getAuthedParents(rsessionid) != null, keyword);
				}

				List<Column<TeacherNotice>> columnList = new ArrayList<ExcelUtils.Column<TeacherNotice>>();
				columnList.add(new Column<TeacherNotice>() {
					@Override
					public String getTitle() {
						return "公告内容";
					}

					@Override
					public String getValue(TeacherNotice t) {
						return t.getDescription();
					}
				});
				columnList.add(new Column<TeacherNotice>() {
					@Override
					public String getTitle() {
						return "推送至家长";
					}

					@Override
					public String getValue(TeacherNotice t) {
						if (t.getPushParents() == null || "".equals(t.getPushParents())) {
							return "";
						} else {
							return t.getPushParents() == true ? "是" : "否";
						}
					}
				});
				columnList.add(new Column<TeacherNotice>() {
					@Override
					public String getTitle() {
						return "推送至教师";
					}

					@Override
					public String getValue(TeacherNotice t) {
						if (t.getPushTeacher() == null || "".equals(t.getPushTeacher())) {
							return "";
						} else {
							return t.getPushTeacher() == true ? "是" : "否";
						}
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=notice.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<TeacherNotice> getList(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);

		NoticeService service = super.getService(NoticeService.class);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		if (teacher != null && teacher.getType() >= 6) {
			return service.getList(page * rows, rows, school.getId(), true, true, keyword);
		} else {
			return service.getList(page * rows, rows, school.getId(), super.getAuthedTeacher(rsessionid) != null,
					super.getAuthedParents(rsessionid) != null, keyword);
		}
	}

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);
		NoticeService service = super.getService(NoticeService.class);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		if (teacher != null && teacher.getType() >= 6) {
			return service.getListCount(school.getId(), true, true, keyword);
		} else {
			return service.getListCount(school.getId(), super.getAuthedTeacher(rsessionid) != null,
					super.getAuthedParents(rsessionid) != null, keyword);
		}
	}

	@GET
	@Path("rule")
	@Description("notice表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "notice");
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Notice get(@PathParam("id") Long id) {
		return super.getService(NoticeService.class).get(id);
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		School school = super.getAuthedSchool(rsessionid);
		Map<String, PartField> map = super.partMulti(request);
		Long id = (long) super.getService(SequenceService.class).sequence();
		Notice notice = super.buildBean(Notice.class, map, id);

		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), school.getId() + "/notice/" + id
							+ "/" + i + "@l.png"));
					ImageUtils.zoom(fileList.get(i), new File(super.getFilePath(), school.getId() + "/notice/" + id
							+ "/" + i + ".png"), IMAGE_W, IMAGE_H);
				}
				notice.setPic((long) fileList.size());
			}
		}

		PartField voiceField = map.get("voice");
		if (voiceField != null) {
			PartFieldFile file = voiceField.getFile();
			if (file != null) {
				FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/notice/" + notice.getId()
						+ "/voice.amr"));
				notice.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
			}
		}

		notice.setCreateTime(new Timestamp(System.currentTimeMillis()));
		notice.setCreateUserId(super.getAuthedId(rsessionid));
		notice.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		notice.setUpdateUserId(super.getAuthedId(rsessionid));
		notice.setSchoolId(school.getId());

		PartField pushTField = map.get("pushTeacher");
		PartField pushPField = map.get("pushParents");
		if (pushTField != null && !pushTField.isValueNull()) {
			notice.setPushTeacher("1".equals(pushTField.getValue()));
		}
		if (pushPField != null && !pushPField.isValueNull()) {
			notice.setPushParents("1".equals(pushPField.getValue()));
		}

		NoticeService service = super.getService(NoticeService.class);

		service.insert(notice);

		if (notice.getPushParents()) {
			Map<String, Object> notificationMap = new HashMap<String, Object>();
			notificationMap.put("id", notice.getId());

			PushProducer.sendSchoolNotification(school.getId(), 1, school.getName(), "有新的校园公告", "notice",
					notificationMap);
		}
		if (notice.getPushTeacher()) {
			Map<String, Object> notificationMap = new HashMap<String, Object>();
			notificationMap.put("id", notice.getId());

			PushProducer.sendSchoolNotification(school.getId(), 0, school.getName(), "有新的校园公告", "notice",
					notificationMap);
		}
	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertCourseKnowledge(@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);

			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long userId = super.getAuthedId(rsessionid);

			System.out.println(map);
			System.out.println();

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("notice", file, "1", school.getId());
			System.out.println(list);

			NoticeService service = super.getService(NoticeService.class);
			int n = service.insertNotices(school.getId(), userId, del, list);
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
	public void update(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws IOException {
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		School school = super.getAuthedSchool(rsessionid);
		Map<String, PartField> map = super.partMulti(request);
		Notice notice = super.buildBean(Notice.class, map, id);

		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), school.getId() + "/notice/" + id
							+ "/" + i + "@l.png"));
					ImageUtils.zoom(fileList.get(i), new File(super.getFilePath(), school.getId() + "/notice/" + id
							+ "/" + i + ".png"), IMAGE_W, IMAGE_H);

				}
				notice.setPic((long) fileList.size());
			}
		}

		notice.setAuditTeacherId(teacher.getId());
		notice.setId(id);
		notice.setDescription(URLDecoder.decode(map.get("description").getValue(), "UTF-8"));
		notice.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		notice.setUpdateUserId(super.getAuthedId(rsessionid));

		super.getService(NoticeService.class).update(notice);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(NoticeService.class).delete(id);
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		new PicResource().delete(teacher.getSchoolId() + "/notice/" + id + "/0.png");
		super.getService(NoticeService.class).updateImage(id, teacher.getId(), 0);
	}

}

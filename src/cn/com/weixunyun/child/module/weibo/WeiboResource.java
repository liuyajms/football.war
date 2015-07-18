package cn.com.weixunyun.child.module.weibo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.ws.rs.core.*;

import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.module.point.PointService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.PushProducer;
import cn.com.weixunyun.child.util.ThrowableUtils;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/weibo")
@Produces(MediaType.APPLICATION_JSON)
@Description("微博")
public class WeiboResource extends AbstractResource {

	private static final int IMAGE_W = 200;
	private static final int IMAGE_H = 200;

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response exportClasses(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("keyword") Long keyword, final @QueryParam("classesId") Long classesId,
			final @QueryParam("studentId") Long studentId, final @QueryParam("teacherId") Long teacherId) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

				List<UserWeibo> list = getService(WeiboService.class).getList(0L, 1000L, school.getId(),
						getAuthedId(rsessionid), classesId, studentId, teacherId);

				List<Column<UserWeibo>> columnList = new ArrayList<ExcelUtils.Column<UserWeibo>>();
				columnList.add(new Column<UserWeibo>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(UserWeibo t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<UserWeibo>() {
					@Override
					public String getTitle() {
						return "内容";
					}

					@Override
					public String getValue(UserWeibo t) {
						return t.getDescription();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=weibo.xlsx").build();
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public UserWeibo get(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		return super.getService(WeiboService.class).get(id, super.getAuthedId(rsessionid));
	}

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("studentId") Long studentId, @QueryParam("teacherId") Long teacherId) {
		School school = super.getAuthedSchool(rsessionid);
		return super.getService(WeiboService.class).getListCount(school.getId(), super.getAuthedId(rsessionid),
				classesId, studentId, teacherId);
	}

	@GET
	@Description("列表")
	public List<UserWeibo> getList(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("studentId") Long studentId, @QueryParam("teacherId") Long teacherId) {
		School school = super.getAuthedSchool(rsessionid);
		return super.getService(WeiboService.class).getList(page * rows, rows, school.getId(),
				super.getAuthedId(rsessionid), classesId, studentId, teacherId);
	}

	@GET
	@Path("classes")
	@Description("班级微博列表")
	public List<UserWeibo> selectClassesWeiboList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("classesId") Long classesId) {
		School school = super.getAuthedSchool(rsessionid);
		return super.getService(WeiboService.class).getClassesWeiboList(page * rows, rows, school.getId(), classesId);
	}

	@GET
	@Path("classes/count")
	@Description("班级微博总数")
	public int selectClassesWeiboListCount(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") Long classesId) {
		School school = super.getAuthedSchool(rsessionid);
		return super.getService(WeiboService.class).getClassesWeiboListCount(school.getId(), classesId);
	}

	@GET
	@Path("school")
	@Description("学校微博列表")
	public List<UserWeibo> select(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);

		Long userId = super.getAuthedId(rsessionid);
		List<UserWeibo> list = super.getService(WeiboService.class).getSchoolWeiboList(page * rows, rows,
				school.getId(), userId, keyword);
		return list;
	}

	@GET
	@Path("school/count")
	@Description("学校微博总数")
	public int select(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);

		Long userId = super.getAuthedId(rsessionid);
		int total = super.getService(WeiboService.class).getSchoolWeiboListCount(school.getId(), userId, keyword);
		return total;
	}

	@GET
	@Path("rulesClasses")
	@Description("weibo表对应的导入字段约束列表")
	public List<Rule> selectClassesRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "weiboClasses");
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		School school = super.getAuthedSchool(rsessionid);
		Map<String, PartField> map = super.partMulti(request);
		Long id = (long) super.getService(SequenceService.class).sequence();
		Weibo weibo = super.buildBean(Weibo.class, map, id);
		if (map.get("classesId") == null || "0".equals(map.get("classesId").getValue())) {
			weibo.setClassesId(null);
		}
		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), school.getId() + "/weibo/" + id
							+ "/" + i + "@l.png"));
					ImageUtils.zoom(fileList.get(i), new File(super.getFilePath(), school.getId() + "/weibo/" + id
							+ "/" + i + ".png"), IMAGE_W, IMAGE_H);
				}
				weibo.setPic((long) fileList.size());
			}
		}

		weibo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		weibo.setCreateUserId(super.getAuthedId(rsessionid));
		weibo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		weibo.setUpdateUserId(super.getAuthedId(rsessionid));
		weibo.setSchoolId(school.getId());
		// 敏感词替换
		weibo.setDescription(SensitivewordFilter.replaceSensitiveWord(weibo.getDescription()));

		WeiboService service = super.getService(WeiboService.class);

		service.insert(weibo);

		PointService pService = super.getService(PointService.class);
		pService.insertPoint(super.getAuthedSchoolId(rsessionid), super.getAuthedId(rsessionid), "weibo", true);

		String des = weibo.getDescription();
		if (des.length() > 80) {
			des = des.substring(0, 80);
		}

		if (weibo.getClassesId() == null) {
			Map<String, Object> notificationMap = new HashMap<String, Object>();
			notificationMap.put("id", weibo.getId());
			PushProducer.sendClassesNotification(weibo.getSchoolId(), weibo.getClassesId(), school.getName(), "校园微博："
					+ des, "weibo", notificationMap);
		} else {
			Map<String, Object> notificationMap = new HashMap<String, Object>();
			notificationMap.put("id", weibo.getId());
			PushProducer.sendClassesNotification(weibo.getSchoolId(), weibo.getClassesId(), school.getName(), "班级微博："
					+ des, "weibo", notificationMap);
		}
	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertWeibos(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);

			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long userId = super.getAuthedId(rsessionid);
			Long classesId = null;

			PartField classesIdField = map.get("classesId");

			if (classesIdField != null && !classesIdField.isValueNull() && !"0".equals(classesIdField.getValue())) {
				classesId = Long.parseLong(classesIdField.getValue());
			}

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = null;
			if (classesId == null) {
				// list = new ExcelParser().redXlsx("weiboSchool", file, "1",
				// school.getId());
			} else {
				list = new ExcelParser().redXlsx("weiboClasses", file, "1", school.getId());
			}
			// System.out.println(list);

			WeiboService service = super.getService(WeiboService.class);
			int n = service.insertWeibos(school.getId(), classesId, userId, super.getCurrentTerm(school.getId()), del,
					list);
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

		Weibo weibo = super.buildBean(Weibo.class, map, id);

		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null && fileList.size() > 0) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), school.getId() + "/weibo/" + id
							+ "/" + i + "@l.png"));
					ImageUtils.zoom(fileList.get(i), new File(super.getFilePath(), school.getId() + "/weibo/" + id
							+ "/" + i + ".png"), IMAGE_W, IMAGE_H);
				}
				weibo.setPic((long) fileList.size());
			} else {
				Long pic = super.getService(WeiboService.class).get(id, super.getAuthedId(rsessionid)).getPic();
				weibo.setPic(pic);
			}
		}

		weibo.setAuditTeacherId(teacher.getId());
		weibo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		weibo.setUpdateUserId(super.getAuthedId(rsessionid));

		super.getService(WeiboService.class).update(weibo);
	}

	@DELETE
	@Path("{weiboId}")
	@Description("删除")
	public void delete(@PathParam("weiboId") Long id, @CookieParam("rsessionid") String rsessionid) {

		Weibo weibo = super.getService(WeiboService.class).get(id, null);

		Long createUserId = weibo.getCreateUserId();

		super.getService(WeiboService.class).delete(id);

		PointService pService = super.getService(PointService.class);
		pService.insertPoint(super.getAuthedSchoolId(rsessionid), createUserId, "weibo", false);
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		School school = super.getAuthedSchool(rsessionid);
		new PicResource().delete(school.getId() + "/weibo/" + id + "/0.png");
		WeiboService service = super.getService(WeiboService.class);
		super.getService(WeiboService.class).updateImage(service.get(id, super.getAuthedId(rsessionid)).getPic() - 1,
				id);
	}

	@POST
	@Path("{id}/zan")
	@Description("添加")
	public void insertZan(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) throws Exception {

		WeiboZan zan = new WeiboZan();
		zan.setWeiboId(id);
		zan.setUserId(super.getAuthedId(rsessionid));
		zan.setTime(new Timestamp(System.currentTimeMillis()));
		super.getService(WeiboService.class).insertZan(zan);

	}

	@DELETE
	@Path("{id}/zan")
	@Description("删除")
	public void deleteZan(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {
		super.getService(WeiboService.class).deleteZan(id, super.getAuthedId(rsessionid));
	}
}

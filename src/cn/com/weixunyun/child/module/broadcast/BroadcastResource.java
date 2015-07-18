package cn.com.weixunyun.child.module.broadcast;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.control.AbstractResource.PartField;
import cn.com.weixunyun.child.control.AbstractResource.PartFieldFile;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.module.point.PointService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.PushProducer;
import cn.com.weixunyun.child.util.ThrowableUtils;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

@Path("/broadcast")
@Produces(MediaType.APPLICATION_JSON)
@Description("广播")
public class BroadcastResource extends AbstractResource {

	private static final int IMAGE_W = 400;
	private static final int IMAGE_H = 400;

	@GET
	@Path("grade/export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("keyword") String keyword) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

				GlobalService globalService = getService(GlobalService.class);
				String term = globalService.select(getAuthedSchool(rsessionid).getId(), "term", "default").getValue();

				BroadcastService broadcastService = getService(BroadcastService.class);
				List<ClassesBroadcast> list = broadcastService.getGradeBroadcastList(0L, 1000L, school.getId(),
						keyword, term);

				List<Column<ClassesBroadcast>> columnList = new ArrayList<ExcelUtils.Column<ClassesBroadcast>>();
				/*
				 * columnList.add(new Column<ClassesBroadcast>() {
				 * 
				 * @Override public String getTitle() { return "学期"; }
				 * 
				 * @Override public String getValue(ClassesBroadcast t) { return
				 * t.getTerm().substring(0, 4)+ "学年" +
				 * ("1".equals(t.getTerm().substring(4, 5))==true?"下学期":"上学期");
				 * } });
				 */
				columnList.add(new Column<ClassesBroadcast>() {
					@Override
					public String getTitle() {
						return "广播年级";
					}

					@Override
					public String getValue(ClassesBroadcast t) {
						StringBuilder sb = new StringBuilder();
						int grade = t.getGrade();
						for (int i = 0; i < 8; i++) {
							if ((grade & (1 << i)) > 0) {
								if (sb.length() > 0) {
									sb.append("、");
								}
								sb.append(i + 1);
							}
						}
						return sb.toString() + "年级";
					}
				});
				columnList.add(new Column<ClassesBroadcast>() {
					@Override
					public String getTitle() {
						return "标题";
					}

					@Override
					public String getValue(ClassesBroadcast t) {
						return t.getTitle();
					}
				});
				columnList.add(new Column<ClassesBroadcast>() {
					@Override
					public String getTitle() {
						return "内容";
					}

					@Override
					public String getValue(ClassesBroadcast t) {
						return t.getDescription();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=broadcast_grade.xlsx").build();
	}

	@GET
	@Path("classes/export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response exportClasses(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("keyword") Long keyword) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

				GlobalService globalService = getService(GlobalService.class);
				String term = globalService.select(getAuthedSchool(rsessionid).getId(), "term", "default").getValue();

				BroadcastService broadcastService = getService(BroadcastService.class);
				List<ClassesBroadcast> list = broadcastService.getClassesBroadcast(0L, 1000L, school.getId(), keyword,
						term);

				List<Column<ClassesBroadcast>> columnList = new ArrayList<ExcelUtils.Column<ClassesBroadcast>>();
				/*
				 * columnList.add(new Column<ClassesBroadcast>() {
				 * 
				 * @Override public String getTitle() { return "学期"; }
				 * 
				 * @Override public String getValue(ClassesBroadcast t) { return
				 * t.getTerm().substring(0, 4)+ "学年" +
				 * ("1".equals(t.getTerm().substring(4, 5))==true?"下学期":"上学期");
				 * } });
				 */
				columnList.add(new Column<ClassesBroadcast>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesBroadcast t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<ClassesBroadcast>() {
					@Override
					public String getTitle() {
						return "标题";
					}

					@Override
					public String getValue(ClassesBroadcast t) {
						return t.getTitle();
					}
				});
				columnList.add(new Column<ClassesBroadcast>() {
					@Override
					public String getTitle() {
						return "内容";
					}

					@Override
					public String getValue(ClassesBroadcast t) {
						return t.getDescription();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=broadcast_classes.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesBroadcast> select(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId,
			@QueryParam("year") String year, @QueryParam("audit") Long audit) {
		School school = super.getAuthedSchool(rsessionid);

		if (year == null && classesId != null) {
			ClassesService classesService = super.getService(ClassesService.class);
			Classes classes = classesService.select(classesId);

			year = Long.toString(classes.getYear());
		}

		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();

		int grade = super.getGrade(school.getId(), Integer.parseInt(year));

		int total = super.getService(BroadcastService.class)
				.getListCount(school.getId(), grade, classesId, audit, term);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		List<ClassesBroadcast> list = super.getService(BroadcastService.class).getList(page * rows, rows,
				school.getId(), grade, classesId, term);
		map.put("rows", list);
		return list;
	}

	@GET
	@Path("classes")
	@Description("班级广播列表")
	public List<ClassesBroadcast> selectClasses(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") Long keyword) {
		School school = super.getAuthedSchool(rsessionid);
		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();
		BroadcastService broadcastService = super.getService(BroadcastService.class);
		return broadcastService.getClassesBroadcast(page * rows, rows, school.getId(), keyword, term);
	}

	@GET
	@Path("classes/count")
	@Description("班级广播总数")
	public int selectClassesCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") Long keyword) {
		School school = super.getAuthedSchool(rsessionid);
		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();
		BroadcastService broadcastService = super.getService(BroadcastService.class);
		return broadcastService.getClassesBroadcastCount(school.getId(), keyword, term);
	}

	@GET
	@Path("school")
	@Description("学校广播列表")
	public List<ClassesBroadcast> selectSchool(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);
		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();
		BroadcastService broadcastService = super.getService(BroadcastService.class);
		return broadcastService.getSchoolBroadcastList(page * rows, rows, school.getId(), keyword, term);
	}

	@GET
	@Path("school/count")
	@Description("学校广播总数")
	public int selectSchoolCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);
		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();
		BroadcastService broadcastService = super.getService(BroadcastService.class);
		return broadcastService.getSchoolBroadcastListCount(school.getId(), keyword, term);
	}

	@GET
	@Path("grade")
	@Description("年级广播列表")
	public List<ClassesBroadcast> getGradeBroadcastList(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);
		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();

		BroadcastService broadcastService = super.getService(BroadcastService.class);
		return broadcastService.getGradeBroadcastList(page * rows, rows, school.getId(), keyword, term);
	}

	@GET
	@Path("grade/count")
	@Description("年级广播总数")
	public int getGradeBroadcastListCount(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") String keyword) {
		School school = super.getAuthedSchool(rsessionid);
		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();

		BroadcastService broadcastService = super.getService(BroadcastService.class);
		return broadcastService.getGradeBroadcastListCount(school.getId(), keyword, term);
	}

	@GET
	@Path("rulesGrade")
	@Description("broadcast表对应的导入字段约束列表")
	public List<Rule> selectGradeRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "broadcastGrade");
	}

	@GET
	@Path("rulesClasses")
	@Description("broadcast表对应的导入字段约束列表")
	public List<Rule> selectClassesRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "broadcastClasses");
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Broadcast get(@PathParam("id") Long id) {
		return super.getService(BroadcastService.class).get(id);
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Map<String, PartField> map = super.partMulti(request);

		Teacher teacher = super.getAuthedTeacher(rsessionid);

		BroadcastService service = super.getService(BroadcastService.class);
		Broadcast broadcast = super.buildBean(Broadcast.class, map, null);
		broadcast.setSchoolId(teacher.getSchoolId());
		broadcast.setCreateTeacherId(teacher.getId());
		broadcast.setComment(true);

		// 敏感词替换
		// broadcast.setTitle(SensitivewordFilter.replaceSensitiveWord(broadcast.getTitle()));
		// if(broadcast.getDescription() != null){
		// broadcast.setDescription(
		// SensitivewordFilter.replaceSensitiveWord(broadcast.getDescription()));
		// }

		GlobalService globalService = super.getService(GlobalService.class);
		String term = globalService.select(super.getAuthedSchool(rsessionid).getId(), "term", "default").getValue();
		broadcast.setTerm(term);

		PartField classesIdField = map.get("classesId");

		if (classesIdField != null && !classesIdField.isValueNull() && !"0".equals(classesIdField.getValue())) {
			broadcast.setClassesId(Long.parseLong(classesIdField.getValue()));
		} else {
			broadcast.setClassesId(null);
		}

		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), teacher.getSchoolId()
							+ "/broadcast/" + broadcast.getId() + "/" + i + "@l.png"));
					ImageUtils.zoom(fileList.get(i), new File(super.getFilePath(), teacher.getSchoolId()
							+ "/broadcast/" + broadcast.getId() + "/" + i + ".png"), IMAGE_W, IMAGE_H);
				}
				broadcast.setPic((long) fileList.size());
			}
		}

		PartField voiceField = map.get("voice");
		if (voiceField != null) {
			PartFieldFile file = voiceField.getFile();
			if (file != null) {
				FileUtils.copyFile(file, new File(super.getFilePath(), teacher.getSchoolId() + "/broadcast/"
						+ broadcast.getId() + "/voice.amr"));
				broadcast.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
			}
		}

		service.insert(broadcast);

		PointService pService = super.getService(PointService.class);
		pService.insertPoint(teacher.getSchoolId(), teacher.getId(), "broadcast", true);

		String tit = broadcast.getTitle();
		if (tit.length() > 80) {
			tit = tit.substring(0, 80);
		}

		if (broadcast.getClassesId() != null) {
			Map<String, Object> notificationMap = new HashMap<String, Object>();
			notificationMap.put("id", broadcast.getId());
			PushProducer.sendClassesNotification(broadcast.getSchoolId(), broadcast.getClassesId(), super
					.getAuthedSchool(rsessionid).getName(), "班级广播：" + tit, "broadcast", notificationMap);
		} else {
			School school = super.getAuthedSchool(rsessionid);
			int grade = broadcast.getGrade();
			for (int i = 0; i < 8; i++) {
				if ((grade & (1 << i)) > 0) {
					int year = super.getClassesYear(school.getId(), i);
					ClassesService classesService = super.getService(ClassesService.class);
					List<Long> classesIdList = classesService.getYearClassesList(school.getId(), year);
					for (Long classesId : classesIdList) {
						Map<String, Object> notificationMap = new HashMap<String, Object>();
						notificationMap.put("id", broadcast.getId());
						PushProducer.sendClassesNotification(broadcast.getSchoolId(), classesId, school.getName(),
								"年级广播：" + tit, "broadcast", notificationMap);
					}
				}
			}

		}
		// }
	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertBroadcasts(@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
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
				list = new ExcelParser().redXlsx("broadcastGrade", file, "1", school.getId());
			} else {
				list = new ExcelParser().redXlsx("broadcastClasses", file, "1", school.getId());
			}
			// System.out.println(list);

			BroadcastService service = super.getService(BroadcastService.class);
			int n = service.insertBroadcasts(school.getId(), classesId, userId, super.getCurrentTerm(school.getId()),
					del, list);
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
			@PathParam("id") Long id) throws Exception {
		Map<String, PartField> map = super.partMulti(request);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		Broadcast broadcast = super.buildBean(Broadcast.class, map, id);
		broadcast.setUpdateTeacherId(teacher.getId());
		broadcast.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), teacher.getSchoolId()
							+ "/broadcast/" + broadcast.getId() + "/" + i + "@l.png"));
					ImageUtils.zoom(fileList.get(i), new File(super.getFilePath(), teacher.getSchoolId()
							+ "/broadcast/" + broadcast.getId() + "/" + i + ".png"), IMAGE_W, IMAGE_H);
				}
				broadcast.setPic((long) fileList.size());
			}
		}

		PartField voiceField = map.get("voice");
		if (voiceField != null) {
			PartFieldFile file = voiceField.getFile();
			if (file != null) {
				FileUtils.copyFile(file, new File(super.getFilePath(), teacher.getSchoolId() + "/broadcast/"
						+ broadcast.getId() + "/voice.amr"));
				broadcast.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
			}
		}
		super.getService(BroadcastService.class).update(broadcast);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {

		ClassesBroadcast cb = super.getService(BroadcastService.class).get(id);
		Long pid = cb.getCreateTeacherId();

		super.getService(BroadcastService.class).delete(id);

		PointService pService = super.getService(PointService.class);
		pService.insertPoint(super.getAuthedSchoolId(rsessionid), pid, "broadcast", false);
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		School school = super.getAuthedSchool(rsessionid);
		new PicResource().delete(school.getId() + "/broadcast/" + id + "/0.png");
		BroadcastService service = super.getService(BroadcastService.class);
		super.getService(BroadcastService.class).updateImage(service.get(id).getPic() - 1, id);
	}
}

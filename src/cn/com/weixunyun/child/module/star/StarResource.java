package cn.com.weixunyun.child.module.star;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Path("/star")
@Produces(MediaType.APPLICATION_JSON)
@Description("班级明星")
public class StarResource extends AbstractResource {

	private static final int W = 400;
	private static final int H = 400;

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("classesId") Long classesId) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				StarService service = getService(StarService.class);
				List<StarClasses> list = service.getList(0L, 1000L, school.getId(), classesId);

				List<Column<StarClasses>> columnList = new ArrayList<ExcelUtils.Column<StarClasses>>();
				columnList.add(new Column<StarClasses>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(StarClasses t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<StarClasses>() {
					@Override
					public String getTitle() {
						return "姓名";
					}

					@Override
					public String getValue(StarClasses t) {
						return t.getName();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=star.xlsx").build();
	}

	@GET
	@Path("count")
	@Description("总数")
	public int count(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId) {
		StarService service = super.getService(StarService.class);
		return service.getListCount(super.getAuthedSchool(rsessionid).getId(), classesId);
	}

	@GET
	@Description("列表")
	public List<StarClasses> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("classesId") Long classesId) {
		StarService service = super.getService(StarService.class);
		return service.getList(page * rows, rows, super.getAuthedSchool(rsessionid).getId(), classesId);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Star select(@PathParam("id") long id) {
		return super.getService(StarService.class).get(id);
	}

	@GET
	@Path("rule")
	@Description("star表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "star");
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, PartField> map = super.partMulti(request);

		Teacher teacher = super.getAuthedTeacher(rsessionid);

		Star star = super.buildBean(Star.class, map, null);

		File file = map.get("picture").getFile();
		if (file != null) {
			File targetFile = new File(super.getFilePath(), teacher.getSchoolId() + "/star/" + star.getId() + ".png");

			ImageUtils.zoom(file, targetFile, W, H);
		}

		star.setSchoolId(teacher.getSchoolId());
		star.setCreateTeacherId(teacher.getId());
		star.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		StarService service = super.getService(StarService.class);
		service.insert(star);
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

			System.out.println(map);
			System.out.println();

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("star", file, "1", school.getId());
			System.out.println(list);

			StarService service = super.getService(StarService.class);
			int n = service.insertStars(school.getId(), classesId, userId, del, list);
			return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@PUT
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改")
	public void update(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid,
			@QueryParam("id") Long id) throws Exception {
		Map<String, PartField> map = super.partMulti(request);

		Teacher teacher = super.getAuthedTeacher(rsessionid);

		Star star = super.buildBean(Star.class, map, id);
		star.setUpdateTeacherId(teacher.getId());
		star.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
		File file = map.get("picture").getFile();

		if (file != null) {
			File targetFile = new File(super.getFilePath(), teacher.getSchoolId() + "/star/" + star.getId() + ".png");

			ImageUtils.zoom(file, targetFile, W, H);
		}

		super.getService(StarService.class).update(star);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		School school = super.getAuthedSchool(rsessionid);
		new PicResource().delete(school.getId() + "/star/" + id + ".png");
		super.getService(StarService.class).delete(id);
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		new PicResource().delete(teacher.getSchoolId() + "/star/" + id + ".png");
		super.getService(StarService.class).updated(id, teacher.getId());
	}

}

package cn.com.weixunyun.child.module.curriculum;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
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

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/curriculum")
@Produces(MediaType.APPLICATION_JSON)
@Description("课程表")
public class CurriculumResource extends AbstractResource {

	private static final int W = 800;
	private static final int H = 800;

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			final @QueryParam("classesId") Long classesId, @QueryParam("type") final String type) {
		final School school = super.getAuthedSchool(rsessionid);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				String term = getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
				CurriculumService service = getService(CurriculumService.class);
				List<ClassesCurriculum> list = service.selectAll(0, 1000, type, classesId, school.getId(), term);

				List<Column<ClassesCurriculum>> columnList = new ArrayList<ExcelUtils.Column<ClassesCurriculum>>();
				columnList.add(new Column<ClassesCurriculum>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesCurriculum t) {
						return t.getClassName();
					}
				});
				columnList.add(new Column<ClassesCurriculum>() {
					@Override
					public String getTitle() {
						return "课表类型";
					}

					@Override
					public String getValue(ClassesCurriculum t) {
						return t.getTypeName();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=curriculum.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesCurriculum> selectAll(@QueryParam("page") int page, @QueryParam("rows") int rows,
			@QueryParam("classesId") Long classesId, @QueryParam("type") String type,
			@CookieParam("rsessionid") String rsessionid) {

		School school = getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		CurriculumService service = super.getService(CurriculumService.class);
		return service.selectAll(page * rows, rows, type, classesId, school.getId(), term);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAll(@QueryParam("classesId") Long classesId, @QueryParam("type") String type,
			@CookieParam("rsessionid") String rsessionid) {
		School school = getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		CurriculumService service = super.getService(CurriculumService.class);
		return service.selectAllCount(type, classesId, school.getId(), term);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Curriculum select(@PathParam("id") Long id) {
		return super.getService(CurriculumService.class).select(id);
	}

	@GET
	@Path("rule")
	@Description("curriculum表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid), "curriculum");
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Map<String, PartField> map = super.partMulti(request);
		System.out.println(map);

		Curriculum curriculum = super.buildBean(Curriculum.class, map, null);
		Teacher teacher = super.getAuthedTeacher(rsessionid);

		String term = super.getService(GlobalService.class).select(teacher.getSchoolId(), "term", "default").getValue();

		curriculum.setSchoolId(teacher.getSchoolId());
		curriculum.setCreateTeacherId(teacher.getId());
		curriculum.setUpdateTeacherId(teacher.getId());
		curriculum.setDefaultc(map.containsKey("defaultc"));
		curriculum.setTerm(term);

		// String description = URLDecoder.decode(form.getFirst("remark"),
		// "UTF-8");
		//
		// description = description.substring(0,
		// description.indexOf("<script type=\"text/javascript\">"));
		// contact.setDescription(description);

		CurriculumService service = super.getService(CurriculumService.class);
		service.insert(curriculum);

		File file = map.get("picture").getFile();

		if (file != null) {
			ImageUtils
					.zoom(file,
							new File(super.getFilePath(), teacher.getSchoolId() + "/curriculum/" + curriculum.getId()
									+ ".png"), W, H);
		}

	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertElectives(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);

			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long userId = super.getAuthedId(rsessionid);
			String term = super.getCurrentTerm(school.getId());
			Long classesId = Long.parseLong(map.get("classesId").getValue());

			System.out.println(map);
			System.out.println();

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("curriculum", file, "1", school.getId());
			System.out.println(list);

			CurriculumService service = super.getService(CurriculumService.class);
			int n = service.insertMulti(school.getId(), term, classesId, userId, del, list);
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

		Curriculum curriculum = super.buildBean(Curriculum.class, map, id);

		Teacher teacher = super.getAuthedTeacher(rsessionid);
		curriculum.setUpdateTeacherId(teacher.getId());
		curriculum.setDefaultc(map.containsKey("defaultc"));
		curriculum.setSchoolId(teacher.getSchoolId());

		super.getService(CurriculumService.class).update(curriculum);

		File file = map.get("picture").getFile();

		File targetFile = new File(super.getFilePath(), teacher.getSchoolId() + "/curriculum/" + curriculum.getId()
				+ ".png");

		if (file != null) {
			ImageUtils.zoom(file, targetFile, W, H);
		}
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@CookieParam("rsessionid") String rsessionid, @PathParam("id") int id) {
		School school = super.getAuthedSchool(rsessionid);
		new PicResource().delete(school.getId() + "/curriculum/" + id + ".png");
		super.getService(CurriculumService.class).delete(id);
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		School school = super.getAuthedSchool(rsessionid);
		new PicResource().delete(school.getId() + "/curriculum/" + id + ".png");
		super.getService(CurriculumService.class).updateImage(id);
	}

}

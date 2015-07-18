package cn.com.weixunyun.child.module.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Classes;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.ClassesService;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.module.point.PointService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;
import cn.com.weixunyun.child.control.AbstractResource.PartField;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/download")
@Produces(MediaType.APPLICATION_JSON)
@Description("下载")
public class DownloadResource extends AbstractResource {

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") final Long classesId) {
		final School school = super.getAuthedSchool(rsessionid);
		ClassesService cService = getService(ClassesService.class);
		Classes classes = cService.select(classesId);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				DownloadService service = getService(DownloadService.class);
				List<ClassesDownload> list = service.selectAll(0, 1000,
						school.getId(), classesId, 1L);

				List<Column<ClassesDownload>> columnList = new ArrayList<ExcelUtils.Column<ClassesDownload>>();
				columnList.add(new Column<ClassesDownload>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(ClassesDownload t) {
						return t.getClassesName();
					}
				});
				columnList.add(new Column<ClassesDownload>() {
					@Override
					public String getTitle() {
						return "文件名称";
					}

					@Override
					public String getValue(ClassesDownload t) {
						return t.getName();
					}
				});
				columnList.add(new Column<ClassesDownload>() {
					@Override
					public String getTitle() {
						return "置顶天数";
					}

					@Override
					public String getValue(ClassesDownload t) {
						return "0";
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response
				.ok(stream,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition",
						"attachment; filename=" + classes.getYear() + "_"
								+ classes.getNum() + "_download.xlsx").build();
	}

	@GET
	@Description("列表")
	public List<ClassesDownload> selectAll(
			@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") int page, @QueryParam("rows") int rows,
			@QueryParam("classesId") Long classesId,
			@QueryParam("flag") Long flag) {
		School school = super.getAuthedSchool(rsessionid);
		DownloadService service = super.getService(DownloadService.class);
		return service.selectAll(page * rows, rows, school.getId(), classesId, flag);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAllCount(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("classesId") Long classesId, @QueryParam("flag") Long flag) {
		School school = super.getAuthedSchool(rsessionid);
		DownloadService service = super.getService(DownloadService.class);
		return service.selectCount(school.getId(), classesId, flag);
	}

	@GET
	@Path("rule")
	@Description("download表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid),
				"download");
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid) throws IOException {
		try {
			Map<String, PartField> map = super.partMulti(request);
			Long id = (long) super.getService(SequenceService.class).sequence();
			Download d = new Download();
			PartFieldFile file = map.get("file").getFile();
			Teacher teacher = super.getAuthedTeacher(rsessionid);
			String fileName = map.get("file").getFile().getOriName();
			if (file != null) {
				FileUtils.copyFile(file,
						new File(super.getFilePath(), teacher.getSchoolId()
								+ "/download/" + id + "/" + fileName));
			}
			d.setNameFile(fileName);
			if (map.get("name") != null
					&& !"".equals(map.get("name").getValue())) {
				d.setName(map.get("name").getValue());
			}
			if (map.get("remark") != null
					&& !"".equals(map.get("remark").getValue())) {
				d.setDescription(map.get("remark").getValue());
			}
			d.setSize(file.getSize());
			d.setSchoolId(teacher.getSchoolId());
			d.setCreateTeacherId(teacher.getId());
			if (map.get("classesId") != null && !"".equals(map.get("classesId").getValue())) {
				d.setClassesId(Long.valueOf(map.get("classesId").getValue()));
			} else {
				d.setClassesId(null);
			}
			d.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
			d.setId(id);
			d.setContentType(file.getContentType());

			// 如果置顶天数不为空，设置值
			if (map.get("topDays") != null
					&& !"".equals(map.get("topDays").getValue())) {
				d.setTopDays(Long.parseLong(map.get("topDays").getValue()));
			} else {
				d.setTopDays(0L);
			}

			DownloadService service = super.getService(DownloadService.class);
			service.insert(d);

			PointService pService = super.getService(PointService.class);
			pService.insertPoint(super.getAuthedSchoolId(rsessionid),
					super.getAuthedId(rsessionid), "download", true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改")
	public void update(@Context HttpServletRequest request,  @CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws Exception {
		Map<String, PartField> formData = super.partMulti(request);
		Download d = super.buildBean(Download.class, formData, id);

		DownloadService service = super.getService(DownloadService.class);
		service.update(d);

	}
	
	
	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertCourseKnowledge(
			@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);

			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			Long classesId = Long.parseLong(map.get("classesId").getValue());
			Long userId = super.getAuthedId(rsessionid);

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx(
					"download", file, "1", school.getId());
			// System.out.println("导入：：："+list);

			DownloadService service = super.getService(DownloadService.class);
			int n = service.insertDownloads(school.getId(), classesId, userId,
					del, list);
			return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) {
		try {
			Teacher teacher = super.getAuthedTeacher(rsessionid);
			super.getService(DownloadService.class).delete(id);
			FileUtils.deleteDirectory(new File(super.getFilePath()
					+ teacher.getSchoolId() + "/download/" + id));

			PointService pService = super.getService(PointService.class);
			
			Download download = super.getService(DownloadService.class).select(id);
			
			Long pid = download.getCreateTeacherId();
			
			pService.insertPoint(super.getAuthedSchoolId(rsessionid),
					pid, "download", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public Download select(@PathParam("id") Long id) {
		
		return super.getService(DownloadService.class).select(id);
	}

	@GET
	@Path("down/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("详情")
	public void download(@Context HttpServletRequest request,
			@Context HttpServletResponse response, @PathParam("id") Long id)
			throws IOException {

		Download data = super.getService(DownloadService.class).select(id);
		String filename = getFilePath() + data.getSchoolId() + "/download/"
				+ id + "/" + data.getName();
		ServletOutputStream os = null;

		FileInputStream fis = null;

		try {
			File obj = new File(filename);

			if (obj.exists()) {

				os = response.getOutputStream();
				// response.setContentType("application/msword");

				// 判断浏览器
				String agent = request.getHeader("USER-AGENT");

				if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
					response.setHeader(
							"Content-Disposition",
							"attachment;filename="
									+ URLEncoder.encode(filename, "UTF-8"));
				} else {
					response.setHeader(
							"Content-Disposition",
							"attachment;filename="
									+ new String(data.getName().getBytes(
											"UTF-8"), "iso-8859-1"));
				}
				// response.setHeader("Content-Disposition",
				// "attachment;filename=" + newfilename);

				response.setContentType("application/octet-stream");// 定义输出类型

				fis = new FileInputStream(filename);

				byte[] bf = new byte[1024];

				int len = 0;

				while ((len = fis.read(bf, 0, 1024)) > 0) {

					os.write(bf, 0, len);

				}
			} else {
				response.setContentType("text/html;charset=GBK");
				PrintWriter out = response.getWriter();
				out.println("下载错误,文件不存在或文件名为空!");
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (fis != null) {
				fis.close();
			}
			if (os != null) {
				os.flush();
				os.close();
			}
		}
		// return super.getService(DownloadService.class).select(id);
	}

}

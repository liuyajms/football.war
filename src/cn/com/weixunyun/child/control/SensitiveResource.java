package cn.com.weixunyun.child.control;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wink.common.annotations.Workspace;

import com.google.gson.JsonArray;
import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.control.AbstractResource.DMLResponse;

import cn.com.weixunyun.child.model.bean.Sensitive;
import cn.com.weixunyun.child.model.bean.Rule;

import cn.com.weixunyun.child.model.pojo.School;

import cn.com.weixunyun.child.model.service.SensitiveService;

import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;

import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;
import cn.com.weixunyun.child.util.sensitive.SensitivewordFilter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/sensitive")
@Produces(MediaType.APPLICATION_JSON)
@Description("接口模块")
public class SensitiveResource extends AbstractResource {

	private final Logger logger = Logger.getLogger(SensitiveResource.class);

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@QueryParam("keyword") String keyword) {

		SensitiveService service = super.getService(SensitiveService.class);
		return service.getListCount(keyword);
	}

	@GET
	@Path("rule")
	@Description("sensitive表对应的导入字段约束列表")
	public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
		return new Rules().rules(super.getAuthedSchoolId(rsessionid),
				"sensitive");
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		Map<String, PartField> map = super.partMulti(request);

		Sensitive sensitive = super.buildBean(Sensitive.class, map, null);
		if (map.get("available") == null) {
			sensitive.setAvailable(false);
		} else {
			sensitive.setAvailable(true);
		}

		SensitiveService service = super.getService(SensitiveService.class);
		service.insert(sensitive);

	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("导入添加")
	public DMLResponse insertSensitive(@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid) throws Exception {
		try {
			System.out.println("------------imported-----------");
			School school = super.getAuthedSchool(rsessionid);
			Map<String, PartField> map = super.partMulti(request);
			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());
			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}
			List<Map<String, Object>> list = new ExcelParser().redXlsx(
					"sensitive", file, "1", school.getId());

			SensitiveService service = super.getService(SensitiveService.class);
			int n = service.insertSensitives(school.getId(), del, list);
			return new DMLResponse(true, Integer.toString(n));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") final String keyword,
			@QueryParam("classesId") final Long classesId) {

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				SensitiveService service = getService(SensitiveService.class);
				List<Sensitive> list = service.getList(0, 100000, keyword);
				List<Column<Sensitive>> columnList = new ArrayList<ExcelUtils.Column<Sensitive>>();
				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "主关键字";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字1";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName0();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字2";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName1();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字3";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName2();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字4";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName3();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字5";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName4();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字6";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName5();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字7";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName6();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "关键字8";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getName7();
					}
				});

				columnList.add(new Column<Sensitive>() {
					@Override
					public String getTitle() {
						return "是否启用";
					}

					@Override
					public String getValue(Sensitive t) {
						return t.getAvailable() == true ? "是" : "否";
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		return Response
				.ok(stream,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition",
						"attachment; filename=sensitive.xlsx").build();
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改")
	public void update(@Context HttpServletRequest request,
			@CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws Exception {

		Map<String, PartField> map = super.partMulti(request);
		Sensitive sensitive = super.buildBean(Sensitive.class, map, id);

		if (map.get("available") == null) {
			sensitive.setAvailable(false);
		} else {
			sensitive.setAvailable(true);
		}

		SensitiveService service = super.getService(SensitiveService.class);

		service.update(sensitive);

	}

	@GET
	@Path("{id}")
	@Description("查询")
	public Sensitive select(@PathParam("id") Long id) {
		return super.getService(SensitiveService.class).select(id);
	}

	@GET
	@Description("列表")
	public List<Sensitive> getList(@QueryParam("page") int page,
			@QueryParam("rows") int rows, @QueryParam("keyword") String keyword) {
		SensitiveService service = super.getService(SensitiveService.class);
		return service.getList(page * rows, rows, keyword);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(SensitiveService.class).delete(id);
	}

	@GET
	@Path("refresh")
	@Description("应用敏感词")
	public DMLResponse refresh() {
		try {
			String n = SensitivewordFilter.refreshSensitiveWordMap();

		
			return new DMLResponse(true, n);
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}

}

package cn.com.weixunyun.child.module.cook;

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

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Path("/cook")
@Produces(MediaType.APPLICATION_JSON)
@Description("食谱")
public class CookResource extends AbstractResource {
	
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid, final @QueryParam("keyword") String keyword) {
		final School school = super.getAuthedSchool(rsessionid);
		final String term = super.getCurrentTerm(school.getId());
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				CookService service = getService(CookService.class);
				List<Cook> list = service.getList(0, 1000, keyword, school.getId(), term);
				
				List<Column<Cook>> columnList = new ArrayList<ExcelUtils.Column<Cook>>();
				columnList.add(new Column<Cook>() {
					@Override
					public String getTitle() {
						return "食谱名称";
					}

					@Override
					public String getValue(Cook t) {
						return t.getName();
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=cook_" + term + ".xlsx").build();
	}

	@GET
	@Description("列表")
	public List<Cook> getList(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") int page,
			@QueryParam("rows") int rows, @QueryParam("keyword") String keyword) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		CookService service = super.getService(CookService.class);
		return service.getList(page*rows, rows, keyword, schoolId, term);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		String term = super.getCurrentTerm(schoolId);

		CookService service = super.getService(CookService.class);
		return service.getListCount(schoolId, keyword, term);
	}
	
	@GET
	@Path("{id}")
	@Description("详情")
	public Cook get(@PathParam("id") int id) {
		return super.getService(CookService.class).get(id);
	}
	
	@GET
    @Path("rule")
    @Description("cook表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "cook");
    }

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> form)
			throws Exception {
		Cook cook = super.buildBean(Cook.class, form, null);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		String term = super.getCurrentTerm(teacher.getSchoolId());

		cook.setSchoolId(teacher.getSchoolId());
		cook.setTerm(term);
		cook.setCreateTeacherId(teacher.getId());
		cook.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
		cook.setUpdateTeacherId(teacher.getId());
		cook.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		CookService service = super.getService(CookService.class);
		service.insert(cook);
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
			String term = super.getCurrentTerm(school.getId());
			
			System.out.println(map);
			System.out.println();
			
			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}
			
			List<Map<String, Object>> list = new ExcelParser().redXlsx("cook", file, "1", school.getId());
			System.out.println(list);
			
			CookService service = super.getService(CookService.class);
			int n = service.insertCooks(school.getId(), term, userId, del, list);
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
	public void update(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid,
			@PathParam("id") Long id) throws Exception {
		Cook contact = super.buildBean(Cook.class, formData, id);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		contact.setUpdateTeacherId(teacher.getId());
		contact.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		super.getService(CookService.class).update(contact);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") int id) {
		super.getService(CookService.class).delete(id);
	}
}

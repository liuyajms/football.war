package cn.com.weixunyun.child.module.security;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
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
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.ExcelUtils.Column;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/security")
@Produces(MediaType.APPLICATION_JSON)
@Description("安全监管")
public class SecurityResource extends AbstractResource {
	
	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description("导出")
	public Response export(final @CookieParam("rsessionid") String rsessionid) {
		final School school = super.getAuthedSchool(rsessionid);
		final String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				SecurityService service = getService(SecurityService.class);
				
				List<StudentSecurity> list = service.getTermSecurityList(0L, 10000L, school.getId(), term, null);

				List<Column<StudentSecurity>> columnList = new ArrayList<ExcelUtils.Column<StudentSecurity>>();
				columnList.add(new Column<StudentSecurity>() {
					@Override
					public String getTitle() {
						return "学期";
					}

					@Override
					public String getValue(StudentSecurity t) {
						if ("1".equals(t.getTerm().substring(4, 5))) {
							return t.getTerm().substring(0, 4) + "学年" + " 下学期";
						} else {
							return t.getTerm().substring(0, 4) + "学年" + " 上学期";
						}
					}
				});
				columnList.add(new Column<StudentSecurity>() {
					@Override
					public String getTitle() {
						return "学生";
					}

					@Override
					public String getValue(StudentSecurity t) {
						return t.getStudentName();
					}
				});
				columnList.add(new Column<StudentSecurity>() {
					@Override
					public String getTitle() {
						return "日期";
					}

					@Override
					public String getValue(StudentSecurity t) {
						return new SimpleDateFormat("yyyy-MM-dd").format(t.getDate());
					}
				});
				columnList.add(new Column<StudentSecurity>() {
					@Override
					public String getTitle() {
						return "迟到";
					}

					@Override
					public String getValue(StudentSecurity t) {
						return t.isReachOver()==true?"迟到":"";
					}
				});
				columnList.add(new Column<StudentSecurity>() {
					@Override
					public String getTitle() {
						return "早退";
					}

					@Override
					public String getValue(StudentSecurity t) {
						return t.isLeaveOver()==true?"早退":"";
					}
				});
				ExcelUtils.writeXlsx(output, list, columnList);
			}
		};
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=security.xlsx").build();
	}

	@GET
	@Path("{id}")
	public Security select(@PathParam("id") Long id) {
		return super.getService(SecurityService.class).select(id);
	}

	@GET
	@Description("安全监管列表")
	public List<StudentSecurity> selectAll(@QueryParam("page") Long page, @QueryParam("rows") Long rows,
			@CookieParam("rsessionid") String rsessionid, @QueryParam("studentId") Long studentId) {
		try {
			School school = super.getAuthedSchool(rsessionid);
			SecurityService service = super.getService(SecurityService.class);
			String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
			return service.selectAll(0L, 10000L, school.getId(), term, studentId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/count")
	@Description("总数")
	public int selectClassesCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("studentId") Long studentId) {
		SecurityService service = super.getService(SecurityService.class);
		School school = super.getAuthedSchool(rsessionid);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		return service.selectAllCount(school.getId(), term, studentId);
	}

	@GET
	@Path("/classes")
	@Description("本学期迟到早退记录")
	public List<StudentSecurity> getTermSecurityList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("classesId") Long classesId) {
		School school = super.getAuthedSchool(rsessionid);
		SecurityService service = super.getService(SecurityService.class);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		return service.getTermSecurityList(0L, 10000L, school.getId(), term, classesId);
	}
	
	@GET
	@Path("/classes/count")
	@Description("本学期迟到早退记录")
	public int getTermSecurityListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId) {
		School school = super.getAuthedSchool(rsessionid);
		SecurityService service = super.getService(SecurityService.class);
		String term = super.getService(GlobalService.class).select(school.getId(), "term", "default").getValue();
		return service.getTermSecurityListCount(school.getId(), term, classesId);
	}

	/*
	 * public void summary(List<Security> list) {
	 * 
	 * for (Security s : list) { SummarySecurity summary = new
	 * SummarySecurity();
	 * 
	 * 
	 * DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Date
	 * date = dateFormat.parse(); if (month == -1) { year = date.getYear();
	 * month = date.getMonth(); } int day = date.getDate(); int minute =
	 * date.getHours() * 100 + date.getMinutes(); int valid = isValid(minute);
	 * if (valid == -1) { continue; } }
	 * 
	 * for (Map.Entry<String, int[][]> entry : map.entrySet()) { ClockSummary
	 * summary = new ClockSummary(); //
	 * summary.setCode(entry.getKey().getCode()); //
	 * summary.setName(entry.getKey().getName());
	 * summary.setName(entry.getKey()); int[][] iss = entry.getValue(); int i =
	 * 0; for (int[] ss : iss) { Date d = new Date(year, month, i++); if
	 * (d.getMonth() != month || d.getDay() == 0 || d.getDay() == 6) { continue;
	 * }
	 * 
	 * if (ss[0] == 0) { summary.getMorningNullList().add(d); } else if (ss[0] >
	 * this.morning) { summary.getMorningList().add(d); } if (ss[1] == 0) {
	 * summary.getAfternoonNullList().add(d); } else if (ss[1] != 0 && ss[1] <
	 * this.afternoon) { summary.getAfternoonList().add(d); } }
	 * list.add(summary); } }
	 */

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Security security = super.buildBean(Security.class, formData, null);
		// 上学打卡：查看数据库中是否有当天上学的记录，如果没有则添加一条记录；如果有，则不添加

		//SecurityService service = super.getService(SecurityService.class);

		// 放学的时候，需要先查找当天有没有记录，若有上学的记录，则修改该条记录；若没有，则添加一条记录

		security.setSchoolId(super.getAuthedSchool(rsessionid).getId());
		security.setDate(new Date(System.currentTimeMillis()));
		security.setReachTime(new Timestamp(System.currentTimeMillis()));

		super.getService(SecurityService.class).insert(security);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Security Security = super.buildBean(Security.class, formData, null);
		Security.setSchoolId(super.getAuthedSchool(rsessionid).getId());
		super.getService(SecurityService.class).insert(Security);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@QueryParam("id") Long id) {
		super.getService(SecurityService.class).delete(id);
	}
}

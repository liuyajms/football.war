package cn.com.weixunyun.child.module.question;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.ClientResource;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("question")
@Produces(MediaType.APPLICATION_JSON)
@Description("留言咨询")
public class QuestionResource extends AbstractResource {

    @GET
    @Path("/count")
    @Description(value = "查询总数")
    public int count(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        QuestionService service = super.getService(QuestionService.class);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("schoolId", schoolId);
        params.put("keyword", keyword);
        return service.count(params);
    }

    @GET
    @Description("查询分页数据")
    public List<Question> list(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") int page,
                               @QueryParam("rows") int rows, @QueryParam("keyword") String keyword) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        QuestionService service = super.getService(QuestionService.class);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("schoolId", schoolId);
        params.put("keyword", keyword);
        params.put("offset", page * rows);
        params.put("rows", rows);
        return service.list(params);
    }

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public void insert(MultivaluedMap<String, String> formData, @CookieParam("code") String code, @CookieParam("rsessionid") String rsessionid)
            throws Exception {

        Question question = super.buildBean(Question.class, formData, null);
        question.setUpdateTime(null);
        String T = ClientResource.clients.get(code);
        if (T == null || !T.equals("web")) {
            question.setCreateUserId(super.getAuthedId(rsessionid));
        }
        //qusetion.setCreateUserId();
        QuestionService service = super.getService(QuestionService.class);
        service.insert(question);

    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改")
    public void update(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid,
                       @CookieParam("code") String code, @PathParam("id") Long id) throws Exception {

        Question question = super.buildBean(Question.class, formData, id);
        String T = ClientResource.clients.get(code);
        QuestionService service = super.getService(QuestionService.class);
        if (T == null || !T.equals("web")) {
            question.setCreateUserId(super.getAuthedTeacher(rsessionid).getId());
            service.answer(question);
        } else {
            service.update(question);
        }


    }

    @GET
    @Path("{id}")
    @Description("查询")
    public Question select(@PathParam("id") Long id) {
        return super.getService(QuestionService.class).select(id);
    }

    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id) {
        super.getService(QuestionService.class).delete(id);
    }

	/*@GET
    @Path("export")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description(value = "导出", mobile = false)
	public Response export(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") final String keyword) {
		final Long schoolId = super.getAuthedSchool(rsessionid).getId();

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				QuestionService service = getService(QuestionService.class);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("schoolId", schoolId);
				params.put("keyword", keyword);
				List<Question> list = service.list(params);

				List<ExcelUtils.Column<Question>> columnList = new ArrayList<ExcelUtils.Column<Question>>();

				columnList.add(new ExcelUtils.Column<Question>() {
					@Override
					public String getTitle() {
						return "学校";
					}

					@Override
					public String getValue(Question t) {
						return t.getSchool();
					}
				});
				columnList.add(new ExcelUtils.Column<Question>() {
					@Override
					public String getTitle() {
						return "年级";
					}

					@Override
					public String getValue(Question t) {
						return t.getGrade();
					}
				});
				columnList.add(new ExcelUtils.Column<Question>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(Question t) {
						return t.getClasses();
					}
				});
				columnList.add(new ExcelUtils.Column<Question>() {
					@Override
					public String getTitle() {
						return "姓名";
					}

					@Override
					public String getValue(Question t) {
						return t.getName();
					}
				});
				columnList.add(new ExcelUtils.Column<Question>() {
					@Override
					public String getTitle() {
						return "疫苗名称";
					}

					@Override
					public String getValue(Question t) {
						return t.getVaccine();
					}
				});
				columnList.add(new ExcelUtils.Column<Question>() {
					@Override
					public String getTitle() {
						return "创建时间";
					}

					@Override
					public String getValue(Question t) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 定义格式，不显示毫秒
						return df.format(t.getCreateTime());
					}

				});

				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		String file = "Question";
		try {
			file = java.net.URLEncoder.encode("查漏补种表", "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=" + file + ".xlsx").build();
	}

	@GET
	@Path("exportTemplate")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Description(value = "导出模板", mobile = false)
	public Response exportTemplate(@CookieParam("rsessionid") String rsessionid) {
		final Long schoolId = super.getAuthedSchool(rsessionid).getId();

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

				GapsService service = getService(GapsService.class);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("schoolId", schoolId);
				params.put("rows", 1);
				params.put("offset", 0);
				List<Gaps> list = service.list(params);

				List<ExcelUtils.Column<Gaps>> columnList = new ArrayList<ExcelUtils.Column<Gaps>>();

				columnList.add(new ExcelUtils.Column<Gaps>() {
					@Override
					public String getTitle() {
						return "学校";
					}

					@Override
					public String getValue(Gaps t) {
						return t.getSchool();
					}
				});
				columnList.add(new ExcelUtils.Column<Gaps>() {
					@Override
					public String getTitle() {
						return "年级";
					}

					@Override
					public String getValue(Gaps t) {
						return t.getGrade();
					}
				});
				columnList.add(new ExcelUtils.Column<Gaps>() {
					@Override
					public String getTitle() {
						return "班级";
					}

					@Override
					public String getValue(Gaps t) {
						return t.getClasses();
					}
				});
				columnList.add(new ExcelUtils.Column<Gaps>() {
					@Override
					public String getTitle() {
						return "姓名";
					}

					@Override
					public String getValue(Gaps t) {
						return t.getName();
					}
				});
				columnList.add(new ExcelUtils.Column<Gaps>() {
					@Override
					public String getTitle() {
						return "疫苗名称";
					}

					@Override
					public String getValue(Gaps t) {
						return t.getVaccine();
					}
				});

				ExcelUtils.writeXlsx(output, list, columnList);

			}
		};
		String file = "GapsTemplate";
		try {
			file = java.net.URLEncoder.encode("查漏补种模板", "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
				.header("Content-Disposition", "attachment; filename=" + file + ".xlsx").build();
	}

	@POST
	@Path("imported")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description(value = "导入添加", mobile = false)
	public DMLResponse insertMulti(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		try {

			Map<String, PartField> map = super.partMulti(request);

			PartFieldFile file = map.get("import").getFile();
			int del = Integer.parseInt(map.get("del").getValue());

			Long schoolId = super.getAuthedSchoolId(rsessionid);

			if (!file.getOriName().endsWith(".xlsx")) {
				return new DMLResponse(false, "请导入.xlsx文件！");
			}

			List<Map<String, Object>> list = new ExcelParser().redXlsx("Gaps", file, "1", schoolId);

			super.getService(GapsService.class).insertMulti(list, schoolId, del);

			return new DMLResponse(true, Integer.toString(list.size()));
		} catch (Exception e) {
			Throwable throwable = ThrowableUtils.getRootCause(e);
			return new DMLResponse(false, throwable.getMessage());
		}
	}*/

}

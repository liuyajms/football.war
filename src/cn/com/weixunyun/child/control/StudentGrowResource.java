package cn.com.weixunyun.child.control;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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

import org.apache.log4j.Logger;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.StudentGrow;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.StudentGrowService;
import cn.com.weixunyun.child.model.service.StudentService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/student/grow")
@Produces(MediaType.APPLICATION_JSON)
@Description("学生成长")
public class StudentGrowResource extends AbstractResource {

    private final static Logger logger = Logger.getLogger(StudentGrowResource.class);

    @GET
    @Path("/count")
    @Description("总数")
    public int getListCount(@CookieParam("rsessionid") String rsessionid,
                            @QueryParam("studentId") Long studentId,
                            @QueryParam("queryDate") String queryDate,
                            @QueryParam("keyword") String keyword) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        String term = super.getCurrentTerm(schoolId);
        StudentGrowService service = super.getService(StudentGrowService.class);
        return service.getListCount(schoolId, studentId, term, queryDate,
                keyword);
    }

    @GET
    @Description("查询分页数据")
    public List<StudentGrow> getList(
            @CookieParam("rsessionid") String rsessionid,
            @QueryParam("page") int page, @QueryParam("rows") int rows,
            @QueryParam("studentId") Long studentId,
            @QueryParam("queryDate") String queryDate,
            @QueryParam("keyword") String keyword) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        String term = super.getCurrentTerm(schoolId);
        StudentGrowService service = super.getService(StudentGrowService.class);
        return service.getList(page * rows, rows, schoolId, studentId, term,
                queryDate, keyword);
    }
    
    @GET
    @Path("rule")
    @Description("student_grow表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "student_grow");
    }

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public void insert(MultivaluedMap<String, String> formData,
                       @CookieParam("rsessionid") String rsessionid) throws IOException {
        String des = URLDecoder.decode(formData.getFirst("description"),
                "UTF-8");
        /*
		 * 去掉最后一个换行符
		 */
        String[] str = des.split("\n");
        String descip = "";
        for (int i = 0; i < str.length - 1; i++) {
            descip += str[i] + "<br/>";
        }
        descip += str[str.length - 1];

        Teacher teacher = super.getAuthedTeacher(rsessionid);

        Long schoolId = super.getAuthedSchoolId(rsessionid);

        StudentGrow StudentGrow = super.buildBean(StudentGrow.class, formData,
                null);
        StudentGrow.setDescription(descip);

        StudentGrow.setSchoolId(schoolId);
        StudentGrow.setCreateTeacherId(teacher.getId());
        StudentGrow.setUpdateTeacherId(teacher.getId());

        StudentGrow.setTerm(super.getCurrentTerm(schoolId));
        StudentGrow.setCreateTime(new java.sql.Timestamp(System
                .currentTimeMillis()));
        StudentGrow.setUpdateTime((new java.sql.Timestamp(System
                .currentTimeMillis())));

        StudentGrowService service = super.getService(StudentGrowService.class);
        service.insert(StudentGrow);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改")
    public void update(MultivaluedMap<String, String> formData,
                       @CookieParam("rsessionid") String rsessionid,
                       @PathParam("id") Long id) throws IOException {

        StudentGrow StudentGrow = super.buildBean(StudentGrow.class, formData,
                id);
        Teacher teacher = super.getAuthedTeacher(rsessionid);

        String des = URLDecoder.decode(formData.getFirst("description"),
                "UTF-8");
        String[] str = des.split("\n");
        String descip = "";
        for (int i = 0; i < str.length - 1; i++) {
            descip += str[i] + "<br/>";
        }
        descip += str[str.length - 1];
        StudentGrow.setDescription(descip);
        StudentGrow.setUpdateTeacherId(teacher.getId());
        StudentGrow.setUpdateTime((new java.sql.Timestamp(System
                .currentTimeMillis())));

        super.getService(StudentGrowService.class).update(StudentGrow);
    }

    @GET
    @Path("{id}")
    @Description("查询")
    public StudentGrow select(@PathParam("id") Long id) {
        return super.getService(StudentGrowService.class).select(id);
    }

    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id) {
        super.getService(StudentGrowService.class).delete(id);
    }

    @GET
    @Path("export")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("导出")
    public Response export(@CookieParam("rsessionid") String rsessionid,
                           @QueryParam("studentId") final Long studentId,
                           @QueryParam("queryDate") final String queryDate,
                           @QueryParam("keyword") final String keyword) {
        final Long schoolId = super.getAuthedSchool(rsessionid).getId();

        final String term = super.getCurrentTerm(schoolId);

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException,
                    WebApplicationException {
                // ElectiveService service = getService(ElectiveService.class);

                StudentGrowService service = getService(StudentGrowService.class);

                List<StudentGrow> list = service.getList(0, 9999, schoolId,
                        studentId, term, queryDate, keyword);

                List<Column<StudentGrow>> columnList = new ArrayList<ExcelUtils.Column<StudentGrow>>();
                columnList.add(new Column<StudentGrow>() {
                    @Override
                    public String getTitle() {
                        return "班级名称";
                    }

                    @Override
                    public String getValue(StudentGrow t) {
                        return t.getClassesName();
                    }
                });
                columnList.add(new Column<StudentGrow>() {
                    @Override
                    public String getTitle() {
                        return "学生姓名";
                    }

                    @Override
                    public String getValue(StudentGrow t) {
                        return t.getStudentName();
                    }
                });
                columnList.add(new Column<StudentGrow>() {
                    @Override
                    public String getTitle() {
                        return "成长名称";
                    }

                    @Override
                    public String getValue(StudentGrow t) {
                        return t.getName();
                    }
                });
                columnList.add(new Column<StudentGrow>() {
                    @Override
                    public String getTitle() {
                        return "成长类型";
                    }

                    @Override
                    public String getValue(StudentGrow t) {
                        // TODO Auto-generated method stub
                        return t.getTypeName();
                    }

                });
                columnList.add(new Column<StudentGrow>() {
                    @Override
                    public String getTitle() {
                        return "成长内容";
                    }

                    @Override
                    public String getValue(StudentGrow t) {
                        // TODO Auto-generated method stub
                        return t.getDescription();
                    }

                });
                columnList.add(new Column<StudentGrow>() {
                    @Override
                    public String getTitle() {
                        return "修改时间";
                    }

                    @Override
                    public String getValue(StudentGrow t) {
                        // TODO Auto-generated method stub
                        SimpleDateFormat df = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm");// 定义格式，不显示毫秒
                        // Timestamp now = new
                        // Timestamp(System.currentTimeMillis());//获取系统当前时间
                        String str = df.format(t.getUpdateTime());
                        return str;
                    }

                });

                ExcelUtils.writeXlsx(output, list, columnList);

            }
        };
        return Response
                .ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
                .header("Content-Disposition",
                        "attachment; filename=student_grow.xlsx").build();
    }


    @GET
    @Path("exportTemplate")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("导出定制模板")
    public Response exportTemplate(@CookieParam("rsessionid") String rsessionid,
                                   @QueryParam("classesId") final Long classesId,
                                   @QueryParam("type") final String type,
                                   @QueryParam("name") final String name
    ) {
        final Long schoolId = super.getAuthedSchool(rsessionid).getId();

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException,
                    WebApplicationException {

                StudentService service = getService(StudentService.class);
                List<ClassesStudent> list = service.getList(schoolId, 0L, 9999L, classesId, null);

                List<Column<ClassesStudent>> columnList = new ArrayList<ExcelUtils.Column<ClassesStudent>>();
                columnList.add(new Column<ClassesStudent>() {
                    @Override
                    public String getTitle() {
                        return "学生姓名";
                    }

                    @Override
                    public String getValue(ClassesStudent t) {
                        return t.getName();
                    }
                });
                columnList.add(new Column<ClassesStudent>() {
                    @Override
                    public String getTitle() {
                        return "学籍号";
                    }

                    @Override
                    public String getValue(ClassesStudent t) {
                        return t.getCode();
                    }
                });

                columnList.add(new Column<ClassesStudent>() {
                    @Override
                    public String getTitle() {
                        return "成长名称";
                    }

                    @Override
                    public String getValue(ClassesStudent t) {
                        // TODO Auto-generated method stub
                        return name;
                    }

                });
                columnList.add(new Column<ClassesStudent>() {
                    @Override
                    public String getTitle() {
                        return "成长类型";
                    }

                    @Override
                    public String getValue(ClassesStudent t) {
                        // TODO Auto-generated method stub
                        return type;
                    }

                });
                columnList.add(new Column<ClassesStudent>() {
                    @Override
                    public String getTitle() {
                        return "成长内容";
                    }

                    @Override
                    public String getValue(ClassesStudent t) {
                        // TODO Auto-generated method stub
                        return null;
                    }

                });

                ExcelUtils.writeXlsx(output, list, columnList);
            }
        };
        return Response
                .ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
                .header("Content-Disposition",
                        "attachment; filename=studentGrowTemplate.xlsx").build();
    }


    @POST
    @Path("imported")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("导入添加")
    public DMLResponse insertMulti(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        try {
            logger.info("------------imported-----------");

            Map<String, PartField> map = super.partMulti(request);

            PartFieldFile file = map.get("import_grow").getFile();

            Long classesId = Long.parseLong(map.get("classesId").getValue());

            String del_grow = "";
            if (map.get("del_grow") != null) {
                del_grow = map.get("del_grow").getValue();
            }


            Long schoolId = super.getAuthedSchoolId(rsessionid);
            Long teacherId = super.getAuthedTeacher(rsessionid).getId();
            String term = super.getCurrentTerm(schoolId);

            if (!file.getOriName().endsWith(".xlsx")) {
                return new DMLResponse(false, "请导入.xlsx文件！");
            }

            List<Map<String, Object>> list = new ExcelParser().redXlsx("studentGrow", file, "1", schoolId);
            logger.debug("return result ####:" + list);

            StudentGrowService service = super.getService(StudentGrowService.class);
            int isDelete = service.hasData(list.get(0));
            if (!"0".equals(isDelete)) {//删除当日数据
                service.deleteCurrentDay();
            }
            int n = service.insertMulti(schoolId, classesId, teacherId, term, list, del_grow);
            return new DMLResponse(true, Integer.toString(n));
        } catch (Exception e) {
            Throwable throwable = ThrowableUtils.getRootCause(e);
            return new DMLResponse(false, throwable.getMessage());
        }
    }

}

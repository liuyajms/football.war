package cn.com.weixunyun.child.module.course.score;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.log4j.Logger;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.StudentService;
import cn.com.weixunyun.child.util.ExcelParser;
import cn.com.weixunyun.child.util.ExcelUtils;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ExcelUtils.Column;
import cn.com.weixunyun.child.util.ThrowableUtils;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/course/score")
@Produces(MediaType.APPLICATION_JSON)
@Description("成绩册")
public class CourseScoreResource extends AbstractResource {

    private final Logger logger = Logger.getLogger(CourseScoreResource.class);

    @GET
    @Path("/count")
    @Description("总数")
    public int getListCount(@CookieParam("rsessionid") String rsessionid,
                            @QueryParam("courseId") Long courseId,
                            @QueryParam("classesId") Long classesId,
                            @QueryParam("queryDate") String queryDate,
                            @QueryParam("keyword") String keyword) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        CourseScoreService service = super.getService(CourseScoreService.class);
        String term = super.getCurrentTerm(schoolId);
        return service.getListCount(schoolId, courseId, classesId, term,
                queryDate, keyword);
    }

    @GET
    @Description("查询分页数据")
    public List<CourseScore> getList(
            @CookieParam("rsessionid") String rsessionid,
            @QueryParam("page") int page, @QueryParam("rows") int rows,
            @QueryParam("courseId") Long courseId,
            @QueryParam("classesId") Long classesId,
            @QueryParam("queryDate") String queryDate,
            @QueryParam("keyword") String keyword) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        String term = super.getCurrentTerm(schoolId);
        CourseScoreService service = super.getService(CourseScoreService.class);
        List<CourseScore> list =  service.getList(page * rows, rows, schoolId, courseId,
                classesId, term, queryDate, keyword);
        
        if(term.endsWith("0")){
            term = term.substring(0,4) + "年上学期";
        } else {
            term = term.substring(0,4) + "年下学期";
        }
        for(CourseScore c: list){
        	c.setTerm(term);
        }
        return list;
    }

    
    @GET
    @Path("/student")
    @Description("获取当前学校、某班级学生、当前学期的课程成绩")
    public List<CourseScore> getStudentCourseList(@CookieParam("rsessionid") String rsessionid,
                            @QueryParam("studentId") Long studentId, @QueryParam("courseId") Long courseId
                         ) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        CourseScoreService service = super.getService(CourseScoreService.class);
        String term = super.getCurrentTerm(schoolId);
       
        return service.getStudentCourseList(schoolId, term, studentId, courseId);
    }
    
    
    @GET
    @Path("/teacher")
    @Description("获取登陆教师所教某课程成绩列表")
    public List<CourseScore> getTeacherCourseList(@CookieParam("rsessionid") String rsessionid,
                            @QueryParam("classesId") Long classesId,
                            @QueryParam("courseId") Long courseId
                         ) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        Long teacherId = super.getAuthedTeacher(rsessionid).getId();
        
        CourseScoreService service = super.getService(CourseScoreService.class);
        String term = super.getCurrentTerm(schoolId);
       
        return service.getTeacherCourseList(schoolId, term, classesId, courseId,teacherId);
    }
    
    
    
    @GET
    @Path("/currentInfo")
    @Description("获取当前班级、课程、学期等初始信息")
    public CourseScore getCurrentInfo(@CookieParam("rsessionid") String rsessionid,
                            @QueryParam("courseId") Long courseId,
                            @QueryParam("classesId") Long classesId
                         ) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        CourseScoreService service = super.getService(CourseScoreService.class);
        String term = super.getCurrentTerm(schoolId);
        if(term.endsWith("0")){
            term = term.substring(0,4) + "年上学期";
        } else {
            term = term.substring(0,4) + "年下学期";
        }
        return service.getCurrentInfo(term,classesId,courseId);
    }
    
    @GET
    @Path("rule")
    @Description("course_score表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "course_score");
    }


    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public void insert(MultivaluedMap<String, String> formData,
                       @CookieParam("rsessionid") String rsessionid) throws IOException {

        logger.debug("------------insert-----------");
        logger.debug(formData);

        Teacher teacher = super.getAuthedTeacher(rsessionid);

        Long schoolId = super.getAuthedSchoolId(rsessionid);

        String dateStr = URLDecoder.decode(formData.getFirst("date"), "UTF-8");

        logger.debug("########" + dateStr);

        CourseScore courseScore = super.buildBean(CourseScore.class, formData,
                null);

        courseScore.setSchoolId(schoolId);
        courseScore.setCreateTeacherId(teacher.getId());
        courseScore.setUpdateTeacherId(teacher.getId());
        if (!"".equals(dateStr) && !" ".equals(dateStr)) {
            courseScore.setDate(stringToDate(dateStr, "yyyy-MM-dd"));
        }
        courseScore.setTerm(super.getCurrentTerm(schoolId));
        courseScore.setCreateTime(new java.sql.Timestamp(System
                .currentTimeMillis()));
        courseScore.setUpdateTime((new java.sql.Timestamp(System
                .currentTimeMillis())));
        CourseScoreService service = super.getService(CourseScoreService.class);
        service.insert(courseScore);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改")
    public void update(MultivaluedMap<String, String> formData,
                       @CookieParam("rsessionid") String rsessionid,
                       @PathParam("id") Long id) throws IOException {
        Teacher teacher = super.getAuthedTeacher(rsessionid);
        CourseScore courseScore = super.buildBean(CourseScore.class, formData,
                id);

        String date = URLDecoder.decode(formData.getFirst("date"), "UTF-8");

        if (!"".equals(date) && !" ".equals(date)) {
            courseScore.setDate(stringToDate(date, "yyyy-MM-dd"));
        }
        courseScore.setSchoolId(teacher.getSchoolId());
        courseScore.setUpdateTeacherId(teacher.getId());
        courseScore.setUpdateTime((new java.sql.Timestamp(System
                .currentTimeMillis())));

        CourseScoreService service = super.getService(CourseScoreService.class);

        service.update(courseScore);
    }

    @GET
    @Path("{id}")
    @Description("查询")
    public CourseScore select(@PathParam("id") Long id) {
        return super.getService(CourseScoreService.class).select(id);
    }

    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id) {
        super.getService(CourseScoreService.class).delete(id);
    }


    @GET
    @Path("export")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("导出")
    public Response export(@CookieParam("rsessionid") String rsessionid,
                           @QueryParam("courseId") final Long courseId,
                           @QueryParam("classesId") final Long classesId,
                           @QueryParam("queryDate") final String queryDate,
                           @QueryParam("keyword") final String keyword) {
        final Long schoolId = super.getAuthedSchool(rsessionid).getId();

        final String term = super.getCurrentTerm(schoolId);
        
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException,
                    WebApplicationException {
                // ElectiveService service = getService(ElectiveService.class);

                CourseScoreService service = getService(CourseScoreService.class);

                List<CourseScore> list = service.getList(0, 999, schoolId,
                        courseId, classesId, term, queryDate, keyword);

                List<Column<CourseScore>> columnList = new ArrayList<ExcelUtils.Column<CourseScore>>();
                
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "学期";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                    	 if(term.endsWith("0")){
                             return term.substring(0,4) + "年上学期";
                         } else {
                             return term.substring(0,4) + "年下学期";
                         }
                    }
                });
                
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "班级名称";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                        return t.getClassesName();
                    }
                });
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "课程名称";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                        return t.getCourseName();
                    }
                });
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "学生姓名";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                        return t.getStudentName();
                    }
                });
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "考试类型";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                        // TODO Auto-generated method stub
                        return t.getTypeName();
                    }

                });
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "考试时间";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                        // TODO Auto-generated method stub
                        return dateFormat(t.getDate(), "yyyy-MM-dd");
                    }

                });
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "考试分数";
                    }

                    @Override
                    public String getValue(CourseScore t) {
                        // TODO Auto-generated method stub
                        return t.getScore() + "";
                    }

                });
                columnList.add(new Column<CourseScore>() {
                    @Override
                    public String getTitle() {
                        return "修改时间";
                    }

                    @Override
                    public String getValue(CourseScore t) {
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
                .ok(stream, "application/vnd.ms-excel;charset=utf-8")
                .header("Content-Disposition",
                        "attachment; filename=course_score.xlsx").build();
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

            PartFieldFile file = map.get("import").getFile();

            Long courseId = Long.parseLong(map.get("courseId").getValue());

            Long schoolId = super.getAuthedSchoolId(rsessionid);
            Long teacherId = super.getAuthedTeacher(rsessionid).getId();
            String term = super.getCurrentTerm(schoolId);

            if (!file.getOriName().endsWith(".xlsx")) {
                return new DMLResponse(false, "请导入.xlsx文件！");
            }

            List<Map<String, Object>> list = new ExcelParser().redXlsx("courseScore", file, "1", schoolId);
            logger.debug("return result ####:" + list);

            CourseScoreService service = super.getService(CourseScoreService.class);

            int isDelete = service.hasData(list.get(0));

            if(!"0".equals(isDelete)){//删除当日数据
                service.deleteCurrentDay();
            }
            int n = service.insertMulti(schoolId, courseId, teacherId, term, list);

            return new DMLResponse(true, Integer.toString(n));
        } catch (Exception e) {
            Throwable throwable = ThrowableUtils.getRootCause(e);
            return new DMLResponse(false, throwable.getMessage());
        }
    }


    @GET
    @Path("exportTemplate")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("导出")
    public Response exportTemplate(@CookieParam("rsessionid") String rsessionid,
                           @QueryParam("classesId") final Long classesId,
                           @QueryParam("type") final String type,
                           @QueryParam("date") final String date
                        ) {
        final Long schoolId = super.getAuthedSchool(rsessionid).getId();

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException,
                    WebApplicationException {

                StudentService service = getService(StudentService.class);
                List<ClassesStudent> list = service.getList(schoolId,0L,9999L,classesId,null);


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
                        return "考试类型";
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
                        return "考试时间";
                    }

                    @Override
                    public String getValue(ClassesStudent t) {
                        // TODO Auto-generated method stub
                        return date;
                    }

                });
                columnList.add(new Column<ClassesStudent>() {
                    @Override
                    public String getTitle() {
                        return "考试分数";
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
                .ok(stream, "application/vnd.ms-excel;charset=utf-8")
                .header("Content-Disposition",
                        "attachment; filename=courseScoreTemplate.xlsx").build();
    }

    @GET
    @Path("downTemplate")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("下載模板")
    public void downTemplate(@Context HttpServletRequest request,
                               @Context HttpServletResponse response,
                               @QueryParam("fileName") String fileName
    ) throws IOException {
        // 服务器相对路径
        String filename = request.getSession().getServletContext().getRealPath("/template") + File.separator
                + fileName;

//        System.out.println("######filename：" + filename);
        // System.out.println("文件下载路径：" + fullname);
        // 读取文件名：用于设置客户端保存时指定默认文件名
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
                    response.setHeader("Content-Disposition",
                            "attachment;filename="
                                    + URLEncoder.encode(filename, "UTF-8"));
                } else {
                    response.setHeader("Content-Disposition",
                            "attachment;filename="
                                    + new String(fileName.getBytes("UTF-8"),
                                    "iso-8859-1"));
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
                // System.out.println("cannot find the file!");
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
    }

    /**
     * 字符串转换时间函数
     *
     * @param dateStr 时间字符串
     * @param format  格式： "yyyy-MM-dd HH:mm:ss "
     * @return
     */
    private Date stringToDate(String dateStr, String format) {
        Date date = null;
        if (format != null && !"".equals(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return date;
    }


    /**
     * 字符串转换时间函数
     *
     * @param date   时间
     * @param format 格式： "yyyy-MM-dd HH:mm:ss "
     * @return
     */
    private String dateFormat(Date date, String format) {
        String dateStr = null;
        if (format != null && !"".equals(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }
}

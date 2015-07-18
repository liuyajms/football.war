package cn.com.weixunyun.child.module.news;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
import cn.com.weixunyun.child.util.PushProducer;
import cn.com.weixunyun.child.util.Rules;
import cn.com.weixunyun.child.util.ThrowableUtils;
import org.apache.commons.io.FileUtils;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
@Description("新闻")
public class NewsResource extends AbstractResource {

    private static final int W = 400;
    private static final int H = 400;

    @GET
    @Path("count")
    @Description("总数")
    public int count(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
                     @QueryParam("type") Long type, @QueryParam("pic") Boolean pic) {
        NewsService service = super.getService(NewsService.class);
        return service.queryListCount(super.getAuthedSchool(rsessionid).getId(), keyword, type, pic);
    }

    @GET
    @Description("列表")
    public List<News> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
                                @QueryParam("rows") Long rows, @QueryParam("keyword") String keyword, @QueryParam("type") Long type,
                                @QueryParam("pic") Boolean pic) {
        NewsService service = super.getService(NewsService.class);
        return service.queryList(super.getAuthedSchool(rsessionid).getId(), page * rows, rows, keyword, type, pic);
    }

    @GET
    @Path("export")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("导出")
    public Response export(final @CookieParam("rsessionid") String rsessionid,
                           final @QueryParam("keyword") String keyword, final @QueryParam("type") Long type,
                           final @QueryParam("pic") Boolean pic) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                NewsService service = getService(NewsService.class);
                List<News> newsList = service.queryList(getAuthedSchool(rsessionid).getId(), 0L, -1L, keyword, type,
                        pic);

                List<Column<News>> columnList = new ArrayList<ExcelUtils.Column<News>>();
                columnList.add(new Column<News>() {
                    @Override
                    public String getTitle() {
                        return "标题";
                    }

                    @Override
                    public String getValue(News t) {
                        return t.getTitle();
                    }
                });
                columnList.add(new Column<News>() {
                    @Override
                    public String getTitle() {
                        return "内容摘要";
                    }

                    @Override
                    public String getValue(News t) {
                        return t.getDescriptionSummary();
                    }
                });
				/*
				 * columnList.add(new Column<News>() {
				 * 
				 * @Override public String getTitle() { return "时间"; }
				 * 
				 * @Override public String getValue(News t) { return
				 * t.getUpdateTime() == null ? "" :
				 * t.getUpdateTime().toString(); } });
				 */
                ExcelUtils.writeXlsx(output, newsList, columnList);

            }
        };
        return Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
                .header("Content-Disposition", "attachment; filename=news.xlsx").build();
    }

    @GET
    @Path("{id}")
    @Description("详情")
    public News select(@PathParam("id") long id) {
        return super.getService(NewsService.class).select(id);
    }

    @GET
    @Path("rule")
    @Description("news表对应的导入字段约束列表")
    public List<Rule> getRules(@CookieParam("rsessionid") String rsessionid) {
        return new Rules().rules(super.getAuthedSchoolId(rsessionid), "news");
    }

    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Description("添加")
    public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Teacher teacher = super.getAuthedTeacher(rsessionid);

        Map<String, PartField> map = super.partMulti(request);

        News news = super.buildBean(News.class, map, null);
        if (news != null) {
            File file = map.get("picture").getFile();

            String fileName = teacher.getSchoolId() + "/news/" + news.getId()
                    + ".png";
            if (file != null) {
                ImageUtils.zoom(file, new File(super.getFilePath(), fileName), W, H);
                news.setPic(true);
            }else{//设置默认图片,读取文件系统，随机选择图片
                File dir = new File(super.getFilePath(),teacher.getSchoolId() + "/news/default");
                if(dir.exists() && dir.list().length !=0){
                    File[] imgArray = dir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            if(pathname.isFile() && !pathname.isHidden()){
                                return true;
                            }
                            return false;
                        }
                    });
                    if(imgArray.length > 0 ){
                        int n = new Random().nextInt(imgArray.length);
                        FileUtils.copyFile(imgArray[n], new File(super.getFilePath(), fileName));
                        news.setPic(true);
                    }else {
                        news.setPic(false);
                    }

                }
            }


            //String desc = URLDecoder.decode(map.get("description").getValue(), "UTF-8");
            String desc = map.get("description").getValue();

            if (desc != null) {
                desc = desc.replace("<img", "<img style='width:100%;'");
                news.setDescription(desc);
            }

            news.setSchoolId(teacher.getSchoolId());
            news.setUp(map.get("up") != null && map.get("up").getValue() != null);
            news.setComment(map.get("comment") != null && map.get("comment").getValue() != null);
            news.setCreateTeacherId(teacher.getId());
            news.setUpdateTeacherId(teacher.getId());
            news.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

            NewsService service = super.getService(NewsService.class);
            service.insert(news);

            Map<String, Object> notificationMap = new HashMap<String, Object>();
            notificationMap.put("id", news.getId());
            if (news.getType().intValue() == 1) {
                PushProducer.sendSchoolNotification(
                        teacher.getSchoolId(),
                        super.getAuthedSchool(rsessionid).getName(),
                        new String[] { "学校概况", "学校新闻", "教学新闻", "学校师资" }[news.getType().intValue()] + "："
                                + news.getTitle(), "news" + news.getType(), notificationMap);
            }
        }
    }

    @POST
    @Path("imported")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Description("导入添加")
    public DMLResponse insertNews(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        try {
            System.out.println("------------imported-----------");
            School school = super.getAuthedSchool(rsessionid);

            Map<String, PartField> map = super.partMulti(request);
            PartFieldFile file = map.get("import").getFile();
            int del = Integer.parseInt(map.get("del").getValue());
            Long userId = super.getAuthedId(rsessionid);
            Long type = Long.parseLong(map.get("type").getValue());

            System.out.println(map);
            System.out.println();

            if (!file.getOriName().endsWith(".xlsx")) {
                return new DMLResponse(false, "请导入.xlsx文件！");
            }

            List<Map<String, Object>> list = new ExcelParser().redXlsx("news", file, "1", school.getId());
            System.out.println(list);

            NewsService service = super.getService(NewsService.class);
            int n = service.insertNews(school.getId(), type, userId, del, list);
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

        Teacher teacher = super.getAuthedTeacher(rsessionid);

        Map<String, PartField> map = super.partMulti(request);

        // 反射方法调用赋值
        News news = super.buildBean(News.class, map, id);
        news.setSchoolId(teacher.getSchoolId());
        news.setUpdateTeacherId(teacher.getId());
        news.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        File file = map.get("picture").getFile();

        File targetFile = new File(super.getFilePath(), teacher.getSchoolId() + "/news/" + news.getId() + ".png");

        if (file != null) {
            ImageUtils.zoom(file, targetFile, W, H);
        }
        news.setPic(targetFile.exists());

        //String desc = URLDecoder.decode(map.get("description").getValue(), "UTF-8");
        String desc = map.get("description").getValue();

        if (desc != null) {
            desc = desc.replace("<img style=\"width:100%;\"", "<img");
            desc = desc.replace("<img", "<img style=\"width:100%;\"");
            news.setDescription(desc);
        }

        super.getService(NewsService.class).update(news);
    }

    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
        School school = super.getAuthedSchool(rsessionid);
        new PicResource().delete(school.getId() + "/news/" + id + ".png");
        super.getService(NewsService.class).delete(id);
    }

    @DELETE
    @Path("{id}/image")
    @Description("删除图片")
    public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) throws Exception{
        Long schoolId = super.getAuthedSchoolId(rsessionid);
//		new PicResource().delete(school.getId() + "/news/" + id + ".png");
//		super.getService(NewsService.class).updateImage(id, false);
//        设置默认图片
        String fileName = schoolId + "/news/" + id
                + ".png";
        File dir = new File(super.getFilePath(),schoolId + "/news/default");

        if(dir.exists() && dir.list().length !=0){
            File[] imgArray = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.isFile() && !pathname.isHidden()){
                        return true;
                    }
                    return false;
                }
            });
            if(imgArray.length > 0 ){
                int n = new Random().nextInt(imgArray.length);
                FileUtils.copyFile(imgArray[n], new File(super.getFilePath(), fileName));
            }

        }

    }

}

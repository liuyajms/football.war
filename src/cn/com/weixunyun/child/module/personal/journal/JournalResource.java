package cn.com.weixunyun.child.module.personal.journal;

import java.io.File;
import java.io.IOException;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.model.service.SequenceService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/journal")
@Produces(MediaType.APPLICATION_JSON)
@Description("个人日志")
public class JournalResource extends AbstractResource {

    private final Logger logger = Logger.getLogger(JournalResource.class);

    @GET
    @Path("/count")
    @Description("总数")
    public int getListCount(@CookieParam("rsessionid") String rsessionid,
                            @QueryParam("keyword") String keyword, @QueryParam("userId") Long userId) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        
        if (userId == null) {
        	userId = super.getAuthedId(rsessionid);
		}
        
        JournalService service = super.getService(JournalService.class);
        return service.getListCount(schoolId, userId, keyword);
    }

    @GET
    @Description("查询分页数据")
    public List<UserJournal> getList(
            @CookieParam("rsessionid") String rsessionid,
            @QueryParam("page") int page, @QueryParam("rows") int rows,
            @QueryParam("keyword") String keyword, @QueryParam("userId") Long userId) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        if (userId == null) {
        	userId = super.getAuthedId(rsessionid);
		}
        JournalService service = super.getService(JournalService.class);
        return service.getList(rows* page, rows,schoolId, userId, keyword);
    }


    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Description("添加日志")
    public void insert(@Context HttpServletRequest request,
                       @CookieParam("rsessionid") String rsessionid) throws IOException {

        logger.debug("------------insert-----------");
        Long userId = super.getAuthedId(rsessionid);
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        Map<String, PartField> map = super.partMulti(request);
        Long id = super.getService(SequenceService.class).sequence();
        Journal journal = super.buildBean(Journal.class, map,id);
        PartField picField = map.get("pic");

        if (picField != null) {
            List<PartFieldFile> fileList = picField.getFileList();
            if (fileList != null) {
                for (int i = 0; i < fileList.size(); i++) {
//                	super.getFilePath()
                    FileUtils.copyFile(fileList.get(i), new File( super.getFilePath(), schoolId + "/personal/journal/" + id
                            + "/" + i + ".png" ));
                }
                journal.setPic((long) fileList.size());
            }
        }

        PartField voiceField = map.get("voice");
        
        if (voiceField != null) {
            PartFieldFile file = voiceField.getFile();
            if (file != null) {
                FileUtils.copyFile(file, new File(super.getFilePath(), schoolId + "/personal/journal/" + journal.getId()
                        + "/voice.amr"));
                journal.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
            }
        }

        journal.setSchoolId(schoolId);
        journal.setCreateUserId(userId);
        journal.setUpdateUserId(userId);
        journal.setCreateTime(new java.sql.Timestamp(System
                .currentTimeMillis()));
        journal.setUpdateTime(new java.sql.Timestamp(System
                .currentTimeMillis()));

        JournalService service = super.getService(JournalService.class);
        service.insert(journal);
    }


    @PUT
    @Path("{id}")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Description("修改")
    public void update(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid,
                       @PathParam("id") Long id) throws IOException {

        Long userId = super.getAuthedId(rsessionid);
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        Map<String, PartField> map = super.partMulti(request);
        Journal journal = super.buildBean(Journal.class, map, id);

        PartField picField = map.get("pic");
        if (picField != null) {
            List<PartFieldFile> fileList = picField.getFileList();
            if (fileList != null) {
                for (int i = 0; i < fileList.size(); i++) {
                    FileUtils.copyFile(fileList.get(i), new File(super.getFilePath(), schoolId + "/personal/journal/" + id
                            + "/" + fileList.get(i).getOriName() + ".png"));
                }
                journal.setPic((long) fileList.size());
            }
        }

        PartField voiceField = map.get("voice");
        if (voiceField != null) {
            PartFieldFile file = voiceField.getFile();
            if (file != null) {
                FileUtils.copyFile(file, new File(super.getFilePath(), schoolId + "/personal/journal/" + id
                        + "/voice.amr"));
                journal.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
            }
        }
        journal.setSchoolId(schoolId);
        journal.setUpdateUserId(userId);
        journal.setUpdateTime(new java.sql.Timestamp(System
                .currentTimeMillis()));

        JournalService service = super.getService(JournalService.class);
        service.update(journal);

    }

    @GET
    @Path("{id}")
    @Description("查询")
    public Journal select(@PathParam("id") Long id) {
        return super.getService(JournalService.class).select(id);
    }

    @DELETE
    @Path("{id}")
    @Description("删除")
    public void delete(@PathParam("id") Long id) {
        super.getService(JournalService.class).delete(id);
    }

    @DELETE
    @Path("{id}/image")
    @Description("删除图片")
    public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
        Long userId = super.getAuthedId(rsessionid);
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        long pics = select(id).getPic(); //获取图片个数,全部删除
        for( int i=0; i< pics; i++){
        	new PicResource().delete(schoolId + "/personal/journal/" + id +"/" +i +".png");
        }
        
        super.getService(JournalService.class).updateImage(id, userId, 0);
    }

    @GET
    @Path("/getPics/{id}")
    @Description("获取所有图片列表")
    public List<String> getPics(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
        String filePath = super.getFilePath() + super.getAuthedSchoolId(rsessionid) + "/personal/journal/" + id;
        return getFiles(filePath);
    }


    /*
 * 通过递归得到某一路径下所有的目录及其文件
 */
    public static List<String> getFiles(String filePath) {
        ArrayList<String> fileList = new ArrayList<String>();
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getFiles(file.getAbsolutePath()); //递归调用
//                System.out.println("显示" + filePath + "下所有子目录及其文件" + file.getAbsolutePath());
            } else {
            	fileList.add(file.getAbsolutePath());
//                System.out.println("显示" + filePath + "下所有子目录" + file.getAbsolutePath());
            }
        }
        return fileList;
    }
}

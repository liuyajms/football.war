package cn.com.weixunyun.child.module.marquee;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.util.ThrowableUtils;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/marquee")
@Produces(MediaType.APPLICATION_JSON)
@Description("跑马灯")
public class MarqueeResource extends AbstractResource {

    @GET
    @Description("查询数据")
    public List<Files> getList(@CookieParam("rsessionid") String rsessionid) {
        Long schoolId = super.getAuthedSchoolId(rsessionid);
        File[] files = new File(super.getFilePath(), super.getAuthedSchoolId(rsessionid) + "/marquee/").listFiles();
        List<Files> fileList = new ArrayList<Files>();
        if (files != null) {
            for (File file : files) {
                Files f = new Files();
                f.setPath(schoolId + "/marquee/" + file.getName());
                f.setName(file.getName());
                fileList.add(f);
            }
        }
        return fileList;
    }

    @DELETE
    @Description("删除")
    public void delete(@CookieParam("rsessionid") String rsessionid, @QueryParam("name") String name)
            throws UnsupportedEncodingException {
        System.out.println(name);
        new File(super.getFilePath(), super.getAuthedSchoolId(rsessionid) + "/marquee/" + name).delete();
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("添加")
    public DMLResponse insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        try {
            Map<String, PartField> map = super.partMulti(request);

            File file = map.get("file").getFile();
            if (file != null) {
                File dir = new File(super.getFilePath(), super.getAuthedSchoolId(rsessionid) + "/marquee/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileUtils.copyFileToDirectory(map.get("file").getFile(), dir);
            }
            return new DMLResponse(true, "");
        } catch (Exception e) {
            Throwable throwable = ThrowableUtils.getRootCause(e);
            return new DMLResponse(false, throwable.getMessage());
        }
    }

}

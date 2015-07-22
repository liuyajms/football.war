package cn.com.weixunyun.child.control;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Map;

@Path("/pic")
@Produces(MediaType.APPLICATION_JSON)
public class PicResource extends AbstractResource {

    @PUT
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public void update(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid) {
        Map<String, PartField> map = super.partMulti(request);
        PartField field = map.get("pic");
        if (field != null) {
            PartFieldFile file = field.getFile();
            if (file != null) {
                File targetFile = new File(super.getFilePath(), map.get("path").getValue());
                if (targetFile.getParentFile().exists() == false) {
                    targetFile.getParentFile().mkdirs();
                }
                try {
                    FileUtils.copyFile(file, targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public boolean delete(@javax.ws.rs.QueryParam("path") String path) {
        File file = new File(super.getFilePath(), path);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }

    }
}

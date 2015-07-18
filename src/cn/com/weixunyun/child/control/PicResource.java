package cn.com.weixunyun.child.control;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;

@Path("/pic")
@Produces(MediaType.APPLICATION_JSON)
@Deprecated
public class PicResource extends AbstractResource {

	@PUT
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
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
				System.out.println(targetFile);
				try {
					FileUtils.copyFile(file, targetFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@DELETE
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public boolean delete(@javax.ws.rs.QueryParam("path") String path) {
		File file = new File(super.getFilePath(), path);
		if (file.exists()) {
			return file.delete();
		} else {
			return false;
		}

	}
}

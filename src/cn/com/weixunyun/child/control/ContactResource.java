package cn.com.weixunyun.child.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.SchoolService;
import cn.com.weixunyun.child.util.ImageUtils;

@Path("/contact")
@Produces(MediaType.APPLICATION_JSON)
@Description("联系")
public class ContactResource extends AbstractResource {

	private static final int W = 200;
	private static final int H = 200;

	@GET
	@Description("详情")
	public School select(@CookieParam("rsessionid") String rsessionid) {

		School school = super.getAuthedSchool(rsessionid);
		return super.getService(SchoolService.class).select(school.getId());
	}

	@GET
	@Path("0")
	@Description("开发公司")
	public School selectCompany() {
		return super.getService(SchoolService.class).select(0L);
	}

	@PUT
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改")
	public void update(@CookieParam("rsessionid") String rsessionid, @Context HttpServletRequest request)
			throws IOException {
		School authedSchool = super.getAuthedSchool(rsessionid);

		Map<String, PartField> map = super.partMulti(request);
		School school = super.buildBean(School.class, map, authedSchool.getId());
		school.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		super.getService(SchoolService.class).update(school);

		File file = map.get("picture").getFile();

		File targetFile = new File(super.getFilePath(), authedSchool.getId() + "/logo.png");

		if (file != null) {
			ImageUtils.zoom(file, targetFile, W, H);
		}

		file = map.get("picture_school").getFile();
		if (file != null) {
			ImageUtils.zoom(file, new File(super.getFilePath(), authedSchool.getId() + "/school.png"), W, H);
		}

	}

}

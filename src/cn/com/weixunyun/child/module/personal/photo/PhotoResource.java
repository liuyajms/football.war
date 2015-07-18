package cn.com.weixunyun.child.module.personal.photo;

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
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.PicResource;
import cn.com.weixunyun.child.model.service.SequenceService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/photo")
@Produces(MediaType.APPLICATION_JSON)
@Description("个人相册")
public class PhotoResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<UserPhoto> getList(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") int page, 
			@QueryParam("rows") int rows, @QueryParam("keyword") String keyword, @QueryParam("userId") Long userId) {
		PhotoService service = super.getService(PhotoService.class);
		if (userId == null) {
        	userId = super.getAuthedId(rsessionid);
		}
		return service.getList(rows * page, rows, userId, keyword);
	}

	@GET
	@Path("count")
	@Description("列表-总数")
	public int getListCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword,
			@QueryParam("userId") Long userId) {
		PhotoService service = super.getService(PhotoService.class);
		if (userId == null) {
        	userId = super.getAuthedId(rsessionid);
		}
		return service.getListCount(userId, keyword);
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加个人相册")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid) throws IOException {
		System.out.println("------------insert-----------");
		Long userId = super.getAuthedId(rsessionid);
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		Map<String, PartField> map = super.partMulti(request);
		Long id = super.getService(SequenceService.class).sequence();
		Photo photo = super.buildBean(Photo.class, map, id);
		PartField picField = map.get("pic");

		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i),
						new File(super.getFilePath(), schoolId + "/personal/photo/" + id + "/" + fileList.get(i).getOriName()));
				}
			}
		}

		photo.setSchoolId(schoolId);
		photo.setCreateUserId(userId);
		photo.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		PhotoService service = super.getService(PhotoService.class);
		service.insert(photo);
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
		Photo photo = super.buildBean(Photo.class, map, id);

		PartField picField = map.get("pic");
		if (picField != null) {
			List<PartFieldFile> fileList = picField.getFileList();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					FileUtils.copyFile(fileList.get(i),
						new File(super.getFilePath(), schoolId + "/personal/photo/" + id + "/" + fileList.get(i).getOriName()));
				}
			}
		}

		photo.setUpdateUserId(userId);
		photo.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

		PhotoService service = super.getService(PhotoService.class);
		service.update(photo);
	}

	@GET
	@Path("{id}")
	@Description("查询")
	public Photo select(@PathParam("id") Long id) {
		return super.getService(PhotoService.class).select(id);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(PhotoService.class).delete(id);
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片，keyword：图片名称，包含后缀")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id, 
			@QueryParam("keyword") String keyword) {
		Long userId = super.getAuthedId(rsessionid);
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		new PicResource().delete(schoolId + "/personal/photo/" + id + "/"+keyword);
		super.getService(PhotoService.class).updateImage(id, userId);
	}

	@GET
	@Path("/getPics/{id}")
	@Description("获取所有图片列表")
	public List<String> getPics(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		String filePath = super.getAuthedSchoolId(rsessionid) + "/personal/photo/" + id;
		return getFiles(super.getFilePath() + filePath);
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
				getFiles(file.getAbsolutePath());// 递归调用
			} else {
				fileList.add(file.getAbsolutePath());
			}
		}
		return fileList;
	}
}

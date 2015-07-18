package cn.com.weixunyun.child.control;

import java.io.File;
import java.io.IOException;
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
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.bean.StudentParents;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import cn.com.weixunyun.child.model.pojo.Global;
import cn.com.weixunyun.child.model.pojo.Parents;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.DictionaryValueService;
import cn.com.weixunyun.child.model.service.GlobalService;
import cn.com.weixunyun.child.model.service.ParentsService;
import cn.com.weixunyun.child.model.service.StudentService;
import cn.com.weixunyun.child.third.simiyun.SimiyunHelper;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/parents")
@Produces(MediaType.APPLICATION_JSON)
@Description("家长")
public class ParentsResource extends AbstractResource {

	@GET
	@Description("列表")
	public List<StudentParents> selectAll(@CookieParam("rsessionid") String rsessionid, @QueryParam("page") Long page,
			@QueryParam("rows") Long rows, @QueryParam("studentId") Long studentId,
			@QueryParam("keyword") String keyword) {
		ParentsService service = super.getService(ParentsService.class);
		return service.selectAll(page * rows, rows, super.getAuthedSchool(rsessionid).getId(), studentId, keyword);
	}

	@GET
	@Path("count")
	@Description("总数")
	public int selectAllCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("studentId") Long studentId,
			@QueryParam("keyword") String keyword) {
		ParentsService service = super.getService(ParentsService.class);
		return service.selectAllCount(super.getAuthedSchool(rsessionid).getId(), studentId, keyword);
	}

	@GET
	@Path("{id}")
	@Description("详情")
	public StudentParents select(@PathParam("id") Long id) {
		return super.getService(ParentsService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws IOException {

		Map<String, PartField> map = super.partMulti(request);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		ParentsService service = super.getService(ParentsService.class);
		Long schoolId = super.getAuthedSchoolId(rsessionid);

		Parents p = super.buildBean(Parents.class, map, null);
		service.insertParents(map, p, schoolId);

		File file = map.get("image").getFile();
		if (file != null) {
			FileUtils.copyFile(file, new File(super.getFilePath(), teacher.getSchoolId() + "/parents/" + p.getId()
					+ ".png"));
		}

		Global simiyunAvailableGlobal = getService(GlobalService.class).select(schoolId, "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String schoolSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);

		if ("1".equals(globalSimiyunAvailable) && "1".equals(schoolSimiyunAvailable)) {
			Long studentId = Long.parseLong(map.get("studentId").getValue());
			Long classesId = getService(StudentService.class).select(studentId).getClassesId();

			long uid = SimiyunHelper.addUser(super.getAuthedId(rsessionid), p.getId(), p.getName());
			SimiyunHelper.addGroupUsers(p.getId().toString(), super.getAuthedId(rsessionid), classesId, false);
			service.idDisk(p.getId(), uid);
		}

	}

	@PUT
	@Path("{id}/{classesId}/{flag}/{studentId}")
	@Description("开通云盘")
	public void updateIdDisk(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid,
			@PathParam("flag") int flag, @PathParam("classesId") Long classesId, @PathParam("studentId") Long studentId)
			throws Exception {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		StudentService sService = super.getService(StudentService.class);
		ClassesStudent cs = sService.select(studentId);

		ParentsService service = super.getService(ParentsService.class);
		String type = service.select(id).getType();
		String code = null;

		if (type != null && !"".equals(type)) {
			DictionaryValueService dvService = super.getService(DictionaryValueService.class);
			DictionaryValue dv = dvService.get(schoolId, "parents", "type", type);
			code = dv == null ? "家长" : dv.getName();
		} else {
			code = "家长";
		}

		Global simiyunAvailableGlobal = getService(GlobalService.class).select(schoolId, "simiyun", "available");
		String globalSimiyunAvailable = simiyunAvailableGlobal == null ? "" : simiyunAvailableGlobal.getValue();
		String properSimiyunAvailable = PropertiesListener.getProperty("simiyun.available", null);

		if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable) && flag == 1) {
			
			Long uid = SimiyunHelper.addUser(super.getAuthedId(rsessionid), id, cs.getName() + "的" + code);
			SimiyunHelper.addGroupUsers(id.toString(), super.getAuthedId(rsessionid), classesId, false);
			service.idDisk(id, uid);
			
		} else if ("1".equals(globalSimiyunAvailable) && "1".equals(properSimiyunAvailable) && flag == 0) {
			SimiyunHelper.delUser(id);
			service.idDisk(id, null);
		}
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改")
	public void update(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) throws IOException {

		Map<String, PartField> map = super.partMulti(request);
		Teacher teacher = super.getAuthedTeacher(rsessionid);
		Parents p = super.buildBean(Parents.class, map, id);
		Long schoolId = super.getAuthedSchoolId(rsessionid);

		super.getService(ParentsService.class).updateParents(map, p, schoolId);

		File file = map.get("image").getFile();
		if (file != null) {
			FileUtils.copyFile(file, new File(super.getFilePath(), teacher.getSchoolId() + "/parents/" + p.getId()
					+ ".png"));
		}

	}

	@PUT
	@Path("{id}/image")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("修改头像")
	public void updateImage(@Context HttpServletRequest request, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) {
		Map<String, PartField> map = super.partMulti(request);
		PartField field = map.get("image");
		if (field != null) {
			PartFieldFile file = field.getFile();
			if (file != null) {
				ParentsService parentsService = super.getService(ParentsService.class);
				parentsService.updated(id);

				File targetFile = new File(super.getFilePath(), "/" + super.getAuthedSchool(rsessionid).getId()
						+ "/parents/" + id + ".png");
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

	@PUT
	@Path("{id}/username")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改帐号")
	public Boolean username(MultivaluedMap<String, String> form, @PathParam("id") Long id,
			@CookieParam("rsessionid") String rsessionid) {
		Map<String, PartField> map = super.part(form);

		String username = map.get("username").getValue();

		ParentsService parentservice = super.getService(ParentsService.class);

		Parents parents = parentservice.selectParentsMobile(username);

		if (parents == null) {
			parentservice.username(id, username);
			return true;
		} else {
			return false;
		}
	}

	@PUT
	@Path("{id}/password")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改密码")
	public Boolean password(MultivaluedMap<String, String> form, @PathParam("id") Long id) {
		Map<String, PartField> map = super.part(form);
		String passwordOld = map.get("password_old").getValue();
		String passwordNew = map.get("password_new").getValue();

		ParentsService parentsService = super.getService(ParentsService.class);
		Parents parents = parentsService.select(id);
		if (parents == null || !parents.getPassword().equals(DigestUtils.md5Hex(passwordOld))) {
			return false;
		} else {
			parentsService.password(id, DigestUtils.md5Hex(passwordNew));
			return true;
		}
	}

	@PUT
	@Path("{id}/resetPwd")
	@Description("重置密码")
	public void resetPassword(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid) {
		School school = getAuthedSchool(rsessionid);
		System.out.println("-----------reset----------");
		String pwd = super.getService(GlobalService.class).select(school.getId(), "parents", "password").getValue();
		super.getService(ParentsService.class).password(id, DigestUtils.md5Hex(pwd));
	}

	@DELETE
	@Path("{id}/{studentId}")
	@Description("删除")
	public void delete(@PathParam("id") Long id, @CookieParam("rsessionid") String rsessionid,
			@PathParam("studentId") Long studentId) {
		Long schoolId = super.getAuthedSchoolId(rsessionid);
		new PicResource().delete(schoolId + "/parents/" + id + ".png");
		super.getService(ParentsService.class).deleteParents(id, studentId);

		Global sG = getService(GlobalService.class).select(schoolId, "simiyun", "available");
		String sPG = sG.getValue();
		String sCg = PropertiesListener.getProperty("simiyun.available", null);

		if ("1".equals(sPG) && "1".equals(sCg)) {
			SimiyunHelper.delUser(id);
		}
	}

	@DELETE
	@Path("{id}/image")
	@Description("删除图片")
	public void deleteImage(@CookieParam("rsessionid") String rsessionid, @PathParam("id") Long id) {
		new PicResource().delete(super.getAuthedSchool(rsessionid).getId() + "/parents/" + id + ".png");
	}
}

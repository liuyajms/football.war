package cn.com.weixunyun.child.control;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.TeacherParentsChat;
import cn.com.weixunyun.child.model.pojo.Chat;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.ChatService;
import cn.com.weixunyun.child.model.service.SequenceService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Description("群聊")
public class ChatResource extends AbstractResource {

	@GET
	@Description("某个群的群聊消息列表")
	public List<TeacherParentsChat> selectAll(@QueryParam("classesId") Long classesId, @CookieParam("rsessionid") String rsessionid,
			@QueryParam("page") Long page, @QueryParam("rows") Long rows, @QueryParam("time") Long time) {
		ChatService service = super.getService(ChatService.class);
		if (time != null) {
			String timeStr = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date(time));
			return service.getList(page * rows, rows, super.getAuthedSchoolId(rsessionid), classesId, timeStr);
		} else {
			return service.getList(page * rows, rows, super.getAuthedSchoolId(rsessionid), classesId, null);
		}
	}
	
	@GET
	@Path("count")
	@Description("某个群的群聊消息列表-总数")
	public int selectAllCount(@QueryParam("classesId") Long classesId, @CookieParam("rsessionid") String rsessionid,
			@QueryParam("time") Long time) {
		ChatService service = super.getService(ChatService.class);
		if (time != null) {
			String timeStr = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date(time));
			return service.getListCount(super.getAuthedSchoolId(rsessionid), classesId, timeStr);
		} else {
			return service.getListCount(super.getAuthedSchoolId(rsessionid), classesId, null);
		}
	}

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Description("添加")
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Map<String, PartField> map = super.partMulti(request);
		Chat chat = new Chat();

		School school = super.getAuthedSchool(rsessionid);
		Long id = super.getService(SequenceService.class).sequence();

		chat.setId(id);
		chat.setSchoolId(school.getId());

		int type = Integer.parseInt(map.get("type").getValue());
		File file = map.get("description").getFile();
		switch (type) {
		case 0:
			chat.setDescription(map.get("description").getValue());
			break;
		case 1:
			FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/chat/" + chat.getId()
					+ ".png"));
			break;
		case 2:
			FileUtils.copyFile(file, new File(super.getFilePath(), school.getId() + "/chat/" + chat.getId()
					+ ".amr"));
			chat.setVoiceLength(Long.parseLong(map.get("voiceLength").getValue()));
			break;
		default:
		}

		chat.setUserId(super.getAuthedId(rsessionid));
		chat.setTime(new Timestamp(System.currentTimeMillis()));
		chat.setType((long) type);
		chat.setClassesId(Long.parseLong(map.get("classesId").getValue()));

		ChatService service = super.getService(ChatService.class);
		service.insert(chat);
	}

	@DELETE
	@Path("{id}")
	@Description("删除")
	public void delete(@PathParam("id") Long id) {
		super.getService(ChatService.class).delete(id);
	}

	@GET
	@Path("unreaded")
	@Description("未读总数")
	public int unreaded(@CookieParam("rsessionid") String rsessionid, @QueryParam("classesId") Long classesId, 
			@QueryParam("time") Long time) {
		ChatService service = super.getService(ChatService.class);
		String timeStr = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date(time));
		return service.unreaded(super.getAuthedSchoolId(rsessionid), classesId, super.getAuthedId(rsessionid), timeStr);
	}
}

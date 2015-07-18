package cn.com.weixunyun.child.module.homework;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.AbstractResource.PartField;
import cn.com.weixunyun.child.model.bean.ClassesStudent;
import cn.com.weixunyun.child.model.pojo.Teacher;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/homeworkcomplete")
@Produces(MediaType.APPLICATION_JSON)
public class HomeworkCompleteResource extends AbstractResource {
	@GET
	public Map<String, ?> selectAll(@QueryParam("page") int page,
			@QueryParam("rows") int rows,
			@CookieParam("rsessionid") String rsessionid,
			@QueryParam("sId") Long studentId) {
		try {
			page = page > 0 ? page - 1 : 0;

			HomeworkCompleteService service = super
					.getService(HomeworkCompleteService.class);
			Teacher teacher = super.getAuthedTeacher(rsessionid);
			int total = 0;
			List<ClassesStudent> studentList = null;
			List<StudentHomeworkComplete> homeworkcompleteList = null;
			Map<String, Object> map = new HashMap<String, Object>();
			if (studentId == 0) {
				total = service.selectAllStudentCount(teacher.getId());
				studentList = service.selectAllStudent(page * rows, rows,
						teacher.getId());
				map.put("total", total);
				map.put("rows", studentList);
			} else {

				homeworkcompleteList = service.selectStudentHomework(
						teacher.getId(), studentId);
				map.put("total", homeworkcompleteList.size());
				map.put("rows", homeworkcompleteList);
			}

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public void insert(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid) throws IOException {
		
		Map<String, PartField> map = super.partMulti(request);
		
		HomeworkComplete hkc = super.buildBean(HomeworkComplete.class, map, null);
		hkc.setSchoolId(super.getAuthedSchool(rsessionid).getId());
		HomeworkCompleteService service = super.getService(HomeworkCompleteService.class);
		service.insert(hkc);
		
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public void update(@Context HttpServletRequest request, @PathParam("id") Long id) throws IOException {
		
		Map<String, PartField> map = super.partMulti(request);
		
		HomeworkComplete hkc = super.buildBean(HomeworkComplete.class, map, id);
		HomeworkCompleteService service = super.getService(HomeworkCompleteService.class);
		service.update(hkc);

	}
}

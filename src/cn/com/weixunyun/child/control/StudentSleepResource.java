package cn.com.weixunyun.child.control;

import java.net.URLDecoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.model.pojo.StudentSleep;
import cn.com.weixunyun.child.model.service.StudentSleepService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/studentSleep")
@Produces(MediaType.APPLICATION_JSON)
public class StudentSleepResource extends AbstractResource {

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insert(MultivaluedMap<String, String> formData) throws Exception {
		StudentSleepService service = super.getService(StudentSleepService.class);
		
		StudentSleep studentSleep = new StudentSleep();
		String date = formData.getFirst("date");
		date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
		
		studentSleep.setStudentId(Long.valueOf(formData.getFirst("childId")));
		studentSleep.setSleepTime(URLDecoder.decode(formData.getFirst("sleepTime"),"UTF-8"));
		studentSleep.setDate(date);
		service.insert(studentSleep);
		
	}
	
	@PUT
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void update(MultivaluedMap<String, String> formData) throws Exception {
		StudentSleepService service = super.getService(StudentSleepService.class);
		
		String date = formData.getFirst("date");
		date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
		
		StudentSleep studentSleep = new StudentSleep();	
		studentSleep.setStudentId(Long.valueOf(formData.getFirst("childId")));
		studentSleep.setSleepTime(URLDecoder.decode(formData.getFirst("sleepTime"),"UTF-8"));
		studentSleep.setDate(date);
		service.updateData(studentSleep);
		
	}

}

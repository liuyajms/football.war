package cn.com.weixunyun.child.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.model.pojo.Phrase;
import cn.com.weixunyun.child.model.service.PhraseService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/phrase")
@Produces(MediaType.APPLICATION_JSON)
public class PhraseResource extends AbstractResource {

	@GET
	public Map<String, ?> selectAll(@QueryParam("page") int page,
			@QueryParam("rows") int rows) {
		try {
			page = page > 0 ? page - 1 : 0;

			PhraseService service = super.getService(PhraseService.class);

			int total = service.selectAllCount();
			List<Phrase> cList = service.selectAll(page*rows, rows);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", cList);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("{id}")
	public Phrase select(@PathParam("id") int id) {
		return super.getService(PhraseService.class).select(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void insert(MultivaluedMap<String, String> formData,
			@CookieParam("rsessionid") String rsessionid)
			throws Exception {
		Phrase phrase = super.buildBean(Phrase.class, formData, null);
		phrase.setSchoolId(super.getAuthedTeacher(rsessionid).getSchoolId());

		PhraseService service = super.getService(PhraseService.class);
		service.insert(phrase);
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void update(MultivaluedMap<String, String> formData,
			@PathParam("id") Long id) throws Exception {
		Phrase phrase = super.buildBean(Phrase.class, formData, id);

		super.getService(PhraseService.class).update(phrase);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) {
		super.getService(PhraseService.class).delete(id);
	}
}

package cn.com.weixunyun.child.control;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.DictionaryValueService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/value")
@Produces(MediaType.APPLICATION_JSON)
@Description("数据字典-值")
public class DictionaryValueResource extends AbstractResource {
	@GET
	@Description("列表")
	public Map<String, ?> selectAll(@QueryParam("page") int page, @QueryParam("code") String code,
			 @QueryParam("rows") int rows, @CookieParam("rsessionid") String rsessionid) {
		try {

			DictionaryValueService service = super.getService(DictionaryValueService.class);

			School school = super.getAuthedSchool(rsessionid);

			int total = service.selectAllCount(school.getId(), code);
			List<DictionaryValue> list = service.getDicValues(school.getId(), code, page * rows, rows);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("{list}")
	@Description("数据字典-值列表")
	public List<DictionaryValue> getValueList(@CookieParam("rsessionid") String rsessionid,
			@QueryParam("keyword") String keyword) {
		System.out.println("---------------DictionaryValue------------");
		System.out.println(keyword);
		System.out.println();
		School school = super.getAuthedSchool(rsessionid);
		DictionaryValueService service = super.getService(DictionaryValueService.class);
		try {
			if (keyword != null && !"-1".equals(keyword) && keyword.contains(",")) {
				String attr[] = keyword.split(",");
				return service.getValueList(school.getId(), attr[0], attr[1]);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("list/count")
	@Description("数据字典-值总数")
	public int selectClassesCount(@CookieParam("rsessionid") String rsessionid, @QueryParam("keyword") String keyword) {
		DictionaryValueService service = super.getService(DictionaryValueService.class);
		School school = super.getAuthedSchool(rsessionid);
		try {
			if (keyword != null && !"-1".equals(keyword) && keyword.contains(",")) {
				String attr[] = keyword.split(",");
				return service.getValueListCount(school.getId(), attr[0], attr[1]);
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@GET
	@Description("详情")
	public DictionaryValue get(@QueryParam("tableCode") String tableCode, @QueryParam("fieldCode") String fieldCode, 
			@QueryParam("code") String code, @CookieParam("rsessionid") String rsessionid) {
		School school = super.getAuthedSchool(rsessionid);
		return super.getService(DictionaryValueService.class).get(school.getId(), tableCode, fieldCode, code);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("添加")
	public void insert(MultivaluedMap<String, String> formData, @QueryParam("keyword") String keyword, @CookieParam("rsessionid") String rsessionid)
			throws UnsupportedEncodingException {
		String attr[] = URLDecoder.decode(formData.getFirst("keyword"), "UTF-8").split(",");
		
		DictionaryValue dv = super.buildBean(DictionaryValue.class, formData, null);
		School school = super.getAuthedSchool(rsessionid);
		dv.setSchoolId(school.getId());
		dv.setDictionaryTableCode(attr[0]);
		dv.setDictionaryFieldCode(attr[1]);

		super.getService(DictionaryValueService.class).insert(dv);
	}

	@PUT
	@Path("{code}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Description("修改")
	public void update(MultivaluedMap<String, String> formData, @PathParam("code") String code, @CookieParam("rsessionid") String rsessionid)
			throws UnsupportedEncodingException {
		DictionaryValueService service = super.getService(DictionaryValueService.class);
		
		//需要先判断
		if (code.equals(formData.get("oldCode"))) { //没有修改编码
			DictionaryValue dv = super.buildBean(DictionaryValue.class, formData, null);
			service.update(dv);
		} else {
			//先删除、再添加
			School school = super.getAuthedSchool(rsessionid);
			String attr[] = URLDecoder.decode(formData.getFirst("keyword"), "UTF-8").split(",");
			
			DictionaryValue d = new DictionaryValue();
			d.setCode(formData.getFirst("oldCode"));
			d.setDictionaryFieldCode(attr[1]);
			d.setDictionaryTableCode(attr[0]);
			d.setSchoolId(school.getId());
			super.getService(DictionaryValueService.class).delete(d);
			
			DictionaryValue dv = super.buildBean(DictionaryValue.class, formData, null);
			dv.setSchoolId(school.getId());
			dv.setDictionaryTableCode(attr[0]);
			dv.setDictionaryFieldCode(attr[1]);
			super.getService(DictionaryValueService.class).insert(dv);
		}
		
		
	}

	@DELETE
	@Path("{code}/{tableCode}.{fieldCode}")
	@Description("删除")
	public void delete(@PathParam("code") String code, @PathParam("tableCode") String tableCode,
			@PathParam("fieldCode") String fieldCode, @CookieParam("rsessionid") String rsessionid) {

		DictionaryValue d = new DictionaryValue();

		School school = super.getAuthedSchool(rsessionid);
		d.setCode(code);
		d.setDictionaryFieldCode(fieldCode);
		d.setDictionaryTableCode(tableCode);
		d.setSchoolId(school.getId());

		super.getService(DictionaryValueService.class).delete(d);

	}
}

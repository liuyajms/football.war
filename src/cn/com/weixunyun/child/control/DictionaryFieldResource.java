package cn.com.weixunyun.child.control;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.TableDictionaryField;
import cn.com.weixunyun.child.model.service.DictionaryFieldService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/field")
@Produces(MediaType.APPLICATION_JSON)
@Description("数据字典-表")
public class DictionaryFieldResource extends AbstractResource {
	
	@GET
	@Description("数据字典-表列表")
	public List<TableDictionaryField> getList() {
		try {
			DictionaryFieldService service = super.getService(DictionaryFieldService.class);
			return service.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("count")
	@Description("数据字典-表总数")
	public int getListCount() {
		DictionaryFieldService service = super.getService(DictionaryFieldService.class);
		int total = service.listCount();
		return total;
	}
}

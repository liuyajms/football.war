package cn.com.weixunyun.child.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.DictionaryField;
import cn.com.weixunyun.child.model.pojo.DictionaryTable;
import cn.com.weixunyun.child.model.service.DictionaryFieldService;
import cn.com.weixunyun.child.model.service.DictionaryTableService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/table")
@Produces(MediaType.APPLICATION_JSON)
@Description("数据字典-表")
public class DictionaryTableResource extends AbstractResource {
	@GET
	@Description("列表")
	public List<Map<String, ?>> getDicJson() {
		List<Map<String, ?>> mapList = new ArrayList<Map<String,?>>();
		try {
			DictionaryTableService login = super.getService(DictionaryTableService.class);
			DictionaryFieldService field = super.getService(DictionaryFieldService.class);
			List<DictionaryTable> list = login.getDicTables();
			if (list.size() > 0) {
				for (DictionaryTable d : list) {
					Map<String, Object> parent = new HashMap<String, Object>();
					parent.put("code", d.getCode());
					parent.put("name", d.getName());
					parent.put("state", "closed");
					String dictionaryTableCode = d.getCode();
					List<DictionaryField> listField = field.getAllFields(dictionaryTableCode);
					List<Map<String, ?>> children = new ArrayList<Map<String, ?>>();
					for (DictionaryField f : listField) {
						Map<String, Object> child = new HashMap<String, Object>();
						child.put("code", f.getCode());
						child.put("name", f.getName());
						child.put("dictionaryTableCode", f.getDictionaryTableCode());
						children.add(child);
					}
					parent.put("children", children);
					mapList.add(parent);
				}
			}
			return mapList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("tlist")
	@Description("数据字典-表列表")
	public List<DictionaryTable> getValueList(@QueryParam("page") int page, @QueryParam("code") int row) {
		System.out.println("-------------------table_list-----------------");
		try {
			DictionaryTableService service = super.getService(DictionaryTableService.class);
			return service.getDicTables();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("tlist/count")
	@Description("数据字典-表总数")
	public int selectClassesCount(@QueryParam("page") int page, @QueryParam("code") int row) {
		System.out.println("-------------------table_list_count-----------------");
		DictionaryTableService service = super.getService(DictionaryTableService.class);
		int total = service.getDicTablesCount();
		return total;
	}
}

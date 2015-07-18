package cn.com.weixunyun.child.control;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.model.pojo.Region;
import cn.com.weixunyun.child.model.service.RegionService;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/region")
@Produces(MediaType.APPLICATION_JSON)
public class RegionResource extends AbstractResource {
	@GET
	public List<Region> getAllPrivinces(
			@QueryParam("provinceCode") String provinceCode,
			@QueryParam("cityCode") String cityCode) {
		RegionService login = super.getService(RegionService.class);
		if (provinceCode != null) {
			return login.getAllCities(provinceCode);
		} else {
			if (cityCode != null) {
				return login.getAllCounties(cityCode);
			} else {
				return login.getAllPrivinces();
			}
		}
	}	
}

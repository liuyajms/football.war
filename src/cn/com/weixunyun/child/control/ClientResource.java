package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.pojo.Client;
import cn.com.weixunyun.child.model.service.ClientService;
import cn.com.weixunyun.child.model.service.ServiceFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/client")
@Produces(MediaType.APPLICATION_JSON)
@Description("模板")
public class ClientResource extends AbstractResource {

    public static Map<String,String> clients;

    public static void initClients() {
        if (clients == null) {
            clients = new HashMap<String,String>();
            List<Client> list = ServiceFactory.getService(ClientService.class).listAvailable();
            for (Client client : list) {
                clients.put(client.getCode(),client.getPlatform());
            }
        }
    }

    @GET
    @Path("{code}")
    public Client select(@PathParam("code") String code, @CookieParam("rsessionid") String rsessionid) {
        return super.getService(ClientService.class).select(code);
    }

    @GET
    @Description("列表")
    public List<Client> list() {
        return super.getService(ClientService.class).list(null);
    }

    @GET
    @Path("/count")
    @Description("总数")
    public int selectCount(@CookieParam("rsessionid") String rsessionid) {
        ClientService service = super.getService(ClientService.class);
        return service.count(null);
    }

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public void insert(MultivaluedMap<String, String> formData, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
        Client client = super.buildBean(Client.class, formData, 0L);
        client.setCode(DigestUtils.md5Hex(UUID.randomUUID().toString()));
        ClientService service = super.getService(ClientService.class);
        service.insert(client);

        initClients();
    }

    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改")
    public void update(@CookieParam("rsessionid") String rsessionid, MultivaluedMap<String, String> formData,
                       @PathParam("code") String code) throws Exception {
        System.out.println("--------------------update------------------");

        Client client = super.buildBean(Client.class, formData, 0L);
        client.setCode(code);

        ClientService service = super.getService(ClientService.class);
        service.update(client);

        initClients();
    }

}

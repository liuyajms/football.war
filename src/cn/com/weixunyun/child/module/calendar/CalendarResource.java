package cn.com.weixunyun.child.module.calendar;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.ResultEntity;
import cn.com.weixunyun.child.control.AbstractResource;
import org.apache.http.HttpStatus;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
@Description("足球日历")
public class CalendarResource extends AbstractResource {


    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("添加")
    public ResultEntity insert(@FormParam("freeTime") String freeTime, @CookieParam("rsessionid") String rsessionid)
            throws Exception {

        super.getService(CalendarService.class).insert(super.getAuthedId(rsessionid), Date.valueOf(freeTime));

        return new ResultEntity(HttpStatus.SC_OK, "添加成功");
    }


    @DELETE
    @Description("删除")
    public ResultEntity delete(@FormParam("freeTime") String freeTime, @CookieParam("rsessionid") String rsessionid) {

        int n = super.getService(CalendarService.class).delete(super.getAuthedId(rsessionid), Date.valueOf(freeTime));

        if (n > 0) {
            return new ResultEntity(HttpStatus.SC_OK, "取消成功");
        } else {
            return new ResultEntity(HttpStatus.SC_BAD_REQUEST, "您尚未设置此空闲日期");
        }

    }


}

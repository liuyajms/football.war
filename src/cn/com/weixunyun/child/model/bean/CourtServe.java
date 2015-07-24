package cn.com.weixunyun.child.model.bean;

import java.io.Serializable;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtServe implements Serializable{
    private static final long serialVersionUID = 6978883527789560674L;
    private Long courtId;

    private Long serveId;

    public Long getServeId() {
        return serveId;
    }

    public void setServeId(Long serveId) {
        this.serveId = serveId;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }
}

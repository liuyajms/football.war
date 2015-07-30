package cn.com.weixunyun.child.module.calendar;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by PC on 2015/7/29.
 */
public class Calendar implements Serializable {

    private static final long serialVersionUID = 2161012508178966443L;
    private Long playerId;

    private Date freeTime;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Date getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(Date freeTime) {
        this.freeTime = freeTime;
    }
}

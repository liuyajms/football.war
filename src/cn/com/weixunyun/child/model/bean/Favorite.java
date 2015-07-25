package cn.com.weixunyun.child.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by PC on 2015/7/24.
 */
public class Favorite implements Serializable{
    private static final long serialVersionUID = 4207379190032841040L;
    private Long playerId;

    private Long courtId;

    private Timestamp createTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }
}

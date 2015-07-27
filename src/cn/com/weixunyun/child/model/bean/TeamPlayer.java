package cn.com.weixunyun.child.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class TeamPlayer implements Serializable {
    private static final long serialVersionUID = 4637585629841766074L;
    private Long teamId;

    private Long playerId;

    private Timestamp createTime;

    private Boolean agreed;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Boolean getAgreed() {
        return agreed;
    }

    public void setAgreed(Boolean agreed) {
        this.agreed = agreed;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}

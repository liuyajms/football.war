package cn.com.weixunyun.child.model.bean;

import java.io.Serializable;

public class Friend implements Serializable {

    private static final long serialVersionUID = 1074035989748143065L;

    private Long playerId;

    private Long friendPlayerId;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getFriendPlayerId() {
        return friendPlayerId;
    }

    public void setFriendPlayerId(Long friendPlayerId) {
        this.friendPlayerId = friendPlayerId;
    }
}

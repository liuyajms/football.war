package cn.com.weixunyun.child.model.bean;

import java.io.Serializable;

public class Friend implements Serializable {

    private static final long serialVersionUID = 1074035989748143065L;

    private Long playerId;

    private Long friendId;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}

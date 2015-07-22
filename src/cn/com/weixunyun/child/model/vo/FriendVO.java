package cn.com.weixunyun.child.model.vo;


import cn.com.weixunyun.child.model.bean.Friend;

public class FriendVO extends Friend {
    private static final long serialVersionUID = 5608089840412428884L;

    private String friendName;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}

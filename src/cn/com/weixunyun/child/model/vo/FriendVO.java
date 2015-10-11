package cn.com.weixunyun.child.model.vo;


import cn.com.weixunyun.child.model.bean.Friend;

public class FriendVO extends Friend {
    private static final long serialVersionUID = 5608089840412428884L;

    private String friendName;

    private String teamName;//teamName、teamId用于我的球友列表

    private Long teamId;

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}

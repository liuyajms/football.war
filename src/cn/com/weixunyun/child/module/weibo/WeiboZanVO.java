package cn.com.weixunyun.child.module.weibo;


import java.sql.Timestamp;

public class WeiboZanVO extends WeiboZan {

    private String userName;
    private String userType;
    private Timestamp userUpdateTime;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Timestamp getUserUpdateTime() {
        return userUpdateTime;
    }

    public void setUserUpdateTime(Timestamp userUpdateTime) {
        this.userUpdateTime = userUpdateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

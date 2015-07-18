package cn.com.weixunyun.child.module.weibo;


import java.sql.Timestamp;

public class WeiboZan {
    private Long weiboId;
    private Long userId;
    private Timestamp time;


    public Long getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(Long weiboId) {
        this.weiboId = weiboId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}

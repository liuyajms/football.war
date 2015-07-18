package cn.com.weixunyun.child.module.stats;

import java.sql.Date;

public class ClassStats {

    private Date date;

    private Integer broadcast;//班级广播

    private Integer classWeibo;

    private Integer stars;

    private Integer uploads;

    public ClassStats() {

    }

    public ClassStats(Date date, int broadcast, int classWeibo, int stars, int uploads) {
        this.date = date;
        this.broadcast = broadcast;
        this.classWeibo = classWeibo;
        this.stars = stars;
        this.uploads = uploads;

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Integer broadcast) {
        this.broadcast = broadcast;
    }

    public Integer getClassWeibo() {
        return classWeibo;
    }

    public void setClassWeibo(Integer classWeibo) {
        this.classWeibo = classWeibo;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getUploads() {
        return uploads;
    }

    public void setUploads(Integer uploads) {
        this.uploads = uploads;
    }


}

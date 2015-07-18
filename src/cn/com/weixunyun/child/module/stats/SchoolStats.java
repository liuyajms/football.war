package cn.com.weixunyun.child.module.stats;


import java.sql.Date;

public class SchoolStats {

    private Date date;


    private Integer news;

    private Integer weibo;

    private Integer notice;

    public SchoolStats() {

    }

    public SchoolStats(Date date, int news, int weibo, int notice) {
        this.date = date;
        this.news = news;
        this.weibo = weibo;
        this.notice = notice;
    }

    public Integer getNews() {
        return news;
    }

    public void setNews(Integer news) {
        this.news = news;
    }

    public Integer getWeibo() {
        return weibo;
    }

    public void setWeibo(Integer weibo) {
        this.weibo = weibo;
    }

    public Integer getNotice() {
        return notice;
    }

    public void setNotice(Integer notice) {
        this.notice = notice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}

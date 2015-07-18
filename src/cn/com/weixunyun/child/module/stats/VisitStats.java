package cn.com.weixunyun.child.module.stats;


import java.sql.Date;

public class VisitStats {

    private Date date;

    private Integer teachers;// 访问次数? , 登陆用户数?

    private Integer parents;

    public VisitStats() {

    }

    public VisitStats(Date date, int teachers, int parents) {
        this.date = date;
        this.teachers = teachers;
        this.parents = parents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTeachers() {
        return teachers;
    }

    public void setTeachers(Integer teachers) {
        this.teachers = teachers;
    }

    public Integer getParents() {
        return parents;
    }

    public void setParents(Integer parents) {
        this.parents = parents;
    }


}

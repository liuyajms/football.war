package cn.com.weixunyun.child.module.stats;

import java.sql.Date;

public class LoginStats {

    private Date date;

    private Integer teachersLogin;

    private Integer parentsLogin;

    private Integer teachers;

    private Integer parents;

    public LoginStats(int parents, int teachers, int loginParents, int loginTeachers) {
        this.parents = parents;
        this.teachers = teachers;
        this.parentsLogin = loginParents;
        this.teachersLogin = loginTeachers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTeachersLogin() {
        return teachersLogin;
    }

    public void setTeachersLogin(Integer teachersLogin) {
        this.teachersLogin = teachersLogin;
    }

    public Integer getParentsLogin() {
        return parentsLogin;
    }

    public void setParentsLogin(Integer parentsLogin) {
        this.parentsLogin = parentsLogin;
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

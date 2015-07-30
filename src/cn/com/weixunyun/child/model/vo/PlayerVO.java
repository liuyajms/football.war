package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Player;

import java.sql.Date;
import java.util.List;

public class PlayerVO extends Player {

    private List<String> roleList;

    private List<Date> freeTimeList;//球员近两周空闲时间列表

    private String legName;

    private Boolean isFriend;

    public List<Date> getFreeTimeList() {
        return freeTimeList;
    }

    public void setFreeTimeList(List<Date> freeTimeList) {
        this.freeTimeList = freeTimeList;
    }

    public Boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Boolean isFriend) {
        this.isFriend = isFriend;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getLegName() {
        return legName;
    }

    public void setLegName(String legName) {
        this.legName = legName;
    }
}

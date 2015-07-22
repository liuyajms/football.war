package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Player;

import java.util.List;

public class PlayerVO extends Player {

    private List<String> roleList;

    private String legName;

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

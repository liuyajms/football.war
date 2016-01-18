package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.TeamPlayer;

import java.sql.Timestamp;

public class TeamPlayerVO extends TeamPlayer {

    private String teamName;

    private String playerName;

    private Timestamp updateTime;//球员更新时间

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

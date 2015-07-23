package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.TeamPlayer;

public class TeamPlayerVO extends TeamPlayer {

    private String teamName;

    private String playerName;

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

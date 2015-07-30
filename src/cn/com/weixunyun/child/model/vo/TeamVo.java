package cn.com.weixunyun.child.model.vo;


import cn.com.weixunyun.child.model.bean.Team;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class TeamVO extends Team {
    private Integer avgAge;

    private String courtName;

    private List<String> ruleList;

    private List<String> colorList;

    private List<TeamPlayerVO> teamPlayerList;

    private List<Map<Date, Integer>> freeTimeList;//球队每天的空闲数

    private int PlayerCount;

    private Boolean isJoined;


    public List<Map<Date, Integer>> getFreeTimeList() {
        return freeTimeList;
    }

    public void setFreeTimeList(List<Map<Date, Integer>> freeTimeList) {
        this.freeTimeList = freeTimeList;
    }

    public Boolean getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(Boolean isJoined) {
        this.isJoined = isJoined;
    }

    public int getPlayerCount() {
        return PlayerCount;
    }

    public void setPlayerCount(int playerCount) {
        PlayerCount = playerCount;
    }

    public List<TeamPlayerVO> getTeamPlayerList() {
        return teamPlayerList;
    }

    public void setTeamPlayerList(List<TeamPlayerVO> teamPlayerList) {
        this.teamPlayerList = teamPlayerList;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Integer getAvgAge() {
        return avgAge;
    }

    public void setAvgAge(Integer avgAge) {
        this.avgAge = avgAge;
    }

    public List<String> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<String> ruleList) {
        this.ruleList = ruleList;
    }

    public List<String> getColorList() {
        return colorList;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
    }
}

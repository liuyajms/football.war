package cn.com.weixunyun.child.model.vo;


import cn.com.weixunyun.child.model.bean.Team;

import java.util.List;

public class TeamVO extends Team {
    private Integer avgAge;

    private String courtName;

    private List<String> ruleList;

    private List<String> colorList;

    private List<TeamPlayerVO> teamPlayerList;

    private int PlayerCount;

    private Boolean isJoined;

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

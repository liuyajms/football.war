package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Match;

/**
 * Created by PC on 2015/7/25.
 */
public class MatchVO extends Match{

    private TeamVO team;

    private TeamVO acceptTeam;

    private String ruleName;//赛制

    private String address;//比赛地址

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TeamVO getTeam() {
        return team;
    }

    public void setTeam(TeamVO team) {
        this.team = team;
    }

    public TeamVO getAcceptTeam() {
        return acceptTeam;
    }

    public void setAcceptTeam(TeamVO acceptTeam) {
        this.acceptTeam = acceptTeam;
    }
}

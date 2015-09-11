package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Match;

/**
 * Created by PC on 2015/7/25.
 */
public class MatchVO extends Match{

    private TeamVO team;

    private TeamVO acceptTeam;

    private String ruleName;//赛制

    /**
     * 以下为比赛球场信息
     */
    private String address;//比赛地址

    private String courtName;

    private Double px;

    private Double py;

    private int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Double getPx() {
        return px;
    }

    public void setPx(Double px) {
        this.px = px;
    }

    public Double getPy() {
        return py;
    }

    public void setPy(Double py) {
        this.py = py;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

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

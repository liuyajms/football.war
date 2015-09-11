package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Court;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtVO extends Court {

    private List<String> ruleList;

    private List<CourtServeVO> courtServeList;

    private Boolean isFavorite;

    private int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<CourtServeVO> getCourtServeList() {
        return courtServeList;
    }

    public void setCourtServeList(List<CourtServeVO> courtServeList) {
        this.courtServeList = courtServeList;
    }

    public List<String> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<String> ruleList) {
        this.ruleList = ruleList;
    }
}

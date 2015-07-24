package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Court;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtVO extends Court {

    private List<String> ruleList;

    private List<CourtServeVO> courtServeList;

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
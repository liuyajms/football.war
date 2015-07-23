package cn.com.weixunyun.child.model.vo;

import cn.com.weixunyun.child.model.bean.Court;

import java.util.List;

/**
 * Created by PC on 2015/7/24.
 */
public class CourtVO extends Court {

    private List<String> ruleList;

    public List<String> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<String> ruleList) {
        this.ruleList = ruleList;
    }
}

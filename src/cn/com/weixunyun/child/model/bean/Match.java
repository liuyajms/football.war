package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;

public class Match implements Serializable {
    private static final long serialVersionUID = -6323234265936955615L;
    private Long id;

    private String name;

    @NotNull
    private Integer rule;

    private String fee;

    @NotNull
    private Timestamp beginTime;

    private Timestamp endTime;

    private Long courtId;

    private Integer type;

    private Timestamp createTime;

    private Long createPlayerId;//球赛发起人

    private Timestamp updateTime;

    private Long teamId; //挑战方或训练方，必填字段

    private Long acceptTeamId; //应战方

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getAcceptTeamId() {
        return acceptTeamId;
    }

    public void setAcceptTeamId(Long acceptTeamId) {
        this.acceptTeamId = acceptTeamId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getRule() {
        return rule;
    }

    public void setRule(Integer rule) {
        this.rule = rule;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee == null ? null : fee.trim();
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreatePlayerId() {
        return createPlayerId;
    }

    public void setCreatePlayerId(Long createPlayerId) {
        this.createPlayerId = createPlayerId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
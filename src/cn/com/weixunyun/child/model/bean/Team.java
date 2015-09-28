package cn.com.weixunyun.child.model.bean;

import cn.com.weixunyun.child.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;

public class Team implements Serializable{
    private static final long serialVersionUID = 7827089729283371496L;
    private Long id;

    private String name;

//    @NotNull
    private Integer rule;

    private Integer color;

    private Long courtId;

//    @NotNull
    private String address;

    private String description;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Long createPlayerId;

    private Long srcTeamId;

    private Boolean tmp;

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getTmp() {
        return tmp;
    }

    public void setTmp(Boolean tmp) {
        this.tmp = tmp;
    }

    public Long getSrcTeamId() {
        return srcTeamId;
    }

    public void setSrcTeamId(Long srcTeamId) {
        this.srcTeamId = srcTeamId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
}
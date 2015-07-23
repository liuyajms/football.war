package cn.com.weixunyun.child.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Court implements Serializable{
    private static final long serialVersionUID = -4645615977019188204L;
    private Long id;

    private String name;

    private Integer rule;

    private String mobile;

    private String address;

    private Long px;

    private Long py;

    private String openTime;

    private String description;

    private Boolean tmp;

    private Timestamp createTime;

    private Long createPlayerId;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Long getPx() {
        return px;
    }

    public void setPx(Long px) {
        this.px = px;
    }

    public Long getPy() {
        return py;
    }

    public void setPy(Long py) {
        this.py = py;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime == null ? null : openTime.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Boolean getTmp() {
        return tmp;
    }

    public void setTmp(Boolean tmp) {
        this.tmp = tmp;
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
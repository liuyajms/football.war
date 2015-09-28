package cn.com.weixunyun.child.model.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by PC on 2015/9/10.
 */
public class Near implements Serializable {
    private static final long serialVersionUID = 5884275088900692930L;
    private int type;//球赛1(训练赛),2(友谊赛)、球队3、球员4、球场5

    private Long id;

    private int distance;

    private String address;

    //player
    private String name;

    private Integer sex;//性别

    private Integer age;//球员年龄

    private Integer dic;//数据字典
    private List<String> dicList;//球员场上位置，球队赛制，球场赛制，

    private String description;

    //court
    private String detailAddress;

    //team
//    private Integer rule;
//    private List<String> ruleList;
//    private Integer count;

    //match
    private Timestamp beginTime;

    private Timestamp endTime;

    private Long acceptTeamId;

    private String acceptTeamName;

    private Integer acceptTeamNum;

    private Long teamId;

    private String teamName;

    private Integer teamNum;

    private String courtName;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(Integer teamNum) {
        this.teamNum = teamNum;
    }

    public Integer getAcceptTeamNum() {
        return acceptTeamNum;
    }

    public void setAcceptTeamNum(Integer acceptTeamNum) {
        this.acceptTeamNum = acceptTeamNum;
    }

    public String getAcceptTeamName() {
        return acceptTeamName;
    }

    public void setAcceptTeamName(String acceptTeamName) {
        this.acceptTeamName = acceptTeamName;
    }

    public Long getAcceptTeamId() {
        return acceptTeamId;
    }

    public void setAcceptTeamId(Long acceptTeamId) {
        this.acceptTeamId = acceptTeamId;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDicList() {
        return dicList;
    }

    public void setDicList(List<String> dicList) {
        this.dicList = dicList;
    }

    public Integer getDic() {
        return dic;
    }

    public void setDic(Integer dic) {
        this.dic = dic;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

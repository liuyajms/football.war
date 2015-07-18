package cn.com.weixunyun.child.module.news;

import java.io.Serializable;

public class News implements Serializable {
	private Long id;
	private Long schoolId;
	private String title;
	private String titleShort;
	private String titleColor;
	private String tag;
	private Long ord;
	private String source;
	private String author;
	private String descriptionSummary;
	private String description;
	private Boolean comment;
	private Long clicked;
	private Boolean up;
	private Long createTeacherId;
	private java.sql.Timestamp createTime;
	private Long updateTeacherId;
	private java.sql.Timestamp updateTime;
	private Long auditTeacherId;
	private java.sql.Timestamp auditTime;
	private Boolean pic;
	private Long type;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitleShort(String titleShort) {
		this.titleShort = titleShort;
	}

	public String getTitleShort() {
		return titleShort;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setOrd(Long ord) {
		this.ord = ord;
	}

	public Long getOrd() {
		return ord;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setDescriptionSummary(String descriptionSummary) {
		this.descriptionSummary = descriptionSummary;
	}

	public String getDescriptionSummary() {
		return descriptionSummary;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setComment(Boolean comment) {
		this.comment = comment;
	}

	public Boolean getComment() {
		return comment;
	}

	public void setClicked(Long clicked) {
		this.clicked = clicked;
	}

	public Long getClicked() {
		return clicked;
	}

	public void setUp(Boolean up) {
		this.up = up;
	}

	public Boolean getUp() {
		return up;
	}

	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}

	public Long getCreateTeacherId() {
		return createTeacherId;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}

	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}

	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setAuditTeacherId(Long auditTeacherId) {
		this.auditTeacherId = auditTeacherId;
	}

	public Long getAuditTeacherId() {
		return auditTeacherId;
	}

	public void setAuditTime(java.sql.Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public java.sql.Timestamp getAuditTime() {
		return auditTime;
	}

	public void setPic(Boolean pic) {
		this.pic = pic;
	}

	public Boolean getPic() {
		return pic;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getType() {
		return type;
	}

}
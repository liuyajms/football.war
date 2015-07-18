package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;

public class StudentGrow implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long studentId;
	private Long schoolId;
	private String type;
	private String name;
	private String description;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp updateTime;
	private Long createTeacherId;
	private Long updateTeacherId;
	private String term;


    //other
    private String typeName; //成长类型名称
    private String classesName;
    private String studentName;


    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}
	public java.sql.Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Long getCreateTeacherId() {
		return createTeacherId;
	}
	public void setCreateTeacherId(Long createTeacherId) {
		this.createTeacherId = createTeacherId;
	}
	public Long getUpdateTeacherId() {
		return updateTeacherId;
	}
	public void setUpdateTeacherId(Long updateTeacherId) {
		this.updateTeacherId = updateTeacherId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}

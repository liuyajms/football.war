package cn.com.weixunyun.child.model.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Halt implements Serializable {
	private Long id;
	private Date time;
	private String type;
	private String description;
	private String readed;
	private Timestamp readedTime;
	private Boolean available;

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Timestamp getReadedTime() {
		return readedTime;
	}

	public void setReadedTime(Timestamp readedTime) {
		this.readedTime = readedTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReaded() {
		return readed;
	}

	public void setReaded(String readed) {
		this.readed = readed;
	}

}
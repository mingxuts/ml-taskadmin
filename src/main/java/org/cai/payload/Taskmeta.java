package org.cai.payload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Taskmeta {
	
	private final Gson gson;	

	@Expose
    private String scptxt;
	@Expose
    private String id;
	@Expose
    private String desc;
	@Expose
    private Integer subtask;
	@Expose
    private String stime;
	@Expose
    private String etime;
	@Expose
    private String host;
	@Expose
    private String cmd;

    public Taskmeta() {
    	this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

	public Taskmeta(String id, String scptxt) {
		super();
		this.scptxt = scptxt;
		this.id = id;
		this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}
	
	public String toJson() {
		return gson.toJson(this);
	}

	public String getScptxt() {
		return scptxt;
	}

	public String getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSubtask() {
		return subtask;
	}

	public void setSubtask(Integer subtask) {
		this.subtask = subtask;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

}

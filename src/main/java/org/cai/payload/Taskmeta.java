package org.cai.payload;

public class Taskmeta {

    private String scptxt;
    private String id;
    private String desc;
    private Integer subtask;
    private String stime;
    private String etime;
    private String host;
    private String cmd;

    public Taskmeta() {
    }

	public Taskmeta(String id, String scptxt) {
		super();
		this.scptxt = scptxt;
		this.id = id;
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

package org.cai.payload;

public class HelloMessage {
	
    private String envname;
    private String cudadevs;
    private String runcmd;
    private String workdir;

    public HelloMessage() {
    }

	public HelloMessage(String envname, String cudadevs, String runcmd, String workdir) {
		super();
		this.envname = envname;
		this.cudadevs = cudadevs;
		this.runcmd = runcmd;
		this.workdir = workdir;
	}

	public String getEnvname() {
		return envname;
	}

	public void setEnvname(String envname) {
		this.envname = envname;
	}

	public String getRuncmd() {
		return runcmd;
	}

	public void setRuncmd(String runcmd) {
		this.runcmd = runcmd;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	public String getCudadevs() {
		return cudadevs;
	}

	public void setCudadevs(String cudadevs) {
		this.cudadevs = cudadevs;
	}
	
}

package org.cai.payload;

public class HelloMessage {
	
    private String envname;
    private String cudadev;
    private String runcmd;

    public HelloMessage() {
    }

	public HelloMessage(String envname, String cudadev, String runcmd) {
		super();
		this.envname = envname;
		this.cudadev = cudadev;
		this.runcmd = runcmd;
	}

	public String getEnvname() {
		return envname;
	}

	public void setEnvname(String envname) {
		this.envname = envname;
	}

	public String getCudadev() {
		return cudadev;
	}

	public void setCudadev(String cudadev) {
		this.cudadev = cudadev;
	}

	public String getRuncmd() {
		return runcmd;
	}

	public void setRuncmd(String runcmd) {
		this.runcmd = runcmd;
	}

	
}

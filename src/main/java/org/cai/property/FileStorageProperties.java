package org.cai.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

	private String taskdir;

	public String getTaskdir() {
		return taskdir;
	}

	public void setTaskdir(String taskdir) {
		this.taskdir = taskdir;
	}
}

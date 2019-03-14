package org.cai.payload;

public class Script {

	private String interpreter;
	private String lineEndings;
	private String envPrepare;
	
	
	public Script() {
		// Default value for those are:
		this.interpreter = "#!/bin/bash";
		this.lineEndings = "\n";
		this.envPrepare = "source ~/anaconda3/bin/activate";
	}

	public Script(String interpreter, String lineEndings, String envPrepare) {
		super();
		this.interpreter = interpreter;
		this.lineEndings = lineEndings;
		this.envPrepare = envPrepare;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public String getLineEndings() {
		return lineEndings;
	}

	public String getEnvPrepare() {
		return envPrepare;
	}
	
	public String toString(String env, String device, String cmd, String workdirectory) {
		//String str = this.interpreter;
		//str += this.lineEndings;
		//str += this.lineEndings;
		String str = this.envPrepare + " " + env;
		str += addCommaEnd();
		if (! "".equalsIgnoreCase(workdirectory.trim())) {
			str += "cd " + workdirectory;
			str += addCommaEnd();
		}
		str += "export CUDA_VISIBLE_DEVICES=" + device + addCommaEnd();
		str += cmd + addCommaEnd();
		str += "exit" + addCommaEnd();
		return str;
	}
	
	private String addCommaEnd() {
		return ";" +  this.lineEndings;
	}
}

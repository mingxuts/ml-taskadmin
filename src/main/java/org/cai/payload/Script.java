package org.cai.payload;

public class Script {

	private String interpreter;
	private String lineEndings;
	private String envPrepare;
	private String ON_ALL_AVL_CUDA = "-1";
	
	
	public Script() {
		// Default value for those are:
		this.interpreter = "#!/bin/bash";
		this.lineEndings = "\n";
		this.envPrepare = ". ~/anaconda3/etc/profile.d/conda.sh;conda activate";
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
		String str = this.interpreter;
		str += this.lineEndings;
		str += this.envPrepare + " " + env;
		str += addCommaEnd();
		if (! "".equalsIgnoreCase(workdirectory.trim())) {
			str += "cd " + workdirectory;
			str += addCommaEnd();
		}
		// We will determin if this machine is exclusive to us, 
		// then we use -1 means run all GPUs
		if (!ON_ALL_AVL_CUDA.equals(device.trim()))
		  str += "export CUDA_VISIBLE_DEVICES=" + device + addCommaEnd();
		str += cmd + addCommaEnd();
		return str;
	}
	
	private String addCommaEnd() {
		return ";" +  this.lineEndings;
	}
}

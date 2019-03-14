package org.cai.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class CommandService {

	public String executeCommand(String[] command, String[] envp, File dir) {
		
		//System.out.println("running subprocess, " + command);
		StringBuffer output = new StringBuffer();

		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command, envp, dir);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("something wrong:" + e.getMessage());
			if (p!= null) {
				p.destroy();
			}
		}

		System.out.println(output.toString());
		return output.toString();
	}
}

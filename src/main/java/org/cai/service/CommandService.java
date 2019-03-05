package org.cai.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class CommandService {

	public String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (p!= null) {
				p.destroy();
			}
		}

		return output.toString();
	}
}
package org.cai.controller;

import java.io.File;

import org.cai.payload.Taskmeta;
import org.cai.payload.HelloMessage;
import org.cai.payload.Script;
import org.cai.service.CommandService;
import org.cai.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.uuid.Generators;

@Controller
public class GreetingController {

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private CommandService cmdService;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Taskmeta greeting(HelloMessage message) throws Exception {
		Thread.sleep(200); // simulated delay
		
		return handle(message);
	}

	private Taskmeta handle(HelloMessage message) {
		Script shscr = new Script();
		String env = message.getEnvname();
		String dev = message.getCudadevs();
		String cmd = message.getRuncmd();
		String workdir = message.getWorkdir();
		System.out.print(shscr.toString(env, dev, cmd, workdir));

		String taskdir = Generators.randomBasedGenerator().generate().toString();
		String script = fileStorageService.saveText(taskdir, "script.txt", shscr.toString(env, dev, cmd, workdir));
		
		Taskmeta taskmeta = new Taskmeta(taskdir, script);
		taskmeta.setCmd(cmd);
		return taskmeta;

	}
}

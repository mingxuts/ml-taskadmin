package org.cai.controller;

import java.io.File;

import org.cai.payload.Greeting;
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
	private  CommandService cmdService ;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        handle(message);
        return new Greeting("Command, " + HtmlUtils.htmlEscape(message.getRuncmd()) + "!");
    }
    
    private void handle(HelloMessage message) {
    	Script shscr = new Script();
    	String env = message.getEnvname();
    	String dev = message.getCudadevs();
    	String cmd = message.getRuncmd();
    	String workdir = message.getWorkdir();
    	System.out.print(shscr.toString(env, dev, cmd, workdir));
    	
    	String taskdir = Generators.randomBasedGenerator().generate().toString();
    	String script = fileStorageService.saveText(taskdir, "script.txt", shscr.toString(env, dev, cmd, workdir));
    	
    	File scriptFile = new File(script);
    	String ssh_cmd = "cd all-task/b98574a2-67a1-48e7-8574-a267a1d8e7eb; ssh delta < script.txt";
    	//ssh_cmd += " |& tee " + scriptFile.getParent() + "/cmd.txt";
    	cmdService.executeCommand(ssh_cmd);
    }
}

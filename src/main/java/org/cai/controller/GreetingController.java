package org.cai.controller;

import org.cai.payload.Greeting;
import org.cai.payload.HelloMessage;
import org.cai.payload.Script;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        handle(message);
        return new Greeting("Command, " + HtmlUtils.htmlEscape(message.getRuncmd()) + "!");
    }
    
    private void handle(HelloMessage message) {
    	Script shscr = new Script();
    	System.out.print(shscr.toString("tf-gpu", "2", message.getRuncmd()));
    }
}

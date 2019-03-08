package org.cai.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.cai.payload.Taskmeta;
import org.cai.taskexecution.TaskRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StateController {
	
	@Autowired
	private TaskRunnable taskRunnable;

	@MessageMapping("/taskcommence")
	public String start(List<Taskmeta> taskmetaList) {
		Thread newThread = new Thread(taskRunnable);
		taskRunnable.setTaskmetaList(taskmetaList);
		newThread.start();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	
}

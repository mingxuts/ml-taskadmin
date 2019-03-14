package org.cai.taskexecution;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.cai.payload.Taskmeta;
import org.cai.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


// A bash tutorial which I think very good: http://tldp.org/HOWTO/Bash-Prog-Intro-HOWTO-7.html
@Component
public class TaskRunnable implements Runnable {
	
	private final String channeltoSend = "/topic/alltaskfinished";
	
	private @Autowired SimpMessagingTemplate template;
	
	private List<Taskmeta> taskmetaList = null;
	
	private @Autowired CommandService cmdService;
	
	private @Value("${system.shell}") String shell;
	
	private @Value("${keyfile-path}") String keyfile;
	
    private boolean doStop = false;

    public void setTaskmetaList(List<Taskmeta> taskmetaList) {
		this.taskmetaList = taskmetaList;
	}

	public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }
    

	@Override
	public void run() {		
		int idx = 0;
		if (taskmetaList != null) {
			for (Taskmeta task : taskmetaList) {
				idx += 1;
				try {
					Thread.sleep(200);
					doTask(task);
					System.out.println("task " + idx + "@" + task.getHost() + " is finished");
				} catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			}
		}
		
	}
	
	public void doTask(Taskmeta taskmeta) {
		File scriptFile = new File(taskmeta.getScptxt());
		String ssh_cmd = "cd %1$s; ssh %2$s $(cat script.txt) | tee -a out.txt ";
		String login;
		if ("".equals(keyfile)) {
			login = taskmeta.getHost();
		} else 
			login = " -i " + keyfile + " " + taskmeta.getHost();
		ssh_cmd = String.format(ssh_cmd, scriptFile.getParent(), login);
		//String ssh_cmd = "ssh delta date";
		//TODO now on windows the shell has been set to powershell, remeber to change it if on win
		String[] cmds = {shell, "-c", ssh_cmd };
		this.cmdService.executeCommand(cmds);

		String etime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		taskmeta.setEtime(etime);
		String json = taskmeta.toJson();
		System.out.println(json);		
		this.template.convertAndSend(channeltoSend, json);
	}

}

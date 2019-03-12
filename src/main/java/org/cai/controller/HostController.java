package org.cai.controller;

import org.cai.payload.Hostnames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HostController {
	
	
	private @Value("${remote.server}") String[] servers;
	
	@GetMapping("/getslaves")
	public Hostnames getSlaves() {
		return new Hostnames(servers);
	}
}

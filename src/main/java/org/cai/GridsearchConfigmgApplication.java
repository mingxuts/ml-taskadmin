package org.cai;

import org.cai.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;



@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class GridsearchConfigmgApplication {

	public static void main(String[] args) {
		SpringApplication.run(GridsearchConfigmgApplication.class, args);
	}

}

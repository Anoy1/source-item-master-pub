package com.anoy.load.item.master.pub.sourceitemmasterpub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SourceItemMasterPubApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SourceItemMasterPubApplication.class)
		.properties("spring.config.name:bootstrap")
		.build()
		.run(args);
		//SpringApplication.run(SourceItemMasterPubApplication.class, args);
	}

}

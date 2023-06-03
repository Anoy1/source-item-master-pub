package com.anoy.load.item.master.pub.sourceitemmasterpub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring")
public class DatabaseConfiguration {
	private DataSourceMySql dataSourceMySql;
	
	public static class DataSourceMySql {
		private String url;
		private String username;
		private String password;
	}

}

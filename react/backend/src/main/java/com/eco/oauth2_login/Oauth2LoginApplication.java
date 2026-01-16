package com.eco.oauth2_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@RestController
@EnableAsync
@org.springframework.scheduling.annotation.EnableScheduling
public class Oauth2LoginApplication {
	public static void main(String[] args) {
		SpringApplication.run(Oauth2LoginApplication.class, args);
	}

}

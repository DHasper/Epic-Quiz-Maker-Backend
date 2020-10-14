package com.quizmaker.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class StartBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartBackendApplication.class, args);
	}

}

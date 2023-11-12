package com.example.kyfbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class KyfBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KyfBackendApplication.class, args);
	}

}

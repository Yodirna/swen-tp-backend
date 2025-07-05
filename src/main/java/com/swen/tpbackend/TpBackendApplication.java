package com.swen.tpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class TpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpBackendApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "Hello, Spring Boot!";
	}
}




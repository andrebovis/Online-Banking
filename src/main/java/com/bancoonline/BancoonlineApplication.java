package com.bancoonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bancoonline")
public class BancoonlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoonlineApplication.class, args);
	}

}
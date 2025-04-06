package com.dmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dmart")

public class DmartReadyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmartReadyApplication.class, args);
	}

}

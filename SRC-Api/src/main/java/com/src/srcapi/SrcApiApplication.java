package com.src.srcapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SrcApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrcApiApplication.class, args);
	}

}

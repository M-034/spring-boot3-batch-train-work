package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//元コード(自動生成)
		//SpringApplication.run(DemoApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		int exitCode = SpringApplication.exit(context);
		System.exit(exitCode);
	}
}

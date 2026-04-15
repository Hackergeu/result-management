package com.school.result_management;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResultManagementApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		System.setProperty("DB_URL", dotenv.get("DB_URL", ""));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME", ""));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD", ""));

		SpringApplication.run(ResultManagementApplication.class, args);
	}
}
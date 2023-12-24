package com.example.workTogetherTimeTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.example.workTogetherTimeTracker")
public class WorkTogetherTimeTrackerApp {

	public static void main(String[] args) {
		SpringApplication.run(WorkTogetherTimeTrackerApp.class, args);
	}

}

package com.laptopfriend.ai;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "LaptopFriend AI API", version = "1.0", description = "Voice AI Laptop Controller")
)
public class LaptopfriendAiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaptopfriendAiBackendApplication.class, args);
	}

}

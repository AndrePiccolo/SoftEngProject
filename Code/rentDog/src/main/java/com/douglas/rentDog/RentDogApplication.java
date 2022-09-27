package com.douglas.rentDog;

import com.douglas.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ConfigProperties.class)
public class RentDogApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentDogApplication.class, args);
	}

}

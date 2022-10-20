package com.douglas.rentDogWeb;

import com.douglas.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ConfigProperties.class)
public class RentDogWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentDogWebApplication.class, args);
	}

}

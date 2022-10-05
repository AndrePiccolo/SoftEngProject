package com.douglas.contract;

import com.douglas.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ConfigProperties.class)
public class ContractApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractApplication.class, args);
	}

}

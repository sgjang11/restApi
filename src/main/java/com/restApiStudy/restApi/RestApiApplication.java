package com.restApiStudy.restApi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

	//ModelMapper사용할 경우 bean으로 추가해줘야함
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}

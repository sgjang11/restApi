package com.restApiStudy.restApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

	//ModelMapper사용할 경우 bean으로 추가해줘야함 (AppConfig로 이동)
//	@Bean
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//	}
}

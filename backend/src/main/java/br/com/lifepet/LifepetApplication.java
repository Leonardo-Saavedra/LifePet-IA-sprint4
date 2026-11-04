package br.com.lifepet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LifepetApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifepetApplication.class, args);
	}
}

package io.codurance.pouch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class PouchApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PouchApiApplication.class, args);
	}
}

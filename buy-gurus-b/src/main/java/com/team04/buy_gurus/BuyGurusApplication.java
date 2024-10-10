package com.team04.buy_gurus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BuyGurusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyGurusApplication.class, args);
	}

}

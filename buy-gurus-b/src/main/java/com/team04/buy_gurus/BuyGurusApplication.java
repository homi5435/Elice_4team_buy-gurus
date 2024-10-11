package com.team04.buy_gurus;

import com.team04.buy_gurus.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// @EnableConfigurationProperties(JwtProperties.class)
@EnableJpaAuditing
public class BuyGurusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyGurusApplication.class, args);
	}

}

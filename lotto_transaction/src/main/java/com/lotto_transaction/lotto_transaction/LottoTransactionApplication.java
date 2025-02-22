package com.lotto_transaction.lotto_transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Habilita Feign en este microservicio
public class LottoTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottoTransactionApplication.class, args);
	}

}

package br.com.ifpe.eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EventosBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventosBackendApplication.class, args);
	}

}

package com.example.demo;

import com.example.demo.entity.SessionEntity;
import com.example.demo.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		ZoneId zonaHorariaSistema = ZoneId.systemDefault();
		System.out.println("Zona horaria del sistema: " + zonaHorariaSistema);

		ZoneId zonaHorariaSistema2 = ZoneId.systemDefault();
		ZonedDateTime horaActual = ZonedDateTime.now(zonaHorariaSistema2);
		System.out.println("Hora actual en " + horaActual);

	}




}

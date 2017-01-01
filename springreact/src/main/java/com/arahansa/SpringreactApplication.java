package com.arahansa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
@SpringBootApplication
@EnableAsync
public class SpringreactApplication {

	@Component
	public static class MyService{
		@Async
		public ListenableFuture<String> hello() throws InterruptedException {
			log.info("hello()");
			Thread.sleep(2000);
			return new AsyncResult<>("Hello");
		}
	}

	public static void main(String[] args) {
		System.setProperty("server.tomcat.max-threads","20");
		SpringApplication.run(SpringreactApplication.class, args);
	}

}

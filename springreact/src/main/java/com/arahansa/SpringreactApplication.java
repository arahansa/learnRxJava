package com.arahansa;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@SpringBootApplication
@EnableAsync
public class SpringreactApplication {

	@RestController
	public static class MyController {

		@GetMapping("/callable")
		public String callable() throws InterruptedException {
			log.info("async");
			Thread.sleep(2000);
			return "hello";
		}
//		public Callable<String> callable() throws InterruptedException {
//			log.info("callable");
//			return () -> {
//				log.info("async");
//				Thread.sleep(1000);
//				return "hello";
//			};
//		}
	}



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

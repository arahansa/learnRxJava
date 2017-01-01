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
import org.springframework.expression.spel.InternalParseException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
@SpringBootApplication
@EnableAsync
public class SpringreactApplication {

	@RestController
	public static class MyController {

		Queue<DeferredResult<String>> results = new ConcurrentLinkedQueue<>();

		@GetMapping("/dr")
		public DeferredResult<String> dr() throws InterruptedException{
			log.info("dr");
			DeferredResult<String> dr = new DeferredResult<>(60000000L);
			results.add(dr);
			return dr;
		}

		@GetMapping("/dr/count")
		public String drcount(){
			return String.valueOf(results.size());
		}

		@GetMapping("/dr/event")
		public String drcount(String msg){
			for(DeferredResult<String> dr : results){
				dr.setResult("Hello " + msg);
				results.remove(dr);
			}
			return "OK";
		}

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

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
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

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
		try(ConfigurableApplicationContext c = SpringApplication.run(SpringreactApplication.class, args)){

		}
	}

	@Autowired MyService myService;

	@Bean
	ApplicationRunner run(){
		return args->{
			log.info("run()");
			ListenableFuture<String> f = myService.hello();
			f.addCallback(s-> System.out.println(s), e-> System.out.println(e.getMessage()));
			log.info("exit");
		};
	}


}

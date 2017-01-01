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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@Slf4j
@SpringBootApplication
@EnableAsync
public class SpringreactApplication {

	@RestController
	public static class Controller{
		@RequestMapping("/hello")
		public Publisher<String> hello(String name){
			return (sub)->{
				sub.onSubscribe(new Subscription() {
					@Override
					public void request(long l) {
						sub.onNext("Hello :"+name);
						sub.onComplete();
					}

					@Override
					public void cancel() {

					}
				});
			};
		}
	}

	@Component
	public static class MyService{
		@Async
		public Future<String> hello() throws InterruptedException {
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
			final Future<String> hello = myService.hello();
			log.info("exit : "+ hello.isDone());
			log.info("exit "+ hello.get());
		};
	}


}

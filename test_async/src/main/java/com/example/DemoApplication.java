package com.example;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

@EnableAsync
@lombok.extern.slf4j.Slf4j
@SpringBootApplication
public class DemoApplication {

	public static final String URL = "http://localhost:8091/service?req={req}";
	public static final String URL2 = "http://localhost:8091/service2?req={req}";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RestController
	public static class MyController {
		AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
		@Autowired MyService myService;


		@GetMapping("/rest")
		public DeferredResult<String> rest(int idx){
			DeferredResult<String> dr = new DeferredResult<>();

			log.info("idx : {}", idx);
			ListenableFuture<ResponseEntity<String>> f1 = rt.getForEntity(URL, String.class, "hello" + idx);
			f1.addCallback(s->{
				ListenableFuture<ResponseEntity<String>> f2 = rt.getForEntity(URL2, String.class, s.getBody());
				f2.addCallback(s2->{
					ListenableFuture<String> f3 = myService.work(s2.getBody());
					f3.addCallback(s3->{
						dr.setResult(s3);
					}, e->{
						dr.setErrorResult(e.getMessage());
					});
				}, e->{dr.setErrorResult(e.getMessage());});
			}, e->{
				dr.setErrorResult(e.getMessage());
			});
			return dr;
		}

		@Service
		public static class MyService{
			@Async
			public ListenableFuture<String> work(String req){
				return new AsyncResult<>(req +"/asyncwork");
			}
		}

		@Bean
		ThreadPoolTaskExecutor myThreadPool(){
			ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
			// 코어 - 큐 - 맥스풀
			te.setCorePoolSize(1);
			te.setMaxPoolSize(1);
			te.initialize();
			return te;
		}


		@GetMapping("/emitter")
		public ResponseBodyEmitter emitter() {
			ResponseBodyEmitter emitter = new ResponseBodyEmitter();
			Executors.newSingleThreadExecutor().submit(() -> {
				try {
					for (int i = 0; i <= 50; i++) {
						emitter.send("<p>Stream " + i + "</p>");
						Thread.sleep(2000);
					}
				} catch (Exception e) {
				}
			});
			return emitter;
		}

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
}

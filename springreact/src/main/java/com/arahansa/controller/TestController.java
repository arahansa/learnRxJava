package com.arahansa.controller;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController{
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
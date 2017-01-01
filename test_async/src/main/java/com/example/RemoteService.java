package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by arahansa on 2017-01-01.
 */
@Slf4j
@SpringBootApplication
public class RemoteService {

    @RestController
    public static class MyController {

        @GetMapping("/service")
        public String rest(String req) throws InterruptedException {
            log.info("req : {}", req);
            Thread.sleep(2000);
            return req +"/service";
        }
    }
    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8091");
        System.setProperty("server.tomcat.max.max-threads", "1000");
        SpringApplication.run(RemoteService.class, args);
    }

}

package com.example;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by arahansa on 2017-01-01.
 */
@Slf4j
public class LoadTest {

    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.toLevel("info"));
        System.setProperty("logging.level", "INFO");
        System.setProperty("logging.level.org.springframework", "INFO");

        ExecutorService es = Executors.newFixedThreadPool(100);
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8090/rest?idx={idx}";

        CyclicBarrier barrier = new CyclicBarrier(101);

        for(int i=0;i<100;i++){
            es.submit(()->{
                int idx = counter.addAndGet(1);
                barrier.await();
                log.info("Thread {}", idx);
                StopWatch sw = new StopWatch();
                sw.start();
                final String res = rt.getForObject(url, String.class, idx);
                sw.stop();
                log.info("Elapsed: {} -> {} . {}" , idx, +sw.getTotalTimeSeconds(), res);
                return null;
            });
        }
        barrier.await();
        StopWatch main = new StopWatch();
        main.start();
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        main.stop();
        log.info("Total : {} ", main.getTotalTimeSeconds());
    }
}

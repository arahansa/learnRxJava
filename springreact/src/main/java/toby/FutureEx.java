package toby;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by arahansa on 2017-01-01.
 */
@Slf4j
public class FutureEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        Future<String> f = es.submit(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });
        System.out.println(f.get());
        log.info("Exit");
    }

}

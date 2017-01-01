package toby;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by arahansa on 2017-01-01.
 */
@Slf4j
public class FutureEx {

    // Future
    // Callback

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<>(()->{
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });

        es.execute(f);

        System.out.println(f.isDone());
        Thread.sleep(2100);
        log.info("Exit");
        System.out.println(f.isDone());
        System.out.println(f.get()); // Blocking..  Non-Blocking
    }

}

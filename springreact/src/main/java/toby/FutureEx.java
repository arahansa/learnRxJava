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

        FutureTask<String> f = new FutureTask<String>(()->{
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        }){
            @Override
            protected void done() {
                try {
                    System.out.println(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(f);
        es.shutdown();
    }

}

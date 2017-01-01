package toby;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by arahansa on 2017-01-01.
 */
@Slf4j
public class FutureEx {

    interface SuccessCallback {
        void onSucessCallback(String result);
    }

    public static class CallbackFutreTask extends FutureTask<String> {
        SuccessCallback sc;

        public CallbackFutreTask(Callable<String> callable, SuccessCallback sc) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
        }

        @Override
        protected void done() {
            try {
                sc.onSucessCallback(get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutreTask f = new CallbackFutreTask(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        }, System.out::println);

        es.execute(f);
        es.shutdown();
    }

}

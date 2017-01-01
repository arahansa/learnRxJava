package toby;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

// Future
// Callback
// .. ?

/**
 * Created by arahansa on 2017-01-01.
 */
@Slf4j
public class FutureEx {

    interface SuccessCallback {
        void onSucessCallback(String result);
    }
    interface ExceptionCallback{
        void onError(Throwable t);
    }

    public static class CallbackFutreTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutreTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSucessCallback(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutreTask f = new CallbackFutreTask(() -> {
            Thread.sleep(2000);
            if(1==1) throw new RuntimeException("Async ERROR");
            log.info("Async");
            return "Hello";
        }, s-> System.out.println("Result : "+s)
         , e-> System.out.println("Error :"+e.getMessage()));

        es.execute(f);
        es.shutdown();
    }

}

package live;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by jarvis on 2016. 12. 18..
 */
@Slf4j
public class FluxScEx {
  public static void main(String[] args) throws InterruptedException {
    Flux.interval(Duration.ofMillis(200))
        .take(10)
        .subscribe(s->{log.debug("onNext:{}", s);});
    log.debug("exit");
    TimeUnit.SECONDS.sleep(5);
  }
}

package live;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Created by jarvis on 2016. 12. 18..
 */
public class FluxScEx {
  public static void main(String[] args) {
    Flux.range(1, 10)
            .publishOn(Schedulers.newSingle("pub"))
            .log()
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(System.out::println);

    System.out.println("Exit.");
  }
}

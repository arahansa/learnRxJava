package live;

import reactor.core.publisher.Flux;

/**
 * Created by jarvis on 2016. 12. 4..
 */
public class ReactorEx {
  public static void main(String[] args) {
    Flux.<Integer>create(e->{
      e.next(1);
      e.next(2);
      e.next(3);
      e.complete();
    })
    //.log()
    .map(s->s*10)
    .subscribe(System.out::println);
  }
}

package live;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * Created by jarvis on 2016. 12. 3..
 * Publiser -> Data1 -> Operator --> Dat2 -> Op2 -> Data3 -> Subscriber
 */
public class PubSub {
  public static void main(String[] args) {

    // Publisher
    Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(toList()));
    Publisher<Integer> mapPub = mapPub(pub, s->s*10);
    mapPub.subscribe(logSub());

  }

  private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f){
    return new Publisher<Integer>() {
      @Override
      public void subscribe(Subscriber<? super Integer> sub) {
        pub.subscribe(new Subscriber<Integer>() {
          @Override
          public void onSubscribe(Subscription s) {
            sub.onSubscribe(s);
          }

          @Override
          public void onNext(Integer i) {
            sub.onNext(i);
          }

          @Override
          public void onError(Throwable t) {
            sub.onError(t);
          }

          @Override
          public void onComplete() {
            sub.onComplete();
          }
        });
      }
    };
  }

  private static Subscriber<Integer> logSub() {
    return new Subscriber<Integer>() {
        @Override
        public void onSubscribe(Subscription s) {
          System.out.println("onSub");
          s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(Integer i) {
          System.out.println("onNext:"+i);
        }

        @Override
        public void onError(Throwable t) {
          System.out.println("onError");
        }

        @Override
        public void onComplete() {
          System.out.println("onComplete");
        }
      };
  }

  private static Publisher<Integer> iterPub(List<Integer> iter) {
    return new Publisher<Integer>() {
      @Override
      public void subscribe(Subscriber<? super Integer> sub) {
        sub.onSubscribe(new Subscription() {
          @Override
          public void request(long n) {
            try{
              iter.forEach(s->sub.onNext(s));
              sub.onComplete();
            }catch(Throwable t){
              sub.onError(t);
            }
          }

          @Override
          public void cancel() {

          }
        });
      }
    };
  }


}

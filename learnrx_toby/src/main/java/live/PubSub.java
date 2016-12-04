package live;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
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
//    Publisher<List> mapPub = mapPub(pub, s-> Collections.singletonList(s));
    //Publisher<String> reducePub = reducePub(pub, "", (a,b)->a+"-"+b);
    Publisher<StringBuilder> reducePub = reducePub(pub, new StringBuilder(), (a,b)->a.append(b+","));
    reducePub.subscribe(logSub());
  }

  private static <T,R> Publisher<R> reducePub(Publisher<T> pub, R init, BiFunction<R, T, R> bf) {
    return (sub)-> pub.subscribe(new DelegateSub<T, R>(sub){
      R result = init;
      @Override
      public void onNext(T i) {
        result = bf.apply(result, i);
      }
      @Override
      public void onComplete() {
        sub.onNext(result);
        sub.onComplete();
      }
    });
  }


  private static <T, R> Publisher<R> mapPub(Publisher<T> pub, Function<T, R> f){
    return new Publisher<R>() {
      @Override
      public void subscribe(Subscriber<? super R> sub) {
        pub.subscribe(new DelegateSub<T, R>(sub) {
          @Override
          public void onNext(T i) {
            sub.onNext(f.apply(i));
          }
        });
      }
    };
  }

  private static <T> Subscriber<T> logSub() {
    return new Subscriber<T>() {
        @Override
        public void onSubscribe(Subscription s) {
          System.out.println("onSub");
          s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T i) {
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

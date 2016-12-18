package live;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by jarvis on 2016. 12. 18..
 */
public class SchedulerEx {

  public static void main(String[] args) {
    Publisher<Integer> pub = sub ->{
      sub.onSubscribe(new Subscription() {
        @Override
        public void request(long n) {
          sub.onNext(1);
          sub.onNext(2);
          sub.onNext(3);
          sub.onNext(4);
          sub.onNext(5);
          sub.onComplete();
        }

        @Override
        public void cancel() {

        }
      });
    };

    pub.subscribe(new Subscriber<Integer>() {
      @Override
      public void onSubscribe(Subscription s) {
        System.out.println("onSubscribe");
        s.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(Integer integer) {
        System.out.println("onNext: "+integer);
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("onError :"+t);
      }

      @Override
      public void onComplete() {
        System.out.println("onComplete");
      }
    });
  }



}

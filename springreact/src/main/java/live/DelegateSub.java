package live;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by jarvis on 2016. 12. 4..
 */
public class DelegateSub<T, R> implements Subscriber<T> {

  Subscriber sub;
  public DelegateSub(Subscriber<? super R> sub) {
    this.sub = sub;
  }

  @Override
  public void onSubscribe(Subscription s) {
    sub.onSubscribe(s);
  }

  @Override
  public void onNext(T i) {
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
}

package top.lemonsoda.gunners.utils;

import rx.Subscription;

/**
 * Created by Chuan on 09/03/2017.
 */

public class RxUtils {

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null) {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    public static void unsubscribe(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            unsubscribe(subscription);
        }
    }
}

package com.hilifecare.ui.base;

import nucleus.presenter.RxPresenter;
import rx.Subscription;

public abstract class BasePresenter<V extends PresenterView> extends RxPresenter <V> {
    protected Subscription subscription;

    protected void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }


}

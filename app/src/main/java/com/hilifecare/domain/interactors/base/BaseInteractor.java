package com.hilifecare.domain.interactors.base;

import java.util.concurrent.Executor;

public abstract class BaseInteractor {

    protected Executor executor;

    public BaseInteractor(Executor executor) {
        this.executor = executor;
    }

}

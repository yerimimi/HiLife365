package com.hilifecare.domain.executors;

import com.hilifecare.domain.repository.firebase.FirebaseRepository;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface ThreadExecutor extends Executor, FirebaseRepository {

    Future<?> submit(Runnable task);

    <T> Future<T> submit(Callable<T> task);

}

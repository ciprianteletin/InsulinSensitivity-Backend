package com.insulin.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private ExecutorService executorService;

    public void runCallableVoid(Callable<Void> callable) {
        initExecutor();
        this.executorService.submit(callable);
    }

    private void initExecutor() {
        this.executorService = Executors.newSingleThreadExecutor();
    }
}

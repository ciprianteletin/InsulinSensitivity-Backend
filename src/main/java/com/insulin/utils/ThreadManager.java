package com.insulin.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private Thread thread;
    private ExecutorService executorService;

    public void runThread(Runnable run) {
        initThread(run);
        thread.start();
    }

    public void runCallableVoid(Callable<Void> callable) {
        initExecutor();
        this.executorService.submit(callable);
    }

    private void initThread(Runnable runnable) {
        this.thread = new Thread(runnable);
    }

    private void initExecutor() {
        this.executorService = Executors.newSingleThreadExecutor();
    }
}

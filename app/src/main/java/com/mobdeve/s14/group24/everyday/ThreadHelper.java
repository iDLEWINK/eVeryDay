package com.mobdeve.s14.group24.everyday;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class ThreadHelper {

    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final int MAX_POOL_SIZE = CORES + 8;
    private static final int KEEP_ALIVE_TIME = 1500;
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.MILLISECONDS;

    //Sets the configurations for executor with regards to keep alive, and pool size
    public static final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            CORES,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_UNIT,
            new LinkedBlockingDeque<Runnable>()
    );

    //Executes the runnable argument
    public static void execute(Runnable runnable) {
        poolExecutor.execute(runnable);
    }

}

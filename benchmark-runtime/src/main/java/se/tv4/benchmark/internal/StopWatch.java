package se.tv4.benchmark.internal;

/**
 * Created by dimitris.lachanas on 26/10/15.
 */

import java.util.concurrent.TimeUnit;

/**
 * Class representing a StopWatch for measuring time.
 */
public class StopWatch {
    private long startTime = 0;
    private long endTime = 0;
    private long elapsedTime = 0;

    public StopWatch() {
        //empty
    }

    private void reset() {
        startTime = 0;
        endTime = 0;
    }

    public void start() {
        reset();
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        reset();

    }

    public long getTotalTimeMillis() {
        return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(elapsedTime) : 0;
    }

    public long getCurrentTimeMillisDifference() {
        if(startTime == 0) {
            start();
        }
        long currentTime = System.nanoTime();
        elapsedTime = currentTime - startTime;
        return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(currentTime - startTime) : 0;

    }

}
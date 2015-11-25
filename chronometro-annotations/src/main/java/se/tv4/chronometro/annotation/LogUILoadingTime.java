package se.tv4.chronometro.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation enables logging for loading times.
 *
 * The developer can provide a state for declaring if she wants to start or stop counting. Also the developer can just print the loading
 * time difference between this method and the one the counter started to count.
 *
 * When a method has the {@link #START} value in {@link #state()} the loading time will start count right before the execution of the
 * method.
 * When a method has the {@link #END} value in {@link #state()} the loading time will end count right after the execution of the
 * method.
 * When a method has the {@link #CHECKPOINT_START} value in {@link #state()} the loading time will pick the time right before the execution of the
 * method.
 *
 * The name is just an indicator for printing and distinguishing the printed loading times.
 *
 * Created by dimitris.lachanas on 26/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface LogUILoadingTime {

    int START = 0;
    int CHECKPOINT_START = 1;
    int CHECKPOINT_END = 2;
    int END = 3;

    int state();

    String name();

}

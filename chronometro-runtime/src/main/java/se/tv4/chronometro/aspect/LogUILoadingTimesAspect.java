package se.tv4.chronometro.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import se.tv4.chronometro.annotation.LogUILoadingTime;
import se.tv4.chronometro.internal.DebugLog;
import se.tv4.chronometro.internal.StopWatch;

/**
 * This aspect handles the logging of UI loading times, in the methods the developer uses the {@link LogUILoadingTime} annotation
 *
 * Created by dimitris.lachanas on 26/10/15.
 */

@Aspect
public class LogUILoadingTimesAspect {

    private static final String POINTCUT_METHOD =
            "execution(@se.tv4.chronometro.annotation.LogUILoadingTime * *(..))";

    private Map watchMap = new HashMap();

    /**
     * That is the pointcut for all the methods that are annotated with {@link LogUILoadingTime}
     */
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithLogUILoadingTime() {
    }


    /**
     * This is the method that is called right before the annotated method is getting called. The code before {@link
     * ProceedingJoinPoint#proceed()} will run before the execution of the method and the code after that will run after the execution.
     * In the particular method we decide when to log the current time or the difference with the starting time, based on the {@link
     * LogUILoadingTime#state()} the developer provided in the annotation
     *
     * @param joinPoint is the joinPoint that represents the actual method call. Don't forget to call {@link ProceedingJoinPoint#proceed
     * ()} in order for the method to run.
     *
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotatedWithLogUILoadingTime()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();

        Method method = methodSignature.getMethod();

        LogUILoadingTime logUILoadingTimeAnnotation = method.getAnnotation(LogUILoadingTime.class);
        Object result;

        if (logUILoadingTimeAnnotation != null) {

            switch (logUILoadingTimeAnnotation.state()) {
                // here we start the time watcher and log the starting time
                case LogUILoadingTime.START:
                    StopWatch stopWatch = new StopWatch();
                    watchMap.put(logUILoadingTimeAnnotation.name(), stopWatch);
                    stopWatch.start();
                    DebugLog.log(className, buildStartLogMessage(logUILoadingTimeAnnotation.name() + " started"));
                    result = joinPoint.proceed();
                    break;
                // here we get the current time before the method execution and log it as a checkpoint
                case LogUILoadingTime.CHECKPOINT_START:
                    stopWatch = (StopWatch) watchMap.get(logUILoadingTimeAnnotation.name());
                    if(stopWatch != null) {
                        DebugLog.log(className, buildLogMessage(logUILoadingTimeAnnotation.name() + " checkpoint before method " +
                                        method.getName() ,
                                stopWatch
                                .getCurrentTimeMillisDifference()));
                    }
                    result = joinPoint.proceed();
                    break;
                // here we get the current time after the method execution and log it as a checkpoint
                case LogUILoadingTime.CHECKPOINT_END:
                    result = joinPoint.proceed();
                    stopWatch = (StopWatch) watchMap.get(logUILoadingTimeAnnotation.name());
                    if(stopWatch != null) {
                        DebugLog.log(className, buildLogMessage(logUILoadingTimeAnnotation.name() + " checkpoint after method " + method.getName(),
                                stopWatch
                                .getCurrentTimeMillisDifference()));
                    }
                    break;
                // here we stop the time watcher and log the difference in millis from when the time watcher started counting
                case LogUILoadingTime.END:
                    result = joinPoint.proceed();
                    stopWatch = (StopWatch) watchMap.get(logUILoadingTimeAnnotation.name());
                    if(stopWatch != null) {
                        stopWatch.stop();
                        DebugLog.log(className, buildLogMessage(logUILoadingTimeAnnotation.name() + " created", stopWatch.getTotalTimeMillis()));
                    }
                    break;
                // otherwise just proceed
                default:
                    result = joinPoint.proceed();
                    break;
            }

        } else {
            result = joinPoint.proceed();
        }


        return result;
    }

    /**
     * Create a log message.
     *
     * @param methodName     A string with the method name.
     * @param loadingTimeDuration The duration of the loading time
     *
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long loadingTimeDuration) {
        StringBuilder message = new StringBuilder();
        message.append("Chronometro ----> ");
        message.append(methodName);
        message.append(" in ");
        message.append("[");
        message.append(loadingTimeDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }

    /**
     * Create a log message.
     *
     * @param methodName     A string with the method name.
     * @return A string representing message.
     */
    private static String buildStartLogMessage(String methodName) {
        StringBuilder message = new StringBuilder();
        message.append("Chronometro ----> ");
        message.append(methodName);


        return message.toString();
    }
}

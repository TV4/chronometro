package se.tv4.chronometro.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import se.tv4.chronometro.annotation.Chronometro;
import se.tv4.chronometro.internal.DebugLog;
import se.tv4.chronometro.internal.StopWatch;

/**
 * This aspect handles the logging of UI loading times, in the methods the developer uses the {@link Chronometro} annotation
 *
 * Created by dimitris.lachanas on 26/10/15.
 */

@Aspect
public class ChronometroAspect {

    private static final String POINTCUT_METHOD =
            "execution(@se.tv4.chronometro.annotation.Chronometro * *(..))";

    private Map watchMap = new HashMap();

    /**
     * That is the pointcut for all the methods that are annotated with {@link Chronometro}
     */
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithChronometro() {
    }


    /**
     * This is the method that is called right before the annotated method is getting called. The code before {@link
     * ProceedingJoinPoint#proceed()} will run before the execution of the method and the code after that will run after the execution.
     * In the particular method we decide when to log the current time or the difference with the starting time, based on the {@link
     * Chronometro#state()} the developer provided in the annotation
     *
     * @param joinPoint is the joinPoint that represents the actual method call. Don't forget to call {@link ProceedingJoinPoint#proceed
     * ()} in order for the method to run.
     *
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotatedWithChronometro()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();

        Method method = methodSignature.getMethod();

        Chronometro chronometroAnnotation = method.getAnnotation(Chronometro.class);
        Object result;

        if (chronometroAnnotation != null) {

            switch (chronometroAnnotation.state()) {
                // here we start the time watcher and log the starting time
                case Chronometro.START:
                    StopWatch stopWatch = new StopWatch();
                    watchMap.put(chronometroAnnotation.name(), stopWatch);
                    stopWatch.start();
                    DebugLog.log(className, buildStartLogMessage(chronometroAnnotation.name() + " started"));
                    result = joinPoint.proceed();
                    break;
                // here we get the current time before the method execution and log it as a checkpoint
                case Chronometro.CHECKPOINT_START:
                    stopWatch = (StopWatch) watchMap.get(chronometroAnnotation.name());
                    if(stopWatch != null) {
                        DebugLog.log(className, buildLogMessage(chronometroAnnotation.name() + " checkpoint before method " +
                                        method.getName() ,
                                stopWatch
                                .getCurrentTimeMillisDifference()));
                    }
                    result = joinPoint.proceed();
                    break;
                // here we get the current time after the method execution and log it as a checkpoint
                case Chronometro.CHECKPOINT_END:
                    result = joinPoint.proceed();
                    stopWatch = (StopWatch) watchMap.get(chronometroAnnotation.name());
                    if(stopWatch != null) {
                        DebugLog.log(className, buildLogMessage(chronometroAnnotation.name() + " checkpoint after method " + method.getName(),
                                stopWatch
                                .getCurrentTimeMillisDifference()));
                    }
                    break;
                // here we stop the time watcher and log the difference in millis from when the time watcher started counting
                case Chronometro.END:
                    result = joinPoint.proceed();
                    stopWatch = (StopWatch) watchMap.get(chronometroAnnotation.name());
                    if(stopWatch != null) {
                        stopWatch.stop();
                        DebugLog.log(className, buildLogMessage(chronometroAnnotation.name() + " created", stopWatch.getTotalTimeMillis()));
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

# Chronometro

!!! not public yet!!!

The *chronometro* plugin enables developers to use the benchmark library in order to track the loading times for their different classes in their apps.

It's really hard to benchmark loading times when developing especially if you want to track fragment's or activity's creation before and after your network calls have been finished (successfully or not).

This library provides an annotation used to mark the methods you want to start and stop your tracking. You can also mark some methods as checkpoints so the timer doesn't stop, but just reports the loading time so far. 

Tha plugin is inspired and heavily based on [Jake Wharton's Hugo plugin](https://github.com/JakeWharton/hugo), as well as on [Fernando Cejas example on Android Aspect Oriented Programming](http://fernandocejas.com/2014/08/03/aspect-oriented-programming-in-android/).
```java
@LogUILoadingTime(state = LogUILoadingTime.START, name = "Fragment1")
@Override
public void onAttach(Activity activity) {
  super.onAttach(activity);
  // some code here
}
    
@LogUILoadingTime(state = LogUILoadingTime.END, name = "Fragment1")
@Override
public void onDataSuccess(Data data) {
  // some code here
}
```

    
After that you will get some debugging logs like
```
Performance Measure Loading Times --> Fragment1 started
Performance Measure Loading Times --> Fragment1 created --> [2000ms]
```

You can have different timers by providing a different name value in the annotation. Here two states are available on for get the loading time right before the method runs (LogUILoadingTime.CHECKPOINT_START) and one right after (LogUILoadingTime.CHECKPOINT_END)

```java    
// logging the time until after the view has been created
@LogUILoadingTime(state = LogUILoadingTime.CHECKPOINT_END, name = "Fragment1")
@Override
public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
  super.onViewCreated(view, savedInstanceState);
  // some code here
}
    
// logging the time until before the fetching of data
@LogUILoadingTime(state = LogUILoadingTime.CHECKPOINT_START, name = "Fragment1")
@Override
private void fetchData() {
  // some code here
}
```
    
After that you will get some debugging logs like:
  Performance Measure Loading Times --> Fragment1 checkpoint after method onViewCreated --> [68ms]
  Performance Measure Loading Times --> Fragment1 checkpoint before method fetchData --> [108ms]
  
  
You can add it in your project by:
```groovy
buildscript {
  repositories {
    mavenCentral()
  }

dependencies {
  classpath 'se.tv4:benchmark-plugin:1.0.0'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'se.tv4.benchmark'
```

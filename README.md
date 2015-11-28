# Chronometro

!!! not public yet!!!

The *chronometro* plugin enables developers to use the *chronometro* library in order to track the loading times for their different classes in their apps.

Many times while developing we need to time the loading times of our app so we detect possible issues and optimise the app. In android 
it's really hard to do that, especially if you want to track the whole flow of a feature, which most probably goes like that:
Fragment creation -> some networking -> some business logic -> fragment views update

This library provides an annotation used to mark the methods you want in order to start and stop your tracking. You can also mark some 
methods as timer checkpoints so the timer doesn't stop, but just reports the loading time until before this method runs or after 
it finishes. 

Tha plugin is based on Aspect Oriented Programming for android and uses the aspectj library. It is inspired and heavily based on [Jake 
Wharton's Hugo plugin](https://github.com/JakeWharton/hugo), as well as on [Fernando Cejas example on Android Aspect Oriented Programming](http://fernandocejas.com/2014/08/03/aspect-oriented-programming-in-android/).
Also [Sittiphol Phanvilai's article around bintray and packaging was more than helpful!](http://inthecheesefactory.com/blog/how-to-upload-library-to-jcenter-maven-central-as-dependency/en)

```java
@Chronometro(state = Chronometro.START, name = "Fragment1")
@Override
public void onAttach(Activity activity) {
  super.onAttach(activity);
  // some code here
}
    
@Chronometro(state = Chronometro.END, name = "Fragment1")
@Override
public void onDataSuccess(Data data) {
  // some code here
}
```

    
After that you will get some debugging logs like
```
Chronometro ----> Fragment1 started
Chronometro ----> Fragment1 created --> [2000ms]
```

You can have different timers by providing a different name value in the annotation. Here two states are available on for get the loading time right before the method runs (Chronometro.CHECKPOINT_START) and one right after (Chronometro.CHECKPOINT_END)

```java    
// logging the time until after the view has been created
@Chronometro(state = Chronometro.CHECKPOINT_END, name = "Fragment1")
@Override
public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
  super.onViewCreated(view, savedInstanceState);
  // some code here
}
    
// logging the time until before the fetching of data
@Chronometro(state = Chronometro.CHECKPOINT_START, name = "Fragment1")
@Override
private void fetchData() {
  // some code here
}
```
    
After that you will get some debugging logs like:
```
Chronometro ----> Fragment1 checkpoint after method onViewCreated --> [68ms]
Chronometro ----> Fragment1 checkpoint before method fetchData --> [108ms]
```
  
  
You can add it in your project by:
```groovy
buildscript {
  repositories {
    mavenCentral()
  }

dependencies {
  classpath 'se.tv4:chronometro-plugin:1.0.0'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'se.tv4.chronometro'
```

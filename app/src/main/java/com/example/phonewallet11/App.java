package com.example.phonewallet11;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class App extends Application {
    public static Activity activity;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks
                //ActivityLifecycleCallbacks 是用来监听所有 Activity 的生命周期回调
                //registerActivityLifecycleCallbacks使用这个类，我们可以很方便的监听到 Activity 的状态，从而可以判断 APP 是否在前台或者后台
                () {
            @Override
            public void onActivityCreated(@NonNull Activity act, @Nullable Bundle savedInstanceState)
            //定义接口
            //onActivityCreated 回调是在 Activity 的 onCreate 方法中调用
            {
                activity = act;

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}

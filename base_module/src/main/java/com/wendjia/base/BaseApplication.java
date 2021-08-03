package com.wendjia.base;

import android.app.Application;
/**
 * Created by jd on 2020/9/15.
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}


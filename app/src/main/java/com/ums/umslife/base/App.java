package com.ums.umslife.base;

import android.app.Application;

import com.ums.umslife.utils.Utils;


/**
 * Created by sunhuahui on 2017/1/29.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}

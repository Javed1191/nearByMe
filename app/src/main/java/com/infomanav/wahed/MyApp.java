package com.infomanav.wahed;


import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/CenturyGothic_regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}

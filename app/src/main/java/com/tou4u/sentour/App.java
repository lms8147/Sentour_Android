package com.tou4u.sentour;

import android.app.Application;
/*
import com.tsengvn.typekit.Typekit;
*/
public class App extends Application {

    /*
    private static final String FONT_NAME_NanumPen = "font/NanumPen.ttf";
    private static final String FONT_NAME_GothicBold = "font/NanumGothicBold.ttf";
    private static final String FONT_NAME_MyeongjoBold = "font/NanumMyeongjoBold.ttf";
    private static final String FONT_NAME_Barun = "font/NanumBarunGothic.ttf";
    private static final String FONT_NAME_BarunBold = "font/NanumBarunGothicBold.ttf";
*/

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        Typekit.getInstance()
                .add("NanumPen",Typekit.createFromAsset(this, FONT_NAME_NanumPen))
                .add("GothicBold",Typekit.createFromAsset(this, FONT_NAME_GothicBold))
                .add("MyeongjoBold",Typekit.createFromAsset(this, FONT_NAME_MyeongjoBold))
                .add("NanumBarunGothic",Typekit.createFromAsset(this, FONT_NAME_Barun))
                .add("NanumBarunGothicBold",Typekit.createFromAsset(this, FONT_NAME_BarunBold));
         */

    }
}

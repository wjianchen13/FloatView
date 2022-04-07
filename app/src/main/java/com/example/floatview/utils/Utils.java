package com.example.floatview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

import java.lang.reflect.Method;

public class Utils {
    
    public static void log(boolean log, String str) {
        if(log)
            log(str);
    }
    
    public static void log(String str) {
        System.out.println("============================> " + str);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * 返回屏幕密度
     */
    public static float getDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2.0f;
    }


    /**
     * 返回屏幕宽(px)
     */
    public static int getScreenWidth(Context context) {
        if (context == null)
            return 0;

        Resources resources = context.getResources();
        if (resources == null)
            return 0;

        DisplayMetrics metrics = resources.getDisplayMetrics();
        if (metrics == null)
            return 0;

        return metrics.widthPixels;
    }

    /**
     * 返回屏幕高(px)
     */
    public static int getScreenHeight(Context context) {
        if (context == null)
            return 0;

        Resources resources = context.getResources();
        if (resources == null)
            return 0;

        DisplayMetrics metrics = resources.getDisplayMetrics();
        if (metrics == null)
            return 0;

        return metrics.heightPixels;
    }

}

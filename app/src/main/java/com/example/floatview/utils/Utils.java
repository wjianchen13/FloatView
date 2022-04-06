package com.example.floatview.utils;

public class Utils {
    
    public static void log(boolean log, String str) {
        if(log)
            log(str);
    }
    
    public static void log(String str) {
        System.out.println("============================> " + str);
    }
    
}

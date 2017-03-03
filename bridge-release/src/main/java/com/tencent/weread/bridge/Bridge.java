package com.tencent.weread.bridge;

/**
 * Created by roylkchen on 10/26/16.
 */
public class Bridge {

    private volatile static Bridge sInstance;

    public static Bridge instance() {
        if (sInstance == null) {
            synchronized (Bridge.class) {
                sInstance = new Bridge();
            }
        }

        return sInstance;
    }

    public boolean connect(ClassLoader classLoader, String path) {
        //oops.
        return false;
    }
}

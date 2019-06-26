package com.dev.lyhoangvinh.mvparchitecture.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

import java.lang.reflect.Field;

public class ScreenUtils {

    private Context cxt;

    private static ScreenUtils instance = null;

    public static ScreenUtils getInstance(Context cxt) {
        if (instance == null) {
            synchronized (ScreenUtils.class) {
                if (instance == null) {
                    instance = new ScreenUtils(cxt);
                }
            }
        }
        return instance;
    }

    public ScreenUtils(Context cxt) {
        this.cxt = cxt;
    }

    /**
     * get screen width
     *
     * @return screen width in px
     */
    public int getScreenWidth() {
        return cxt.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * get screen height
     *
     * @return screen height in px
     */
    public int getScreenHeight() {
        return cxt.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * get title bar height
     * @return title bar height
     */
    public int getTitlebarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int titlebarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            titlebarHeight =  cxt.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return titlebarHeight;
    }

    /**
     * get status bar height
     * @param activity instance of activity
     * @return status bar height
     */
    public int getStatusBarHeight(Activity activity) {
        if (null == activity) return 0;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}

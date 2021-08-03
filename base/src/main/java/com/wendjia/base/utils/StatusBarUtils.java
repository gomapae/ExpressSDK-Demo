package com.wendjia.base.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import com.wendjia.base.widgets.StatusBarCompatRootLayout;


public class StatusBarUtils {

    private static final int DEFAULT_COLOR_TRANSLUCENT = Color.argb(112, 0, 0, 0);

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的Activity
     * @param color 状态栏颜色
     * @param isFullScreen 是否全屏
     * @param isLightMode 状态栏设置字体颜色，6.0及以上版本生效
     */
    public static void updateStatusBar(Activity activity,
            StatusBarColor color,
            boolean isFullScreen,
            boolean isLightMode) {
        if (activity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            View statusBarView = decorView
                    .findViewWithTag(StatusBarCompatRootLayout.FAKE_STATUS_BAR_TAG);
            if (statusBarView != null) {
                if (isFullScreen) {
                    statusBarView.setVisibility(View.GONE);
                } else {
                    statusBarView.setVisibility(View.VISIBLE);
                    statusBarView.setBackgroundColor(getStatusBarColor(color));
                }
            }

            if (Build.VERSION.SDK_INT >= 23) {
                DeviceUtils.INSTANCE.setStatusBarLightMode(window, isLightMode);
            }
        }
    }


    public enum StatusBarColor {
        WHITE,
        GRAY,
        BLACK
    }

    private static int getStatusBarColor(StatusBarColor statusBarColor) {
        if (statusBarColor == null) {
            return DEFAULT_COLOR_TRANSLUCENT;
        }

        int color;
        switch (statusBarColor) {
            case WHITE:
                color = getColorWhite();
                break;

            case GRAY:
                color = getColorGreyTransparent();
                break;

            case BLACK:
                color = getColorBlack();
                break;

            default:
                color = DEFAULT_COLOR_TRANSLUCENT;
                break;
        }

        return color;
    }

    private static int getColorWhite() {
        int color;

        if (Build.VERSION.SDK_INT >= 23) {
            color = Color.WHITE;
        } else {
            color = DEFAULT_COLOR_TRANSLUCENT;
        }

        return color;
    }

    private static int getColorBlack() {
        return Color.BLACK;
    }

    private static int getColorGreyTransparent() {
        int color;

        if (Build.VERSION.SDK_INT >= 23) {
            color = 0xaa000000;
        } else {
            color = DEFAULT_COLOR_TRANSLUCENT;
        }

        return color;
    }
}

package com.wendjia.base.utils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 动画工具类
 *
 * @author coofee
 * @createtime 2020年5月22日20点53分
 */
public class AnimUtil {
    /**
     * @param millis
     * @return
     */
    public static Animation getTopInAnimation(long millis) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(millis);
        return animation;
    }

    public static Animation getTopOutAnimation(long millis) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);
        animation.setDuration(millis);
        return animation;
    }

    public static Animation getBottomInAnimation(long millis) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(millis);
        return animation;
    }

    public static Animation getBottomOutAnimation(long millis) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(millis);
        return animation;
    }

    public static Animation getRotateAnimation(long millis) {
        //构造ObjectAnimator对象的方法
        RotateAnimation animator = new RotateAnimation(0.0f, 360f * 4, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animator.setDuration(millis * 4);//设置旋转时间
//        animator.setRepeatMode(Animation.RESTART);
        animator.setRepeatCount(Animation.INFINITE);
        return animator;
    }
}

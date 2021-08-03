package com.wendjia.base.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;

/**
 * Created by jd on 2020/9/3.
 */
public class HwUtil {

    /**
     * 华为安全键盘遮挡toast,隐藏软键盘
     * @param activity
     */
    public static void checkSoftKeyboard(Context activity){
        if ((isHw() || isHonor()) && activity instanceof Activity){
            View focusView = ((Activity) activity).getWindow().getDecorView().findFocus();
            if(focusView instanceof EditText){
                EditText et = (EditText) focusView;
                if(et.getInputType()==(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT)){
                    KeyboardUtils.hideSoftInput(((Activity) activity));
                }
            }
        }
    }

    public static boolean isHw(){
        return Build.BRAND.equalsIgnoreCase("huawei");
    }

    public static boolean isHonor(){
        return Build.BRAND.equalsIgnoreCase("honor");
    }

}

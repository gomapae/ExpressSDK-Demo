package com.wendjia.base.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;

import com.wendjia.base.R;
import com.wendjia.base.utils.StatusBarUtil;

/**
 * @author lxm
 * @date 2018/4/20/020
 */
public class StatusBarCompatRootLayout extends LinearLayout {
    private View mStatusBarView;

    /**
     * statusBar的tag，方便获取View
     */
    public static final Object FAKE_STATUS_BAR_TAG = new Object();

    public StatusBarCompatRootLayout(Context context) {
        this(context, null);
    }

    public StatusBarCompatRootLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public StatusBarCompatRootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusBarCompat);
        int color = typedArray.getColor(R.styleable.StatusBarCompat_StatusBarBg, -1);
        boolean isHasView = typedArray.getBoolean(R.styleable.StatusBarCompat_StatusBarView, true);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.status_bar_layout, this);
        mStatusBarView = view.findViewById(R.id.fake_status_bar);

        if (isHasView) {
            mStatusBarView.setVisibility(View.VISIBLE);
        } else {
            mStatusBarView.setVisibility(View.GONE);
        }

        if (color != -1) {
            mStatusBarView.setBackgroundColor(color);
        }

        this.setOrientation(LinearLayout.VERTICAL);
        mStatusBarView.setTag(FAKE_STATUS_BAR_TAG);

        ViewGroup.LayoutParams layoutParams = mStatusBarView.getLayoutParams();
        layoutParams.height = StatusBarUtil.INSTANCE.getStatusBarHeight(context);
        mStatusBarView.setLayoutParams(layoutParams);

        this.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void setStatusBarViewBg(@ColorInt int color) {
        if (color != -1) {
            mStatusBarView.setBackgroundColor(color);
        }
    }

    public void showStatusBar(boolean isShow) {
        if (isShow) {
            mStatusBarView.setVisibility(View.VISIBLE);
        } else {
            mStatusBarView.setVisibility(View.GONE);
        }
    }
}
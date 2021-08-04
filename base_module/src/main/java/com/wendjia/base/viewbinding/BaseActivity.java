package com.wendjia.base.viewbinding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.wendjia.base.view.BaseAppCompatActivity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 描述：activity基类
 *
 * @author coo_fee.
 * @Time 2020/4/13.
 */
public abstract class BaseActivity<T extends ViewDataBinding, M extends BaseViewModel> extends BaseAppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    protected T binding;
    protected M model;

    @NotNull
    @Override
    public View getLayoutView() {
        binding = getBinding();
        return binding.getRoot();
    }

    @Override
    public void initData() {
        super.initData();
        //model 可能需要 intent中数据  一定等Aroutter解析完 ，
        model = getModel();
    }

    protected abstract T getBinding();

    protected abstract M getModel();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

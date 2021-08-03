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

    public void hideFragment(FragmentTransaction transaction, Fragment fragment){
        if (fragment != null && fragment.isAdded()){
            transaction.hide(fragment);
        }
    }
    public void showFragment(FragmentTransaction transaction, Fragment fragment, Bundle bundle,@IdRes int id){
        if (fragment == null){
            return;
        }
        if (fragment.isAdded()){
            transaction.show(fragment);
        }else {
            if (bundle != null){
                fragment.setArguments(bundle);
            }
            transaction.add(id,fragment);
        }
    }

    protected abstract T getBinding();

    protected abstract M getModel();

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

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

package com.wendjia.base.viewbinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.wendjia.base.view.BaseRxFragment;

import org.jetbrains.annotations.NotNull;

/**
 * 描述：fragment基类
 *
 * @author coo_fee.
 * @Time 2020/4/13.
 */
public abstract class BaseFragment<T extends ViewDataBinding, M extends BaseViewModel> extends BaseRxFragment {
    protected T binding;
    protected M model;

    @org.jetbrains.annotations.Nullable
    @Override
    public View onDefineCreateView(@NotNull LayoutInflater inflater, @org.jetbrains.annotations.Nullable ViewGroup container, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = getBinding(inflater, container, savedInstanceState);
        model = getModel();
        return binding.getRoot();
    }

    protected abstract T getBinding(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract M getModel();

}
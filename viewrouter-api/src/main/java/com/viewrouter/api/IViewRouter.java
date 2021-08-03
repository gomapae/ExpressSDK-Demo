package com.viewrouter.api;


import android.os.Bundle;

public interface IViewRouter<T> {
    void bindData(T router, Bundle bundle);
}

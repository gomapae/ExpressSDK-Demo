package com.viewrouter.api

import android.os.Bundle
import android.os.Parcelable
import com.gomap.base.viewtask.IView
import com.viewrouter.model.ViewInfo
import java.io.Serializable

/**
 * @author lxm
 * @createtime 2021/6/13
 */
class ViewParamsPostcard(var iView: IView) :ViewInfo() {

    private val mBundle = Bundle()

    fun withObject(key: String, value: Any?): ViewParamsPostcard {

        if (value != null){
            if (value is Serializable){
                mBundle.putSerializable(key, value)
            }else if (value is Parcelable){
                mBundle.putParcelable(key,value)
            }
        }
        return this@ViewParamsPostcard
    }

    fun withString(key: String, value: String?): ViewParamsPostcard {
        mBundle.putString(key, value)
        return this
    }

    fun withBoolean(key: String, value: Boolean): ViewParamsPostcard {
        mBundle.putBoolean(key, value)
        return this
    }

    fun withShort(key: String, value: Short): ViewParamsPostcard {
        mBundle.putShort(key, value)
        return this
    }

    fun withInt(key: String, value: Int): ViewParamsPostcard {
        mBundle.putInt(key, value)
        return this
    }

    fun withLong(key: String, value: Long): ViewParamsPostcard {
        mBundle.putLong(key, value)
        return this
    }

    fun withDouble(key: String, value: Double): ViewParamsPostcard {
        mBundle.putDouble(key, value)
        return this
    }

    fun withByte(key: String, value: Byte): ViewParamsPostcard {
        mBundle.putByte(key, value)
        return this
    }

    fun withChar(key: String, value: Char): ViewParamsPostcard {
        mBundle.putChar(key, value)
        return this
    }

    fun withFloat(key: String, value: Float): ViewParamsPostcard {
        mBundle.putFloat(key, value)
        return this
    }

    fun navigation(): IView {
        iView.setBundleData(mBundle)
        return iView
    }

}
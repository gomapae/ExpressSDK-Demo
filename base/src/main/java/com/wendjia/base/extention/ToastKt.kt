package com.wendjia.base.extention

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

//fun Context.toast(str: String) {
//    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
//}

fun Context.toast(str: String?) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

/**
 * 去除[GoMap:xxx]前缀
 */
fun Activity.toastNoTitle(str: String) {
    Toast.makeText(this, null, Toast.LENGTH_SHORT).apply {
        setText(str)
        show()
    }
}

fun View.toast(str: String) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(str: String) {
    Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
}
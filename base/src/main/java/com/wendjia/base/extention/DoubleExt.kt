package com.wendjia.base.extention

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Create by lxm
 * 2020/11/13
 */

infix fun Double.equalAsLocation(number: Double): Boolean {
    val bigDecimal1 = BigDecimal(this).setScale(7, RoundingMode.HALF_UP)
    val bigDecimal2 =  BigDecimal(number).setScale(7, RoundingMode.HALF_UP)
    return bigDecimal1.compareTo(bigDecimal2) == 0
}

fun Double.mSToKmH(num:Int): Double {
    val bigDecimal = BigDecimal((this * 3600.0) / 1000.0).setScale(num, RoundingMode.HALF_UP)
    return bigDecimal.toDouble()
}
fun Double.mToKm(num:Int): Double {
    val bigDecimal = BigDecimal(this / 1000.0).setScale(num, RoundingMode.HALF_UP)
    return bigDecimal.toDouble()
}
package com.wendjia.base.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * @author lxm
 * @createtime 2021/3/31
 * 经纬度的唯一类
 */
private const val LAT_MIN = -90.0
private const val LAT_MAX = 90.0
private const val LNG_MIN = -180.0
private const val LNG_MAX = 180.0
private const val ALL_LNGS = 360.0

open class LngLatBean : Parcelable, Serializable {

    constructor()

    constructor(lng: Double?, lat: Double?) : this() {
        try {
            this.lng = lng
            this.lat = lat
        } catch (e: Exception) {
        }
    }

    /**
     * lat : 24.458204599367836
     */
    var lng: Double? = 0.0
        get() {
            return if (LNG_MIN <= field ?: 0.0 && field ?: 0.0 < LNG_MAX) {
                field
            } else {
                ((field ?: 0.0 - LNG_MAX) % ALL_LNGS + ALL_LNGS) % ALL_LNGS - LNG_MAX
            }
        }

    /**
     * lat : 24.458204599367836
     */
    var lat: Double? = 0.0
        get() {
            return Math.max(LAT_MIN, Math.min(LAT_MAX, field ?: 0.0))
        }

    constructor(parcel: Parcel) : this() {
        this.lat = parcel.readDouble()
        this.lng = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        this.lat?.let { parcel.writeDouble(it) }
        this.lng?.let { parcel.writeDouble(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LngLatBean> {
        override fun createFromParcel(parcel: Parcel): LngLatBean {
            return LngLatBean(parcel)
        }

        override fun newArray(size: Int): Array<LngLatBean?> {
            return arrayOfNulls(size)
        }
    }
}
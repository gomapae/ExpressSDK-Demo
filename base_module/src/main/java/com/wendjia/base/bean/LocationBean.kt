package com.wendjia.base.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


/**
 * 扩展这个作为位置信息的主类
 * 位置信心类
 * 地图 poi 点 坐标的 唯一类
 */

open class LocationBean : LngLatBean, Parcelable,Serializable, NoProguard {

    constructor()

    var name: String? = null
    var subclass: String? = null
    var poiclass: String? = null
    var poi_code: String? = null//编码  poi_id
    var isMarker = false
    var markerId: Long = 0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        subclass = parcel.readString()
        poiclass = parcel.readString()
        poi_code = parcel.readString()
        isMarker = parcel.readByte() != 0.toByte()
        markerId = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(subclass)
        parcel.writeString(poiclass)
        parcel.writeString(poi_code)
        parcel.writeByte(if (isMarker) 1 else 0)
        parcel.writeLong(markerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationBean> {
        override fun createFromParcel(parcel: Parcel): LocationBean {
            return LocationBean(parcel)
        }

        override fun newArray(size: Int): Array<LocationBean?> {
            return arrayOfNulls(size)
        }
    }

}
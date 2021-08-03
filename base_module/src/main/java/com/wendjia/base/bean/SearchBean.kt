package com.wendjia.base.bean

import android.os.Parcel
import android.os.Parcelable
import com.blankj.utilcode.util.StringUtils
import com.gomap.maps.base_module.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author lxm
 * @createtime 2021/3/21
 *
 * 搜索 poi model  后名字替换为 MpoiDetailsBean
 */
class SearchBean() : Serializable, Parcelable {

    var id: Long = 0
    var name: String? = null
    var lat: String? = null//纬度
    var lng: String? = null//经度
    var poi_code: String? = null//编码  poi_id
    var subclass: String? = null //二级分类code
    var poiclass: String? = null //一级分类
    @SerializedName("subclass_name")
    var subclass_format: String? = null // 二级分类显示的name

    var address: String? = null
    @SerializedName("phone_number")
    var phoneNumber: String? = null //poi描述

    @SerializedName("poi_images")
    var title_img: List<String>? = null


    /**
     * 地点备注
     */
    var remark: String? = null

    var isMarker = false
    var markerId: Long = 0

    var distance:String?= null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        lat = parcel.readString()
        lng = parcel.readString()
        poi_code = parcel.readString()
        subclass = parcel.readString()
        poiclass = parcel.readString()
        subclass_format = parcel.readString()
        address = parcel.readString()
        phoneNumber = parcel.readString()
        title_img = parcel.createStringArrayList()
        isMarker = parcel.readByte() != 0.toByte()
        markerId = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(lat)
        parcel.writeString(lng)
        parcel.writeString(poi_code)
        parcel.writeString(subclass)
        parcel.writeString(poiclass)
        parcel.writeString(subclass_format)
        parcel.writeString(address)
        parcel.writeString(phoneNumber)
        parcel.writeStringList(title_img)
        parcel.writeByte(if (isMarker) 1 else 0)
        parcel.writeLong(markerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchBean> {
        override fun createFromParcel(parcel: Parcel): SearchBean {
            return SearchBean(parcel)
        }

        override fun newArray(size: Int): Array<SearchBean?> {
            return arrayOfNulls(size)
        }
    }


    fun getDLat(): Double {
        try {
            return lat?.toDouble() ?: 0.0
        } catch (e: Exception) {
        }
        return 0.0
    }

    fun getPoiCode(): String {
        return poi_code ?: ""
    }

    fun getDLng(): Double {
        try {
            return lng?.toDouble() ?: 0.0
        } catch (e: Exception) {
        }
        return 0.0
    }

}
package com.gomap.tangram_map.model

import android.os.Parcel
import android.os.Parcelable
import com.wendjia.base.bean.LngLatBean


/**
 * Represents a rectangular area.
 */
class LngLatBounds : Parcelable {
    private val northeast: LngLatBean
    private val southwest: LngLatBean

    /**
     * Constructs a new object given southwest and northwest points.
     * @param southwest
     * @param northeast
     */
    constructor(southwest: LngLatBean, northeast: LngLatBean) {
        this.southwest = southwest
        this.northeast = northeast
    }

    fun getSouthwest(): LngLatBean {
        return southwest
    }

    fun getNortheast(): LngLatBean {
        return northeast
    }

    /**
     * Determines whether the given point is contained within the lat/lng's bounds.
     * @param point
     * @return
     */
    operator fun contains(point: LngLatBean): Boolean {
        return includesLat(point.lat?:0.0) && includesLng(point.lng?:0.0)
    }

    /**
     * Returns a new object which includes the given point.
     * @param point
     * @return
     */
    fun including(point: LngLatBean): LngLatBounds {
        val swLat: Double = Math.min(southwest.lat?:0.0, point.lat?:0.0)
        val neLat: Double = Math.max(northeast.lat?:0.0, point.lat?:0.0)
        var neLng: Double = northeast.lng?:0.0
        var swLng: Double = southwest.lng?:0.0
        val ptLng: Double = point.lng?:0.0
        if (!includesLng(ptLng)) {
            if (swLngMod(swLng, ptLng) < neLngMod(neLng, ptLng)) {
                swLng = ptLng
            } else {
                neLng = ptLng
            }
        }
        return LngLatBounds(LngLatBean(swLng, swLat), LngLatBean(neLng, neLat))
    }

    /**
     * Returns the center of the lat/lng bounds.
     * @return
     */
    val center: LngLatBean
        get() {
            val midLat: Double = (southwest.lat?:0.0 + (northeast.lat?:0.0)) / 2.0
            val neLng: Double = northeast.lng?:0.0
            val swLng: Double = southwest.lng?:0.0
            val midLng: Double
            midLng = if (swLng <= neLng) {
                (neLng + swLng) / 2.0
            } else {
                (neLng + 360.0 + swLng) / 2.0
            }
            return LngLatBean(midLng, midLat)
        }

    private fun includesLat(lat: Double): Boolean {
        return southwest.lat?:0.0 <= lat && lat <= northeast.lat?:0.0
    }

    private fun includesLng(lng: Double): Boolean {
        return if (southwest.lng?:0.0 <= northeast.lng?:0.0) southwest.lng?:0.0 <= lng && lng <= northeast.lng?:0.0 else southwest.lng?:0.0 <= lng || lng <= northeast.lng?:0.0
    }

    private constructor(`in`: Parcel) {
        northeast = `in`.readParcelable(LngLatBean::class.java.getClassLoader())!!
        southwest = `in`.readParcelable(LngLatBean::class.java.getClassLoader())!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeParcelable(northeast, i)
        parcel.writeParcelable(southwest, i)
    }

    /**
     * Builder class for [LngLatBounds].
     */
    class Builder {
        private var northeastLat = 0.0
        private var northeastLng = 0.0
        private var southwestLat = 0.0
        private var southwestLng = 0.0
        /**
         * Includes this point for building of the bounds. The bounds will be extended in a minimum way
         * to include this point.
         * @param point
         * @return builder object
         */
        fun include(point: LngLatBean): Builder {
            if (northeastLat == 0.0) {
                northeastLat = point.lat?:0.0
                northeastLng = point.lng?:0.0
                southwestLat = point.lat?:0.0
                southwestLng = point.lng?:0.0
            }
            if (point.lat?:0.0 > northeastLat) {
                northeastLat = point.lat?:0.0
            } else if (point.lat?:0.0 < southwestLat) {
                southwestLat = point.lat?:0.0
            }
            if (point.lng?:0.0 > northeastLng) {
                northeastLng = point.lng?:0.0
            } else if (point.lng?:0.0 < southwestLng) {
                southwestLng = point.lng?:0.0
            }
            return this
        }

        /**
         * Constructs a new [LngLatBounds] from current boundaries.
         * @return
         */
        fun build(): LngLatBounds {
            val sw = LngLatBean(southwestLng, southwestLat)
            val ne = LngLatBean(northeastLng, northeastLat)
            return LngLatBounds(sw, ne)
        }
    }

    companion object CREATOR : Parcelable.Creator<LngLatBounds> {
        private fun swLngMod(swLng: Double, ptLng: Double): Double {
            return (swLng - ptLng + 360.0) % 360.0
        }

        private fun neLngMod(neLng: Double, ptLng: Double): Double {
            return (ptLng - neLng + 360.0) % 360.0
        }

        override fun createFromParcel(`in`: Parcel): LngLatBounds {
            return LngLatBounds(`in`)
        }

        override fun newArray(size: Int): Array<LngLatBounds?> {
            return arrayOfNulls(size)
        }
    }
}
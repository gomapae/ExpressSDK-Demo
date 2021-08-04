# GoMap-ExpressSDK-Demo
GoMap express Sdk Demo
# 接入文档

1.项目级build.gradle （默认支持华为 tts语音）

```fortran
buildscript {
    ext.kotlin_version = '1.3.72'

    repositories {
       
        maven { url "https://developer.huawei.com/repo/" }
    }
   
}

allprojects {
    repositories {
       
        maven { url "https://developer.huawei.com/repo/" }
    }
}
```

地图依赖：

```fortran
implementation 'com.gomap.android:express:1.0.0'
```

2.手机权限

```fortran
<!-- 网络 -->
    <uses-permission android:name="android.permission.INTERNET" /> 
<!-- 存储 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
<!-- 位置 -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

```

3.地图SDK初始化：

```fortran
GoMapSDK.getInstance().init()
```

4.地图 UI framgment  以及首次加载

UI

```fortran
<fragment
            android:id="@+id/fragment"
            android:name="com.mapzen.android.graphics.MapFragment2"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            app:overlayMode="classic" />
```

首次加载

```fortran
fun initGoMap(mapFragment2: MapFragment2?,success:()->Unit) {
        this.mapFragment2 = mapFragment2
        
        mapFragment2?.getMapAsync { map ->
           //初始化完成
        }
    }

/**
     * 初始化配置
     */
    private fun configureMap() {
        Gomap?.isMyLocationEnabled = true
//指南针
        Gomap?.setCompassButtonEnabled(true)

//缩放按钮
//        Gomap?.setZoomButtonsEnabled(true)

        Gomap?.setPersistMapState(true)
//图层按钮
        Gomap?.setLayersIconEnable(true)
// 定位按钮离底部的距离
       Gomap?.setFindMePosition(50)
        //bottom 是离屏幕中间的距离 单位dp

//zoom 缩放按钮的位置
//        Gomap?.setZoomOutPosition(40)
        //设置点击地图区域大小 dp
        Gomap?.mapController?.setPickRadius(10f)
//默认的缩放 zoom 大小
        Gomap?.zoom = ZOOM_INIT_START
    }
```

5.其他功能

（1）位置变化监听

```fortran
MapFragment2?.setLocationListener {
                baseMapListener.onLocationUpdated(it)
            }
```

（2）地图长按 监听

```fortran
Gomap?.longPressResponder = LongPressResponder { x, y ->
            val extra = "x:$x y:$y"
            LogUtils.iTag("lxm MapProxy longPressResponder", extra)
            
        }
```

（3）地图 poi点击监听/空白处点击的返回结果为空

```fortran
Gomap?.setLabelPickListener {
            LogUtils.iTag("lxm MapProxy setLabelPickListener", it)
           
        }
```

（4）添加标记点：返回marker对象，可用于精准的移除marker点

```fortran
Gomap
public Marker addMarker(@NonNull ILnglat lngLat) {
}

Gomap?.addMarker(LngLat4Category(searchBean.lng?:0.0, searchBean.lat?:0.0))

```

（5）添加标记点：自定义标记的图片资源

```fortran
fun addBitmapMarker(searchBean: LocationBean,drawable: Drawable):BitmapMarker?{
        val bitmapMarkerOptions =
            BitmapMarkerOptions().position(LngLat(searchBean.lng ?: 0.0, searchBean.lat ?: 0.0))
                .icon(drawable)
        return Gomap?.addBitmapMarker(bitmapMarkerOptions)
    }
```

（6）将标记点移动到屏幕中心

```fortran
/**
     * 移动到地图中心
     */
    fun moveToCenter(dest: LocationBean, duration: Int) {
       
        Gomap?.setPosition(LngLat(dest.lng?:0.0, dest.lat?:0.0), duration)
    }
```

（7）清除标记点

```fortran
//移除所有的标记点
map?.removeMarker()

// 移除 指定markerId的标记点
fun removeMaker(markerId:Long){
        map?.removeMarker(markerId)
    }
//移除指定的自定义图标的marker点
fun removeBitmapMarker(bitmapMarker:BitmapMarker){
        map?.removeBitmapMarker(bitmapMarker)
    }

```

（8）路线规划/导航

```fortran
Gomap：
/**
     * plan route
     * @param merchant the location of merchant, necessary
     * @param dests the destinations, must more than one
     */
    public void planRoute(@NonNull Poi merchant ,@NonNull List<Poi> dests) {
        planRoute(DrivingType.CAR, merchant, dests);
    }
```

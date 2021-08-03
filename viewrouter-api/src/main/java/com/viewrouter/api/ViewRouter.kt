package com.viewrouter.api

import android.content.Context
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.gomap.base.viewtask.IView
import com.viewrouter.model.ViewInfo
import com.wendjia.base.view.IBaseView
import java.util.*

/**
 * @author lxm
 * @createtime 2021/6/8
 */
object ViewRouter {
    // 获取所有的
    private val viewMap =  HashMap<String, ViewInfo>()

    private var context:Context?= null

    fun init(context:Context){
        this.context = context
        LogUtils.iTag("ViewRouter init")
        initRouterMap()
    }

    fun buildPath(iBaseView: IBaseView,path:String): ViewParamsPostcard? {
        if(viewMap.containsKey(path)){
            val viewInfo = viewMap[path]
            viewInfo?.let {
                val viewClass = viewInfo.viewClass
                val constructor = viewClass.getConstructor(IBaseView::class.java)
                val view = constructor.newInstance(iBaseView) as IView
                val viewParamsPostcard = ViewParamsPostcard(view)
                return viewParamsPostcard
            }
        }else {
            ToastUtils.showShort("no view route")
        }
        return null
    }

    private fun initRouterMap(): HashMap<String, ViewInfo> {
        LogUtils.iTag("ViewRouter getRouterMap")
        var routers = HashMap<String, ViewInfo>()
        try {
            val routerMapS  = ClassUtils.getFileNameByPackageName(context, Constants.ROUTER_BASE_PACKAGE)
            LogUtils.iTag("ViewRouter getRouterMap 1 "+ routerMapS.size)
            for (className in routerMapS) {
                LogUtils.iTag("ViewRouter getRouterMap 2 "+className)
                if (className.startsWith(Constants.ROUTER_BASE_PACKAGE+"."+Constants.ROUTER_MAP + Constants.SEPARATOR)) {
                    LogUtils.iTag("ViewRouter getRouterMap 3 "+className)
                    // This one of root elements, load root.
                    (Class.forName(className).getConstructor().newInstance() as IRouterMap).loadInto(viewMap)
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
        } catch (e: InstantiationException) {
        } catch (e: NoClassDefFoundError) {
        }
        return routers
    }
    fun <T:View> bind(routerObj: T, bundle: Bundle) {
        try {
            val routerClass = Class.forName(routerObj.javaClass.name + "$\$Router")
            LogUtils.iTag("ViewRouter bind",routerObj.javaClass.name)
            val router = routerClass.newInstance() as IViewRouter<T>
            router.bindData(routerObj, bundle)
        } catch (e: ClassNotFoundException) {
            // no-op
            LogUtils.e(e)
        } catch (e: InstantiationException) {
            LogUtils.e(e)
        } catch (e: IllegalAccessException) {
            LogUtils.e(e)
        }
    }

    /**
     * cast操作
     *
     * @param obj cast的对象
     */
    @JvmStatic
    fun <T> castValue(obj: Any): T {
        return obj as T
    }

}
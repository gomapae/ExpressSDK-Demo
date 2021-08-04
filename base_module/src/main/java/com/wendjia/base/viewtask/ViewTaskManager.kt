package com.wendjia.base.viewtask

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.core.view.isEmpty
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.wendjia.base.view.BaseAppCompatActivity
import com.wendjia.base.view.IBaseView
import com.wendjia.base.viewbinding.BaseMVVMView
import java.util.*
import kotlin.collections.HashMap

/**
 * @description view task manager
 * @author shenfei.wang@g42.ai
 * @createtime 2021/1/20 15:02
 */
class ViewTaskManager(var iBaseView: IBaseView, private var container: ViewGroup, private var callBack: ViewTaskManagerCallBack?) {
    private val viewTask = Stack<IView>()
    private var context: Context? = null

    /**
     * tab position
     */
    private var tabPosition: Int = -1

    companion object {

        private var viewTaskManagerMap = HashMap<String,ViewTaskManager>()

        @JvmStatic
        fun init(iBaseView: IBaseView, view: ViewGroup, callBack: ViewTaskManagerCallBack?) {
            val simpleName = (view.context as BaseAppCompatActivity).javaClass.simpleName
            LogUtils.iTag("ViewTaskManager" ,simpleName)
            if (!viewTaskManagerMap.containsKey(simpleName)){
                initViewTaskManager(iBaseView,view,callBack)
            }
        }
        private fun initViewTaskManager(iBaseView: IBaseView, view: ViewGroup, callBack: ViewTaskManagerCallBack?) =  synchronized(this) {
            ViewTaskManager(iBaseView,view,callBack).also {
                val simpleName = (view.context as BaseAppCompatActivity).javaClass.simpleName
                LogUtils.iTag("ViewTaskManager" ,simpleName)
                viewTaskManagerMap[simpleName] = it
            }
        }

        @JvmStatic
        @JvmOverloads
        fun getInstance(contanerName:String?= "DemoMainActivity"):ViewTaskManager{
            if (viewTaskManagerMap.containsKey(contanerName)){

            }else {
                ToastUtils.showShort("please init ")
            }
            return viewTaskManagerMap[contanerName]!!
        }

        fun onDestroy(appCompatActivity: BaseAppCompatActivity){
            viewTaskManagerMap.remove(appCompatActivity.javaClass.simpleName)
        }

    }

    init {
        context = container.context
    }

    /**
     * @param view
     * @param isNeedSelectPointFromMap 是否需要显示地图
     */
    fun startView(view: IView?, isNeedSelectPointFromMap: Boolean){
        startView(view,isNeedSelectPointFromMap,false)
    }

    /**
     * @param view
     * @param isNeedSelectPointFromMap 是否需要显示地图
     * @param isNavigationIn 是否进入导航页面
     */
    fun startView(view: IView?, isNeedSelectPointFromMap: Boolean, isNavigationIn:Boolean){
        //首次记录当前tabposition  待优化
        if (viewTask.size == 0){
            tabPosition = callBack?.getCurrentTabPosition()?:0
        }

        if (isNeedSelectPointFromMap) {
            callBack?.needSelectPointFromMap(isNavigationIn)
        }

        if (view != null) {
            startView(view)
        }
    }

    private fun startView(view: IView) {

        //移除UI容器中view
        getLastIVew()?.let {
            val view = it as View
            if (view.visibility == View.VISIBLE) {
                container.removeView(view)
            }
        }

        val viewShowNum = view.viewShowNum()
        if (viewShowNum != -1) {
            finishView(view.javaClass, viewShowNum)
        }

        //保存view到list栈
        viewTask.add(view)

        //设置页面 view 返回的监听
        (view as BaseMVVMView<*, *>).setViewListener(viewListener(view))

        //执行view 创建的生命周期
        view.onCreate()

        visibilityView(view,true)
        refreshViewVisible()
    }

    fun onReStart() {
        if (visiable()) {
            getLastIVew()?.onReStart()
        }
    }

    fun onResume() {
        if (visiable()) {
            getLastIVew()?.onResume()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (visiable()) {
            getLastIVew()?.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun onPause() {
        if (visiable()) {
            getLastIVew()?.onPause()
        }
    }

    fun onStop() {
        if (visiable()) {
            getLastIVew()?.onStop()
        }
    }

    fun onDestroy(appCompatActivity: BaseAppCompatActivity) {
        ViewTaskManager.onDestroy(appCompatActivity)
    }

    /**
     * num 表示 保留 View  的个数
     */
    private fun finishView(cls: Class<*>, num: Int) {
        LogUtils.iTag("ViewTaskManager finishView1", num)
        val newViewStack = Stack<IView>()
        for (view in viewTask) {
            if (view.javaClass == cls) {
                newViewStack.add(view)
            }
        }
        //当栈中个数大于等于保留的个数时 从栈最深的开始结束  一般情况是相等
        if (newViewStack.size >= num) {
            LogUtils.iTag("ViewTaskManager finishView2", num)
            val endNum = newViewStack.size - num + 1
            LogUtils.iTag("ViewTaskManager finishView3", endNum)
            for (i in 0 until endNum) {
                val view = newViewStack[i]
                viewTask.remove(view)
                LogUtils.iTag("ViewTaskManager finishView4", num)
                break
            }
        }
    }

    /**
     * 判断view 栈是否空
     */
    fun isNotEmpty(): Boolean {
        return viewTask.isNotEmpty()

    }

    /**
     * 获取当前view栈的大小
     */
    fun size(): Int {
        return viewTask.size
    }

    /**
     * 判断view 栈是否显示
     */
    fun visiable(): Boolean {
        return container.visibility == View.VISIBLE
    }

    /**
     * 移出所有的view 并返回首页
     */
    fun removeAllViewAndBackToHome() {

        if (viewTask.isNotEmpty()) {
            viewTask.clear()
        }
        if (!container.isEmpty()) {
            container.removeAllViews()
        }
        refreshViewVisible()
    }

    /**
     * 显示最后一个view
     */
    private fun visibilityLastView() {
        if (viewTask.size > 0) {
            var itemView = getLastIVew()
            if (!container.contains(itemView as View)) {
                visibilityView(itemView,false)
            }
        }
        refreshViewVisible()
    }

    /**
     * 显示 view
     * @param isAnimation 是否动画打开
     */
    private fun visibilityView(iView: IView, isAnimation: Boolean) {
        val view = iView as View
        container.addView(
                view,
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        )
        iView.show(isAnimation)
    }

    /**
     * 获取最后一个view
     */
    private fun getLastIVew(): IView? {
        val iViewList = viewTask.toMutableList()
        val num = iViewList.size

        if (num > 0) {
            return iViewList[num - 1]
        }
        return null
    }

    /**
     * 获取当前view  默认是 当前显示的是最后一个view
     */
    fun getCurrentIView(): IView? {
        return getLastIVew()
    }

    /**
     * 显示 获取隐藏 view
     * 根据 view task 是否为空
     */
    private fun refreshViewVisible() {
        if (viewTask.isNullOrEmpty()) {
            hide()
        } else {
            show()
        }
        viewTaskChange()
    }

    private fun viewTaskChange() {
        callBack?.viewTaskChange(viewTask.size, tabPosition)
        if (viewTask.size == 0) {
            tabPosition = -1
        }
    }

    fun onBackPressed(): Boolean {
        if (visiable()){
            if (viewTask.isNullOrEmpty()) return false
            getLastIVew()?.let {
                it.onBackPressed()
                back(it)
                return true
            }
            return false
        }else {
            return false
        }

    }

    private fun back(iView: IView) {
        val view = iView as View
        iView.onDestroy()
        iView.hide(false){isFnish->
            if (isFnish){
                if (container.contains(view)) {
                    container.removeView(view)
                }
                viewTask.remove(iView)
                visibilityLastView()
            }
        }
    }

    /**
     * show container and show top view in task
     * @author shenfei.wang@g42.ai
     * @createtime 2021年1月21日22点09分
     */
    fun show() {
        if (size() > 0) {
            container.visibility = View.VISIBLE
            getLastIVew()?.onResume()
        }
    }

    /**
     * hide container (hide all view in view task)
     * @author shenfei.wang@g42.ai
     * @createtime 2021年1月21日22点09分
     */
    fun hide() {
        callBack?.backToActivity()
        container.visibility = View.GONE
    }

    private fun viewListener(iView: IView) = object : ViewListener {
        override fun onBack() {
            back(iView)
        }

        override fun setViewResult(intent: Intent?) {
            val indexOf = viewTask.indexOf(iView)
            //判断顶层 是否是当前附属activity（这里是MainActivity）
            // 暂时 不考虑 ，view activity view 的情况暂时不存在
            //暂时的情况；view page 的终点都可以进入选点页面，中间的page 都是view
            if (indexOf > 0) {
                viewTask[indexOf - 1].onViewResult(intent)
            } else {
                callBack?.onViewResult(intent)
            }
        }

        override fun removeMakers() {
            callBack?.removeMakers()
        }
    }

}

/**
 * view 管理交互的唯一 listener
 */
interface ViewListener {
    fun onBack()
    fun setViewResult(intent: Intent?)
    fun removeMakers()
}

/**
 * 和附属activity 的交互
 */
interface ViewTaskManagerCallBack {

    fun getCurrentTabPosition():Int
    fun viewTaskChange(size: Int, tabPosition: Int)
    fun backToActivity()
    fun removeMakers()
    fun onViewResult(intent: Intent?)

    fun needSelectPointFromMap(isNavigationIn:Boolean)
}
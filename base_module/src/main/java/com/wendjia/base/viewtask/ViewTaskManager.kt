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
     * @param isNeedSelectPointFromMap ????????????????????????
     */
    fun startView(view: IView?, isNeedSelectPointFromMap: Boolean){
        startView(view,isNeedSelectPointFromMap,false)
    }

    /**
     * @param view
     * @param isNeedSelectPointFromMap ????????????????????????
     * @param isNavigationIn ????????????????????????
     */
    fun startView(view: IView?, isNeedSelectPointFromMap: Boolean, isNavigationIn:Boolean){
        //??????????????????tabposition  ?????????
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

        //??????UI?????????view
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

        //??????view???list???
        viewTask.add(view)

        //???????????? view ???????????????
        (view as BaseMVVMView<*, *>).setViewListener(viewListener(view))

        //??????view ?????????????????????
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
     * num ?????? ?????? View  ?????????
     */
    private fun finishView(cls: Class<*>, num: Int) {
        LogUtils.iTag("ViewTaskManager finishView1", num)
        val newViewStack = Stack<IView>()
        for (view in viewTask) {
            if (view.javaClass == cls) {
                newViewStack.add(view)
            }
        }
        //????????????????????????????????????????????? ???????????????????????????  ?????????????????????
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
     * ??????view ????????????
     */
    fun isNotEmpty(): Boolean {
        return viewTask.isNotEmpty()

    }

    /**
     * ????????????view????????????
     */
    fun size(): Int {
        return viewTask.size
    }

    /**
     * ??????view ???????????????
     */
    fun visiable(): Boolean {
        return container.visibility == View.VISIBLE
    }

    /**
     * ???????????????view ???????????????
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
     * ??????????????????view
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
     * ?????? view
     * @param isAnimation ??????????????????
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
     * ??????????????????view
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
     * ????????????view  ????????? ??????????????????????????????view
     */
    fun getCurrentIView(): IView? {
        return getLastIVew()
    }

    /**
     * ?????? ???????????? view
     * ?????? view task ????????????
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
     * @createtime 2021???1???21???22???09???
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
     * @createtime 2021???1???21???22???09???
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
            //???????????? ?????????????????????activity????????????MainActivity???
            // ?????? ????????? ???view activity view ????????????????????????
            //??????????????????view page ????????????????????????????????????????????????page ??????view
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
 * view ????????????????????? listener
 */
interface ViewListener {
    fun onBack()
    fun setViewResult(intent: Intent?)
    fun removeMakers()
}

/**
 * ?????????activity ?????????
 */
interface ViewTaskManagerCallBack {

    fun getCurrentTabPosition():Int
    fun viewTaskChange(size: Int, tabPosition: Int)
    fun backToActivity()
    fun removeMakers()
    fun onViewResult(intent: Intent?)

    fun needSelectPointFromMap(isNavigationIn:Boolean)
}
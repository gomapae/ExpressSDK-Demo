package com.wendjia.base.view

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.wendjia.base.R
import com.wendjia.base.utils.AdjustLayoutSize
import com.wendjia.base.utils.StatusBarUtils
import com.wendjia.base.widgets.StatusBarCompatRootLayout

/**
 * Create by lxm
 * 2020/8/24
 */
abstract class BaseAppCompatActivity : RxActivity(), IBaseView {

    private var mStatusBarCompatRootLayout: StatusBarCompatRootLayout? = null

    /**
     * 主题编号
     */
    protected var themeIndex = 1

    protected open fun statusBarColor(): Int {
        return Color.WHITE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        setContentView()
        initData()
        initUIView()
        fetchData()

    }

    /**
     * 需要存在的个数
     * 默认-1
     */
    open fun getActivityShowNum():Int{
        return -1
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        ARouter.getInstance().inject(this)
    }

    override fun initData() {
        ARouter.getInstance().inject(this)
    }

    override fun initUIView() {
    }

    override fun fetchData() {

    }

    private fun setContentView(){
        if (useCustomStatusBar()) {
            setContentViewWithCustomStatusBar(getLayoutView())
        } else {
            super.setContentView(getLayoutView())
        }
        setDefaultSystemStatusBarStatus()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    abstract fun getLayoutView(): View

    open fun useCustomStatusBar(): Boolean {
        return Build.VERSION.SDK_INT >= 21 && openUseCustomStatusBar()
    }

    protected open fun openUseCustomStatusBar(): Boolean {
        return false
    }

    fun showOrHideSoftInputWindow() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 请求全屏显示
     */
    protected fun requestFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)//remove title bar  即隐藏标题栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)//remove notification bar  即全屏
    }
    private var assistActivity: AdjustLayoutSize ?= null
    /**
     * 设置是否需要占位的statusBar
     *
     * @param layoutResId 渲染的layoutId
     */
    private fun setContentViewWithCustomStatusBar( layoutView: View) {
        mStatusBarCompatRootLayout = StatusBarCompatRootLayout(this)
        mStatusBarCompatRootLayout?.showStatusBar(true)
        mStatusBarCompatRootLayout?.setStatusBarViewBg(resources.getColor(R.color.light_transparent))

        mStatusBarCompatRootLayout?.addView(layoutView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        super.setContentView(mStatusBarCompatRootLayout)
        assistActivity = AdjustLayoutSize.assistActivity(findViewById(android.R.id.content))
    }
    /**
     * 设置默认的沉浸式为全透明
     */
    private fun setDefaultSystemStatusBarStatus() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            if (isUseStatusBar()){
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        } else {
            mStatusBarCompatRootLayout?.showStatusBar(false)
        }
    }

    protected open fun isUseStatusBar(): Boolean {
        return true
    }

    // 更新statusBar的状态
    open fun updateStatusBar() {
        if (useCustomStatusBar() && isAlive()){
            StatusBarUtils.updateStatusBar(this, StatusBarUtils.StatusBarColor.WHITE, true, false)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            if (mStatusBarCompatRootLayout != null){
                updateStatusBar()
            }
        } catch (e: Exception) {
        }
    }

    override fun showErrorMessage(message: String?) {
    }

    override fun showLoadingView() {
    }

    override fun hideLoadingView() {
    }

    override fun onDestroy() {
        super.onDestroy()
        assistActivity?.onDestroy()
    }

    override fun isAlive(): Boolean = !isDestroyed && !isFinishing

    override fun getContext(): Context? = this
    override fun getIBaseView(): IBaseView = this
    override fun getViewModelStoreOwner(): ViewModelStoreOwner {
        return this
    }
    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }
    private var mServiceClassMap = HashMap<String,Any>()

    fun <T:Any> getServiceClass(service: Class<out T>): T {
        if (mServiceClassMap.containsKey(service.simpleName)){
            return mServiceClassMap[service.simpleName] as T
        }
        val navigation = ARouter.getInstance().navigation(service)
        mServiceClassMap[service.simpleName] = navigation
        return navigation
    }

}
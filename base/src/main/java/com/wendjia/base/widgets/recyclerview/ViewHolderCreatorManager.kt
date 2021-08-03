package com.wendjia.base.widgets.recyclerview

import android.view.View
import android.view.ViewGroup
import com.wendjia.base.BuildConfig
import java.util.ArrayList

/**
 * 多类型ViewHolderCreator管理器
 * Create by lxm
 * 2020/8/25
 */
class ViewHolderCreatorManager {
    /**
     * 类型值的基数，[.getItemViewType]返回的值为代理在代理列表中的位置加上这个数值
     */
    private val TYPE_BASE = 100
    /**
     * 未匹配到代理的类型
     */
    private val TYPE_NONE = -1

    /**
     * ViewHolderCreator集合
     */
    private val creatorList = ArrayList<ViewHolderCreator<*>>()

    /**
     * 增加一个ViewHolder
     *
     * @param holder 需要添加的ViewHolder
     * @return self，方便链式操作
     */
    fun addCreator(holder: ViewHolderCreator<*>): ViewHolderCreatorManager {
        creatorList.add(holder)
        return this
    }


    /**
     * 移除一个ViewHolderCreator
     *
     * @param holder 需要移除的ViewHolder
     * @return self，方便链式操作
     */
    fun removeCreator(holder: ViewHolderCreator<*>): ViewHolderCreatorManager {
        creatorList.remove(holder)
        return this
    }


    /**
     * 找到对应的ViewHolder，返回该代理在列表中的位置作为条目类型
     *
     * @param items    数据
     * @param position 位置
     * @return 条目类型
     */
    fun getItemViewType(items: List<Any>, position: Int): Int {
        val proxyCount = creatorList.size
        for (i in 0 until proxyCount) {
            val holder = creatorList[i]
            val item = items[position]
            if (holder.isForViewType(item, position)) {
                return i + TYPE_BASE
            }
        }
        return if (BuildConfig.DEBUG) {
            throw IllegalArgumentException(
                    "No ViewHolderCreator added that matches position=$position in data source")
        } else {
            TYPE_NONE
        }
    }

    /**
     * 根据`viewType`找到对应的代理然后创建ViewHolder
     *
     * @param parent   父View
     * @param viewType 视图类型
     * @return 代理产生的ViewHolder
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (viewType == TYPE_NONE) {
            return BaseViewHolder<Any>(View(parent.context))
        }
        return findCreator(viewType).onCreateBaseViewHolder(parent)
    }


    /**
     * 根据`viewType`找到对应的ViewHolder
     *
     * @param viewType 视图类型
     * @return 对应的代理
     */
    private fun findCreator(viewType: Int): ViewHolderCreator<*> {
        val i = viewType - TYPE_BASE
        return if (i >= creatorList.size || i < 0) {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("Can not find ViewHolder for $viewType ")
            } else {
                creatorList[0]
            }
        } else {
            creatorList[i]
        }
    }
}
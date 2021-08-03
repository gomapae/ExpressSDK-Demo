//package com.gomap.base.viewtask
//
//
///**
// * @description view task interface
// * @author shenfei.wang@g42.ai
// * @createtime 2021/1/20 15:33
// */
//interface IManager {
//    fun pop(): Boolean
//    fun size(): Int
//    fun visiable(): Boolean
//    fun show(): Boolean
//    fun hide()
//    fun clearAll()
//
//    /**
//     * add view to task and show it
//     */
//    fun startView(iView: IView)
//
//    /**
//     * first as tab positon
//     */
//    fun startView(tabPosition: Int, view: IView)
//
//    /**
//     * pop task and remove it from view tree
//     */
//    fun finishView(view: IView)
//
//    fun setListener(listener: IManagerListener)
//}
//
//interface IManagerListener {
//    fun viewTaskChange(size: Int, tabPosition: Int)
//}

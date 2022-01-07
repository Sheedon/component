package org.sheedon.common.handler

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import java.util.*

/**
 * 软键盘监听者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 12:07 下午
 */
class SoftKeyboardStateHandler(
    _activityRootView: View,
    _isSoftKeyboardOpened: Boolean = false
) :
    ViewTreeObserver.OnGlobalLayoutListener {

    private val listeners = LinkedList<SoftKeyboardStateListener>()
    private var activityRootView: View? = _activityRootView
    private var isSoftKeyboardOpened = _isSoftKeyboardOpened

    init {
        activityRootView?.viewTreeObserver?.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        activityRootView?.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView?.rootView?.height?.minus((r.bottom - r.top))
        if (heightDiff != null) {
            if (!isSoftKeyboardOpened && heightDiff > 300) { // if more than 100 pixels, its probably a keyboard...
                isSoftKeyboardOpened = true
                notifyOnSoftKeyboardOpened(heightDiff)
            } else if (isSoftKeyboardOpened && heightDiff < 300) {
                isSoftKeyboardOpened = false
                notifyOnSoftKeyboardClosed()
            }
        }
    }

    fun addSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.add(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        listeners.forEach {
            it.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        listeners.forEach {
            it.onSoftKeyboardClosed()
        }
    }

    fun isSoftKeyboardOpened() = isSoftKeyboardOpened

    fun clear() {
        listeners.clear()
        activityRootView = null
    }


    interface SoftKeyboardStateListener {
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)

        fun onSoftKeyboardClosed()
    }
}
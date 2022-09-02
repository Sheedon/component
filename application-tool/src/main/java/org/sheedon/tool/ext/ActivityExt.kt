package org.sheedon.tool.ext

import android.app.Activity
import android.view.inputmethod.InputMethodManager

/**
 * Activity扩展函数
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/9/2 10:57
 */

/**
 * 隐藏软键盘
 */
fun Activity.hideSoftKeyboard() {
    val view = this.currentFocus
    view?.let {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

/**
 * 显示软键盘
 */
fun Activity.showKeyboard() {
    val view = this.currentFocus
    view?.showKeyboard()
}
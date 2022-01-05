package org.sheedon.common.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Toast工具箱  可防止用户多次点击之后 显示消息的时长太长
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 6:38 下午
 */
object ToastUtils {

    private var oldMsg: String? = null
    private var toast: Toast? = null
    private var oneTime: Long = 0

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context 上下文
     * @param msg       文本内容
     */
    fun showToast(context: Context, msg: String) {
        if (toast == null) {
            toast = Toasty.normal(context, msg)
            toast!!.show()
            oneTime = System.currentTimeMillis()
        } else {
            val newTime = System.currentTimeMillis()
            if (msg == oldMsg) {
                if ((newTime - oneTime) / 1000 > Toast.LENGTH_LONG) {
                    oneTime = newTime
                    setText(context, msg)
                    toast!!.show()
                }
            } else {
                oneTime = newTime
                oldMsg = msg
                setText(context, msg)
                toast!!.show()
            }
        }
    }

    private fun setText(context: Context, msg: String) {
        try {
            toast?.setText(msg);
        } catch (e: RuntimeException) {
            toast = Toasty.normal(context, msg)
        }
    }

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context 上下文对象
     * @param resId   显示内容资源ID
     */
    fun showToast(context: Context, resId: Int) {
        showToast(context, context.getString(resId));
    }

    /**
     * 吐出一个显示时间较长的提示
     *
     * @param context     上下文对象
     * @param formatResId 被格式化的字符串资源的ID
     * @param args        参数数组
     */
    fun toastL(context: Context, formatResId: Int, vararg args: Any) {
        Toast.makeText(
            context,
            String.format(context.getString(formatResId), args),
            Toast.LENGTH_LONG
        ).show();
    }

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context     上下文对象
     * @param formatResId 被格式化的字符串资源的ID
     * @param args        参数数组
     */
    fun toastS(context: Context, formatResId: Int, vararg args: Any) {
        Toast.makeText(
            context,
            String.format(context.getString(formatResId), args),
            Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * 吐出一个显示时间较长的提示
     *
     * @param context 上下文对象
     * @param format  被格式化的字符串
     * @param args    参数数组
     */
    fun toastL(context: Context, format: String, vararg args: Any) {
        Toast.makeText(context, String.format(format, args), Toast.LENGTH_LONG).show();
    }

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context 上下文对象
     * @param format  被格式化的字符串
     * @param args    参数数组
     */
    fun toastS(context: Context, format: String, vararg args: Any) {
        Toast.makeText(context, String.format(format, args), Toast.LENGTH_SHORT).show();
    }
}
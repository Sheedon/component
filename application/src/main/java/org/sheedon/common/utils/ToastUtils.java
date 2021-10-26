package org.sheedon.common.utils;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Toast工具箱  可防止用户多次点击之后 显示消息的时长太长
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/5/18 4:21 下午
 */
public class ToastUtils {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context 上下文
     * @param s       文本内容
     */
    public static void showToast(Context context, String s) {
        if (toast == null) {
            toast = Toasty.normal(context, s);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            long newTime = System.currentTimeMillis();
            if (s != null && s.equals(oldMsg)) {
                if ((newTime - oneTime)/1000 > Toast.LENGTH_LONG) {
                    oneTime = newTime;
                    setText(context, s);
                    toast.show();
                }
            } else {
                oneTime = newTime;
                oldMsg = s;
                setText(context, s);
                toast.show();
            }
        }
    }

    private static void setText(Context context, String s){
        try {
            toast.setText(s);
        }catch (RuntimeException e){
            toast = Toasty.normal(context, s);
        }
    }


    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context 上下文对象
     * @param resId   显示内容资源ID
     */
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }


    /**
     * 吐出一个显示时间较长的提示
     *
     * @param context     上下文对象
     * @param formatResId 被格式化的字符串资源的ID
     * @param args        参数数组
     */
    public static void toastL(Context context, int formatResId, Object... args) {
        Toast.makeText(context, String.format(context.getString(formatResId), args), Toast.LENGTH_LONG).show();
    }

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context     上下文对象
     * @param formatResId 被格式化的字符串资源的ID
     * @param args        参数数组
     */
    public static void toastS(Context context, int formatResId, Object... args) {
        Toast.makeText(context, String.format(context.getString(formatResId), args), Toast.LENGTH_SHORT).show();
    }

    /**
     * 吐出一个显示时间较长的提示
     *
     * @param context 上下文对象
     * @param format  被格式化的字符串
     * @param args    参数数组
     */
    public static void toastL(Context context, String format, Object... args) {
        Toast.makeText(context, String.format(format, args), Toast.LENGTH_LONG).show();
    }

    /**
     * 吐出一个显示时间较短的提示
     *
     * @param context 上下文对象
     * @param format  被格式化的字符串
     * @param args    参数数组
     */
    public static void toastS(Context context, String format, Object... args) {
        Toast.makeText(context, String.format(format, args), Toast.LENGTH_SHORT).show();
    }

}

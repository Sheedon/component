package org.sheedon.tool.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * View工具类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2023/3/4 14:38
 */
object ViewUtils {

    fun expandAndCollapseToUpdateImage(
        view: ImageView,
        isShow: Boolean, expandRes: Drawable, collapseRes: Drawable
    ) {
        view.setImageDrawable(if (!isShow) expandRes else collapseRes)
    }
}
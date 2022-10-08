package org.sheedon.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * 正方形相对布局
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/9/13 17:08
 */
class SquareRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
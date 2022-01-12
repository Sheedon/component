package org.sheedon.layout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.sheedon.tool.DisplayUtil
import org.sheedon.tool.dip2px
import org.sheedon.tool.px2sp

/**
 * 空视图，居中显示空图标和文字描述
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/11 10:50 上午
 */
class EmptyLayout : FrameLayout {

    private val emptyImage = ImageView(context)
    private val emptyText = TextView(context)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attachAttrs(attrs, defStyleAttr)
    }

    private fun attachAttrs(attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        context.obtainStyledAttributes(attrs, R.styleable.EmptyLayout, defStyleAttr, 0)
            .apply {
                attachImageAttrs(this)
                attachTextAttrs(this)
            }.recycle()

        addView(emptyImage)
        addView(emptyText)
    }

    /**
     * 附加图片基础信息
     * @param types 类型数组
     */
    private fun attachImageAttrs(types: TypedArray) {
        // 获取手动设置的图片宽高
        val imageWidth = types.getResourceId(
            R.styleable.EmptyLayout_imageWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imageHeight = types.getResourceId(
            R.styleable.EmptyLayout_imageHeight,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imageLayoutParams = LayoutParams(imageWidth, imageHeight)

        imageLayoutParams.gravity = Gravity.CENTER
        // 设置下外边距，使得图片文字相对于页面居中
        val imageMarginBottom = types.getResourceId(
            R.styleable.EmptyLayout_imageMarginBottom,
            22
        ).toFloat()
        imageLayoutParams.bottomMargin = context.dip2px(imageMarginBottom)

        // 获取空图片
        val imageEmptyDrawable = types.getResourceId(
            R.styleable.EmptyLayout_comEmptyDrawable,
            R.drawable.img_empty
        )

        // 填充数据
        with(emptyImage) {
            layoutParams = imageLayoutParams
            maxWidth = context.dip2px(100F)
            maxHeight = context.dip2px(100F)
            minimumWidth = context.dip2px(48F)
            minimumHeight = context.dip2px(48F)
            setBackgroundResource(imageEmptyDrawable)
        }
    }

    /**
     * 附加文字基础信息
     * @param types 类型数组
     */
    private fun attachTextAttrs(types: TypedArray) {
        val textLayoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textLayoutParams.gravity = Gravity.CENTER
        val textMarginTop = types.getResourceId(
            R.styleable.EmptyLayout_textMarginTop,
            22
        ).toFloat()

        textLayoutParams.topMargin = context.dip2px(textMarginTop)


        val color =
            types.getResourceId(R.styleable.EmptyLayout_android_textColor, R.color.empty_text_color)
        val size =
            types.getResourceId(R.styleable.EmptyLayout_android_textSize, R.dimen.empty_text_size)
        val content =
            types.getResourceId(R.styleable.EmptyLayout_comEmptyText, R.string.empty_content)

        // 填充数据
        with(emptyText) {
            layoutParams = textLayoutParams
            setTextColor(ContextCompat.getColor(context, color))
            textSize = context.px2sp(resources.getDimension(size)).toFloat()
            text = resources.getString(content)
        }

    }

}
package org.sheedon.layout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.databinding.BindingAdapter

/**
 * 虚线
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/12/7 13:50
 */
class DashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    /**间距宽度 */
    private var dashWidth: Float

    /**线段高度 */
    private var lineHeight: Float

    /**线段宽度 */
    private var lineWidth: Float

    /**线段颜色 */
    private var lineColor: Int
    private var dashOrientation: Int
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var widthSize = 0
    private var heightSize = 0


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight()
        heightSize = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom())
        if (dashOrientation == ORIENTATION_HORIZONTAL) {
            ////不管在布局文件中虚线高度设置为多少，控件的高度统一设置为线段的高度
            setMeasuredDimension(widthSize, lineHeight.toInt())
        } else {
            //当为竖直方向时，控件宽度设置为虚线的高度
            setMeasuredDimension(lineHeight.toInt(), heightSize)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (dashOrientation) {
            ORIENTATION_VERTICAL -> drawVerticalLine(canvas)
            else -> drawHorizontalLine(canvas)
        }
    }

    /**
     * 画水平方向虚线
     * @param canvas
     */
    fun drawHorizontalLine(canvas: Canvas) {
        var totalWidth = 0f
        canvas.save()
        val pts = floatArrayOf(0f, 0f, lineWidth, 0f)
        //在画线之前需要先把画布向下平移办个线段高度的位置，目的就是为了防止线段只画出一半的高度
        //因为画线段的起点位置在线段左下角
        canvas.translate(0F, lineHeight / 2)
        while (totalWidth <= widthSize) {
            canvas.drawLines(pts, mPaint)
            canvas.translate(lineWidth + dashWidth, 0F)
            totalWidth += lineWidth + dashWidth
        }
        canvas.restore()
    }

    /**
     * 画竖直方向虚线
     * @param canvas
     */
    fun drawVerticalLine(canvas: Canvas) {
        var totalWidth = 0f
        canvas.save()
        val pts = floatArrayOf(0f, 0f, 0f, lineWidth)
        //在画线之前需要先把画布向右平移半个线段高度的位置，目的就是为了防止线段只画出一半的高度
        //因为画线段的起点位置在线段左下角
        canvas.translate(lineHeight / 2, 0F)
        while (totalWidth <= heightSize) {
            canvas.drawLines(pts, mPaint)
            canvas.translate(0F, lineWidth + dashWidth)
            totalWidth += lineWidth + dashWidth
        }
        canvas.restore()
    }


    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DashView)
        dashWidth = typedArray.getDimension(R.styleable.DashView_dashWidth, DEFAULT_DASH_WIDTH)
        lineHeight = typedArray.getDimension(R.styleable.DashView_lineHeight, DEFAULT_LINE_HEIGHT)
        lineWidth = typedArray.getDimension(R.styleable.DashView_lineWidth, DEFAULT_LINE_WIDTH)
        lineColor = typedArray.getColor(R.styleable.DashView_lineColor, DEFAULT_LINE_COLOR)
        dashOrientation =
            typedArray.getInteger(R.styleable.DashView_dashOrientation, DEFAULT_DASH_ORIENTATION)
        mPaint.color = lineColor
        mPaint.strokeWidth = lineHeight
        typedArray.recycle()
    }

    companion object {
        private const val TAG = "DashView"
        const val DEFAULT_DASH_WIDTH = 100F
        const val DEFAULT_LINE_WIDTH = 100F
        const val DEFAULT_LINE_HEIGHT = 10F
        const val DEFAULT_LINE_COLOR = 0x9E9E9E

        /**虚线的方向 */
        const val ORIENTATION_HORIZONTAL = 0
        const val ORIENTATION_VERTICAL = 1

        /**默认为水平方向 */
        const val DEFAULT_DASH_ORIENTATION = ORIENTATION_HORIZONTAL


        @JvmStatic
        @BindingAdapter(
            value = ["dashWidth", "lineHeight", "lineWidth", "lineColor", "dashOrientation"],
            requireAll = false
        )
        fun attachInfo(
            view: DashView,
            dashWidth: Int?,
            lineHeight: Int?,
            lineWidth: Int?,
            lineColor: Int?,
            dashOrientation: Int?,
        ) {
            dashWidth?.takeIf { it != 0 }?.also { view.dashWidth = it.toFloat() }
            lineHeight?.takeIf { it != 0 }?.also { view.lineHeight = it.toFloat() }
            lineWidth?.takeIf { it != 0 }?.also { view.lineWidth = it.toFloat() }
            lineColor?.takeIf { it != 0 }?.also { view.lineColor = it }
            dashOrientation?.also { view.dashOrientation = it }
        }
    }
}

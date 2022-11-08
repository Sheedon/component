package org.sheedon.layout.special

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 雷达搜索视图
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/8 10:45
 */
class RadarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 默认最小宽度
    private val mDefaultSize = 120 // px

    // 画笔背景色
    private var mBackgroundColor: Int = ContextCompat.getColor(context, R.color.specialColorPrimary)

    // 外轮廓
    private val mOutlinePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = mBackgroundColor
            strokeWidth = mOutlineWidth
        }
    }
    private lateinit var mOutlineRect: Rect

    // 外轮廓宽度
    private var mOutlineWidth = 8F

    // 刻度
    private val mScalePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mBackgroundColor
            style = Paint.Style.STROKE
            strokeWidth = mScaleWidth
            strokeCap = Paint.Cap.ROUND
        }
    }

    // 刻度宽度
    private var mScaleWidth = 3F

    // 刻度中心点
    private var mScaleCenterX = 0

    // 线条数
    private var mDottedLineCount = 160

    // 内部虚线的外部半径
    private var mExternalDottedLineRadius = 0F

    // 内部虚线的内部半径
    private var mInsideDottedLineRadius = 0F

    // 圆弧跟虚线之间的距离
    private val mLineDistance = 20

    // 线条宽度
    private val mDottedLineWidth = 40F

    // 扫描扇形
    private val mScanPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mScanBackgroundColor
            style = Paint.Style.FILL
        }
    }
    private var mStartAngle = 0F
    private val mSweepAngle = -60F

    private val mScanBackgroundColor = ContextCompat.getColor(context, R.color.specialScanColor)

    // 中心圆点
    private val mDotPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mBackgroundColor
            style = Paint.Style.FILL_AND_STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    // 圆点宽度
    private var mDotRadius = 16F

    // 动画是否启动中
    private var animating = false

    // 旋转动画
    private val mAnimator by lazy {
        ValueAnimator.ofFloat(0F, 360F).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
    }


    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val vWidth = measureDimension(widthMeasureSpec)
        val vHeight = measureDimension(heightMeasureSpec)
        val size = min(vWidth, vHeight)

        setMeasuredDimension(size, size)

        setParamUpdate()
    }

    /**
     * 设置外轮廓宽高
     */
    private fun setParamUpdate() {
        mOutlineRect = Rect(
            mOutlineWidth.toInt(),
            mOutlineWidth.toInt(),
            measuredWidth - mOutlineWidth.toInt(),
            measuredHeight - mOutlineWidth.toInt()
        )
    }

    /**
     * 测量尺寸，控制最小尺寸
     */
    private fun measureDimension(spec: Int) = when (MeasureSpec.getMode(spec)) {
        MeasureSpec.EXACTLY -> {
            // exactly number or match_parent
            MeasureSpec.getSize(spec)
        }
        MeasureSpec.AT_MOST -> {
            // wrap_content
            min(mDefaultSize, MeasureSpec.getSize(spec))
        }
        else -> {
            mDefaultSize
        }
    }

    /**
     * 页面发生改变
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mScaleCenterX = (w / 2F).toInt()

        // 虚线的外部半径
        mExternalDottedLineRadius = measuredWidth / 2.0F - mLineDistance
        // 虚线的内部半径
        mInsideDottedLineRadius = mExternalDottedLineRadius - mDottedLineWidth
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 设置默认居中
        var l = left
        var r = right
        var t = top
        var b = bottom
        when {
            width > height -> {
                // 宽度比高度大 那么要设置默认居中就得把left往右移 right往左移
                l = (width - measuredWidth) / 2
                r = width - l
                layout(l, t, r, b)
            }
            height > width -> {
                // 高度比宽度大 那么要设置默认居中就得把top往下移 bottom往上移
                t = (height - measuredHeight) / 2
                b = height - t
                layout(l, t, r, b)
            }
            else -> super.onLayout(changed, left, top, right, bottom)
        }

    }

    /**
     * 绘制
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawOutline(this)
            drawScale(this)
            drawScan(this)
            drawDot(this)
        }
    }

    /**
     * 绘制外轮廓
     */
    private fun drawOutline(canvas: Canvas) {
        canvas.drawCircle(
            measuredWidth.toFloat() / 2,
            measuredHeight.toFloat() / 2,
            measuredWidth.toFloat() / 2 - mOutlineWidth,
            mOutlinePaint
        )
    }

    /**
     * 绘制刻度
     */
    private fun drawScale(canvas: Canvas) {
        val everyDegrees = 2.0f * Math.PI / mDottedLineCount

        for (i in 0 until mDottedLineCount) {
            val degrees = i * everyDegrees
            // 过滤底部90度的弧长
            val startX: Float =
                mScaleCenterX + sin(degrees).toFloat() * mInsideDottedLineRadius
            val startY: Float =
                mScaleCenterX - cos(degrees).toFloat() * mInsideDottedLineRadius
            val stopX: Float =
                mScaleCenterX + sin(degrees).toFloat() * mExternalDottedLineRadius
            val stopY: Float =
                mScaleCenterX - cos(degrees).toFloat() * mExternalDottedLineRadius
            canvas.drawLine(startX, startY, stopX, stopY, mScalePaint)
        }
    }

    /**
     * 绘制扫描的扇形
     */
    private fun drawScan(canvas: Canvas) {
        canvas.drawArc(mOutlineRect.toRectF(), mStartAngle, mSweepAngle, true, mScanPaint)
    }

    /**
     * 绘制圆点
     */
    private fun drawDot(canvas: Canvas) {
        canvas.drawCircle(
            measuredWidth.toFloat() / 2,
            measuredHeight.toFloat() / 2,
            mDotRadius,
            mDotPaint
        )
    }

    /**
     * 启动动画
     */
    fun start() {
        animating = true
        mAnimator.addUpdateListener {
            val angle = it.animatedValue as Float
            mStartAngle = angle
            postInvalidate()
        }
        mAnimator.start()
    }

    /**
     * 停止动画
     */
    fun stop() {
        animating = false
        if (mAnimator.isRunning) {
            mAnimator.cancel()
            mAnimator.removeAllListeners()
        }
        mStartAngle = 0F
    }

    /**
     * 设置外轮廓/刻度/圆心的背景色
     */
    fun setRadarBackgroundColor(res: Int) {
        val color = ContextCompat.getColor(context, res)
        setRealRadarBackgroundColor(color)
    }

    /**
     * 设置外轮廓/刻度/圆心的背景色
     */
    fun setRealRadarBackgroundColor(res: Int) {
        mBackgroundColor = res
        mOutlinePaint.color = mBackgroundColor
        mScalePaint.color = mBackgroundColor
        mDotPaint.color = mBackgroundColor
    }

    /**
     * 设置外轮廓/刻度/圆心的背景色
     */
    fun setRadarBackgroundColor(res: String) {
        mBackgroundColor = Color.parseColor(res)
        mOutlinePaint.color = mBackgroundColor
        mScalePaint.color = mBackgroundColor
        mDotPaint.color = mBackgroundColor
    }

    /**
     * 设置扇形扫描的背景色
     */
    fun setScanBackgroundColor(res: Int) {
        val color = ContextCompat.getColor(context, res)
        setRealScanBackgroundColor(color)
    }

    /**
     * 设置扇形扫描的背景色
     */
    fun setRealScanBackgroundColor(res: Int) {
        mBackgroundColor = res
        mScanPaint.color = mBackgroundColor
    }

    /**
     * 设置外轮廓的宽度
     */
    fun setOutlineWidth(width: Float) {
        this.mOutlineWidth = width
        setParamUpdate()
        this.mOutlinePaint.strokeWidth = mOutlineWidth
    }

    fun setScaleWidth(width: Float) {
        this.mScaleWidth = width
        this.mScalePaint.strokeWidth = width
    }

    fun setDottedLineCount(count: Int) {
        this.mDottedLineCount = count
    }

    fun setDotRadius(radius: Float) {
        this.mDotRadius = radius
    }

    enum class AnimateType {
        START, STOP, NONE
    }

    companion object {


        @JvmStatic
        @BindingAdapter("setAnimate")
        fun setAnimate(radarView: RadarView, type: AnimateType?) {
            if (type == null || type == AnimateType.NONE) return

            if (type == AnimateType.START) {
                radarView.start()
            } else if (type == AnimateType.STOP) {
                radarView.stop()
            }
        }

        @JvmStatic
        @BindingAdapter("updateAnimate")
        fun updateAnimate(radarView: RadarView, type: ObservableField<AnimateType?>) {
            val animateType = type.get()
            if (animateType == AnimateType.NONE) return

            if (animateType == AnimateType.START) {
                radarView.start()
            } else if (animateType == AnimateType.STOP) {
                radarView.stop()
            }
        }

        @JvmStatic
        @BindingAdapter("radarBackgroundColor")
        fun setRadarBackgroundColor(radarView: RadarView, res: Int) {
            radarView.setRealRadarBackgroundColor(res)
        }

        @JvmStatic
        @BindingAdapter("scanBackgroundColor")
        fun setScanBackgroundColor(radarView: RadarView, res: Int) {
            radarView.setRealScanBackgroundColor(res)
        }

        @JvmStatic
        @BindingAdapter("outlineWidth")
        fun setOutlineWidth(radarView: RadarView, width: Float) {
            radarView.setOutlineWidth(width)
        }

        @JvmStatic
        @BindingAdapter("scaleWidth")
        fun setScaleWidth(radarView: RadarView, width: Float) {
            radarView.setScaleWidth(width)
        }

        @JvmStatic
        @BindingAdapter("lineCount")
        fun setDottedLineCount(radarView: RadarView, count: Int) {
            radarView.setDottedLineCount(count)
        }

        @JvmStatic
        @BindingAdapter("dotRadius")
        fun setDotRadius(radarView: RadarView, radius: Float) {
            radarView.setDotRadius(radius)
        }

    }

}
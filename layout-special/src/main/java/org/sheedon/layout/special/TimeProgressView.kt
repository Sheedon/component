package org.sheedon.layout.special

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import org.sheedon.tool.ext.dip2px
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 时间进度视图
 * 中间显示开始或时间，点击开始
 * 圆球开始围绕着圆盘周期运动，时间按秒添加（00:00）
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/16 14:12
 */
class TimeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 默认最小宽度
    private val mDefaultSize = 120 // px

    private val mSpacing = 0F

    // 刻度宽度
    private var mScaleWidth = 3F

    private var mCurrentSize = mDefaultSize

    // 线条数
    private var mDottedLineCount = 40

    // 刻度中心点
    private var mScaleCenterX = 0

    // 内部虚线的外部半径
    private var mExternalDottedLineRadius = 0F

    // 内部虚线的内部半径
    private var mInsideDottedLineRadius = 0F

    // 圆弧跟虚线之间的距离
    private val mLineDistance = 100

    // 线条宽度
    private val mDottedLineWidth = 20F

    // 背景画笔
    private val mBgPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
    }

    // 刻度
    private val mScalePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#A2ABBA")
            style = Paint.Style.STROKE
            strokeWidth = mScaleWidth
            strokeCap = Paint.Cap.ROUND
        }
    }

    private val bgRing by lazy {
        val drawable = AppCompatResources.getDrawable(context, R.drawable.bg_external_ring)!!
        drawable.toBitmap(mCurrentSize, mCurrentSize)
    }

    // 文字
    private val mTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#54607C")
            style = Paint.Style.FILL_AND_STROKE
            textSize = 50F
            strokeWidth = 3F
            textAlign = Paint.Align.CENTER
        }
    }

    // 文字
    private val mSubTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#54607C")
            style = Paint.Style.FILL_AND_STROKE
            textSize = 32F
            textAlign = Paint.Align.CENTER
        }
    }

    private val bgRound by lazy {
        AppCompatResources.getDrawable(context, R.drawable.bg_round)!!
            .toBitmap(mCurrentSize, mCurrentSize)
    }

    private val ball by lazy {
        AppCompatResources.getDrawable(context, R.drawable.ic_ball_indicator)!!
            .toBitmap()
    }

    private var mAngle = 300F

    // 动画是否启动中
    private var animating = false

    // 旋转动画
    private val mAnimator by lazy {
        ValueAnimator.ofFloat(0F, 360F).apply {
            duration = 3600
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
    }

    private var currentTime = 0
    private var isFirst = true
    private var lastTime = 0L


    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val vWidth = measureDimension(widthMeasureSpec)
        val vHeight = measureDimension(heightMeasureSpec)
        val size = min(vWidth, vHeight)
        mCurrentSize = size

        setMeasuredDimension(size, size)

//        setParamUpdate()
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
        mExternalDottedLineRadius = measuredWidth / 2.0F - context.dip2px(52F)
        // 虚线的内部半径
        mInsideDottedLineRadius = mExternalDottedLineRadius - mDottedLineWidth
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawBitmap(bgRing, 0F, 0F, mBgPaint)
            drawBitmap(bgRound, 6F, 6F, mBgPaint)
            drawScale(this)
            drawBall(this)
            drawText(this)
        }
    }

    /**
     * 绘制刻度
     */
    private fun drawScale(canvas: Canvas) {
        val everyDegrees = 2.0f * Math.PI / mDottedLineCount
        val everyAngle = Math.toDegrees(everyDegrees).toInt()

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


            mScalePaint.color = loadColor(degrees, everyAngle)

            canvas.drawLine(startX, startY, stopX, stopY, mScalePaint)
        }
    }


    private fun loadColor(degrees: Double, everyAngle: Int): Int {
        // 角度 逆时针转顺时针 - 90
        var angle = (Math.toDegrees(degrees).toInt() * -1 + 450) % 360
        if (angle < 14 * everyAngle) {
            angle += 360
        }

        if (angle >= mAngle % 360 && angle <= mAngle % 360 + everyAngle) {
            return getColorWithAlpha(0F, DEFAULT_COLOR)
        }

        if (angle < mAngle && angle >= mAngle - 8 * everyAngle) {
            return getColorWithAlpha((mAngle + 360 - angle) % 360 / 8F / everyAngle, DEFAULT_COLOR)
        }

        if (angle >= mAngle + everyAngle && angle <= mAngle + 14 * everyAngle) {
            return getColorWithAlpha(
                (angle + 360 - mAngle) % 360 / 14F / everyAngle,
                DEFAULT_COLOR
            )
        }

        return getColorWithAlpha(1F, DEFAULT_COLOR)
    }

    /**
     * 给color添加透明度
     * @param alpha 透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    private fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = 255.coerceAtMost(0.coerceAtLeast((alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }


    /**
     * 绘制小圆球
     */
    private fun drawBall(canvas: Canvas) {
        val radius = mCurrentSize / 2F
        val mX = radius - context.dip2px(
            28F
        ) + cos(Math.toRadians(mAngle.toDouble())) * (radius - context.dip2px(68F))

        val mY = radius - context.dip2px(
            28F
        ) - sin(Math.toRadians(mAngle.toDouble())) * (radius - context.dip2px(68F))

        canvas.drawBitmap(ball, mX.toFloat(), mY.toFloat(), mBgPaint)
    }


    private fun drawText(canvas: Canvas) {
        if (isFirst) {
            drawStartText(canvas)
            return
        }

        if (!animating) {
            drawScanningCompleted(canvas)
            return
        }

        drawTimeText(canvas)

    }

    private fun drawStartText(canvas: Canvas) {

        val fontMetrics = mTextPaint.fontMetrics
        val baseLineY = measuredHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2

        canvas.drawText(
            START,
            measuredWidth.toFloat() / 2,
            baseLineY,
            mTextPaint
        )
    }

    private fun drawScanningCompleted(canvas: Canvas) {
        var fontMetrics = mTextPaint.fontMetrics
        var baseLineY = measuredHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2 - 50

        canvas.drawText(
            loadTime(),
            measuredWidth.toFloat() / 2,
            baseLineY,
            mTextPaint
        )

        fontMetrics = mSubTextPaint.fontMetrics
        baseLineY = measuredHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2 + 50
        canvas.drawText(
            SCANNING_COMPLETED,
            measuredWidth.toFloat() / 2,
            baseLineY,
            mSubTextPaint
        )


    }

    private fun drawTimeText(canvas: Canvas) {
        val fontMetrics = mTextPaint.fontMetrics
        val baseLineY = measuredHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2

        canvas.drawText(
            loadTime(),
            measuredWidth.toFloat() / 2,
            baseLineY,
            mTextPaint
        )
    }

    private fun loadTime(): String {
        if (currentTime >= 3600) return "60:00"
        val seconds = currentTime % 60
        val minutes = currentTime / 60 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * 启动动画
     */
    fun start() {
        animating = true
        isFirst = false
        currentTime = 0
        lastTime = System.currentTimeMillis()
        mAnimator.addUpdateListener {

            if (System.currentTimeMillis() - lastTime >= 1000) {
                lastTime = System.currentTimeMillis()
                currentTime++
            }

            val angle = it.animatedValue as Float
            mAngle = (300 - angle) % 360
            val everyDegrees = 2.0f * Math.PI / mDottedLineCount
            val everyAngle = Math.toDegrees(everyDegrees).toInt()
            if (mAngle < 14 * everyAngle) {
                mAngle += 360
            }
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
        postInvalidate()
        mAngle = 300F
    }

    companion object {
        //        A2ABBA
        private val DEFAULT_COLOR = Color.parseColor("#A2ABBA")
        private const val START = "START"
        private const val SCANNING_COMPLETED = "扫描完成！"

        @JvmStatic
        @BindingAdapter("setAnimate")
        fun setAnimate(radarView: TimeProgressView, type: AnimateType?) {
            if (type == null || type == AnimateType.NONE) return

            if (type == AnimateType.START) {
                radarView.start()
            } else if (type == AnimateType.STOP) {
                radarView.stop()
            }
        }

        @JvmStatic
        @BindingAdapter("updateAnimate")
        fun updateAnimate(radarView: TimeProgressView, type: ObservableField<AnimateType?>) {
            val animateType = type.get()
            if (animateType == AnimateType.NONE) return

            if (animateType == AnimateType.START) {
                radarView.start()
            } else if (animateType == AnimateType.STOP) {
                radarView.stop()
            }
        }
    }
}
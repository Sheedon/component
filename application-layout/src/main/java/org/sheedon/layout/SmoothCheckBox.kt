/**
 * * Copyright 2016 andy
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sheedon.layout

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Checkable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingAdapter
import org.sheedon.tool.ext.dip2px
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Author : andy
 * Date   : 16/1/21 11:28
 * Email  : andyxialm@gmail.com
 * Github : github.com/andyxialm
 * Description : A custom CheckBox with animation for Android
 */
class SmoothCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),
    Checkable {
    private var mPaint: Paint? = null
    private var mTickPaint: Paint? = null
    private var mFloorPaint: Paint? = null
    private var mTickPoints: Array<Point?> = arrayOf()
    private var mCenterPoint: Point? = null
    private var mTickPath: Path? = null
    private var mLeftLineDistance = 0f
    private var mRightLineDistance = 0f
    private var mDrewDistance = 0f
    private var mScaleVal = 1.0f
    private var mFloorScale = 1.0f
    private var mWidth = 0
    private var mAnimDuration = 0
    private var mStrokeWidth = 0
    private var mCheckedColor = 0
    private var mUnCheckedColor = 0
    private var mFloorColor = 0
    private var mFloorUnCheckedColor = 0
    private var mChecked = false
    private var mTickDrawing = false
    private var mListener: OnCheckedChangeListener? = null

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SmoothCheckBox)
        val tickColor = ta.getColor(R.styleable.SmoothCheckBox_color_tick, COLOR_TICK)
        mAnimDuration = ta.getInt(R.styleable.SmoothCheckBox_duration_time, DEF_ANIM_DURATION)
        mFloorColor =
            ta.getColor(R.styleable.SmoothCheckBox_color_unchecked_stroke, COLOR_FLOOR_UNCHECKED)
        mCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_color_checked, COLOR_CHECKED)
        mUnCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_color_unchecked, COLOR_UNCHECKED)
        mStrokeWidth = ta.getDimensionPixelSize(
            R.styleable.SmoothCheckBox_stroke_width, context.dip2px(0F)
        )
        ta.recycle()
        mFloorUnCheckedColor = mFloorColor
        mTickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTickPaint!!.style = Paint.Style.STROKE
        mTickPaint!!.strokeCap = Paint.Cap.ROUND
        mTickPaint!!.color = tickColor
        mFloorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFloorPaint!!.style = Paint.Style.FILL
        mFloorPaint!!.color = mFloorColor
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mCheckedColor
        mTickPath = Path()
        mCenterPoint = Point()
        mTickPoints = arrayOfNulls(3)
        mTickPoints[0] = Point()
        mTickPoints[1] = Point()
        mTickPoints[2] = Point()
        setOnClickListener {
            toggle()
            mTickDrawing = false
            mDrewDistance = 0f
            if (isChecked) {
                startCheckedAnimation()
            } else {
                startUnCheckedAnimation()
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putBoolean(KEY_INSTANCE_STATE, isChecked)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val isChecked = state.getBoolean(KEY_INSTANCE_STATE)
            setChecked(isChecked)
            super.onRestoreInstanceState(state.getParcelable(KEY_INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun toggle() {
        this.isChecked = !isChecked
    }

    override fun setChecked(checked: Boolean) {
        mChecked = checked
        reset()
        invalidate()
        if (mListener != null) {
            mListener!!.onCheckedChanged(this@SmoothCheckBox, mChecked)
        }
    }

    /**
     * checked with animation
     *
     * @param checked checked
     * @param animate change with animation
     */
    fun setChecked(checked: Boolean, animate: Boolean) {
        if (animate) {
            mTickDrawing = false
            mChecked = checked
            mDrewDistance = 0f
            if (checked) {
                startCheckedAnimation()
            } else {
                startUnCheckedAnimation()
            }
            if (mListener != null) {
                mListener!!.onCheckedChanged(this@SmoothCheckBox, mChecked)
            }
        } else {
            this.isChecked = checked
        }
    }

    private fun reset() {
        mTickDrawing = true
        mFloorScale = 1.0f
        mScaleVal = if (isChecked) 0f else 1.0f
        mFloorColor = if (isChecked) mCheckedColor else mFloorUnCheckedColor
        mDrewDistance = if (isChecked) (mLeftLineDistance + mRightLineDistance) else 0F
    }

    private fun measureSize(measureSpec: Int): Int {
        val defSize: Int = context.dip2px(DEF_DRAW_SIZE)
        val specSize = MeasureSpec.getSize(measureSpec)
        val specMode = MeasureSpec.getMode(measureSpec)
        var result = 0
        when (specMode) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> result = defSize.coerceAtMost(specSize)
            MeasureSpec.EXACTLY -> result = specSize
        }
        return result
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mWidth = measuredWidth
        mStrokeWidth = (if (mStrokeWidth == 0) measuredWidth / 10 else mStrokeWidth)
        mStrokeWidth = if (mStrokeWidth > measuredWidth / 5) measuredWidth / 5 else mStrokeWidth
        mStrokeWidth = if ((mStrokeWidth < 3)) 3 else mStrokeWidth
        mCenterPoint!!.x = mWidth / 2
        mCenterPoint!!.y = measuredHeight / 2
        mTickPoints[0]!!.x = (measuredWidth.toFloat() / 30 * 7).roundToInt()
        mTickPoints[0]!!.y = (measuredHeight.toFloat() / 30 * 14).roundToInt()
        mTickPoints[1]!!.x = (measuredWidth.toFloat() / 30 * 13).roundToInt()
        mTickPoints[1]!!.y = (measuredHeight.toFloat() / 30 * 20).roundToInt()
        mTickPoints[2]!!.x = (measuredWidth.toFloat() / 30 * 22).roundToInt()
        mTickPoints[2]!!.y = (measuredHeight.toFloat() / 30 * 10).roundToInt()
        mLeftLineDistance = sqrt(
            (mTickPoints[1]!!.x - mTickPoints[0]!!.x).toDouble().pow(2.0) +
                    (mTickPoints[1]!!.y - mTickPoints[0]!!.y).toDouble().pow(2.0)
        ).toFloat()
        mRightLineDistance = sqrt(
            (mTickPoints[2]!!.x - mTickPoints[1]!!.x).toDouble().pow(2.0) +
                    (mTickPoints[2]!!.y - mTickPoints[1]!!.y).toDouble().pow(2.0)
        ).toFloat()
        mTickPaint!!.strokeWidth = mStrokeWidth.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        drawBorder(canvas)
        drawCenter(canvas)
        drawTick(canvas)
    }

    private fun drawCenter(canvas: Canvas) {
        mPaint!!.color = mUnCheckedColor
        val radius = (mCenterPoint!!.x - mStrokeWidth) * mScaleVal
        canvas.drawCircle(
            mCenterPoint!!.x.toFloat(),
            mCenterPoint!!.y.toFloat(),
            radius,
            (mPaint)!!
        )
    }

    private fun drawBorder(canvas: Canvas) {
        mFloorPaint!!.color = mFloorColor
        val radius = mCenterPoint!!.x
        canvas.drawCircle(
            mCenterPoint!!.x.toFloat(),
            mCenterPoint!!.y.toFloat(),
            radius * mFloorScale,
            (mFloorPaint)!!
        )
    }

    private fun drawTick(canvas: Canvas) {
        if (mTickDrawing && isChecked) {
            drawTickPath(canvas)
        }
    }

    private fun drawTickPath(canvas: Canvas) {
        mTickPath!!.reset()
        // draw left of the tick
        if (mDrewDistance < mLeftLineDistance) {
            val step: Float = if ((mWidth / 20.0f) < 3) 3F else (mWidth / 20.0f)
            mDrewDistance += step
            val stopX =
                mTickPoints[0]!!.x + (mTickPoints[1]!!.x - mTickPoints[0]!!.x) * mDrewDistance / mLeftLineDistance
            val stopY =
                mTickPoints[0]!!.y + (mTickPoints[1]!!.y - mTickPoints[0]!!.y) * mDrewDistance / mLeftLineDistance
            mTickPath!!.moveTo(mTickPoints[0]!!.x.toFloat(), mTickPoints[0]!!.y.toFloat())
            mTickPath!!.lineTo(stopX, stopY)
            canvas.drawPath((mTickPath)!!, (mTickPaint)!!)
            if (mDrewDistance > mLeftLineDistance) {
                mDrewDistance = mLeftLineDistance
            }
        } else {
            mTickPath!!.moveTo(mTickPoints[0]!!.x.toFloat(), mTickPoints[0]!!.y.toFloat())
            mTickPath!!.lineTo(mTickPoints[1]!!.x.toFloat(), mTickPoints[1]!!.y.toFloat())
            canvas.drawPath((mTickPath)!!, (mTickPaint)!!)

            // draw right of the tick
            if (mDrewDistance < mLeftLineDistance + mRightLineDistance) {
                val stopX =
                    mTickPoints[1]!!.x + (mTickPoints[2]!!.x - mTickPoints[1]!!.x) * (mDrewDistance - mLeftLineDistance) / mRightLineDistance
                val stopY =
                    mTickPoints[1]!!.y - (mTickPoints[1]!!.y - mTickPoints[2]!!.y) * (mDrewDistance - mLeftLineDistance) / mRightLineDistance
                mTickPath!!.reset()
                mTickPath!!.moveTo(mTickPoints[1]!!.x.toFloat(), mTickPoints[1]!!.y.toFloat())
                mTickPath!!.lineTo(stopX, stopY)
                canvas.drawPath((mTickPath)!!, (mTickPaint)!!)
                val step: Float = if ((mWidth / 20) < 3) 3F else (mWidth / 20).toFloat()
                mDrewDistance += step
            } else {
                mTickPath!!.reset()
                mTickPath!!.moveTo(mTickPoints[1]!!.x.toFloat(), mTickPoints[1]!!.y.toFloat())
                mTickPath!!.lineTo(mTickPoints[2]!!.x.toFloat(), mTickPoints[2]!!.y.toFloat())
                canvas.drawPath((mTickPath)!!, (mTickPaint)!!)
            }
        }

        // invalidate
        if (mDrewDistance < mLeftLineDistance + mRightLineDistance) {
            postDelayed({ postInvalidate() }, 10)
        }
    }

    private fun startCheckedAnimation() {
        val animator = ValueAnimator.ofFloat(1.0f, 0f)
        animator.duration = (mAnimDuration / 3 * 2).toLong()
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            mScaleVal = animation.animatedValue as Float
            mFloorColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal)
            postInvalidate()
        }
        animator.start()
        val floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f)
        floorAnimator.duration = mAnimDuration.toLong()
        floorAnimator.interpolator = LinearInterpolator()
        floorAnimator.addUpdateListener { animation ->
            mFloorScale = animation.animatedValue as Float
            postInvalidate()
        }
        floorAnimator.start()
        drawTickDelayed()
    }

    private fun startUnCheckedAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1.0f)
        animator.duration = mAnimDuration.toLong()
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            mScaleVal = animation.animatedValue as Float
            mFloorColor = getGradientColor(mCheckedColor, mFloorUnCheckedColor, mScaleVal)
            postInvalidate()
        }
        animator.start()
        val floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f)
        floorAnimator.duration = mAnimDuration.toLong()
        floorAnimator.interpolator = LinearInterpolator()
        floorAnimator.addUpdateListener { animation ->
            mFloorScale = animation.animatedValue as Float
            postInvalidate()
        }
        floorAnimator.start()
    }

    private fun drawTickDelayed() {
        postDelayed({
            mTickDrawing = true
            postInvalidate()
        }, mAnimDuration.toLong())
    }

    fun setOnCheckedChangeListener(l: OnCheckedChangeListener?) {
        mListener = l
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(checkBox: SmoothCheckBox?, isChecked: Boolean)
    }

    companion object {
        private const val KEY_INSTANCE_STATE = "InstanceState"
        private const val COLOR_TICK = Color.WHITE
        private const val COLOR_UNCHECKED = Color.WHITE
        private val COLOR_CHECKED = Color.parseColor("#FB4846")
        private val COLOR_FLOOR_UNCHECKED = Color.parseColor("#DFDFDF")
        private const val DEF_DRAW_SIZE = 25F
        private const val DEF_ANIM_DURATION = 300
        private fun getGradientColor(startColor: Int, endColor: Int, percent: Float): Int {
            val startA = Color.alpha(startColor)
            val startR = Color.red(startColor)
            val startG = Color.green(startColor)
            val startB = Color.blue(startColor)
            val endA = Color.alpha(endColor)
            val endR = Color.red(endColor)
            val endG = Color.green(endColor)
            val endB = Color.blue(endColor)
            val currentA = (startA * (1 - percent) + endA * percent).toInt()
            val currentR = (startR * (1 - percent) + endR * percent).toInt()
            val currentG = (startG * (1 - percent) + endG * percent).toInt()
            val currentB = (startB * (1 - percent) + endB * percent).toInt()
            return Color.argb(currentA, currentR, currentG, currentB)
        }

        @JvmStatic
        @BindingAdapter("bindCheckedChange")
        fun setValue(view: SmoothCheckBox, isChecked: Boolean) {
            if (view.isChecked != isChecked) {
                view.isChecked = isChecked
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["checkedChanged"])
        fun bindValueChanged(
            view: SmoothCheckBox?,
            listener: InverseBindingListener?
        ) {
            view?.setOnCheckedChangeListener(object : OnCheckedChangeListener {
                override fun onCheckedChanged(checkBox: SmoothCheckBox?, isChecked: Boolean) {
                    listener?.onChange()
                }
            })
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "bindCheckedChange", event = "checkedChanged")
        fun bindValueChange(view: SmoothCheckBox): Boolean {
            return view.isChecked
        }
    }
}
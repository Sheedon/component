package org.sheedon.layout.scroll

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import org.sheedon.layout.scroll.RecordTopScrollView
import androidx.databinding.BindingAdapter

/**
 * 记录Top 数值的Scrollview
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/6/28 9:23 下午
 */
class RecordTopScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    var currentTop = 0
        private set

    // 记录状态 0：不需要记录 1：第一次记录 2：核实
    private
    var recordingState = 0
    var lastTop = 0
        private set

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        currentTop = t
        when (recordingState) {
            TYPE_NOT_RECORD -> {}
            TYPE_RECORDING -> {
                lastTop = oldt
                recordingState = TYPE_CHECK
                if (t != lastTop) {
                    smoothScrollTo(0, lastTop)
                }
            }
            TYPE_CHECK -> if (t != lastTop) {
                smoothScrollTo(0, lastTop)
            } else {
                recordingState = TYPE_NOT_RECORD
            }
        }
    }

    fun setNeedRecording(needRecording: Boolean) {
        recordingState = if (needRecording) {
            TYPE_RECORDING
        } else {
            TYPE_NOT_RECORD
        }
    }

    companion object {
        private const val TYPE_NOT_RECORD = 0
        private const val TYPE_RECORDING = 1
        private const val TYPE_CHECK = 2

        @JvmStatic
        @BindingAdapter(value = ["bindScrollListener"], requireAll = false)
        fun bindScrollListener(
            scrollView: RecordTopScrollView,
            recordHandle: Boolean?
        ) {
            if (recordHandle == null) {
                return
            }
            if (recordHandle) {
                scrollView.setNeedRecording(true)
            } else {
                scrollView.smoothScrollTo(0, scrollView.lastTop)
            }
        }
    }
}
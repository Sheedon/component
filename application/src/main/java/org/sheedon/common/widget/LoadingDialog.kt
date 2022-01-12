package org.sheedon.common.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import org.sheedon.common.R
import org.sheedon.common.databinding.DialogLoadingBinding
import org.sheedon.common.handler.ILoadingDialogHandler
import org.sheedon.common.handler.ResConvertHandler
import org.sheedon.tool.ext.checkValue
import org.sheedon.tool.ext.dip2px

/**
 * 加载框
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 2:34 下午
 */
class LoadingDialog(
    context: Context,
    themeResId: Int = R.style.UploadDialogStyle
) : AlertDialog(context, themeResId), ILoadingDialogHandler, LifecycleObserver {

    private val model = LoadingModel()

    init {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)//点击外部不允许关闭dialog
        window?.setLayout(
            context.dip2px(120f),
            context.dip2px(120f)
        )
        val binding: DialogLoadingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_loading,
            null,
            false
        )
        setContentView(binding.root)
        binding.vm = model
    }


    override fun setTitle(title: CharSequence?) {
        this.setTitle(title.toString())
    }

    fun setTitle(title: String) {
        model.title = title
    }

    /**
     * 显示弹出窗
     *
     * @param message 描述
     */
    override fun showLoading(message: String) {
        setTitle(message)
        isShowing.checkValue { show() }
    }

    /**
     * 隐藏弹出窗
     */
    override fun hideLoading() {
        isShowing.checkValue({ dismiss() })
    }

    /**
     * 隐藏弹窗
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun hideDialog() {
        hideLoading()
    }


    data class LoadingModel(val _title: String? = "") : ViewModel() {
        var title = _title
            get() {
                return if (field.isNullOrEmpty()) {
                    ResConvertHandler.getInstance().convertString(R.string.prompt_loading)
                } else {
                    field
                }
            }
    }


}
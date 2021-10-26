package org.sheedon.common.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import org.sheedon.common.R;
import org.sheedon.common.databinding.DialogLoadingBinding;
import org.sheedon.common.handler.ILoadingDialogHandler;
import org.sheedon.common.handler.ResConvertHandler;
import org.sheedon.common.utils.DisplayUtil;

/**
 * 加载框
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/5/25 3:43 下午
 */
public class LoadingDialog extends AlertDialog implements ILoadingDialogHandler, LifecycleObserver {

    private final LoadingModel model = new LoadingModel();

    public LoadingDialog(@NonNull Context context) {
        this(context, 0);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.UploadDialogStyle);
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        getWindow().setLayout(DisplayUtil.dip2px(getContext(), 120), DisplayUtil.dip2px(getContext(), 120));
        DialogLoadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_loading, null, false);
        setContentView(binding.getRoot());
        binding.setVm(model);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.setTitle(title.toString());
    }

    public void setTitle(String title) {
        if (model != null)
            model.setTitle(title);
    }

    /**
     * 显示弹出窗
     *
     * @param message 描述
     */
    @Override
    public void showLoading(String message) {
        setTitle(message);
        if (isShowing()) {
            return;
        }
        show();
    }

    /**
     * 隐藏弹出窗
     */
    @Override
    public void hideLoading() {
        if (isShowing()) {
            dismiss();
        }
    }


    /**
     * 隐藏弹窗
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void hideDialog() {
        hideLoading();
    }


    public static class LoadingModel extends ViewModel {
        private String title;

        public String getTitle() {
            if (title == null || title.isEmpty()) {
                return ResConvertHandler.getInstance().convertString(R.string.prompt_loading);
            }
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

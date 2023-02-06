package org.sheedon.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 包含空View的RecyclerView
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/5/26 10:27 下午
 */
public class EmptyRecyclerView extends FrameLayout {

    private EmptyLayout emptyLayout;
    private RecyclerView recyclerView;

    public EmptyRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        recyclerView = new RecyclerView(context, attrs, defStyleAttr);
        recyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(recyclerView);

        emptyLayout = new EmptyLayout(context, attrs);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        emptyLayout.setLayoutParams(params);
        addView(emptyLayout);

    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(ListAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setAdapterEmptyRecyclerView(ListAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private boolean lastIsEmpty = true;

    public void notifyEmpty(boolean isEmpty) {
        if (lastIsEmpty == isEmpty)
            return;

        lastIsEmpty = isEmpty;

        if (isEmpty) {
            if (emptyLayout != null) {
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (emptyLayout != null) {
                emptyLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置列表适配器
     *
     * @param recyclerView SwipeRecyclerLayout
     * @param adapter      ListAdapter
     */
    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(EmptyRecyclerView recyclerView, ListAdapter adapter) {
        if (recyclerView.recyclerView != null) {
            recyclerView.recyclerView.setAdapter(adapter);
        }
    }

    /**
     * 设置列表适配器
     *
     * @param recyclerView SwipeRecyclerLayout
     * @param adapter      ListAdapter
     */
    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(EmptyRecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (recyclerView.recyclerView != null) {
            recyclerView.recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter(value = {"submitList"}, requireAll = false)
    public static void submitList(EmptyRecyclerView recyclerView, List list) {
        if (recyclerView.recyclerView != null && recyclerView.recyclerView.getAdapter() != null) {
            ListAdapter adapter = (ListAdapter) recyclerView.recyclerView.getAdapter();
            adapter.submitList(list);
        }
    }

    @BindingAdapter(value = {"scrollingEnabled"}, requireAll = false)
    public static void scrollingEnabled(EmptyRecyclerView recyclerView,
                                        boolean scrollingEnabled) {

        if (recyclerView.recyclerView != null) {
            recyclerView.recyclerView.setNestedScrollingEnabled(scrollingEnabled);
            if(!scrollingEnabled){
                recyclerView.recyclerView.setHasFixedSize(true);
                recyclerView.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        }
    }

    /**
     * 通知列表视图是否为空状态
     *
     * @param recyclerView SwipeRecyclerLayout 上下拉列表视图
     * @param isEmpty      是否为空
     */
    @BindingAdapter(value = {"bindEmptyView"}, requireAll = false)
    public static void bindEmptyView(EmptyRecyclerView recyclerView, boolean isEmpty) {
        recyclerView.notifyEmpty(isEmpty);
    }
}

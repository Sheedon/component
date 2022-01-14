package org.sheedon.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.touch.OnItemMovementListener;
import com.yanzhenjie.recyclerview.touch.OnItemStateChangedListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 自带刷新加载和空页面的列表视图
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/11 3:18 下午
 */
public class SwipeRecyclerLayout extends SwipeRefreshLayout {

    private static final Class<?>[] LAYOUT_LOAD_MORE = new Class<?>[]{Context.class};

    @SuppressWarnings("FieldMayBeFinal")
    private EmptyLayout emptyLayout;
    @SuppressWarnings("FieldMayBeFinal")
    private SwipeRecyclerView swipeRecyclerView;
    private boolean needLoadMore;

    public SwipeRecyclerLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeRecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        emptyLayout = new EmptyLayout(context, attrs);
        swipeRecyclerView = new SwipeRecyclerView(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeRecyclerLayout, 0, 0);
        attachAttrs(typedArray, attrs);
        typedArray.recycle();

        addView(swipeRecyclerView);
        addView(emptyLayout);
    }

    /**
     * 附加参数
     *
     * @param types 类型数组
     * @param attrs 属性集
     */
    private void attachAttrs(TypedArray types, AttributeSet attrs) {
        setEnabled(types.getBoolean(R.styleable.SwipeRecyclerLayout_needRefresh, true));
        needLoadMore = types.getBoolean(R.styleable.SwipeRecyclerLayout_needLoadMore, true);
        String loadMoreLayout = types.getString(R.styleable.SwipeRecyclerLayout_loadMoreLayout);
        if (loadMoreLayout != null && !loadMoreLayout.isEmpty() && needLoadMore) {
            createLoadMoreLayout(getContext(), loadMoreLayout);
        }
    }

    /**
     * 创建加载更多的视图，根据类名反射拿到视图实例
     *
     * @param context   上下午
     * @param className 全类名
     */
    private void createLoadMoreLayout(Context context, String className) {
        if (className != null) {
            className = className.trim();
            if (!className.isEmpty()) {
                className = getFullClassName(context, className);
                try {
                    ClassLoader classLoader;
                    if (isInEditMode()) {
                        // Stupid layoutlib cannot handle simple class loaders.
                        classLoader = this.getClass().getClassLoader();
                    } else {
                        classLoader = context.getClassLoader();
                    }
                    Class<? extends SwipeRecyclerView.LoadMoreView> layoutManagerClass =
                            Class.forName(className, false, classLoader)
                                    .asSubclass(SwipeRecyclerView.LoadMoreView.class);
                    Constructor<? extends SwipeRecyclerView.LoadMoreView> constructor = null;
                    Object[] constructorArgs = null;
                    try {
                        constructor = layoutManagerClass
                                .getConstructor(LAYOUT_LOAD_MORE);
                        constructorArgs = new Object[]{context};
                    } catch (NoSuchMethodException e) {
                        try {
                            constructor = layoutManagerClass.getConstructor();
                        } catch (NoSuchMethodException e1) {
                            e1.initCause(e);
                        }
                    }
                    constructor.setAccessible(true);
                    SwipeRecyclerView.LoadMoreView loadMoreView = constructor.newInstance(constructorArgs);
                    if (loadMoreView instanceof View) {
                        addFooterView((View) loadMoreView);
                    }
                    setLoadMoreView(loadMoreView);
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | ClassCastException ignored) {

                }
            }
        }
    }

    /**
     * 获取完整的类名
     *
     * @param context   上下文
     * @param className 类名
     * @return 完整的类名
     */
    private String getFullClassName(Context context, String className) {
        if (className.charAt(0) == '.') {
            return context.getPackageName() + className;
        }
        if (className.contains(".")) {
            return className;
        }
        return SwipeRecyclerLayout.class.getPackage().getName() + '.' + className;
    }

    /**
     * Set OnItemMoveListener.
     *
     * @param listener {@link OnItemMoveListener}.
     */
    public void setOnItemMoveListener(OnItemMoveListener listener) {
        swipeRecyclerView.setOnItemMoveListener(listener);
    }

    /**
     * Set OnItemMovementListener.
     *
     * @param listener {@link OnItemMovementListener}.
     */
    public void setOnItemMovementListener(OnItemMovementListener listener) {
        swipeRecyclerView.setOnItemMovementListener(listener);
    }

    /**
     * Set OnItemStateChangedListener.
     *
     * @param listener {@link OnItemStateChangedListener}.
     */
    public void setOnItemStateChangedListener(OnItemStateChangedListener listener) {
        swipeRecyclerView.setOnItemStateChangedListener(listener);
    }

    /**
     * Set the item menu to enable status.
     *
     * @param enabled true means available, otherwise not available; default is true.
     */
    public void setSwipeItemMenuEnabled(boolean enabled) {
        swipeRecyclerView.setSwipeItemMenuEnabled(enabled);
    }

    /**
     * True means available, otherwise not available; default is true.
     */
    public boolean isSwipeItemMenuEnabled() {
        return swipeRecyclerView.isSwipeItemMenuEnabled();
    }

    /**
     * Set the item menu to enable status.
     *
     * @param position the position of the item.
     * @param enabled  true means available, otherwise not available; default is true.
     */
    public void setSwipeItemMenuEnabled(int position, boolean enabled) {
        swipeRecyclerView.setSwipeItemMenuEnabled(position, enabled);
    }

    /**
     * True means available, otherwise not available; default is true.
     *
     * @param position the position of the item.
     */
    public boolean isSwipeItemMenuEnabled(int position) {
        return swipeRecyclerView.isSwipeItemMenuEnabled();
    }

    /**
     * Set can long press drag.
     *
     * @param canDrag drag true, otherwise is can't.
     */
    public void setLongPressDragEnabled(boolean canDrag) {
        swipeRecyclerView.setLongPressDragEnabled(canDrag);
    }

    /**
     * Get can long press drag.
     *
     * @return drag true, otherwise is can't.
     */
    public boolean isLongPressDragEnabled() {
        return swipeRecyclerView.isLongPressDragEnabled();
    }


    /**
     * Set can swipe delete.
     *
     * @param canSwipe swipe true, otherwise is can't.
     */
    public void setItemViewSwipeEnabled(boolean canSwipe) {
        swipeRecyclerView.setItemViewSwipeEnabled(canSwipe);
    }

    /**
     * Get can long press swipe.
     *
     * @return swipe true, otherwise is can't.
     */
    public boolean isItemViewSwipeEnabled() {
        return swipeRecyclerView.isItemViewSwipeEnabled();
    }

    /**
     * Start drag a item.
     *
     * @param viewHolder the ViewHolder to start dragging. It must be a direct child of RecyclerView.
     */
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        swipeRecyclerView.startDrag(viewHolder);
    }

    /**
     * Star swipe a item.
     *
     * @param viewHolder the ViewHolder to start swiping. It must be a direct child of RecyclerView.
     */
    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        swipeRecyclerView.startSwipe(viewHolder);
    }

    /**
     * Set item click listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        swipeRecyclerView.setOnItemClickListener(listener);
    }

    /**
     * Set item click listener.
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        swipeRecyclerView.setOnItemLongClickListener(listener);
    }

    /**
     * Set to create menu listener.
     */
    public void setSwipeMenuCreator(SwipeMenuCreator menuCreator) {
        swipeRecyclerView.setSwipeMenuCreator(menuCreator);
    }

    /**
     * Set to click menu listener.
     */
    public void setOnItemMenuClickListener(OnItemMenuClickListener listener) {
        swipeRecyclerView.setOnItemMenuClickListener(listener);
    }

    public void setSwipeLayoutManager(RecyclerView.LayoutManager layoutManager) {
        swipeRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Get the original adapter.
     */
    public RecyclerView.Adapter getOriginAdapter() {
        return swipeRecyclerView.getOriginAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        isAdapted = true;
        swipeRecyclerView.setAdapter(adapter);
        if (footerView != null) {
            addFooterView(footerView);
        }
    }

    /**
     * Add view at the headers.
     */
    public void addHeaderView(View view) {
        swipeRecyclerView.addHeaderView(view);
    }

    /**
     * Remove view from header.
     */
    public void removeHeaderView(View view) {
        swipeRecyclerView.removeHeaderView(view);
    }

    private View footerView;
    private boolean isAdapted;

    /**
     * Add view at the footer.
     */
    public void addFooterView(View view) {
        if (isAdapted) {
            footerView = null;
            swipeRecyclerView.addFooterView(view);
        } else {
            footerView = view;
        }

    }

    public void removeFooterView(View view) {
        swipeRecyclerView.removeFooterView(view);
    }

    /**
     * Get size of headers.
     */
    public int getHeaderCount() {
        return swipeRecyclerView.getHeaderCount();
    }

    /**
     * Get size of footer.
     */
    public int getFooterCount() {
        return swipeRecyclerView.getFooterCount();
    }

    /**
     * Get ViewType of item.
     */
    public int getItemViewType(int position) {
        return swipeRecyclerView.getItemViewType(position);
    }

    /**
     * open menu on left.
     *
     * @param position position.
     */
    public void smoothOpenLeftMenu(int position) {
        swipeRecyclerView.smoothOpenLeftMenu(position);
    }

    /**
     * open menu on left.
     *
     * @param position position.
     * @param duration time millis.
     */
    public void smoothOpenLeftMenu(int position, int duration) {
        swipeRecyclerView.smoothOpenLeftMenu(position, duration);
    }

    /**
     * open menu on right.
     *
     * @param position position.
     */
    public void smoothOpenRightMenu(int position) {
        swipeRecyclerView.smoothOpenRightMenu(position);
    }

    /**
     * open menu on right.
     *
     * @param position position.
     * @param duration time millis.
     */
    public void smoothOpenRightMenu(int position, int duration) {
        swipeRecyclerView.smoothOpenRightMenu(position, duration);
    }

    /**
     * open menu.
     *
     * @param position  position.
     * @param direction use SwipeRecyclerView.DirectionMode.
     * @param duration  time millis.
     */
    public void smoothOpenMenu(int position, @SwipeRecyclerView.DirectionMode int direction, int duration) {
        swipeRecyclerView.smoothOpenMenu(position, direction, duration);
    }

    /**
     * Close menu.
     */
    public void smoothCloseMenu() {
        swipeRecyclerView.smoothCloseMenu();
    }

    /**
     * Use the default to load more View.
     */
    public void useDefaultLoadMore() {
        swipeRecyclerView.useDefaultLoadMore();
    }


    public void setNeedLoadMore(boolean needLoadMore) {
        this.needLoadMore = needLoadMore;
    }

    /**
     * Load more view.
     */
    public void setLoadMoreView(SwipeRecyclerView.LoadMoreView view) {
        swipeRecyclerView.setLoadMoreView(view);
    }

    /**
     * Load more listener.
     */
    public void setLoadMoreListener(SwipeRecyclerView.LoadMoreListener listener) {
        swipeRecyclerView.setLoadMoreListener(listener);
    }

    /**
     * Automatically load more automatically.
     * <p>
     * Non-auto-loading mode, you can to click on the item to load.
     * </p>
     *
     * @param autoLoadMore you can use false.
     * @see SwipeRecyclerView.LoadMoreView#onWaitToLoadMore(SwipeRecyclerView.LoadMoreListener)
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        swipeRecyclerView.setAutoLoadMore(autoLoadMore);
    }

    /**
     * Load more done.
     *
     * @param dataEmpty data is empty ?
     * @param hasMore   has more data ?
     */
    public final void loadMoreFinish(boolean dataEmpty, boolean hasMore) {
        swipeRecyclerView.loadMoreFinish(dataEmpty, hasMore);
    }

    /**
     * Called when data is loaded incorrectly.
     *
     * @param errorCode    Error code, will be passed to the LoadView, you can according to it to customize the prompt
     *                     information.
     * @param errorMessage Error message.
     */
    public void loadMoreError(int errorCode, String errorMessage) {
        swipeRecyclerView.loadMoreError(errorCode, errorMessage);
    }

    private boolean lastIsEmpty = true;

    /**
     * 通知是否信息为空，若数据为空，则显示空视图，否则显示列表视图
     *
     * @param isEmpty 是否为空
     */
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
     * 通知列表视图是否为空状态
     *
     * @param recyclerView SwipeRecyclerLayout 上下拉列表视图
     * @param isEmpty      是否为空
     */
    @BindingAdapter(value = {"bindEmptyView"}, requireAll = false)
    public static void bindEmptyView(SwipeRecyclerLayout recyclerView, boolean isEmpty) {
        recyclerView.notifyEmpty(isEmpty);
    }

    /**
     * 通知列表视图是否为空状态
     *
     * @param recyclerView SwipeRecyclerLayout 上下拉列表视图
     * @param needRefresh  是否是需要刷新
     * @param needLoadMore 是否需要加载
     */
    @BindingAdapter(value = {"needRefresh", "needLoadMore"}, requireAll = false)
    public static void needRefreshOrLoadMore(SwipeRecyclerLayout recyclerView, boolean needRefresh, boolean needLoadMore) {
        if (recyclerView == null) {
            return;
        }

        if (recyclerView.emptyLayout != null) {
            recyclerView.emptyLayout.setEnabled(needRefresh);
        }

        recyclerView.setNeedLoadMore(needLoadMore);
    }

    /**
     * 设置刷新和加载监听器
     *
     * @param recyclerView     SwipeRecyclerLayout 上下拉列表视图
     * @param refreshListener  刷新监听器
     * @param loadMoreListener 加载监听器
     */
    @BindingAdapter(value = {"refreshListener", "loadMoreListener"}, requireAll = false)
    public static void setListener(SwipeRecyclerLayout recyclerView,
                                   OnRefreshListener refreshListener,
                                   SwipeRecyclerView.LoadMoreListener loadMoreListener) {
        if (recyclerView == null) {
            return;
        }

        recyclerView.setOnRefreshListener(refreshListener);
        if (recyclerView.swipeRecyclerView != null) {
            recyclerView.swipeRecyclerView.setLoadMoreListener(loadMoreListener);
        }
    }

    /**
     * 设置加载更多
     *
     * @param recyclerView SwipeRecyclerLayout 上下拉列表视图
     * @param dataEmpty    是否数据为空
     * @param hasMore      是否有更多数据
     */
    @BindingAdapter(value = {"dataEmpty", "hasMore"}, requireAll = false)
    public static void loadMoreFinish(SwipeRecyclerLayout recyclerView,
                                      boolean dataEmpty, boolean hasMore) {
        if (recyclerView == null) {
            return;
        }

        if (recyclerView.swipeRecyclerView != null) {
            recyclerView.swipeRecyclerView.loadMoreFinish(dataEmpty, hasMore);
        }
    }

    /**
     * 设置列表适配器
     *
     * @param recyclerView SwipeRecyclerLayout
     * @param adapter      ListAdapter
     */
    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(SwipeRecyclerLayout recyclerView, ListAdapter adapter) {
        if (recyclerView.swipeRecyclerView != null) {
            recyclerView.swipeRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * 设置列表适配器
     *
     * @param recyclerView SwipeRecyclerLayout
     * @param adapter      ListAdapter
     */
    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(SwipeRecyclerLayout recyclerView, RecyclerView.Adapter adapter) {
        if (recyclerView.swipeRecyclerView != null) {
            recyclerView.swipeRecyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter(value = {"submitList"}, requireAll = false)
    public static void submitList(SwipeRecyclerLayout recyclerView, List list) {
        if (recyclerView.swipeRecyclerView != null && recyclerView.swipeRecyclerView.getAdapter() != null) {
            ListAdapter adapter = (ListAdapter) recyclerView.swipeRecyclerView.getAdapter();
            adapter.submitList(list);
        }
    }

    @BindingAdapter(value = {"spaceTop", "spaceStart", "spaceBottom", "spaceEnd", "fillBorder", "spanCount"}, requireAll = false)
    public static void setGridSpace(SwipeRecyclerLayout recyclerView, int spaceTop, int spaceStart, int spaceBottom, int spaceEnd, boolean fillBorder, int spanCount) {
        if (recyclerView.swipeRecyclerView == null) {
            return;
        }
        recyclerView.swipeRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (fillBorder && spanCount != 0) {
                    if (spaceTop != 0 && parent.getChildLayoutPosition(view) / spanCount != 0)
                        outRect.top = spaceTop;

                    if (spaceStart != 0 && parent.getChildLayoutPosition(view) % spanCount != 0)
                        outRect.left = spaceStart;

                    if (spaceBottom != 0 && parent.getChildLayoutPosition(view) > recyclerView.getChildCount() - spanCount)
                        outRect.bottom = spaceBottom;

                    if (spaceEnd != 0 && parent.getChildLayoutPosition(view) % spanCount != spanCount - 1)
                        outRect.right = spaceEnd;
                } else {
                    if (spaceTop != 0)
                        outRect.top = spaceTop;

                    if (spaceStart != 0)
                        outRect.left = spaceStart;

                    if (spaceBottom != 0)
                        outRect.bottom = spaceBottom;

                    if (spaceEnd != 0)
                        outRect.right = spaceEnd;
                }
            }
        });
    }

    @BindingAdapter(value = {"autoScrollToTopWhenInsert", "autoScrollToBottomWhenInsert"}, requireAll = false)
    public static void autoScroll(SwipeRecyclerLayout recyclerView,
                                  boolean autoScrollToTopWhenInsert,
                                  boolean autoScrollToBottomWhenInsert) {

        if (recyclerView.swipeRecyclerView != null && recyclerView.swipeRecyclerView.getAdapter() != null) {
            recyclerView.swipeRecyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    if (autoScrollToTopWhenInsert) {
                        recyclerView.swipeRecyclerView.scrollToPosition(0);
                    } else if (autoScrollToBottomWhenInsert) {
                        recyclerView.swipeRecyclerView.scrollToPosition(recyclerView.swipeRecyclerView.getAdapter().getItemCount());
                    }
                }
            });
        }
    }
}

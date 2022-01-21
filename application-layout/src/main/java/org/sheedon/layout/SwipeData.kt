package org.sheedon.layout

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * 自带刷新加载和空页面的列表视图 数据
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/14 2:04 下午
 */
data class SwipeData @JvmOverloads constructor(
    val needRefresh: Boolean = true,
    val needLoadMore: Boolean = true,
    val refreshListener: SwipeRefreshLayout.OnRefreshListener? = null,
    val loadMoreListener: SwipeRecyclerView.LoadMoreListener? = null,
    val dataEmpty: ObservableBoolean = ObservableBoolean(false),
    val hasMore: MutableLiveData<Boolean> = MutableLiveData(false),
    val spanCount: Int = 1
)
package org.sheedon.component.recyclerlayout

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yanzhenjie.recyclerview.SwipeRecyclerView.LoadMoreListener
import org.sheedon.component.R
import org.sheedon.component.databinding.ActivityEmptyBinding

class EmptyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmptyBinding
    private lateinit var mAdapter:MainAdapter

    companion object {

        @JvmStatic
        fun show(context: Context) {
            context.startActivity(Intent(context, EmptyActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAdapter = MainAdapter(this)
        binding =
            DataBindingUtil.setContentView<ActivityEmptyBinding>(this, R.layout.activity_empty)
                .also {
                    it.recycler.setSwipeLayoutManager(LinearLayoutManager(this))
                    it.recycler.setAdapter(mAdapter)


//                    val loadMoreView = DefaultLoadMoreView(this)
//                    it.recycler.addFooterView(loadMoreView) // 添加为Footer。
//                    it.recycler.setLoadMoreView(loadMoreView) // 设置LoadMoreView更新监听。
                    it.recycler.setLoadMoreListener(mLoadMoreListener) // 加载更多的监听。
                }
    }

    /**
     * 刷新。
     */
    private val mRefreshListener by lazy {
        SwipeRefreshLayout.OnRefreshListener {
            binding.recycler.postDelayed({
                loadData()
            }, 1000)
        }
    }

    /**
     * 加载更多。
     */
    private val mLoadMoreListener =  LoadMoreListener {

        binding.recycler.postDelayed({
            val strings = createDataList(mAdapter.itemCount)
            mDataList.addAll(strings)
            // notifyItemRangeInserted()或者notifyDataSetChanged().
            mAdapter.notifyItemRangeInserted(mDataList.size - strings.size, strings.size)

            // 数据完更多数据，一定要掉用这个方法。
            // 第一个参数：表示此次数据是否为空。
            // 第二个参数：表示是否还有更多数据。
            binding.recycler.loadMoreFinish(false, true)
        },1000)
    }

    private var mDataList = mutableListOf<String>()

    /**
     * 第一次加载数据。
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        mDataList = createDataList(0)
        mAdapter.notifyDataSetChanged(mDataList)

        // 第一次加载数据：一定要掉用这个方法。
        // 第一个参数：表示此次数据是否为空，假如你请求到的list为空(== null || list.size == 0)，那么这里就要true。
        // 第二个参数：表示是否还有更多数据，根据服务器返回给你的page等信息判断是否还有更多，这样可以提供性能，如果不能判断则传true。
        binding.recycler.loadMoreFinish(false, true)
    }

    fun createDataList(start: Int): MutableList<String> {
        val strings = mutableListOf<String>()
        for (index in start..start + 20) {
            strings.add("第 $index 个Item")
        }
        return strings
    }
}
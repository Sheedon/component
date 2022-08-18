package org.sheedon.component.module.search

import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import org.sheedon.binging.*
import org.sheedon.component.BR
import java.util.*

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 22:47
 */
class SearchEvent(
    private val lifecycleOwner: LifecycleOwner,
    callback: ISearchCallback? = null
) : AbstractEvent<ISearchCallback>(callback), LifecycleObserver, SearchDataHandler {

    val searchData: MutableLiveData<String> = MutableLiveData()
    private var lastTime: Long = 0
    private var lastSearch: String = ""

    constructor(
        fragment: Fragment,
        callback: ISearchCallback? = null
    ) : this(lifecycleOwner = fragment, callback = callback)

    constructor(
        activity: ComponentActivity,
        callback: ISearchCallback? = null
    ) : this(lifecycleOwner = activity, callback = callback)

    override fun convertDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig()
            .addBindingParam(BR.search, this)
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)

        searchData.observe(lifecycleOwner,
            Observer { notifySearch(it) })
    }

    override fun search(searchDate: String) {
        if (lastSearch == searchDate) {
            return
        }
        this.searchData.postValue(searchDate)
    }

    private fun notifySearch(search: String) {
        if (search != lastSearch) {
            lastSearch = search
            callback?.scan(search)
            return
        }

        val nowTime = SystemClock.uptimeMillis()
        if (nowTime - lastTime > INTERVAL_TIME) {
            callback?.scan(search)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        searchData.removeObservers(lifecycleOwner)
    }

    companion object {
        private const val INTERVAL_TIME = 300
    }
}


class SearchConverter : AbstractEventConverter<SearchDataHandler>() {

    override fun createRealEvent(
        activity: ComponentActivity,
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        return SearchEvent(activity, callback as ISearchCallback)
    }

    override fun createRealEvent(
        fragment: Fragment,
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        return SearchEvent(fragment, callback as ISearchCallback)
    }

    override fun loadHandler(): SearchDataHandler {
        return event as SearchEvent
    }


}
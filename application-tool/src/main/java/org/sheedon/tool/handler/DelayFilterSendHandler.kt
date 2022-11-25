package org.sheedon.tool.handler

import kotlinx.coroutines.*

/**
 * 防抖发送执行者
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/20 14:41
 */
class DelayFilterSendHandler<T> @JvmOverloads constructor(
    private val coroutineScope: CoroutineScope,
    private val delayMillisecond: Long = 300,
    private val block: ((T) -> Unit)? = null
) {

    var job: Job? = null

    fun searchRecommendedApp(keywords: T) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(delayMillisecond)
            job = null
            block?.invoke(keywords)
        }
    }

    fun destroy() {
        job?.cancel()
        job = null
    }

}
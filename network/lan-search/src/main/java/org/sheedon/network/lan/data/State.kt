package org.sheedon.network.lan.data

/**
 * 设备状态
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/4 10:21
 */
enum class State {

    /**
     * 链路地址探测之中，但还未完成/可获得
     */
    INCOMPLETE,

    /**
     * 链路地址可达，该状态的维持时间参考
     */
    REACHABLE,

    /**
     * 链路地址缓存失效，但并不意味不可达
     */
    STALE,

    /**
     * 探测开始之间的延迟
     */
    DELAY,

    /**
     * 链路地址探测之中
     */
    PROBE,

    /**
     * 链路地址不可用（被删除）
     */
    FAILED,
    NOARP,

    /**
     * 永久
     */
    PERMANENT,
}
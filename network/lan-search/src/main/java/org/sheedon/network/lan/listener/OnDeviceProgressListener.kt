package org.sheedon.network.lan.listener

import org.sheedon.network.lan.data.LanDeviceModel

/**
 * 设备查询进度监听器
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/4 18:18
 */
interface OnDeviceProgressListener {

    fun onProgress(progress: Int)

    fun onCompleteResult(data: List<LanDeviceModel>)

}
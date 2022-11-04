package org.sheedon.network.lan.listener

import org.sheedon.network.lan.data.LanDeviceModel

/**
 * 设备搜索监听器
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/3 16:15
 */
interface OnDeviceScanListener {

    /**
     * 局域网内搜索到的数据信息
     */
    fun onDevicesResult(model: LanDeviceModel)
}
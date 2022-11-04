package org.sheedon.network.lan.launch

import android.content.Context
import org.sheedon.network.lan.listener.OnDeviceScanListener
import org.sheedon.network.lan.manager.Converter
import org.sheedon.network.lan.manager.ScanDeviceManager

/**
 * 局域网搜索入口
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/4 10:55
 */
class LanSearch(val factory: Converter.Factory) {

    /**
     * 创建搜索设备管理者
     * @param listener 设备搜索监听器
     * @return 设备搜索管理对象
     */
    private fun createScanDeviceManager(
        context: Context,
        listener: OnDeviceScanListener
    ): ScanDeviceManager {
        return ScanDeviceManager(listener, factory.createManufactureConverter(context))
    }

    companion object {

        private lateinit var search: LanSearch

        fun init(factory: Converter.Factory) {
            search = LanSearch(factory)
        }

        fun getScanDeviceManager(
            context: Context,
            listener: OnDeviceScanListener
        ): ScanDeviceManager {
           return search.createScanDeviceManager(context, listener)
        }
    }
}
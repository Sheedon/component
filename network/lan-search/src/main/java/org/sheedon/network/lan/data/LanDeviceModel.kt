package org.sheedon.network.lan.data

import org.sheedon.network.lan.manager.Converter

/**
 * 局域网设备信息
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/3 16:16
 */
class LanDeviceModel(
    val ip: String, // 设备ip
    private val originalMac: String, // 设备原始mac地址
    val state: String, // 当前状态
    val curDev: Boolean, // 是否是当前设备
    val root: Boolean // 是否是根节点
) {

    var mac: String = ""

    // 制造商
    var manufacture: String = ""

    // 设备名
    var name: String = ""

    fun convert(
        converter: Converter<String, String>,
        deviceName: String
    ) {
        this.name = deviceName
        this.manufacture = converter.convert(originalMac)
        this.mac = if (originalMac == "INCOMPLETE") {
            "02:00:00:00:00:00"
        } else {
            originalMac.uppercase()
        }

    }

    override fun toString(): String {
        return "LanDeviceModel(ip='$ip', originalMac='$originalMac', state='$state', curDev=$curDev, root=$root, mac='$mac', manufacture='$manufacture', name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LanDeviceModel

        if (ip != other.ip) return false

        return true
    }

    override fun hashCode(): Int {
        return ip.hashCode()
    }


}
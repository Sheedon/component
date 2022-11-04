package org.sheedon.network.lan.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.DhcpInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.net.*
import java.nio.ByteOrder
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.nio.channels.UnresolvedAddressException
import java.util.*

/**
 * 网络工具类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/3 17:00
 */
object NetworkUtils {

    /**
     * 获取设备的ip地址
     */
    fun getLocalIp(): String? {
        var localIp: String? = null
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkInterface = en.nextElement()
                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (!inetAddress.isLoopbackAddress &&
                        inetAddress is Inet4Address
                    ) {
                        localIp = inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return localIp
    }

    /**
     * 获取设备连接的网关的ip地址
     */
    fun getGateWayIp(context: Context): String? {
        var gatewayIp: String? = null
        val dhcpInfo: DhcpInfo? = getDhcpInfo(context)
        if (dhcpInfo != null) {
            gatewayIp = convertIntToString(dhcpInfo.gateway)
        }
        return gatewayIp
    }

    fun getDhcpInfo(context: Context): DhcpInfo? {
        return getWifiManager(context)?.dhcpInfo
    }

    /**
     * 获取wifi管理者
     */
    fun getWifiManager(context: Context): WifiManager? {
        return context.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as? WifiManager
    }

    fun convertIntToString(IP: Int): String {
        var ipStr = ""
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipStr += (0xFF and IP).toString()
            ipStr += "."
            ipStr += (0xFF and (IP shr 8)).toString()
            ipStr += "."
            ipStr += (0xFF and (IP shr 16)).toString()
            ipStr += "."
            ipStr += (0xFF and (IP shr 24)).toString()
        } else {
            ipStr += (0xFF and (IP shr 24)).toString()
            ipStr += "."
            ipStr += (0xFF and (IP shr 16)).toString()
            ipStr += "."
            ipStr += (0xFF and (IP shr 8)).toString()
            ipStr += "."
            ipStr += (0xFF and IP).toString()
        }
        return ipStr
    }

    /**
     * 是否ping通
     */
    fun isPingOk(ip: String): Boolean {
        try {
            val p = Runtime.getRuntime()
                .exec("/system/bin/ping -c 10 -w 4 $ip") ?: return false
            val inReader = BufferedReader(
                InputStreamReader(p.inputStream)
            )
            var line: String?
            while (inReader.readLine().also { line = it } != null) {
                if (line == null) return false
                if (line!!.contains("bytes from")) {
                    return true
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 该ip的端口某些端口是否打开
     * 22 linux ssh端口
     * 80和8081 http 端口
     * 135 远程打开对方的telnet服务器
     * 137 在局域网中提供计算机的名字或OP地址查询服务，一般安装了NetBIOS协议后，就会自动开放
     * 139 Windows获得NetBIOS/SMB服务
     * 445 局域网中文件的共享端口
     * 3389 远程桌面服务端口
     * 1900 ssdp协议
     * 5351 AppleBonjour、回到我的 Mac
     * 5353 Apple Bonjour、AirPlay、家庭共享、打印机查找、回到我的 Mac
     * 62078 Apple的一个端口
     * see link{https://support.apple.com/zh-cn/HT202944}
     */
    fun isAnyPortOk(ip: String): Boolean {
        val portArray = intArrayOf(
            22, 80, 135, 137, 139, 445, 3389, 4253, 1034, 1900,
            993, 5353, 5351, 62078
        )
        var selector: Selector
        for (i in portArray.indices) {
            try {
                //tcp port detection
                selector = Selector.open()
                val channel = SocketChannel.open()
                val address: SocketAddress = InetSocketAddress(ip, portArray[i])
                channel.configureBlocking(false)
                channel.connect(address)
                channel.register(selector, SelectionKey.OP_CONNECT, address)
                return if (selector.select(500) != 0) {
                    selector.close()
                    true
                } else {
                    selector.close()
                    continue
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: UnresolvedAddressException) {
                e.printStackTrace()
            }
        }
        return false
    }


    fun convertByte(data: ByteArray): String {
        if (data.size > 56) {
            val str = StringBuffer(15)
            for (i in 1..15) {
                str.append((0xFF and data[56 + i].toInt()).toChar())
            }
            return str.toString().trim { it <= ' ' }
        }
        return ""
    }

    /**
     * 获取mac地址（适配所有Android版本）
     * @return
     */
    fun getMac(context: Context): String {
        var mac = "02:00:00:00:00:00"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware()
        }
        return mac
    }

    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @param context * @return
     */
    @SuppressLint("HardwareIds")
    private fun getMacDefault(context: Context?): String {
        var mac = "02:00:00:00:00:00"
        if (context == null) {
            return mac
        }
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var info: WifiInfo? = null
        try {
            info = wifi.connectionInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (info == null) {
            return "02:00:00:00:00:00"
        }
        mac = info.macAddress
        if (mac.isNotEmpty()) {
            mac = mac.uppercase(Locale.ENGLISH)
        }
        return mac
    }

    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    private fun getMacAddress(): String {
        var macSerial = "02:00:00:00:00:00"
        var str: String? = ""
        try {
            val pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address")
            val ir = InputStreamReader(pp.inputStream)
            val input = LineNumberReader(ir)
            while (null != str) {
                str = input.readLine()
                if (str != null) {
                    macSerial = str.trim { it <= ' ' } //去空格
                    break
                }
            }
        } catch (ex: IOException) {
            // 赋予默认值
            ex.printStackTrace()
        }
        return macSerial
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    private fun getMacFromHardware(): String {
        try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            all.forEach {
                if (!it.name.equals("wlan0", ignoreCase = true))
                    return@forEach

                val macBytes = it.hardwareAddress ?: return ""
                val res1 = StringBuilder()
                macBytes.forEach { b ->
                    res1.append(String.format("%02X:", b))
                }

                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }

                return res1.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }
}
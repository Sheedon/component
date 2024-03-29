package org.sheedon.network.lan.manager

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
import org.sheedon.network.lan.NET_COUNT
import org.sheedon.network.lan.R
import org.sheedon.network.lan.client.UdpClient
import org.sheedon.network.lan.data.LanDeviceModel
import org.sheedon.network.lan.data.State
import org.sheedon.network.lan.listener.OnDeviceProgressListener
import org.sheedon.network.lan.listener.OnDeviceScanListener
import org.sheedon.network.lan.utils.NetworkUtils
import java.io.*
import java.net.SocketTimeoutException
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList

/**
 * 设备搜索管理者
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/3 16:13
 */
class ScanDeviceManager(
    val listener: OnDeviceScanListener? = null,
    val progressListener: OnDeviceProgressListener? = null,
    val converter: Converter<String, String>
) {

    // ip列表
    private val ipList: ArrayList<String> = ArrayList()

    // udp客户端
    private val client: UdpClient = UdpClient.get()

    // 在局域网内的ip和mac
    private val ipMacInLan = ArrayList<LanDeviceModel>()

    private var localIp = ""
    private var routeIp = ""

    /**
     * 开始扫描局域网
     */
    suspend fun startScanLan(context: Context) = withContext(Dispatchers.Default) {
        // 当前设备ip
        val isSuccess = loadIps(context)
        if (!isSuccess) return@withContext
        // 查询操作
        progressListener?.onProgress(PROGRESS_READY)
        sendQuery()
        progressListener?.onProgress(PROGRESS_START)
        delay(500)
        // 读取操作
        receiveProcessing()
        // 添加当前设备的信息
        addLocalModel(context)
        // 进一步的扫描
        furtherScan(context)
        progressListener?.onProgress(PROGRESS_COMPLETE)
        progressListener?.onCompleteResult(ArrayList(ipMacInLan))
    }

    /**
     * 发送查询指令
     */
    private suspend fun sendQuery() = withContext(Dispatchers.IO) {
        ipList.forEach {
            client.send(it, NET_BIOS_PORT, BYTES)
        }
    }

    /**
     * 读取操作
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun receiveProcessing() = withContext(Dispatchers.IO) {
        ipMacInLan.clear()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                readIpNeigh()
                progressListener?.onProgress(PROGRESS_RECEIVE + PROGRESS_START)
                delay(500)
                readIpNeigh()
                progressListener?.onProgress(PROGRESS_RECEIVE_TOTAL + PROGRESS_START)
                delay(500)
            } else {
                readIpMacFromFile()
                progressListener?.onProgress(PROGRESS_RECEIVE + PROGRESS_START)
                delay(500)
                readIpMacFromFile()
                progressListener?.onProgress(PROGRESS_RECEIVE_TOTAL + PROGRESS_START)
                delay(500)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    private var position = AtomicInteger()
    private var total = 1

    /**
     * 进一步扫描，获取更详细的信息
     */
    @Suppress("DeferredResultUnused")
    private suspend fun furtherScan(context: Context) = withContext(Dispatchers.IO) {

        if (ipMacInLan.isEmpty()) return@withContext
        val size = ipMacInLan.size
        position.set(0)
        total = size
        for (index in 0 until size) {
            val model = ipMacInLan[index]
            async { updateDeviceInfo(context, model) }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun updateDeviceInfo(context: Context, model: LanDeviceModel) {

        if (!NetworkUtils.isPingOk(model.ip)
            && !NetworkUtils.isAnyPortOk(model.ip)
        ) {
            updateProgress()
            return
        }

        var data: ByteArray? = null
        try {
            data = client.receive(model.ip, NET_BIOS_PORT, BYTES).data
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
        val deviceName =
            data?.run { NetworkUtils.convertByte(data) } ?: context.resources.getString(
                R.string.unknown
            )
        model.convert(converter, deviceName)

        listener?.onDevicesResult(model)
        updateProgress()
    }

    /**
     * 更新进度
     */
    private fun updateProgress() {
        val start = PROGRESS_RECEIVE_TOTAL + PROGRESS_START
        val progress =
            start + (position.incrementAndGet() * 1.0 * PROGRESS_FURTHER_TOTAL / total).toInt()
        progressListener?.onProgress(progress)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(IOException::class, InterruptedException::class)
    private suspend fun readIpNeigh() = withContext(Dispatchers.IO) {
        val runtime = Runtime.getRuntime()
        val proc = runtime.exec(COMMAND)
        proc.waitFor()

        val br = BufferedReader(InputStreamReader(proc.inputStream))

        var line: String?
        while (br.readLine().also { line = it } != null) {
            val info = line?.split(" ")
            val ip = info?.getOrNull(0)
            val mac = info?.getOrNull(4) ?: ""
            val state = info?.getOrNull(5) ?: State.DELAY.name

            if (ip.isNullOrEmpty()) {
                continue
            }

            if (mac == "INCOMPLETE" || mac == "00:00:00:00:00:00") continue

            val model = LanDeviceModel(
                ip, mac, state, ip == localIp, ip == routeIp
            )
            if (ipMacInLan.contains(model)) {
                ipMacInLan.remove(model)
            }

            ipMacInLan.add(model)
        }

    }


    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun readIpMacFromFile() = withContext(Dispatchers.IO) {
        var line: String?
        var ip: String
        var mac: String

        var pattern: Pattern
        var matcher: Matcher
        try {
            val bufferedReader = BufferedReader(FileReader(ARP_FILE))
            bufferedReader.readLine() //忽略标题行
            while (bufferedReader.readLine().also { line = it } != null) {
                ip = line!!.substring(0, line!!.indexOf(" "))
                pattern = Pattern.compile(REG_EXP)
                matcher = pattern.matcher(line!!)
                if (matcher.find()) {
                    mac = matcher.group(1) as String
                    if (mac == "INCOMPLETE" || mac == "00:00:00:00:00:00") continue

                    val model = LanDeviceModel(
                        ip, mac, "", ip == localIp, ip == routeIp
                    )

                    if (ipMacInLan.contains(model)) {
                        ipMacInLan.remove(model)
                    }

                    ipMacInLan.add(model)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     * 加载ip集合
     * @param context 上下文
     * @return 是否加载成功
     */
    private fun loadIps(context: Context): Boolean {
        val localIp = NetworkUtils.getLocalIp()
        val routeIp = NetworkUtils.getGateWayIp(context)

        if (localIp.isNullOrEmpty() || routeIp.isNullOrEmpty()) return false

        this.localIp = localIp
        this.routeIp = routeIp

        ipList.clear()
        val netIp = localIp.substring(0, localIp.lastIndexOf('.') + 1)
        for (i in 1 until NET_COUNT) {
            ipList.add(netIp + i)
        }

        ipList.remove(localIp)

        return true
    }

    /**
     * 添加当前设备信息
     */
    private fun addLocalModel(context: Context) {
        val model =
            LanDeviceModel(
                localIp, NetworkUtils.getMac(context),
                "", curDev = true, root = false
            )
        ipMacInLan.add(model)
        listener?.onDevicesResult(model)
    }

    companion object {

        // 137端口的主要作用是在局域网中提供计算机的名字或IP地址查询服务
        private const val NET_BIOS_PORT = 137

        private val BYTES = byteArrayOf(
            0x00, 0x00, 0x00, 0x10, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x20, 0x43, 0x4B,
            0x41, 0x41, 0x41, 0x41, 0x41,
            0x41, 0x41, 0x41, 0x41, 0x41,
            0x41, 0x41, 0x41, 0x41, 0x41,
            0x41, 0x41, 0x41, 0x41, 0x41,
            0x41, 0x41, 0x41, 0x41, 0x41,
            0x41, 0x41, 0x41, 0x41, 0x41,
            0x00, 0x00, 0x21, 0x00, 0x01
        )

        // 读取arp下的ip/mac信息
        private const val COMMAND = "ip neigh show"
        private const val ARP_FILE = "/proc/net/arp"

        @Suppress("RegExpDuplicateCharacterInClass")
        private const val REG_EXP = "((([0-9,A-F,a-f]{1,2}" + ":" + "){1,5})[0-9,A-F,a-f]{1,2})"

        private const val PROGRESS_READY = 0
        private const val PROGRESS_START = 10
        private const val PROGRESS_RECEIVE_TOTAL = 30
        private const val PROGRESS_RECEIVE = 15
        private const val PROGRESS_FURTHER_TOTAL = 60
        private const val PROGRESS_COMPLETE = 100
    }


}
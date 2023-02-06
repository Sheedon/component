package org.sheedon.network.lan.task

import org.sheedon.network.lan.UPD_TIMEOUT
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

/**
 * 单元
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/4 14:16
 */
class UdpTask {
    private var socket: DatagramSocket? = null

    private var buffer = ByteArray(1024)

    private val lock = Any()

    private var running = false

    @Throws(IOException::class)
    private fun loadSocket(): DatagramSocket {
        if (socket == null) {
            synchronized(lock) {
                if (socket == null) {
                    socket = DatagramSocket().apply {
                        soTimeout = UPD_TIMEOUT
                    }
                }
            }
        }
        return socket!!
    }

    /**
     * 向某个ip，某个端口发送一条消息
     * @param ip ip地址
     * @param port 端口
     * @param data 数据
     */
    fun send(ip: String, port: Int, data: ByteArray) {
        running = true
        try {
            val dp = DatagramPacket(data, data.size, InetAddress.getByName(ip), port)
            val datagramSocket = loadSocket()
            datagramSocket.send(dp)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        running = false
    }

    /**
     * 接收UDP消息
     */
    @Throws(IOException::class, SocketTimeoutException::class)
    fun receive(): DatagramPacket {
        running = true
        buffer = ByteArray(1024)
        val datagramSocket = loadSocket()
        val dp = DatagramPacket(buffer, buffer.size)
        datagramSocket.receive(dp)
        running = false
        return dp
    }

    fun isRunning() = running

    fun close() {
        socket?.close()
        socket = null
    }

}
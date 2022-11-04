package org.sheedon.network.lan.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.sheedon.network.lan.UPD_TIMEOUT
import org.sheedon.network.lan.task.UdpTask
import java.io.IOException
import java.net.DatagramPacket
import java.net.SocketTimeoutException

/**
 * UDP 客户端
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/3 20:51
 */
class UdpClient {

    private val running = ArrayList<UdpTask>()
    private val free = ArrayList<UdpTask>()
    private val lock = Any()

    private suspend fun createUdpTask(): UdpTask = withContext(Dispatchers.Default) {


        while (true) {
            val runningSize = running.size
            val freeSize = free.size
            if (runningSize + freeSize >= 20) {
                delay(UPD_TIMEOUT.toLong())
                continue
            }

            synchronized(lock = lock) {
                if (freeSize > 0) {
                    free.removeFirstOrNull()?.also {
                        running.add(it)
                        return@withContext it
                    }
                }
            }

            break
        }
        UdpTask()
    }

    /**
     * 向某个ip，某个端口发送一条消息
     * @param ip ip地址
     * @param port 端口
     * @param data 数据
     */
    suspend fun send(ip: String, port: Int, data: ByteArray) {
        val task = createUdpTask()
        task.send(ip, port, data)
        running.remove(task)
        free.add(task)
    }

    /**
     * 接收UDP消息
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(IOException::class, SocketTimeoutException::class)
    suspend fun receive(ip: String, port: Int, data: ByteArray): DatagramPacket {
        val task = createUdpTask()
        task.send(ip, port, data)
        val dp = task.receive()
        running.remove(task)
        free.add(task)
        return dp
    }

    /**
     * 接收UDP消息
     */
    @Throws(IOException::class, SocketTimeoutException::class)
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun receive(): DatagramPacket {
        val task = createUdpTask()
        val dp = task.receive()
        running.remove(task)
        free.add(task)
        return dp
    }

    /**
     * 关闭连接
     */
    fun close() {
        running.forEach {
            it.close()
        }
        free.forEach {
            it.close()
        }
    }

    fun isFree(): Boolean {
        return running.isEmpty()
    }


    companion object {
        private val instance = UdpClient()

        fun get() = instance
    }
}
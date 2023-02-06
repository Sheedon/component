package org.sheedon.network.lan.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.sheedon.network.lan.UPD_TIMEOUT
import org.sheedon.network.lan.task.UdpTask
import java.io.IOException
import java.net.DatagramPacket
import java.net.SocketTimeoutException
import kotlin.collections.ArrayList

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

    @Suppress("SYNCHRONIZED_ON_SUSPEND")
    @Synchronized
    private suspend fun createUdpTask(): UdpTask = withContext(Dispatchers.Default) {

        while (true) {
            val runningSize = running.size
            val freeSize = free.size
            if (runningSize >= 20) {
                delay(UPD_TIMEOUT.toLong())
                removeTask()
                continue
            }

            if (freeSize > 0) {
                loadFirstFreeTask()?.also {
                    addTask(it)
                    return@withContext it
                }
            }

            break
        }
        val udpTask = UdpTask()
        addTask(udpTask)
        udpTask
    }

    /**
     * 移除任意一个
     */
    private fun removeTask() {
        synchronized(running) {
            var size = running.size
            var index = 0
            while (index < size) {
                val udpTask = running[index]
                if (udpTask.isRunning()) {
                    index++
                    continue
                }

                removeTask(udpTask)
                size = running.size
            }
        }
    }

    private fun addTask(udpTask: UdpTask) {
        synchronized(running) {
            running.add(udpTask)
        }
    }

    private fun removeTask(udpTask: UdpTask) {
        synchronized(running) {
            running.remove(udpTask)
        }
    }

    private fun addFreeTask(udpTask: UdpTask) {
        synchronized(free) {
            free.add(udpTask)
        }
    }

    private fun removeFreeTask(udpTask: UdpTask) {
        synchronized(free) {
            free.remove(udpTask)
        }
    }

    private fun loadFirstFreeTask(): UdpTask? {
        synchronized(free) {
            return free.removeFirstOrNull()?.also {
                return it
            }
        }
    }

    /**
     * 向某个ip，某个端口发送一条消息
     * @param ip ip地址
     * @param port 端口
     * @param data 数据
     */
    @Suppress("SYNCHRONIZED_ON_SUSPEND")
    suspend fun send(ip: String, port: Int, data: ByteArray) {
        val task = createUdpTask()
        try {
            task.send(ip, port, data)
        } catch (e: Exception) {
            throw e
        } finally {
            removeTask(task)
            addFreeTask(task)
        }
    }

    /**
     * 接收UDP消息
     */
    @Suppress("BlockingMethodInNonBlockingContext", "SYNCHRONIZED_ON_SUSPEND")
    @Throws(IOException::class, SocketTimeoutException::class)
    suspend fun receive(ip: String, port: Int, data: ByteArray): DatagramPacket {
        val task = createUdpTask()
        val dp: DatagramPacket
        try {
            task.send(ip, port, data)
            dp = task.receive()
        } catch (e: Exception) {
            throw e
        } finally {
            removeTask(task)
            addFreeTask(task)
        }
        return dp
    }

    /**
     * 接收UDP消息
     */
    @Throws(IOException::class, SocketTimeoutException::class)
    @Suppress("BlockingMethodInNonBlockingContext", "SYNCHRONIZED_ON_SUSPEND")
    suspend fun receive(): DatagramPacket {

        val task = createUdpTask()
        val dp: DatagramPacket
        try {
            dp = task.receive()
        } catch (e: Exception) {
            throw e
        } finally {
            removeTask(task)
            addFreeTask(task)
        }
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
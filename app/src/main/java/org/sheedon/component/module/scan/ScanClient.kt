package org.sheedon.component.module.scan

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import java.util.*

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 22:00
 */
class ScanClient private constructor() : LifecycleObserver {

    private var listener: OnScanListener? = null

    constructor(
        fragment: Fragment,
        listener: OnScanListener? = null
    ) : this() {
        fragment.lifecycle.addObserver(this)
        this.listener = listener
    }

    constructor(
        activity: ComponentActivity,
        listener: OnScanListener? = null
    ) : this() {
        activity.lifecycle.addObserver(this)
        this.listener = listener
    }

    fun scan() {
        listener?.onScan(UUID.randomUUID().toString())
    }

    interface OnScanListener {
        fun onScan(result: String?)
    }
}
package org.sheedon.component.module.scan

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import org.sheedon.binging.*
import org.sheedon.component.BR

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 22:02
 */
class ScanEvent private constructor(
    callback: IScanCallback? = null
) : AbstractEvent<IScanCallback>(), ScanHandler {

    private lateinit var scanClient: ScanClient

    private val scanListener = object : ScanClient.OnScanListener {
        override fun onScan(result: String?) {
            callback?.scan(result ?: "")
        }
    }

    constructor(
        fragment: Fragment,
        callback: IScanCallback? = null
    ) : this(callback) {
        scanClient = ScanClient(fragment, scanListener)
    }

    constructor(
        activity: ComponentActivity,
        callback: IScanCallback? = null
    ) : this(callback) {
        scanClient = ScanClient(activity, scanListener)
    }

    override fun scan() {
        scanClient.scan()
    }

    override fun convertDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig()
            .addBindingParam(BR.scanEvent, object : ScanListener {
                override fun onScanClick() {
                    scanClient.scan()
                }
            })
    }
}

class ScanConverter : AbstractEventConverter<ScanHandler>() {

    override fun createRealEvent(
        activity: ComponentActivity,
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        return ScanEvent(activity, callback as IScanCallback)
    }

    override fun createRealEvent(
        fragment: Fragment,
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        return ScanEvent(fragment, callback as IScanCallback)
    }

    override fun loadHandler(): ScanHandler {
        return event as ScanHandler
    }


}
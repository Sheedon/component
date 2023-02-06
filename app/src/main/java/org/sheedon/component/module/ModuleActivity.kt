package org.sheedon.component.module

import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.component.R
import org.sheedon.layout.special.TimeProgressView
import org.sheedon.mvvm.ui.activities.AbstractModuleActivity
import org.sheedon.network.lan.data.LanDeviceModel
import org.sheedon.network.lan.launch.LanSearch
import org.sheedon.network.lan.listener.OnDeviceScanListener
import org.sheedon.network.lan.manager.Converter

class ModuleActivity : AbstractModuleActivity<ModuleViewModel, SearchCallback, SearchHandler>() {


    override fun loadEventComponent() = SearchEventComponent()

    override fun getContentLayoutId(): Int = R.layout.activity_module


    override fun enableToolbar(): Boolean = true
    override fun buildToolbarEvent(): IToolbarModel = ToolbarModel(R.string.default_title)

    override fun initData() {
        super.initData()

        findViewById<TimeProgressView>(R.id.radar).start()

//        LanSearch.init(Converter.Factory())
//        val manager = LanSearch.getScanDeviceManager(this, object : OnDeviceScanListener {
//            override fun onDevicesResult(model: LanDeviceModel) {
//                Log.v("SXD", "" + model.toString())
//            }
//        })
//
//
//        lifecycleScope.launch{
//            manager.startScanLan(this@ModuleActivity)
//        }

    }



}
package org.sheedon.component.module


import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.common.utils.ToastUtils
import org.sheedon.component.R
import org.sheedon.mvvm.ui.activities.AbstractModuleActivity
import org.sheedon.tool.ext.getScreenPhysicalSize

class ModuleActivity : AbstractModuleActivity<ModuleViewModel, SearchCallback, SearchHandler>() {


    override fun loadEventComponent() = SearchEventComponent()

    override fun getContentLayoutId(): Int = R.layout.activity_module


    override fun enableToolbar(): Boolean = true
    override fun buildToolbarEvent(): IToolbarModel = ToolbarModel(R.string.default_title)

    override fun initData() {
        super.initData()

        ToastUtils.showToast(this, "" + getScreenPhysicalSize())

//        findViewById<TimeProgressView>(R.id.radar).start()

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
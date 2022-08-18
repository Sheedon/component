package org.sheedon.component.module

import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.component.R
import org.sheedon.mvvm.ui.activities.AbstractModuleActivity

class ModuleActivity : AbstractModuleActivity<ModuleViewModel, SearchCallback, SearchHandler>() {


    override fun loadEventComponent() = SearchEventComponent()

    override fun getContentLayoutId(): Int = R.layout.activity_module


    override fun enableToolbar(): Boolean = true
    override fun buildToolbarEvent(): IToolbarModel = ToolbarModel(R.string.default_title)

}
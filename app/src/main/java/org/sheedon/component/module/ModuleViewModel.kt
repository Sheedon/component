package org.sheedon.component.module

import android.util.Log
import org.sheedon.mvvm.viewmodel.AbstractModuleViewModel

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/17 17:43
 */
class ModuleViewModel : AbstractModuleViewModel<SearchCallback, SearchHandler>() {



    override fun loadCallback(): SearchCallback {
        return object : SearchCallback {
            override fun scan(code: String) {
                val handler = loadHandler()
                handler?.search(code)
                Log.v("SXD", "code:$code")
            }

        }
    }
}
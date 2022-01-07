package org.sheedon.component

import android.content.Context
import android.content.Intent
import org.sheedon.common.app.DataBindingActivity

class KotlinActivity : DataBindingActivity() {

    companion object {

        @JvmStatic
        fun show(context: Context) {
            context.startActivity(Intent(context, KotlinActivity::class.java))
        }
    }

    override fun initViewModel() {

    }

    override fun getContentLayoutId() = R.layout.activity_kotlin
}
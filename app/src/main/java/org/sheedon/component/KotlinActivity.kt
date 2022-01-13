package org.sheedon.component

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import org.sheedon.common.app.DataBindingActivity
import org.sheedon.component.databinding.ActivityKotlinBinding

class KotlinActivity : DataBindingActivity<ActivityKotlinBinding>() {

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
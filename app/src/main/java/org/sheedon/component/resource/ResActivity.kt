package org.sheedon.component.resource

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import org.sheedon.component.R
import org.sheedon.component.databinding.ActivityResBinding

class ResActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun show(context: Context) {
            context.startActivity(Intent(context, ResActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityResBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_res)

//        Glide.with(this)
//            .load(R.color.amber)
//            .into(object:ViewTarget<Button, Drawable>(binding.btn){
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?
//                ) {
//                    binding.btn.background = resource
//                }
//
//            })

//        binding.btn.backgroundTintList = ColorStateList.valueOf(Color.CYAN)
//        val drawable = GradientDrawable()
//        drawable.setColor(resources.getColor(R.color.amber))
//        binding.btn.background = drawable


    }
}
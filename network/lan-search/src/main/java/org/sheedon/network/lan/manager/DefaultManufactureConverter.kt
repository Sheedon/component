package org.sheedon.network.lan.manager

import android.content.Context
import org.sheedon.network.lan.R
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

/**
 * 默认制造商转化者
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/4 12:44
 */
class DefaultManufactureConverter(context: Context) : Converter<String, String> {

    private val mContext = context.applicationContext

    private var mProperties: Properties? = null


    override fun convert(data: String): String {
        if (data.length < 8) return mContext.resources.getString(R.string.unknown_manufacture)
        return try {
            val properties = getProperties()
            val key = data.substring(0, 2) + data.substring(3, 5) + data.substring(6, 8)

            properties.getProperty(key.uppercase()).also {
                String(it.toByteArray(charset("ISO8859-1")), Charset.forName("UTF-8"))
            }


        } catch (e: Exception) {
            mContext.resources.getString(R.string.unknown_manufacture)
        }
    }

    @Throws(Exception::class)
    private fun getProperties(): Properties {
        if (mProperties == null) {
            synchronized(this) {
                if (mProperties == null) {
                    mProperties = Properties()
                    val raw: InputStream = mContext.resources.openRawResource(R.raw.manufacture)
                    mProperties?.load(raw)
                }
            }
        }
        return mProperties!!
    }
}
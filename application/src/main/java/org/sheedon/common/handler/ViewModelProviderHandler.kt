package org.sheedon.common.handler

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sheedon.common.app.BaseApplication

/**
 * ViewModelProvider 调度者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 10:09 下午
 */
class ViewModelProviderHandler private constructor() : ViewModelStoreOwner {
    private val mAppViewModelStore: ViewModelStore = ViewModelStore()
    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    companion object {
        val instance = ViewModelProviderHandler()

        /**
         * 获取Fragment持有的ViewModel
         *
         * @param mFragmentProvider Fragment's ViewModelProvider
         * @param modelClass        ViewModel's class
         * @param <T>               ViewModel
         * @return ViewModel
        </T> */
        fun <T : ViewModel?> getFragmentScopeViewModel(
            mFragmentProvider: ViewModelProvider,
            modelClass: Class<T>
        ): T {
            return mFragmentProvider[modelClass]
        }

        /**
         * 获取Activity持有的ViewModel
         *
         * @param mActivityProvider Activity's ViewModelProvider
         * @param modelClass        ViewModel's class
         * @param <T>               ViewModel
         * @return ViewModel
        </T> */
        fun <T : ViewModel?> getActivityScopeViewModel(
            mActivityProvider: ViewModelProvider,
            modelClass: Class<T>
        ): T {
            return mActivityProvider[modelClass]
        }

        /**
         * 获取全局持有的ViewModel
         *
         * @param mApplicationProvider Overall Situation's ViewModelProvider
         * @param modelClass           ViewModel's class
         * @param <T>                  ViewModel
         * @return ViewModel
        </T> */
        fun <T : ViewModel?> getApplicationScopeViewModel(
            modelClass: Class<T>,
            application: Application = BaseApplication.getInstance()
        ): T {
            return instance.getAppViewModelProvider(application)[modelClass]
        }
    }


    /**
     * 获取一个全局的ViewModel
     */
    private fun getAppViewModelProvider(application: Application = BaseApplication.getInstance()): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory(application))
    }

    private fun getAppFactory(application: Application)
            : ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory as ViewModelProvider.Factory
    }

}
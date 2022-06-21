package org.sheedon.common.handler

import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModelProvider 调度者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 10:09 下午
 */
class ViewModelProviderHandler private constructor() : ViewModelStoreOwner {
    private val mAppViewModelStore: ViewModelStore = ViewModelStore()
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
            mApplicationProvider: ViewModelProvider,
            modelClass: Class<T>
        ): T {
            return mApplicationProvider[modelClass]
        }
    }

}
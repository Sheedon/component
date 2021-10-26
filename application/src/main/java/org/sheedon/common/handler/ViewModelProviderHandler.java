package org.sheedon.common.handler;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * ViewModelProvider 调度者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/21 9:45 上午
 */
public class ViewModelProviderHandler implements ViewModelStoreOwner {

    private static final ViewModelProviderHandler INSTANCE = new ViewModelProviderHandler();
    private final ViewModelStore mAppViewModelStore;

    private ViewModelProviderHandler() {
        mAppViewModelStore = new ViewModelStore();
    }


    /**
     * 获取Fragment持有的ViewModel
     *
     * @param mFragmentProvider Fragment's ViewModelProvider
     * @param owner             Fragment
     * @param modelClass        ViewModel's class
     * @param <T>               ViewModel
     * @return ViewModel
     */
    public static <T extends ViewModel> T getFragmentScopeViewModel(@NonNull ViewModelProvider mFragmentProvider,
                                                                    @NonNull Fragment owner,
                                                                    @NonNull Class<T> modelClass) {
        if (mFragmentProvider == null) {
            mFragmentProvider = new ViewModelProvider(owner);
        }
        return mFragmentProvider.get(modelClass);
    }

    /**
     * 获取Activity持有的ViewModel
     *
     * @param mActivityProvider Activity's ViewModelProvider
     * @param owner             Activity
     * @param modelClass        ViewModel's class
     * @param <T>               ViewModel
     * @return ViewModel
     */
    public static <T extends ViewModel> T getActivityScopeViewModel(@NonNull ViewModelProvider mActivityProvider,
                                                                    @NonNull ComponentActivity owner,
                                                                    @NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(owner);
        }
        return mActivityProvider.get(modelClass);
    }

    /**
     * 获取全局持有的ViewModel
     *
     * @param mApplicationProvider Overall Situation's ViewModelProvider
     * @param modelClass           ViewModel's class
     * @param <T>                  ViewModel
     * @return ViewModel
     */
    public static <T extends ViewModel> T getApplicationScopeViewModel(@NonNull ViewModelProvider mApplicationProvider,
                                                                       @NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider(INSTANCE);
        }
        return mApplicationProvider.get(modelClass);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mAppViewModelStore;
    }
}

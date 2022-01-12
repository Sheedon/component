package org.sheedon.common.navigation

import android.view.View
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import org.sheedon.common.R

/**
 * Hide - Show NavHostFragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 12:21 下午
 */
class NavHostFragmentHideShow : NavHostFragment() {

    private val containerId: Int
        get() {
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
                // Fallback to using our own ID if this Fragment wasn't added via
                // add(containerViewId, Fragment)
            } else R.id.nav_host_fragment_container
        }

    /**
     * @return 使用自己的FragmentNavigator
     */
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination?> {
        return FragmentNavigatorHideShow(requireContext(), childFragmentManager, containerId)
    }

}
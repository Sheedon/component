package org.sheedon.common.widget.rootview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.sheedon.common.R
import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.common.handler.ConfigHandler
import org.sheedon.common.widget.toolbar.IToolbarView


/**
 * 建议根布局
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/11 22:48
 */
class NormalRootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val toolbar: IToolbarView = ConfigHandler.loadToolbarView(context),
) : CoordinatorLayout(context, attrs, defStyleAttr), IRootView {

    private lateinit var frameLayout: FrameLayout

    init {
        // 设置CoordinatorLayout基本配置
        overScrollMode = View.OVER_SCROLL_NEVER
        fitsSystemWindows = true
        id = R.id.root_id
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        initLayout()
    }

    /**
     * 初始化布局文件
     */
    private fun initLayout() {
        // 将 toolbarView 附加到 Layout
        val layout = initAppBarLayout()
        val toolbarView = toolbar.loadToolbarView()
        toolbarView.layoutParams =
            CollapsingToolbarLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                convertDimen(R.dimen.navigationBarSize)
            ).apply {
                this.topMargin = convertDimen(R.dimen.statusBarSize)
                gravity = Gravity.NO_GRAVITY
            }
        layout.addView(toolbarView)

        // 初始化FrameLayout，用于承载子布局
        initFrameLayout()
    }

    /**
     * 初始化应用栏布局，将承载toolbar的ViewGroup返回
     */
    private fun initAppBarLayout(): ViewGroup {
        val layout = AppBarLayout(context).apply {
            minimumHeight = convertDimen(R.dimen.navigationBarAndStatusBarSize)
            fitsSystemWindows = true
            ConfigHandler.loadToolbarBackground(context)?.also {
                background = it
            }
        }
        layout.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            convertDimen(R.dimen.navigationBarAndStatusBarSize)
        )
        this.addView(layout)

        // 初始化折叠工具栏布局
        return initCollapsingToolbarLayout(layout)
    }

    /**
     * 折叠工具栏布局，将承载toolbar的ViewGroup返回
     */
    private fun initCollapsingToolbarLayout(parent: ViewGroup): ViewGroup {
        val layout = CollapsingToolbarLayout(context)
        layout.layoutParams = AppBarLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            convertDimen(R.dimen.navigationBarAndStatusBarSize)
        ).apply {
            scrollFlags = behaviorScrollFlags()
        }
        parent.addView(layout)
        return layout
    }

    /**
     * 初始化FrameLayout，用于承载子布局
     */
    private fun initFrameLayout() {
        frameLayout = FrameLayout(context)
        frameLayout.layoutParams =
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        (frameLayout.layoutParams as LayoutParams).behavior = AppBarLayout.ScrollingViewBehavior()
        this.addView(frameLayout)
    }

    /**
     * 获取根布局
     */
    override fun getRootView(): ViewGroup = this


    fun behaviorScrollFlags() = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL

    /**
     * 获取承载子布局的承载Layout
     */
    override fun getChildLayout(): FrameLayout {
        return frameLayout
    }

    /**
     * 设置标题栏中配置信息
     * 返回/标题栏/菜单键
     */
    override fun setToolbarModel(model: IToolbarModel) {
        toolbar.setToolbarModel(model)
    }

    /**
     * 转化后的px
     */
    private fun convertDimen(res: Int): Int {
        return context.resources.getDimension(res).toInt()
    }

    /**
     * 销毁动作
     */
    override fun destroy() {
        toolbar.destroy()
    }

}
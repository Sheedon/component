package org.sheedon.common.handler;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import org.sheedon.common.R;
import org.sheedon.common.utils.BarUtils;

/**
 * 配置处理器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/20 5:12 下午
 */
public class ConfigHandler {

    private static final ConfigHandler INSTANCE = new ConfigHandler();

    // 是否为白天模式
    private boolean lightModel = true;
    // toolbar资源id
    private int toolbarId = R.layout.layout_toolbar;


    private ConfigHandler() {

    }

    public static ConfigHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 附加是否是亮模式
     *
     * @param lightModel 是否为明亮模式
     */
    public void attachLightModel(boolean lightModel) {
        this.lightModel = lightModel;
    }

    /**
     * 获取是否是亮模式
     */
    public static void setStatusBarMode(AppCompatActivity activity) {
        BarUtils.setStatusBarColor(activity, Color.TRANSPARENT);
        BarUtils.setStatusBarLightMode(activity, INSTANCE.lightModel);
    }

    /**
     * 获取 Toolbar ID
     */
    public static int getToolbarId() {
        return INSTANCE.toolbarId;
    }
}

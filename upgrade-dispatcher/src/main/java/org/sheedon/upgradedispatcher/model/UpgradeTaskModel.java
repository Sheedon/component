package org.sheedon.upgradedispatcher.model;

/**
 * 更新任务model
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/25 11:24 上午
 */
public class UpgradeTaskModel {

    // 版本号
    private int versionCode;
    // 版本名称
    private int versionName;
    // 描述信息
    private String description;

    public UpgradeTaskModel() {
    }

    public UpgradeTaskModel(int versionCode, int versionName, String description) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.description = description;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionName() {
        return versionName;
    }

    public void setVersionName(int versionName) {
        this.versionName = versionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

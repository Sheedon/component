package org.sheedon.lifecycle;

/**
 * 生命周期接口
 * 注意承载销毁的职责
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/12/20 3:45 下午
 */
public interface ILifecycle {

    /**
     * 销毁
     */
    void onDestroy();
}

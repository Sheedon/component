package org.sheedon.component.lifecycle;

import org.sheedon.lifecycle.ILifecycle;

/**
 * 生命元素包装类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/12/20 6:33 下午
 */
public class LifecycleElementWrapper implements ILifecycle {

    private LifecycleElement element;

    private LifecycleElementWrapper() {

    }

    public static ILifecycle build(LifecycleElement element) {
        LifecycleElementWrapper wrapper = new LifecycleElementWrapper();
        wrapper.element = element;
        return wrapper;
    }

    @Override
    public void onDestroy() {
        if (element != null) {
            element.destroy();
        }
        element = null;
    }
}

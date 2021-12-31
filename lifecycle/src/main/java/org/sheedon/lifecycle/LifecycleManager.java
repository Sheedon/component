package org.sheedon.lifecycle;

import java.util.HashSet;
import java.util.Set;

/**
 * 生命周期管理类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/12/20 3:39 下午
 */
public class LifecycleManager implements ILifecycle {

    private final Set<ILifecycle> elements = new HashSet<>();

    /**
     * 追加持有生命周期的元素
     *
     * @param element 持有生命周期的元素
     */
    public void addLifecycle(ILifecycle element) {
        if (element == null) {
            return;
        }

        elements.add(element);
    }

    @Override
    public void onDestroy() {
        for (ILifecycle element : elements) {
            if (element == null) continue;
            element.onDestroy();
        }

        elements.clear();
    }
}

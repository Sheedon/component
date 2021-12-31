package org.sheedon.component.lifecycle;

/**
 * 生命周期单元
 * 这部分有第三方sdk创建而来
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/12/20 6:31 下午
 */
public class LifecycleElement {


    public static LifecycleElement get(){
        return new LifecycleElement();
    }


    public void destroy(){
        // 销毁动作
    }
}

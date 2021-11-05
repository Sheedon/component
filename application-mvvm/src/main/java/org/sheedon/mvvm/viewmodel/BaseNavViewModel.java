package org.sheedon.mvvm.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import org.sheedon.mvvm.viewmodel.actuator.Actuator;
import org.sheedon.mvvm.viewmodel.actuator.ActuatorProvider;
import org.sheedon.mvvm.viewmodel.livedata.UnPeekLiveData;

/**
 * 内部包含Fragment的Activity，且内部Fragment也需要持有该ViewModel而使用
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/28 9:22 上午
 */
public abstract class BaseNavViewModel extends ViewModel {

    // 消息发送端
    private final UnPeekLiveData<String> messageEmitter = new UnPeekLiveData<>();
    // 执行动作发送器
    private final UnPeekLiveData<Integer> handleAction = new UnPeekLiveData<>();

    // 执行器提供者
    private ActuatorProvider actuatorProvider;

    public UnPeekLiveData<String> getMessageEmitter() {
        return messageEmitter;
    }

    public UnPeekLiveData<Integer> getHandleAction() {
        return handleAction;
    }

    /**
     * 初始化创建数据分发执行器
     */
    public abstract void initActuators();

    /**
     * 创建数据分发执行器
     *
     * @param modelClass 执行器类
     * @param <T>        Actuator
     * @return Actuator
     */
    protected <T extends Actuator> T getActuator(@NonNull Class<T> modelClass) {
        if (actuatorProvider == null) {
            actuatorProvider = create();
        }
        return actuatorProvider.get(modelClass);
    }

    /**
     * 创建「数据分发执行器」的持有者类
     *
     * @return ActuatorProvider
     */
    protected ActuatorProvider create() {
        return new ActuatorProvider();
    }

    /**
     * 清空销毁
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (actuatorProvider != null) {
            actuatorProvider.clear();
        }
        actuatorProvider = null;
    }
}

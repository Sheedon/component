package org.sheedon.mvvm.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sheedon.mvvm.viewmodel.actuator.Actuator;
import org.sheedon.mvvm.viewmodel.actuator.ActuatorProvider;

/**
 * 基础ViewModel
 * 包含错误消息message 和 执行动作handleAction
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/27 6:47 下午
 */
public abstract class BaseViewModel extends ViewModel {

    // 消息发送端
    private final MutableLiveData<String> messageEmitter = new MutableLiveData<>();
    // 执行动作发送器
    private final MutableLiveData<Integer> handleAction = new MutableLiveData<>();

    // 执行器提供者
    private ActuatorProvider actuatorProvider;

    public MutableLiveData<String> getMessageEmitter() {
        return messageEmitter;
    }

    public MutableLiveData<Integer> getHandleAction() {
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

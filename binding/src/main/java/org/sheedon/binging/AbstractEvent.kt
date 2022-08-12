package org.sheedon.binging

/**
 * 抽象事件处理类
 *
 * 应用采用MVVM设计，使用Jetpack-ViewModel+DataBinding，实现XML与ViewModel的数据绑定。
 * 为了将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，采用binding.setVariable(key,value)实现。
 * 因此，在事件处理类中，主要职责有三点：
 * 其一：配置XML中配置的variable-name 与实际实现的绑定。
 * 其二：实现该类固定实现方法，例如扫码实现，提供扫码的功能，将扫码结果返回到调用该事件类的对象中。
 * 其三：实现指定<Provide>方法，向外提供事件额外方法。例如：分页列表，更新刷新状态等。
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/10 15:03
 */
abstract class AbstractEvent<Handler : EventHandler> {

    /**
     * 得到转化为数据绑定的配置内容，将下图中的name与具体事件绑定。
     *
     * XML
     * <code>
     *  <layout>
     *      <variable
     *          name="xx"
     *          type="Event" />
     *  </layout>
     * </code>
     * @return DataBindingConfig
     * */
    abstract fun convertDataBindingConfig(): DataBindingConfig

    /**
     * 加载事件额外处理的提供对象，使得外部类可以调用该方法所得到处理对象。
     * */
    abstract fun loadProvider(): Handler?
}
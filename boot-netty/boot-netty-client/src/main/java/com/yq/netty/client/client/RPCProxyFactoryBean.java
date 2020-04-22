package com.polan.netty.client.client;

import com.polan.kernel.utils.IdGen;
import com.polan.netty.client.util.ChannelUtil;
import com.polan.netty.client.util.WrapMethodUtils;
import com.polan.netty.commons.entity.MethodInvokeMeta;
import com.polan.netty.commons.exception.ErrorParamsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p> JDK动态代理类</p>
 * @author youq  2019/4/19 19:43
 */
@Slf4j
public class RPCProxyFactoryBean extends AbstractFactoryBean<Object> implements InvocationHandler {
    /**
     * 远程服务接口
     */
    private Class interfaceClass;

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    /**
     * 创建实例的方法
     * @return 由工厂创建的实例
     */
    @Override
    protected Object createInstance() {
        log.info("[代理工厂] 初始化代理Bean : {}", interfaceClass);
        // 返回代理类
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, this);
    }

    /**
     * 动态调用方法的方法
     * 该方法不会显示调用
     * @param proxy  被代理的实例
     * @param method 调用的方法
     * @param args   参数列表
     * @return 返回值
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ErrorParamsException {
        log.info("{} -> [准备进行远程服务调用] ", this.getClass().getName());
        log.info("{} -> [封装调用信息] ", this.getClass().getName());
        final MethodInvokeMeta methodInvokeMeta = WrapMethodUtils.readMethod(interfaceClass, method, args);
        log.info("{} -> [远程服务调用封装完毕] 调用接口 -> {}\n调用方法 -> {}\n参数列表 -> {} \n 参数类型 -> {}" +
                        "\n 返回值类型 -> {}", this.getClass().getName(), methodInvokeMeta.getInterfaceClass(),
                methodInvokeMeta.getMethodName() , methodInvokeMeta.getArgs(),
                methodInvokeMeta.getParameterTypes(), methodInvokeMeta.getReturnType());
        // 构造一个时间戳
        String uuid = IdGen.DEFAULT.gen();
        // 真正开始使用netty进行通信的方法
        ChannelUtil.remoteCall(methodInvokeMeta, uuid);
        Object result;
        do {
            // 接收返回信息
            result = ChannelUtil.getResult(uuid);
        } while (result == null);
        // 服务器有可能返回异常信息，所以在这里可以进行异常处理
        if (result instanceof ErrorParamsException) {
            throw (ErrorParamsException) result;
        }
        return result;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

}
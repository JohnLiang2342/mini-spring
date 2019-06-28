package com.java.ljh.web.handler;

import com.java.ljh.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 每一个MappingHandler都是一个请求映射器
 *
 */
public class MappingHandler {
    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    public MappingHandler(String uri, Method method, Class<?> cls, String[] args){
        this.uri = uri;
        this.method = method;
        this.controller = cls;
        this.args = args;
    }

    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        if(!uri.equals(requestUri)){
            return false;
        }
        //如果能处理
        Object[] parameters = new Object[args.length];
        for(int i=0; i<args.length; i++){
            parameters[i] = req.getParameter(args[i]);
        }

        //Object ctl = controller.newInstance(); //获取类实例
        Object ctl = BeanFactory.getBean(controller); //从BeanBeanFactory获取类实例
        Object response = method.invoke(ctl, parameters); //调用方法
        res.getWriter().println(response.toString());
        return true;

    }
}

package com.java.ljh.web.handler;


import com.java.ljh.web.mvc.Controller;
import com.java.ljh.web.mvc.RequestMapping;
import com.java.ljh.web.mvc.RequestParame;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * HandlerManager管理器，用来管理MappingHandler
 * 存放着所有controller下的所有可以访问的路径
 * 有前台访问时DispatcherServlet会遍历mappingHandlerList看是否有相应的mappingHandler
 */
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>(); //保存多个MappingHandler

    /**
     * 挑选出controller类，把类里的MappingHandler方法初始化成MappingHandler
     */
    public static void resolveMappingHandler(List<Class<?>> classList){
        //遍历所有扫描出的类，把带有@Controller注解的类拿出来
        for(Class<?> cls : classList){
            if(cls.isAnnotationPresent(Controller.class)){
                //解析Controller类
                parseHandlerFromController(cls);
            }
        }
    }

    private static void parseHandlerFromController(Class<?> cls){
        //通过反射获取类所有方法
        Method[] methods = cls.getDeclaredMethods();
        //遍历所有方法，找到被@RequestMapping注解的方法
        for(Method method : methods){
            if(!method.isAnnotationPresent(RequestMapping.class)){
                continue; //不处理
            }
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            List<String> paramNameList = new ArrayList<>(); //参数容器
            for(Parameter parameter : method.getParameters()){
                if(parameter.isAnnotationPresent(RequestParame.class)){
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParame.class).value());
                }
            }
            //把参数名容器转为数组形式
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            //已知方法和类，构造MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            //存入HandlerManage
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}

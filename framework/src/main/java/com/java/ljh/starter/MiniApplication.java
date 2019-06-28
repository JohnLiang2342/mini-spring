package com.java.ljh.starter;

import com.java.ljh.beans.BeanFactory;
import com.java.ljh.core.ClassScanner;
import com.java.ljh.web.handler.HandlerManager;
import com.java.ljh.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.util.List;

/**
 * 框架入口类
 * cls :应用的入口类
 */
public class MiniApplication {
    public static void run(Class<?> cls, String[] args){
        System.out.println("hello mini-spring!");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer(); //启动内嵌tomcat
            //扫描结果有很多框架类，因为框架类也是使用同一个包路径:com.java.ljh，实际情况是只扫描出传入应用的所有类
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            classList.forEach(it -> System.out.println(it.getName()));
            //初始化Bean，将各种带有特殊注解（controller,bean,autowired,...）的类扔进bean工厂
            BeanFactory.initBean(classList);
            //将扫描出的类挑选出Controller放进管理器，用于路径映射
            HandlerManager.resolveMappingHandler(classList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

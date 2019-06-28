package com.java.ljh.beans;

import com.java.ljh.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean工厂，用来初始化和保存Bean
 */
public class BeanFactory {
    //存储Bean类型到属性的映射
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    /**
     * 根据类从映射获取Bean 例如：getBean();
     */
    public static Object getBean(Class<?> cls){
        return classToBean.get(cls);
    }

    /**
     * Bean初始化
     * classList : 扫描出的所有类
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        //复制一份到新容器中进行操作
        List<Class<?>> toCreate = new ArrayList<>(classList);
        //处理所有类
        while(toCreate.size()!=0){
            int remainSize = toCreate.size(); //容器大小，用来记录
            for(int i=0; i<toCreate.size(); i++){
                //如果完成bean创建，从容器中删除
                if(finishCreate(toCreate.get(i))){
                    toCreate.remove(i);
                }
            }
            //如果每次循环完容器长度没有变化，即陷入死循环，抛出异常
            if(toCreate.size()==remainSize){
                throw new Exception("cycle dependency!");
            }
        }
    }

    /**
     * 创建Bean
     * @return
     */
    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        //判断是否有特定注解来决定是否要初始化为Bean
        if(!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)){
            return true; //返回true让当前类从classList从删除
        }

        //这就是bean！？？
        Object bean = cls.newInstance();
        //遍历当前class中的属性，看是否有依赖
        for(Field field : cls.getDeclaredFields()){
            //如果当前属性有@AutoWired注解，则需要依赖注入
            if(field.isAnnotationPresent(AutoWired.class)){
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType); //从bena工厂中获取bean
                //如果工厂中没有被依赖的bean，则当前bean创建失败
                if(reliantBean == null){
                    return false;
                }
                //如果工厂中存在被依赖的bean，则创建成功
                field.setAccessible(true); //该方法可以取消Java的权限控制检查，使private属性可以访问
                field.set(bean, reliantBean);//set(要修改属性的那个类，新的属性值)
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}

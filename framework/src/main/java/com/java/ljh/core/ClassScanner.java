package com.java.ljh.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> cLassList = new ArrayList<>(); //扫描出的类容器
        String path = packageName.replace(".", "/"); //包名转为文件路径
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //获取默认类加载器
        Enumeration<URL> resources = classLoader.getResources(path); //返回可遍历的url资源
        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();
            if(resource.getProtocol().contains("jar")){ //判断当前资源类型
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName(); //通过连接获取jar包路径名
                cLassList.addAll(getClassesFromJar(jarFilePath, path));
            }else {
                //todo
            }
        }
        return cLassList;
    }

    /**
     * 通过jar包路径获取jar包下所有的类
     * @param jarFilePath
     * @param path 指定哪些类是需要的，通过类的相对路径指定
     * @return
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);//把jar路径转化为jarFile实例
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()){ //每个jarEntries都是一个jar包的文件
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName(); //com/java/ljh/test/Test.class
            if(entryName.startsWith(path) && entryName.endsWith(".class")){
                //获取类全限定名
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length()-6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;

    }
}

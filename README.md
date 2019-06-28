# 项目名称
**mini-spring**   

仿照慕课网教程 https://coding.imooc.com/class/350.html 的非常简陋的spring框架
# 部署
项目使用gradle构建，如没安装gradle环境的小伙伴请自行安装~  

导入项目到IDEA后在项目根目录mini-spring下依次执行如下命令（cmd或IDEA自带终端）:  
1. gradle clean build  
2. gradle build  
3. java -jar test/build/libs/test-1.0-SNAPSHOT.jar

看到输出一堆类目录就代表成功啦  

接着浏览器运行 http://localhost:6699/get_salary.json?name=JohnLiang&experience=3  
看到网页输出15000就是成功了，这个就是mini-spring框架的整个流程

# 项目概述
项目分为框架模块framework，与测试模块test  
使用jdk1.8，IDE是IDEA

mini-spring实现了Spring的@Controller，@Bean，@Autowired，@RequestMapping，@RequestParame注解，  
有内置tomcat，前端有请求发来后使用DispatcherServlet接受。DispatcherServlet接受后循环HandlerManager，
查找在HandlerManager中是否有相对应的HandlerMapping，然后交由HandlerMapping处理请求


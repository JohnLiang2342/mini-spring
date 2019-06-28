package com.java.ljh.service;

import com.java.ljh.beans.Bean;

@Bean //加了注解可以从BeanFactory中获取该类实例
public class SalaryService {
    public Integer calSalary(Integer experience){
        return experience * 5000;
    }
}

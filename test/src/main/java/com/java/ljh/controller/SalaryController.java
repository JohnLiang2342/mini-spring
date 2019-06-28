package com.java.ljh.controller;


import com.java.ljh.beans.AutoWired;
import com.java.ljh.service.SalaryService;
import com.java.ljh.web.mvc.Controller;
import com.java.ljh.web.mvc.RequestMapping;
import com.java.ljh.web.mvc.RequestParame;

@Controller
public class SalaryController {

    @AutoWired
    private SalaryService salaryService;

    @RequestMapping("/get_salary.json")
    public String getSalary(@RequestParame("name") String name, @RequestParame("experience") String experience){
        return "Name: " + name + "  Salary: " + salaryService.calSalary(Integer.parseInt(experience));
    }
}

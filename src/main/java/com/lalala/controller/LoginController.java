package com.lalala.controller;

import com.lalala.log.AOPLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 控制登录相关功能的控制层
 */
@Controller
public class LoginController {
    @RequestMapping(value = "/login")
    public String login(){
        /**
         * 返回登录页面
         */
        return "login.html";
    }

    @RequestMapping(value = "/index")
    @AOPLog(operatetype = "登录",operatedesc = "用户登录后台管理系统")
    public String index(){
        /**
         * 返回首页
         */
        return "index.html";
    }
}

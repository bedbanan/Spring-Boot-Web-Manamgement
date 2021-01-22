package com.lalala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Quartz表达式的控制器
 */
@Controller
@RequestMapping("/quartz")
public class quartzController {

    @RequestMapping("cron")
    public String Cron(){
        return "quartz/Test.html";
    }

    @RequestMapping("crone")
    public String Crone(){
        return "quartz/cron.html";
    }
}

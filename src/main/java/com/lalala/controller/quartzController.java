package com.lalala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Quartz表达式的控制器
 */
@Controller
public class QuartzController {
    @RequestMapping("/cron")
    public String Cron(){
        /**
         * 返回定时任务列表页面
         */
        return "quartz/ListJob.html";
    }

    @RequestMapping("crone")
    public String Crone(){
        //打开Cron表达式有页面
        return "quartz/cron.html";
    }
    @RequestMapping("table")
    public String Table(){
        //暂时废弃
        return "quartz/index.html";
    }
    @RequestMapping("adds")
    public String add(){
        /**
         * 打开添加任务的页面
         */
        return "quartz/add.html";
    }
    @RequestMapping("quartz/edit")
    public String Edit(){
        /**
         * 打开修改任务的页面
         */
        return "quartz/add.html";
    }

}

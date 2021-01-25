package com.lalala.controller;

import com.lalala.pojo.QuartzEntity;
import com.lalala.pojo.Result;
import com.lalala.service.JobService;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 任务类的控制层，负责前端定时任务的增删改查功能
 */
@Controller
@RequestMapping("/job")
public class JobController {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private Scheduler scheduler; //加入scheduler

    @Autowired
    private JobService jobService;

    /**
     * 返回界面的方法
     */
    @RequestMapping("cron")
    public String Cron(){
        /**
         * 返回定时任务列表页面
         */
        return "quartz/ListJob.html";
    }
    @RequestMapping("table")
    public String Table(){
        //暂时废弃
        return "quartz/index.html";
    }
    @RequestMapping("adds")
    public String Add(){
        /**
         * 打开添加任务的页面
         */
        return "quartz/AddJob.html";
    }

    @RequestMapping("crone")
    public String Crone(){
        //打开Cron表达式有页面
        return "quartz/cron.html";
    }


    //返回参数用方法

    @PostMapping("/add")
    @ResponseBody
    public Result save(QuartzEntity quartzEntity){
        /**
         * 保存或更改功能
         */
        LOGGER.info("新增任务");
        try {
            jobService.save(quartzEntity);
        } catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public Result List(QuartzEntity quartzEntity,Integer pageNo,Integer pageSize) throws SchedulerException{
        /**
         * 任务列表的分页查询和显示
         */
        LOGGER.info("任务列表");
        return jobService.listQuartzEntity(quartzEntity,pageNo,pageSize);
    }

    @PostMapping("/trigger")
    @ResponseBody
    public  Result trigger(QuartzEntity quartz,HttpServletResponse response) {
        /**
         * 触发任务,开始执行
         */
        LOGGER.info("触发任务");
        try {
            JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }
    @PostMapping("/pause")
    @ResponseBody
    public  Result pause(QuartzEntity quartz,HttpServletResponse response) {
        /**
         * 暂停任务
         */
        LOGGER.info("停止任务");
        try {
            JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }
    @PostMapping("/resume")
    @ResponseBody
    public  Result resume(QuartzEntity quartz, HttpServletResponse response) {
        /**
         * 恢复当前定时任务的执行
         */
        LOGGER.info("恢复任务");
        try {
            JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
            scheduler.resumeJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }
    @PostMapping("/remove")
    @ResponseBody
    public  Result remove(QuartzEntity quartz,HttpServletResponse response) {
        /**
         * 删除定时任务
         */
        LOGGER.info("移除任务");
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));
            System.out.println("removeJob:"+JobKey.jobKey(quartz.getJobName()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }
}

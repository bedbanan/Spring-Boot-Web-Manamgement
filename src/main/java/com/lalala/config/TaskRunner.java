package com.lalala.config;

import com.lalala.pojo.QuartzEntity;
import com.lalala.service.JobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 配置类，在启动项目的时候自动生成一个任务载入数据库并开始运行
 */
@Component
public class TaskRunner implements ApplicationRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(ApplicationArguments var) throws Exception{
        /**
         * 系统启动的时候会初始化一个任务
         */
        Long count = jobService.listQuartzEntity(null); //如果开始为空
        if(count==0){ //为空则创建并加入数据库中
            LOGGER.info("初始化测试任务");
            QuartzEntity quartz = new QuartzEntity();
            quartz.setJobName("test01");
            quartz.setJobGroup("test");
            quartz.setDescription("测试任务");
            quartz.setJobClassName("com.lalala.job.ChickenJob");
            quartz.setCronExpression("*/5 * * * * ?");
            quartz.setJobMethodName("test1");
            Class cls = Class.forName(quartz.getJobClassName()) ;
            cls.newInstance();
            //构建job信息
            JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
                    quartz.getJobGroup())
                    .withDescription(quartz.getDescription()).build();
            job.getJobDataMap().put("jobMethodName", "test1");
            // 触发时间点
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
                    .startNow().withSchedule(cronScheduleBuilder).build();
            //交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
        }
    }
}

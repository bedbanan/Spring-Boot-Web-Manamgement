package com.lalala.service.impl;

import com.lalala.dynamicquery.DynamicQuery;
import com.lalala.pojo.PageBean;
import com.lalala.pojo.QuartzEntity;
import com.lalala.pojo.Result;
import com.lalala.service.JobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Job的实现类，用于实现其相关的功能用于给控制层使用
 */
@Service("jobService")
public class JobServiceImpl implements JobService {
    @Autowired
    private DynamicQuery dynamicQuery; //持久层仓库类
    @Autowired
    private Scheduler scheduler;

    /**
     * 分页查询
     * @param quartz  quartz实体
     * @param pageNo  起始页
     * @param pageSize 每页大小
     * @return
     * @throws SchedulerException
     */
    @Override
    public Result listQuartzEntity(QuartzEntity quartz, Integer pageNo, Integer pageSize) throws SchedulerException {
        String countSql = "SELECT COUNT(*) FROM qrtz_job_details AS job ";
        if(!StringUtils.isEmpty(quartz.getJobName())){
            countSql+=" WHERE job.JOB_NAME = "+quartz.getJobName();
        }
        Long totalCount = dynamicQuery.nativeQueryCount(countSql);
        PageBean<QuartzEntity> data = new PageBean<>();
        if(totalCount>0){
            StringBuffer nativeSql = new StringBuffer();
            nativeSql.append("SELECT job.JOB_NAME as jobName,job.JOB_GROUP as jobGroup,job.DESCRIPTION as description,job.JOB_CLASS_NAME as jobClassName,");
            nativeSql.append("cron.CRON_EXPRESSION as cronExpression,tri.TRIGGER_NAME as triggerName,tri.TRIGGER_STATE as triggerState,");
            nativeSql.append("job.JOB_NAME as oldJobName,job.JOB_GROUP as oldJobGroup ");
            nativeSql.append("FROM qrtz_job_details AS job ");
            nativeSql.append("LEFT JOIN qrtz_triggers AS tri ON job.JOB_NAME = tri.JOB_NAME  AND job.JOB_GROUP = tri.JOB_GROUP ");
            nativeSql.append("LEFT JOIN qrtz_cron_triggers AS cron ON cron.TRIGGER_NAME = tri.TRIGGER_NAME AND cron.TRIGGER_GROUP= tri.JOB_GROUP ");
            nativeSql.append("WHERE tri.TRIGGER_TYPE = 'CRON'");
            Object[] params = new  Object[]{};
            if(!StringUtils.isEmpty(quartz.getJobName())){
                nativeSql.append(" AND job.JOB_NAME = ?");
                params = new Object[]{quartz.getJobName()};
            }
            Pageable pageable = PageRequest.of(pageNo-1,pageSize);
            List<QuartzEntity> list = dynamicQuery.nativeQueryPagingList(QuartzEntity.class,pageable, nativeSql.toString(), params);
            for (QuartzEntity quartzEntity : list) {
                JobKey key = new JobKey(quartzEntity.getJobName(), quartzEntity.getJobGroup());
                JobDetail jobDetail = scheduler.getJobDetail(key);
                quartzEntity.setJobMethodName(jobDetail.getJobDataMap().getString("jobMethodName"));
            }
            data = new PageBean<>(list, totalCount);
        }
        return Result.ok(data);
    }

    /**
     * 查询使用
     * @param quartz
     * @return
     */
    @Override
    public Long listQuartzEntity(QuartzEntity quartz) {
        StringBuffer nativeSql = new StringBuffer();
        nativeSql.append("SELECT COUNT(*)");
        nativeSql.append("FROM qrtz_job_details AS job LEFT JOIN qrtz_triggers AS tri ON job.JOB_NAME = tri.JOB_NAME ");
        nativeSql.append("LEFT JOIN qrtz_cron_triggers AS cron ON cron.TRIGGER_NAME = tri.TRIGGER_NAME ");
        nativeSql.append("WHERE tri.TRIGGER_TYPE = 'CRON'");
        return dynamicQuery.nativeQueryCount(nativeSql.toString(), new Object[]{});
    }

    /**
     * 保存新建的任务以及更新任务使用
     * @param quartz
     * @throws Exception
     */
    @Override
    public void save(QuartzEntity quartz) throws Exception {
        //如果是修改  展示旧的 任务
        if(quartz.getOldJobGroup()!=null){
            JobKey key = new JobKey(quartz.getOldJobName(),quartz.getOldJobGroup());
            scheduler.deleteJob(key);
        }
        Class cls = Class.forName(quartz.getJobClassName()) ;
        cls.newInstance();
        //构建job信息
        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
                quartz.getJobGroup())
                .withDescription(quartz.getDescription()).build();
        job.getJobDataMap().put("jobMethodName", quartz.getJobMethodName());
        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
                .startNow().withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(job, trigger);
    }
}

package com.lalala.service;

import com.lalala.pojo.QuartzEntity;
import com.lalala.pojo.Result;
import org.quartz.SchedulerException;

/**
 * 任务的Service接口
 */
public interface JobService {
    //分页查询quartz的任务返回
    Result listQuartzEntity(QuartzEntity quartzEntitym,Integer pageNo,Integer pageSize) throws SchedulerException;

    //直接查询
    Long listQuartzEntity(QuartzEntity quartz);

    //保存任务
    void save(QuartzEntity quartz) throws Exception;
}

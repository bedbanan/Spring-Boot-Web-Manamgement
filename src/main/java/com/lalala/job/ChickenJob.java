package com.lalala.job;

import org.quartz.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Job的实现类
 */
@DisallowConcurrentExecution  //保证上一个任务执行完后，再去执行下一个任务，这里的任务是同一个任务
public class ChickenJob implements Job, Serializable {
    //Serializable实现序列化接口、防止重启应用出现quartz Couldn't retrieve job because a required class was not found 的问题
    //Job 的作用是实例要到该执行它们的时候才会实例化出来。每次 Job 被执行，一个新的 Job 实例会被创建。
    // Job 不必担心线程安全性，因为同一时刻仅有一个线程去执行给定 Job 类的实例，甚至是并发执行同一 Job 也是如此。
    //保证了执行的同步性
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        //动态的调用方法，获取任务中保存的方法名字
        String methodName = dataMap.getString("jobMethodName");
        try {
            ChickenJob job = new ChickenJob();
            Method method = job.getClass().getMethod(methodName);
            method.invoke(job);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void test1(){
        System.out.println("测试方法1");
    }
    public void test2(){
        System.out.println("测试方法2");
    }
    public void test3(){
        System.out.println("测试方法3");
    }
}

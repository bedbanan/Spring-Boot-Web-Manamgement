package com.lalala.pojo;

/**
 * quartz的任务类
 */
public class QuartzEntity {
    private String jobName;//任务名称
    private String jobGroup;//任务分组
    private String description;//任务描述
    private String jobClassName;//执行类
    private String jobMethodName;//执行方法
    private String cronExpression;//执行时间
    private String triggerName;//执行时间
    private String triggerState;//任务状态
    private String oldJobName;//任务名称 用于修改
    private String oldJobGroup;//任务分组 用于修改

    public QuartzEntity() {

    }

    public QuartzEntity(String jobName, String jobGroup, String description, String jobClassName, String jobMethodName, String cronExpression, String triggerName, String triggerState, String oldJobName, String oldJobGroup) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.jobClassName = jobClassName;
        this.jobMethodName = jobMethodName;
        this.cronExpression = cronExpression;
        this.triggerName = triggerName;
        this.triggerState = triggerState;
        this.oldJobName = oldJobName;
        this.oldJobGroup = oldJobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobMethodName() {
        return jobMethodName;
    }

    public void setJobMethodName(String jobMethodName) {
        this.jobMethodName = jobMethodName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getOldJobName() {
        return oldJobName;
    }

    public void setOldJobName(String oldJobName) {
        this.oldJobName = oldJobName;
    }

    public String getOldJobGroup() {
        return oldJobGroup;
    }

    public void setOldJobGroup(String oldJobGroup) {
        this.oldJobGroup = oldJobGroup;
    }

    @Override
    public String toString() {
        return "QuartzEntity [jobName=" + jobName + ", jobGroup=" + jobGroup + ", description=" + description
                + ", jobClassName=" + jobClassName + ", jobMethodName=" + jobMethodName + ", cronExpression="
                + cronExpression + ", triggerName=" + triggerName + ", triggerState=" + triggerState + ", oldJobName="
                + oldJobName + ", oldJobGroup=" + oldJobGroup + "]";
    }
}

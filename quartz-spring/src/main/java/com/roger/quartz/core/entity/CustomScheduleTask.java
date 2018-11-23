package com.roger.quartz.core.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomScheduleTask implements Serializable {
    private String id;//任务Id
    private String jobName;//任务名
    private String jobClassName;//任务所在类全名称
    private String expression;//任务频率 和cron语法保持一致
    private Object[] param;//执行任务方法的参数
}

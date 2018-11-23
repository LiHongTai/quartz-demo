package com.roger;

import com.roger.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobStart {

    public static void main(String[] args) throws Exception {
        //step1:创建任务调度工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        //step2:通过任务调度工厂获取任务调度器
        Scheduler scheduler = schedulerFactory.getScheduler();

        //step3:定义一个触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("simpleTriggerName", "simpleTriggerGroup")
                .startNow()//一旦加入scheduler立即执行
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()//使用简单调度器
                        .withIntervalInSeconds(1)//每隔一秒执行一次
                        .withRepeatCount(10)//总共执行10次
                ).build();
        //step4:定义一个JobDetail
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)//真正的执行逻辑所在
                        .withIdentity("simpleJobDeatilName","simpleJobDetailGroup")
                        .usingJobData("name","Roger")//定义属性
                        .build();

        //step5:把触发器和JobDetail加入调度器
        scheduler.scheduleJob(jobDetail,trigger);
        //step6:启动
        scheduler.start();

    }
}

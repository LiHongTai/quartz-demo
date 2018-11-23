package com.roger.quartz.biz.job;

import com.roger.quartz.SpringBaseTestSuit;
import com.roger.quartz.core.entity.CustomScheduleTask;
import com.roger.quartz.core.service.ScheduleTaskService;
import com.roger.quartz.core.utils.SnowflakeIdWorker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class TestHelloJob extends SpringBaseTestSuit {

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Test
    public void testHellJob() throws Exception{
        CustomScheduleTask task = new CustomScheduleTask();
        long nextId = SnowflakeIdWorker.getInstance().nextId();
        task.setId(String.valueOf(nextId));
        task.setJobName("sayHello");
        task.setJobClassName("com.roger.quartz.biz.job.HelloJob");
        Object[] param = new Object[]{"Roger"};
        task.setParam(param);
        String cronExp = "0/1 * * * * ?";
        task.setExpression(cronExp);
        scheduleTaskService.addTask(task);

        TimeUnit.SECONDS.sleep(5);
        System.out.println("暂停任务...");
        scheduleTaskService.pauseTask(task);
        TimeUnit.SECONDS.sleep(5);
        System.out.println("恢复任务...");
        scheduleTaskService.resumeTask(task);


        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}

package com.roger.quartz.biz.job;

import com.roger.quartz.core.constant.ScheduleTaskConstant;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static com.roger.quartz.core.constant.ScheduleTaskConstant.JOB_PARAM;

import java.util.Date;

public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        Object[] param = (Object[]) jobDetail.getJobDataMap().get(JOB_PARAM);
        System.out.println("Say hello to " + param[0] + " at " + new Date());
    }
}

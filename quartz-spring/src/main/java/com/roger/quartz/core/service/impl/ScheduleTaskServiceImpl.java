package com.roger.quartz.core.service.impl;

import com.roger.quartz.core.entity.CustomScheduleTask;
import com.roger.quartz.core.service.ScheduleTaskService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.roger.quartz.core.constant.ScheduleTaskConstant.*;

@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

    @Autowired
    private Scheduler clusterSchedule;

    @Override
    public void addTask(CustomScheduleTask task) {

        JobKey jobKey = getJobKey(task);
        try {
            if (!clusterSchedule.checkExists(jobKey)) {
                Class<? extends Job> targetJob = (Class<? extends Job>) Class.forName(task.getJobClassName());
                JobDataMap jobData = new JobDataMap();
                jobData.put(JOB_PARAM, task.getParam());
                JobDetail jobDetail = JobBuilder.newJob(targetJob)
                        .setJobData(jobData)
                        .withIdentity(jobKey).build();

                TriggerKey triggerKey = new TriggerKey(task.getJobName() + TRIGGER_NAME_SUFFIX, task.getJobName() + TRIGGER_GROUP_SUFFIX);

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule(task.getExpression()))
                        .build();
                clusterSchedule.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("执行的任务异常：" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("执行的任务不存在：" + e.getMessage());
        }

    }

    @Override
    public void deleteTask(CustomScheduleTask task) {
        try {
            JobKey jobKey = getJobKey(task);
            clusterSchedule.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseTask(CustomScheduleTask task) {
        try {
            JobKey jobKey = getJobKey(task);
            clusterSchedule.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复任务
     *
     * @param task
     */
    @Override
    public void resumeTask(CustomScheduleTask task) {
        try {
            JobKey jobKey = getJobKey(task);
            clusterSchedule.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTasks(List<CustomScheduleTask> scheduleTasks) {
        if (CollectionUtils.isEmpty(scheduleTasks)) {
            return;
        }
        try {
            List<JobKey> jobKeys = new ArrayList<>();
            for (CustomScheduleTask task : scheduleTasks) {
                JobKey jobKey = getJobKey(task);
                jobKeys.add(jobKey);
            }
            clusterSchedule.deleteJobs(jobKeys);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseAllTask() {
        try {
            clusterSchedule.pauseAll();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private JobKey getJobKey(CustomScheduleTask task) {
        return new JobKey(task.getJobName() + JOB_NAME_SUFFIX, task.getJobName() + JOB_GROUP_SUFFIX);
    }
}

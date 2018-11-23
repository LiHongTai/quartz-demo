package com.roger.quartz.core.service;

import com.roger.quartz.core.entity.CustomScheduleTask;

import java.util.List;

public interface ScheduleTaskService {

    /**
     * 添加一个新的定时任务
     * @param task
     */
    void addTask(CustomScheduleTask task);

    /**
     * 删除指定定时任务
     * @param task
     * @return
     */
    void deleteTask(CustomScheduleTask task);

    void pauseTask(CustomScheduleTask task);
    /**
     * 恢复任务
     * @param task
     */
    void resumeTask(CustomScheduleTask task);
    /**
     * 批量删除定时任务
     */
    void deleteTasks(List<CustomScheduleTask> scheduleTasks);

    void pauseAllTask();

}

package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ChooseCourseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService tackService;

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinishChoosecourseTask(XcTask xcTask) {
        if (xcTask != null && StringUtils.isEmpty(xcTask.getId())) {
            tackService.finishTack(xcTask.getId());
        }
    }

    /**
     * 定时发送添加选课任务
     */
    @Scheduled(cron = "0/3 * * * * *")
    public void sendChoosecourseTack() {
        //得到1分钟之前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        Date time = calendar.getTime();
        List<XcTask> list = tackService.findXcTaskList(time, 100);
        //System.out.println(list);
        //调用service发布消息，将添加选课的任务发送给mq
        for (XcTask xcTask : list) {
            //取任务
            if (tackService.getTask(xcTask.getId(), xcTask.getVersion()) > 0) {
                String ex = xcTask.getMqExchange();
                String routingkey = xcTask.getMqRoutingkey();
                tackService.publish(xcTask, ex, routingkey);
            }
        }
    }


    /**
     * 定义任务调试策略
     */
    // @Scheduled(cron = "0/3 * * * * *")
    public void task1() {
        LOGGER.info("===============测试定时任务1开始===============");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("===============测试定时任务1结束===============");
    }


}

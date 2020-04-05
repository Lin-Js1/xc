package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTackRepository;
import com.xuecheng.order.dao.XcTaskHisRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    XcTackRepository xcTackRepository;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;
    @Autowired
    RabbitTemplate rabbitTemplatel;

    /**
     * 查询前n条任务
     */
    public List<XcTask> findXcTaskList(Date updateTime,int size){
        //设置分页参数
        Pageable pageable = new PageRequest(0,size);

        Page<XcTask> all = xcTackRepository.findByUpdateTimeBefore(pageable, updateTime);
        List<XcTask> list = all.getContent();
        return list;
    }

    /**
     * 发布消息
     */
    public void publish(XcTask xcTask,String ex,String routingKey){
        Optional<XcTask> optional = xcTackRepository.findById(xcTask.getId());
        if (optional.isPresent()){
            rabbitTemplatel.convertAndSend(ex,routingKey,xcTask);
            //更新任务时间
            XcTask one = optional.get();
            one.setUpdateTime(new Date());
            xcTackRepository.save(one);
        }
    }

    /**
     * 获取任务
     */
    @Transactional(rollbackFor = Exception.class)
    public int getTask(String id,int version){
        //通过乐观锁的方式更新数据表，如果结果大于0说明取到任务
        return xcTackRepository.updateTaskVersion(id,version);
    }

    /**
     * 完成任务
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishTack(String taskId){
        Optional<XcTask> optional = xcTackRepository.findById(taskId);
        if (!optional.isPresent()){
            //当前任务
            XcTask xcTask = optional.get();
            //历史任务
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTackRepository.delete(xcTask);
        }
    }
}

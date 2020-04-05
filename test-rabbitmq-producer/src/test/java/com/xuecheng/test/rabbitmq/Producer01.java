package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer01 {

    //队列
    private static final String QUEUE = "helloword";

    public static void main(String[] args) {
        //和mq建立连接 通过连接工厂创建新的链接
        ConnectionFactory conn = new ConnectionFactory();
        conn.setHost("127.0.0.1");
        conn.setPort(5672);
        conn.setUsername("guest");
        conn.setPassword("guest");
        //设置虚拟机
        conn.setVirtualHost("/");
        //建议新连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = conn.newConnection();
            //创建会话通道
            channel = connection.createChannel();
            //声明队列
            /**
             * 1.队列名称
             * 2.是否持久化，如果持久化 mq重启后队列还在
             * 3.exclusive 是否独占连接，队列只允许在该连接中访问
             * 4.autoDelete 自动删除
             * 5.arguments 参数，可以设置一个队列的扩展参数么，比如存活时间
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            //发送消息
            /**
             * 1.exchange 交换机，如果不指定将使用mq的默认交换机
             * 2.routingKey 路由key 交换机根据路由key来将消息转发到指定的队列
             * 3.props 消息属性
             * 4.body 消息内容
             */
            String message = "hello world 允儿";
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("send to mq " + message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //先关闭通道
            //关闭连接
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

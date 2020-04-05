package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer03_routing {

    //队列
    private static final String QUEUE_INFORM_EMAI = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM = "exchange_routing_inform";
    private static final String ROUTING_EMAIL = "inform_email";
    private static final String ROUTING_SMS = "inform_sms";


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
            channel.queueDeclare(QUEUE_INFORM_EMAI, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            //声明一个交换机
            /**
             * 1.交换机的名称
             * 2.交换机的类型
             *      fanout: 对应rabbitmq的工作模式是 publish/subscribe
             *      direct: 对应的Routing工作模式
             *      topic： 对应的Topics工作模式
             *      headers：对应的headers工作模式
             */
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            //进行交换机和队列绑定
            /**
             * 1.queue 队列名称
             * 2.exchange 交换机名称
             * 3.routingKey 路由key，在发布订阅模式中设置为空字符串（作用是交换机根据路由key来将消息转发到指定队列）
             */
            channel.queueBind(QUEUE_INFORM_EMAI, EXCHANGE_ROUTING_INFORM, ROUTING_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_ROUTING_INFORM, ROUTING_SMS);
            //发送消息
            /**
             * 1.exchange 交换机，如果不指定将使用mq的默认交换机
             * 2.routingKey 路由key 交换机根据路由key来将消息转发到指定的队列
             * 3.props 消息属性
             * 4.body 消息内容
             */
            for (int i = 0; i < 5; i++) {
                //发送消息的时候指定routingKey
                String message = "send email inform message to user 允儿";
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, ROUTING_EMAIL, null, message.getBytes());
                System.out.println("send to mq " + message);
            }
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

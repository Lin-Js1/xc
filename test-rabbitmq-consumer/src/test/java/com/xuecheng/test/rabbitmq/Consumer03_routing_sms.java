package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer03_routing_sms {

    //队列
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM = "exchange_routing_inform";
    private static final String ROUTING_SMS = "inform_sms";

    public static void main(String[] args) throws IOException, TimeoutException {
        //和mq建立连接 通过连接工厂创建新的链接
        ConnectionFactory conn = new ConnectionFactory();
        conn.setHost("127.0.0.1");
        conn.setPort(5672);
        conn.setUsername("guest");
        conn.setPassword("guest");
        //设置虚拟机
        conn.setVirtualHost("/");
        //建议新连接
        Connection connection = conn.newConnection();
        //创建会话通道
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM,BuiltinExchangeType.DIRECT);

        //声明队列
        /**
         * 1.队列名称
         * 2.是否持久化，如果持久化 mq重启后队列还在
         * 3.exclusive 是否独占连接，队列只允许在该连接中访问
         * 4.autoDelete 自动删除
         * 5.arguments 参数，可以设置一个队列的扩展参数么，比如存活时间
         */
        channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

        channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,ROUTING_SMS);

        //实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            //当接收到消息后此方法被调用
            /**
             * @param consumerTag 消费者标签，用来标识消费者的，可在监听队列时设置
             * @param envelope    信封，通过envelope
             * @param properties  属性属性
             * @param body        消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
    String message = new String(body, "utf-8");
                System.out.println("receive message: "+message);
                        }
                        };

                        //监听队列
                        /**
                         * 1.queue 队列名称
                         * 2.autoAck 自动回复，当消费者接受消息后要告诉mq消息已接收，若是false需要手动编程回复
                         * 3.callback 消费方法，当消费者收到消息要执行的方法
                         */
                        channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);
                        }
                        }

package com.rodrigo.backend.rabbitmq.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiverDlx {

    private static final String CONSUMER_QUEUE = "queueConsumer";

    public static void main(String[] args) throws IOException, TimeoutException {
        //Criação da conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);

        Connection connection =  connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        System.out.println(channel);


        //receber mensagem
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message: '" + message + "'");
        };

        //enviar a mensagem
        boolean autoAck = true; //ack is false
        channel.basicConsume(CONSUMER_QUEUE, autoAck, deliverCallback, ConsumeTag -> {});
    }
}

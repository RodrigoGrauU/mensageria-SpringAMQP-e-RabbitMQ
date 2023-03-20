package com.rodrigo.backend.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiverTopic {

    private static final String NAME_EXCHANGE = "TopicExchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //Criação da conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);


        Connection connection =  connectionFactory.newConnection();

        //criar um novo canal
        Channel channel = connection.createChannel();
        System.out.println("Canal criado: " + channel);

        //retorna nome aleatorio -> servidor irá determinar nome randomico e por consequencia será temporáia
        String nameQueue = channel.queueDeclare().getQueue();
        String bindingKey = "*.*.rabbit";
        System.out.println("nome da queue gerado: " + nameQueue);

        //declaração da exchange
        channel.exchangeDeclare(NAME_EXCHANGE, "topic");
        channel.queueBind(nameQueue, NAME_EXCHANGE,bindingKey);

        //receber mensagem
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message: '" + message + "'");
        };

        //enviar a mensagem
        boolean autoAck = true; //ack is false
        channel.basicConsume(nameQueue, autoAck, deliverCallback, ConsumeTag -> {});
    }
}

package com.rodrigo.backend.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SenderTopic {

    private static final String NAME_EXCHANGE = "TopicExchange";

    public static void main(String[] args) {
        //Criação da conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);


        Connection connection;
        try {
            connection = connectionFactory.newConnection();

            System.out.println(connection.hashCode());

            //criar um novo canal
            Channel channel = connection.createChannel();
            System.out.println("Canal: " + channel);

            //declarar a exchange (nome
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");

            //criar a mensagem
            String message = "Hello! This is a original message!";
            String routingKey2 = "quick.rabbit";
            String message2 = "Hello! This is the message with the routing key: " + routingKey2;
            String routingKey = "quick.orange.rabbit";

            //enviar a mensagem - não há o nome da exchange (primeiro parâmetro) pois não está sendo utilizado nesse primeiro momento)
            channel.basicPublish(NAME_EXCHANGE, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(NAME_EXCHANGE, routingKey2, null, message2.getBytes(StandardCharsets.UTF_8));

            System.out.println("[x] Sent '" + message + "'");
            System.out.println("[x] Sent '" + message2 + "'");
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}

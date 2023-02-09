package com.rodrigo.backend.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Receiver {

    private static final String NAME_QUEUE = "HELLO";

    public static void main(String[] args) throws IOException, TimeoutException {
        //Criação da conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);


        Connection connection =  connectionFactory.newConnection();

        System.out.println(connection.hashCode());

        //criar um novo canal
        Channel channel = connection.createChannel();
        System.out.println(channel);

        //declarar a fila que será usada
        //nome da fila, exclusiva, autodelete, durable, map(args)
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //receber mensagem
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message: '" + message + "'");
        };

        channel.basicConsume(NAME_QUEUE, true, deliverCallback, ConsumeTag -> {});
    }
}

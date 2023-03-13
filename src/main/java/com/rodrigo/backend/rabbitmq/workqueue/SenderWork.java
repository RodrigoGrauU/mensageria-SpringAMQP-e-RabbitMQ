package com.rodrigo.backend.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SenderWork {

    private static final String NAME_QUEUE = "Work";

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

            //declarar a fila que será usada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            channel.queueDeclare(NAME_QUEUE, false, false, false, null);

            //criar a mensagem
            String message = ".....";

            //enviar a mensagem - não há o nome da exchange (primeiro parâmetro) pois não está sendo utilizado nesse primeiro momento)
            channel.basicPublish("", NAME_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));

            System.out.println("[x] Sent '" + message + "'");
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
